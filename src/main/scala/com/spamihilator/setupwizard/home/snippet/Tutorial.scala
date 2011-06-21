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

package com.spamihilator.setupwizard.home.snippet

import com.spamihilator.setupwizard.db.Database
import com.spamihilator.setupwizard.common.TextRenderer
import net.liftweb._
import http._
import provider.servlet._
import util.Helpers._
import scala.xml.Unparsed
import java.text.DateFormat
import java.util.Locale

/** Snippet for tutorials
  * @author Michel Kraemer */
class Tutorial {
  /** Renders the snippet */
  def render = {
    //check request parameters
    val clientSlug = S.param("mail-client") openOr {
      S.error("mail-client", S.?("error-slug"))
      S.redirectTo("/")
    }
    
    val client = Database.clientDao.find(clientSlug) getOrElse {
      S.error(S.?("error-client"))
      S.redirectTo("/")
    }
    
    val document = Database.documentDao.findLatest(client) getOrElse {
      S.error(S.?("error-document"))
      S.redirectTo("/")
    }
    
    //render text
    val text = TextRenderer.render(document.text, S.param("server"),
        S.param("username"))
    
    val req = S.containerRequest map { _.asInstanceOf[HTTPRequestServlet].req }
    val loc = req map { _.getLocale() } getOrElse Locale.ENGLISH
    val date = DateFormat.getDateTimeInstance(DateFormat.MEDIUM,
        DateFormat.MEDIUM, loc).format(document.date)
    
    ".name" #> client.name &
    ".version" #> client.version &
    ".authors *" #> document.authors.mkString(", ") &
    ".date *" #> date &
    ".text *" #> text
  }
}
