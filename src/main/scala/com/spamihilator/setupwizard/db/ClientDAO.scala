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

import java.net.URL

import com.mongodb.casbah.Imports._
import com.mongodb.DBCollection
import com.spamihilator.setupwizard.model.Client

/** A data access object for mail clients
  * @param db the handle to the MongoDB database
  * @author Michel Kraemer */
class ClientDAO(db: MongoDB) extends DAO[Client] {
  private lazy val coll = db("clients")
  
  ensureIndex()
  
  /** Makes sure the mail client collection contains proper indexes */
  private def ensureIndex() {
    val primaryIndex = MongoDBObject("slug" -> 1)
    val primaryIndexName = DBCollection.genIndexName(primaryIndex)
    coll.ensureIndex(primaryIndex, primaryIndexName, true)
  }
  
  /** Converts a database object to a mail client
    * @param o the database object
    * @return the mail client */
  private def toClient(o: DBObject) = Client(
    o.getAs[ObjectId]("_id").get,
    o.getAs[String]("name") getOrElse "",
    o.getAs[String]("version") getOrElse "",
    o.getAs[String]("slug") getOrElse "",
    (for (u <- o.getAs[String]("url")) yield new URL(u)),
    o.getAs[String]("icon")
  )
  
  /** Converts a mail client to a database object */
  private implicit def clientToDBObject(client: Client): DBObject = MongoDBObject(
    "name" -> client.name,
    "version" -> client.version,
    "slug" -> client.slug
  )
  
  /** Finds a mail client with the given ID
    * @param id the ID
    * @return the mail client or <code>None</code> if there is no
    * mail client with the given ID */
  def find(id: ObjectId): Option[Client] =
    coll.findOne(MongoDBObject("_id" -> id)) map toClient
  
  /** Finds a mail client with the given slug
    * @param slug the slug
    * @return the mail client or <code>None</code> if there is no
    * mail client with the given slug */
  def find(slug: String): Option[Client] =
    coll.findOne(MongoDBObject("slug" -> slug)) map toClient
  
  /** Finds a mail client with the given name and version
    * @param name the name
    * @param version the version
    * @return the mail client or <code>None</code> if there is no
    * mail client with the given name and version */
  def find(name: String, version: String): Option[Client] =
    coll.findOne(MongoDBObject("name" -> name, "version" -> version)) map toClient
  
  override def find(): Iterable[Client] = coll map toClient
  
  override def insert(client: Client) {
    coll += client
  }
}
