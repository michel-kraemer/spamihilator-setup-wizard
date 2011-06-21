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
import java.net.URL

/** A mail client
  * @param id the client's ID in the database
  * @param name the client's name
  * @param version the version number
  * @param slug the human-readable ID of this client. Used to reference it
  * in URLs and the like.
  * @param url an optional URL to the mail client's website
  * @param icon an optional program icon
  * @author Michel Kraemer */
case class Client(id: ObjectId, name: String, version: String,
    slug: String, url: Option[URL], icon: Option[String])

/** Companion object for {@link Client} */
object Client {
  /** Creates a new mail client object with an empty database ID,
    * no URL and no icon
    * @param name the client's name
    * @param version the version number
    * @param slug the human-readable ID of this client. Used to reference it
    * in URLs and the like. */
  def apply(name: String, version: String, slug: String): Client =
    Client(new ObjectId(), name, version, slug, None, None)
}
