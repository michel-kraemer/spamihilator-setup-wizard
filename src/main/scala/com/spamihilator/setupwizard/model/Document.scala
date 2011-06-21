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
import java.util.{Calendar, Date}

/** A document containing a mail client tutorial
  * @param id the document's ID in the database
  * @param text the document's contents
  * @param authors a list of authors who contributed to this document
  * @param date the date when the document has been created or updated
  * @param client the client this document is about
  * @author Michel Kraemer */
case class Document(id: ObjectId, text: String, authors: List[String],
    date: Date, client: Client)

/** Companion object for {@link Document} */
object Document {
  /** Creates a new document with an empty database ID. Uses the current
    * time as the document's creation date.
    * @param text the document's contents
    * @param authors a list of authors who contributed to this document
    * @param client the client this document is about */
  def apply(text: String, authors: List[String], client: Client): Document =
    Document(new ObjectId(), text, authors, Calendar.getInstance().getTime(),
        client)
}
