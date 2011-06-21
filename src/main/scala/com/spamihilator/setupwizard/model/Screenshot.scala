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
import java.io.{ByteArrayInputStream, InputStream}

/** A screenshot
  * @param id the screenshot's ID in the database
  * @param title the screenshot's title
  * @param width the screenshot's width in pixels
  * @param height the screenshot's height in pixels
  * @param client the client this screenshot belongs to */
abstract class Screenshot(val id: ObjectId, val title: String,
    val width: Int, val height: Int, val client: Client) {
  /** @return the screenshot's binary contents as a stream */
  def getInputStream(): InputStream
}

/** Factory for screenshot objects */
object Screenshot {
  /** Creates a new screenshot with an empty ID and the given contents
    * @param title the screenshot's title
    * @param width the screenshot's width in pixels
    * @param height the screenshot's height in pixels
    * @param client the client this screenshot belongs to
    * @param data the screenshot's binary contents */
  def apply(title: String, width: Int, height: Int, client: Client,
      data: Array[Byte]): Screenshot = {
    new Screenshot(new ObjectId(), title, width, height, client) {
      override def getInputStream() = new ByteArrayInputStream(data)
    }
  }
  
  /** Creates a new screenshot with an empty ID and the given contents
    * @param title the screenshot's title
    * @param width the screenshot's width in pixels
    * @param height the screenshot's height in pixels
    * @param client the client this screenshot belongs to
    * @param data the screenshot's binary contents */
  def apply(title: String, width: Int, height: Int, client: Client,
      data: InputStream): Screenshot = {
    new Screenshot(new ObjectId(), title, width, height, client) {
      override def getInputStream() = data
    }
  }
}
