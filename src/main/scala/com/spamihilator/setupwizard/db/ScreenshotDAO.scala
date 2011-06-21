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

import com.spamihilator.setupwizard.model.{Client, Screenshot}
import com.mongodb.casbah.Imports._
import com.mongodb.casbah.gridfs.Imports._

/** A data access object for screenshots
  * @param db the handle to the MongoDB database
  * @author Michel Kraemer */
class ScreenshotDAO(db: MongoDB) extends DAO[Screenshot] {
  private lazy val coll = db("screenshots")
  
  coll.ensureIndex("client")
  coll.ensureIndex("title")
  
  /** Converts a database object to a screenshot. Sets the given client
    * as the screenshot's owner.
    * @param client the client
    * @param o the database object
    * @return the screenshot */
  private def toScreenshot(client: Client, o: DBObject): Screenshot = new Screenshot(
    o.getAs[ObjectId]("_id").get,
    o.getAs[String]("title") getOrElse "",
    o.getAs[Int]("width") getOrElse 0,
    o.getAs[Int]("height") getOrElse 0,
    client
  ) {
    override def getInputStream() = {
      val gridfs = GridFS(db)
      gridfs.find(o.getAs[ObjectId]("dataid").get).underlying.getInputStream()
    }
  }
  
  /** Converts a database object to a screenshot
    * @param o the database object
    * @return the screenshot */
  private def toScreenshot(o: DBObject): Screenshot = {
    val client = (for (id <- o.getAs[ObjectId]("client");
        c <- Database.clientDao.find(id)) yield c) getOrElse {
      throw new IllegalStateException("Screenshot has an unknown client")
    }
    toScreenshot(client, o)
  }
  
  /** Converts a screenshot to a database object
    * @param screenshot the screenshot
    * @return the database object */
  private implicit def screenshotToDBObject(screenshot: Screenshot)
      (implicit dataId: ObjectId): DBObject = MongoDBObject(
    "title" -> screenshot.title,
    "width" -> screenshot.width,
    "height" -> screenshot.height,
    "client" -> screenshot.client.id,
    "dataid" -> dataId
  )
  
  override def find(): Iterable[Screenshot] = coll map toScreenshot
  
  /** Finds all screenshots for a given client
    * @param client the client
    * @return all screenshots for this client */
  def find(client: Client): Iterable[Screenshot] =
    (coll.find(MongoDBObject("client" -> client.id)) map {
      s => toScreenshot(client, s) }).toList
  
  /** Finds a screenshot with a given title
    * @param title the title
    * @return the screenshot or <code>None</code> if there is no
    * screenshot with that title */
  def find(title: String): Option[Screenshot] =
    coll.findOne(MongoDBObject("title" -> title)) map toScreenshot
  
  override def insert(screenshot: Screenshot) {
    if (exists(screenshot.title)) {
      throw new IllegalStateException("A screenshot with the title \"" +
          screenshot.title + "\" already exists in the database")
    }
    
    val f = GridFS(db).createFile(screenshot.getInputStream, screenshot.title)
    f.save
    implicit val dataId = f._id.get
    coll += screenshot
  }
  
  /** Deletes a screenshot from the database
    * @param screenshot the screenshot to delete */
  def delete(screenshot: Screenshot) {
    coll.findOne(MongoDBObject("_id" -> screenshot.id)) map { obj =>
      coll.remove(obj)
      obj.getAs[ObjectId]("dataid") map GridFS(db).remove
    }
  }
  
  /** @return the number of screenshots having the given title */
  def count(title: String) = coll.count(MongoDBObject("title" -> title))
  
  /** @return true if there is a screenshot having the given title */
  def exists(title: String) = count(title) > 0
}
