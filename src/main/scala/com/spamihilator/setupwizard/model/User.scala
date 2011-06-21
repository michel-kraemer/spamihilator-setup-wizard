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

package com.spamihilator.setupwizard.model

import org.bson.types.ObjectId

/** A user
  * @param id the user's ID in the database
  * @param userName the user's name
  * @param passwordHash the hash of the user's password
  * @param realName the user's real name
  * @param email the user's email address
  * @param role the user's role. Used for authentication.
  * @author Michel Kraemer */
case class User(id: ObjectId, userName: String, passwordHash: String,
    realName: String, email: String, role: String)

/** Companion object for {@link User} */
object User {
  /** Creates a new user with an empty ID
    * @param userName the user's name
    * @param passwordHash the hash of the user's password
    * @param realName the user's real name
    * @param email the user's email address
    * @param role the user's role. Used for authentication. */
  def apply(userName: String, passwordHash: String, realName: String,
      email: String, role: String): User = User(new ObjectId(), userName,
          passwordHash, realName, email, role)
}
