// Copyright 2011 Michel Kraemer
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.spamihilator.setupwizard.db

import com.spamihilator.setupwizard.model.{Client, Document}
import com.mongodb.casbah.Imports._
import java.util.{Calendar, Date}

/** A data access object for mail client tutorials. Documents
  * are saved as immutable objects in the database. There can
  * be several documents for one client, the latest one represents
  * the current version.
  * @param db the handle to the MongoDB database
  * @author Michel Kraemer */
class DocumentDAO(db: MongoDB) extends DAO[Document] {
  private lazy val coll = db("documents")
  
  coll.ensureIndex("client")
  
  /** Converts a database object to a document. Sets the given client
    * as the document's owner.
    * @param client the client
    * @param o the database object
    * @return the document */
  private def toDocument(client: Client, o: DBObject): Document = Document(
    o.getAs[ObjectId]("_id").get,
    o.getAs[String]("text") getOrElse "",
    ((for (l <- o.getAs[BasicDBList]("authors"))
      yield l.toList) getOrElse List.empty).asInstanceOf[List[String]],
    o.getAs[Date]("date") getOrElse (Calendar.getInstance().getTime()),
    client
  )
  
  /** Converts a document to a database object
    * @param doc the document
    * @return the database object */
  private implicit def documentToDBObject(doc: Document): DBObject = MongoDBObject(
    "text" -> doc.text,
    "authors" -> doc.authors,
    "date" -> doc.date,
    "client" -> doc.client.id
  )
  
  override def find(): Iterable[Document] = coll map { o =>
    val client = (for (id <- o.getAs[ObjectId]("client");
        c <- Database.clientDao.find(id)) yield c) getOrElse {
      throw new IllegalStateException("Document has an unknown client")
    }
    toDocument(client, o)
  }
  
  /** Finds all documents for a given client
    * @param client the client
    * @return all documents about this client */
  def find(client: Client): Iterable[Document] =
    (coll.find(MongoDBObject("client" -> client.id)) map { o =>
      toDocument(client, o) }).toList
  
  /** Finds the latest document for a given client
    * @param client the client
    * @return the latest document */
  def findLatest(client: Client): Option[Document] = {
    val f = coll.find(MongoDBObject("client" -> client.id)).sort(
        MongoDBObject("date" -> -1))
    if (f.hasNext) Some(toDocument(client, f.next)) else None
  }
  
  override def insert(doc: Document) {
    coll += doc
  }
}
