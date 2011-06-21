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

import com.spamihilator.setupwizard.db.Database
import com.spamihilator.setupwizard.model.Client
import net.liftweb.http.RequestVar
import net.liftweb.util.Helpers._
import net.liftweb.http.SHtml
import net.liftweb.http.S

/** Snippet for the add-mail-client page
  * @author Michel Kraemer */
object Add {
  private object name extends RequestVar("")
  private object version extends RequestVar("")
  private object slug extends RequestVar("")
  
  /** Renders this snippet */
  def render = {
    "#name" #> SHtml.textElem(name) &
    "#version" #> SHtml.textElem(version) &
    "#slug" #> SHtml.textElem(slug) &
    "#add-button" #> SHtml.onSubmitUnit(process) &
    "#cancel-button" #> SHtml.onSubmitUnit(() => S.redirectTo("/admin"))
  }
  
  /** Will be called when the user presses the add button */
  private def process() {
    //validate form
    if (name.isEmpty) {
      S.error("Please enter a name")
    }
    
    if (version.isEmpty) {
      S.error("Please enter a version")
    }
    
    if (slug.isEmpty) {
      S.error("Please enter a slug")
    }
    
    if (S.errors.isEmpty) {
      //check if the client and/or the slug are already used
      Database.clientDao.find(name, version) match {
        case Some(_) => S.error(name + " " + version + " does already exist")
        case _ =>
          Database.clientDao.find(slug) match {
            case Some(e) => S.error("This slug is already reserved for " +
                e.name + " " + e.version)
            case _ =>
              //save client to database
              val client = Client(name, version, slug)
              Database.clientDao.insert(client)
              
              //send user back to admin page
              S.redirectTo("/admin")
          }
      }
    }
  }
}
