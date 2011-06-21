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
import com.spamihilator.setupwizard.model.Screenshot
import net.liftweb._
import common._
import http._
import S.?
import js._
import SHtml._
import JsCmds._
import js.jquery._
import JE._
import util.Helpers._
import scala.xml._

/** Snippets for the insert-screenshot dialog
  * @author Michel Kraemer */
class InsertScreenshots {
  /** Parses the request and tries to get the referred mail client
    * from the database.
    * @return the mail client
    * @throws IllegalStateException if the request is incorrect */
  private def getClientFromRequest() = {
    val slug = S.param("slug") openOr {
      throw new IllegalStateException("Please specify a mail client");
    }
    
    Database.clientDao.find(slug) getOrElse {
      throw new IllegalStateException("Unknown mail client: " + slug)
    }
  }
  
  /** Renders the dialog */
  def render = {
    def makeDeleteId(s: Screenshot) = "delete-screenshot-" + s.id
    
    /** Will be called when a screenshot is about to be deleted
      * @param s the screenshot */
    def onDelete(s: Screenshot) = {
      /** Deletes a screenshot from the database */
      def doIt(n: String) = {
        Database.screenshotDao.delete(s)
        Noop
      }
      
      //call doIt() through Ajax and then fade out the list item
      Run(ajaxCall("", doIt _)._2.toJsCmd) &
        JqJsCmds.FadeOut(makeDeleteId(s), 0 seconds, 300 millis)
    }
    
    /** @return an image tag for a given screenshot */
    def makeImageTag(s: Screenshot) = "[image title=\"" + s.title + "\"]"
    
    val client = getClientFromRequest()
    val screenshots = Database.screenshotDao.find(client).toList.sortBy(
        _.title.toLowerCase)
    ".screenshots" #> screenshots.map {
      case x =>
        ".screenshot [id]" #> makeDeleteId(x) &
        ".title" #> x.title &
        ".delete-link" #> ((n: NodeSeq) => a(() => Confirm(
          ?("really-delete", x.title), onDelete(x)), n)) &
        ".insert-into-text-link" #> ((n: NodeSeq) => a(() =>
          Call("insertIntoText", makeImageTag(x)), n))
    }
  }
  
  /** Renders the file upload form */
  def renderForm = {
    var fileHolder: Box[FileParamHolder] = Empty
    
    val client = getClientFromRequest()
    
    def process() {
      fileHolder match {
        case Full(f) =>
          val sd = Database.screenshotDao
          if (sd.exists(f.fileName)) {
            S.error(?("already-uploaded", f.fileName))
          } else {
            sd.insert(Screenshot(f.fileName, 0, 0, client, f.fileStream))
            S.notice(?("successfully-uploaded", f.fileName))
          }
        case _ =>
          S.error(?("select-a-file"))
      }
    }
    
    "#slug [value]" #> client.slug &
    "#screenshot-file" #> SHtml.fileUpload(f => fileHolder = Full(f)) &
    "#upload-button" #> SHtml.onSubmitUnit(process) &
    "#upload-button [value]" #> ?("upload")
  }
}
