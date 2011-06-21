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

package com.spamihilator.setupwizard.admin.snippet

import com.spamihilator.setupwizard.db.{CurrentUserAware, Database}
import com.spamihilator.setupwizard.model.Document
import com.spamihilator.setupwizard.common.TextRenderer
import net.liftweb._
import http._
import util.Helpers._
import scala.xml.Unparsed

/** A page that allows editing mail client tutorials
  * @author Michel Kraemer */
object Edit extends CurrentUserAware {
  private object previewText extends RequestVar("")
  
  /** Parses the request and tries to get the referred mail client
    * from the database. Produces an error message and redirects the user
    * to the administration page if the request is incorrect.
    * @return the mail client */
  private def getClientFromRequest() = {
    val slug = S.param("slug") openOr {
      S.error("Please specify the mail client to be edited");
      S.redirectTo("/admin")
    }
    
    Database.clientDao.find(slug) getOrElse {
      S.error("Unknown mail client")
      S.redirectTo("/admin")
    }
  }
  
  /** Renders the page */
  def render = {
    val blank: Option[String] = None
    val client = getClientFromRequest()
    val p = previewText.is
    previewText.set("")
    ".name" #> client.name &
    ".version" #> client.version &
    (if (p.isEmpty)
      ".preview" #> blank &
      ".preview-heading" #> blank
    else
      ".preview *" #> TextRenderer.render(p, Some("pop.server.com"),
          Some("username")))
  }
  
  /** Renders the edit form */
  def renderForm = {
    val client = getClientFromRequest()
    
    val realName = {for (user <- Database.userDao.find(currentUsername))
      yield user.realName} getOrElse currentUsername

    val document = Database.documentDao.findLatest(client) getOrElse
      Document("", List(realName), client)
    object text extends RequestVar(document.text)
    
    def process() {
      if (text.is != document.text) {
        //add current user as author
        val authors = if (!document.authors.contains(realName))
          document.authors :+ realName else document.authors
        
        //save document to database
        val doc = Document(text, authors, client)
        Database.documentDao.insert(doc)
        S.notice("Tutorial successfully saved.")
      } else {
        S.notice("Nothing to save.")
      }
    }
    
    "#slug [value]" #> client.slug &
    "#text" #> SHtml.textareaElem(text) &
    "#preview-button" #> SHtml.onSubmitUnit(() => previewText.set(text)) &
    "#save-button" #> SHtml.onSubmitUnit(process) &
    "#cancel-button" #> SHtml.onSubmitUnit(() => S.redirectTo("/admin"))
  }
}
