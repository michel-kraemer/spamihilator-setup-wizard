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

import com.spamihilator.setupwizard.model.User
import com.mongodb.casbah.Imports._

/** A data access object for user account objects
  * @param db the handle to the MongoDB database
  * @author Michel Kraemer */
class UserDAO(db: MongoDB) extends DAO[User] {
  private lazy val coll = db("users")
  
  coll.ensureIndex("userName")
  
  /** Converts a database object to a user
    * @param o the database object
    * @return the user */
  private def toUser(o: DBObject) = User(
    o.getAs[ObjectId]("_id").get,
    o.getAs[String]("userName") getOrElse "",
    o.getAs[String]("passwordHash") getOrElse "",
    o.getAs[String]("realName") getOrElse "",
    o.getAs[String]("email") getOrElse "",
    o.getAs[String]("role") getOrElse "ROLE_USER"
  )
  
  /** Converts a user to a database object
    * @param user the user to convert
    * @return the database object */
  private implicit def userToDBObject(user: User): DBObject = MongoDBObject(
    "userName" -> user.userName,
    "passwordHash" -> user.passwordHash,
    "realName" -> user.realName,
    "email" -> user.email,
    "role" -> user.role
  )
  
  override def find(): Iterable[User] = coll map toUser
  
  /** Finds a user with the given name
    * @param userName the user's name
    * @return the user or <code>None</code> if there is no user
    * with such a name */
  def find(userName: String): Option[User] =
    coll.findOne(MongoDBObject("userName" -> userName)) map toUser
  
  def insert(user: User) {
    coll += user
  }
  
  /** Updates a user already stored in the database
    * @param user a user object containing the information to update. The
    * user's ID must refer to an existing database item. */
  def update(user: User) {
    coll.update(MongoDBObject("_id" -> user.id), user)
  }
}
