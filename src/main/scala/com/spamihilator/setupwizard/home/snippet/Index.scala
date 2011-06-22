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
import net.liftweb._
import common._
import http._
import util.Helpers._

/** Snippet for the setup wizard's main page
  * @author Michel Kraemer */
object Index {
  private object server extends SessionVar("")
  private object username extends SessionVar("")
  private object client extends SessionVar("")
  
  /** Renders the page */
  def render = {
    val blank: Option[String] = None
    val clients = Database.clientDao.find().filter(c =>
      (for (d <- Database.documentDao.findLatest(c)) yield
        !d.text.trim.isEmpty) getOrElse false
    ).toList.sortBy(c => c.name + " " + c.version)
    val clientRadio = SHtml.radio(clients.map(_.slug).toList,
        Full(client.is), client.set)
    ".client *" #> clients.map {
      case x =>
        ".name" #> x.name &
        ".version" #> x.version &
        "input" #> clientRadio(x.slug) &
        "label [for]" #> x.slug
    } &
    "#server" #> SHtml.textElem(server) &
    "#username" #> SHtml.textElem(username) &
    "type=submit" #> SHtml.onSubmitUnit(process) &
    "type=submit [value]" #> S.loc("next")
  }
  
  /** Redirects the user to a tutorial when he presses the "next" button */
  private def process() = {
    val querySeq =
      (server.is match {
        case "" => Nil
        case s => Seq("server=" + urlEncode(s))
      }) ++
      (username.is match {
        case "" => Nil
        case u => Seq("username=" + urlEncode(u))
      }) ++
      (client.is match {
        case "" => Nil
        case c => Seq("mail-client=" + urlEncode(c))
      })
    
    val query = if (querySeq.isEmpty) "" else "?" + querySeq.mkString("&")
    S.redirectTo("tutorial.html" + query)
  }
}
