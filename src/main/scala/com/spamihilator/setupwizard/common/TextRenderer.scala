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

package com.spamihilator.setupwizard.common
import scala.util.matching.Regex
import scala.xml.NodeSeq
import scala.xml.Unparsed

import org.pegdown.PegDownProcessor

import net.liftweb.http.S
import S.?

/** Renders tutorials using the markdown syntax augmented with short tags in
  * the form <code>[tag name=value]</code>.
  * @author Michel Kraemer */
object TextRenderer extends Util {
  /** Regular expression fragments */
  private val mandatoryParams = """([^\]]+)"""
  private val optionalParams = """([^\]]*)"""
  
  /** Some regular expressions used to parse tags */
  private val imageTagRegex = """\[\s*image""" + mandatoryParams + """\]"""r
  private val paramRegex = """([a-zA-Z0-9_-]+)\s*=\s*([^\s"]+|"[^"]+")"""r
  private val quoteRegex = "\"([^\"]+)\""r
  private val usernameRegex = """\[\s*username""" + optionalParams + """\]"""r
  private val newUsernameRegex = """\[\s*newusername""" + optionalParams + """\]"""r
  private val serverRegex = """\[\s*server""" + optionalParams + """\]"""r
  private val newServerRegex = """\[\s*newserver""" + optionalParams + """\]"""r
  
  /** Removes surrounding quotation marks, if there are any
    * @param s the string potentially containing quotation marks
    * @return the new string (without quotation marks) or the old one (if
    * it did not contain quotation marks) */
  private def unquote(s: String) = s match {
    case quoteRegex(s) => s
    case s => s
  }
  
  /** Parses the parameters of a short tag
    * @param s the string containing the tag and its parameters
    * @return a map of parameter names and values */
  private def parseTagParams(s: String) =
    (paramRegex.findAllIn(s).matchData.toList map { p =>
      (p.group(1), unquote(p.group(2)))
    }).toMap
  
  /** Creates the HTML code for a small button the user can press
    * to copy the given text into the clipboard
    * @param content the text to copy
    * @return the HTML code */
  private def createClippy(content: String) = {
    val flashVars = "text=" + urlencode(content) +
      "&label=" + ?("clippy-copy") + "&feedback=" + ?("clippy-copied")
    val clippyUrl = S.hostAndPath + "/toserve/clippy/clippy.swf"
    <span class="copy">
      <object classid="clsid:d27cdb6e-ae6d-11cf-96b8-444553540000"
        width="110"
        height="16"
        id="clippy">
        <param name="movie" value={ clippyUrl } />
        <param name="allowScriptAccess" value="always" />
        <param name="quality" value="high" />
        <param name="scale" value="noscale" />
        <param NAME="FlashVars" value={ flashVars } />
        <param name="bgcolor" value="#ffffff" />
        <embed src={ clippyUrl }
          width="110"
          height="16"
          name="clippy"
          quality="high"
          allowScriptAccess="always"
          type="application/x-shockwave-flash"
          pluginspage="http://www.macromedia.com/go/getflashplayer"
          FlashVars={ flashVars }
          bgcolor="#ffffff"
        />
      </object>
    </span>
  }
  
  /** Replaces all occurrences of a short tag in the given text by a given
    * replacement string. If the short tag contains the parameter
    * <code>copy=true</code> a small button will be displayed next to the
    * string. This button can be used to copy the replacement string to the
    * clipboard.
    * @param text the text to search
    * @param regex the regular expression describing the text to replace
    * @param replacement the replacement string
    * @return the new text */
  private def replaceValue(text: String, regex: Regex, replacement: String) = {
    var newText = text
    var offset = 0
    regex.findAllIn(text).matchData.toList map { m =>
      val params = parseTagParams(m.group(1).trim)
      val copy = params.get("copy") map { _.toBoolean } getOrElse false
      
      val newreplacement = if (copy) {
        val clippy = createClippy(replacement)
        val span =
          <span class="text-with-clippy">
            { replacement ++ clippy }
          </span>
        span.toString
      } else {
        replacement
      }
      
      newText = newText.substring(0, m.start - offset) +
        newreplacement + newText.substring(m.end - offset)
      offset += (m.end - m.start) - newreplacement.length
    }
    newText
  }
  
  /** Renders the tutorial
    * @param text the tutorial's raw source
    * @param server the server for incoming mail. The content of short tags
    * like <code>[server]</code> and <code>[newserver]</code> will be
    * calculated using this value. If it is <code>None</code> the default value
    * <code>pop.server.com</code> will be used.
    * @param username the username for the mail account. The content of short
    * tags like <code>[username]</code> and <code>[newusername]</code> will be
    * calculated using this value. If it is <code>None</code> the default value
    * <code>username</code> will be used.
    * @return the rendered text */
  def render(text: String, server: Option[String],
      username: Option[String]): Option[NodeSeq] = {
    val defaultserver = "pop.server.com"
    val defaultusername = "username"
    
    //calculate replacement string
    val oldserver = server match {
      case None => defaultserver  
      case Some("") => defaultserver
      case Some(s) => s
    }
    val oldusername = username match {
      case None => defaultusername
      case Some("") => defaultusername
      case Some(u) => u
    }
    val newserver = "localhost"
    val newusername = oldserver + "&" + oldusername

    //replace all image tags
    var newText = text
    var offset = 0
    imageTagRegex.findAllIn(text).matchData.toList map { m =>
      val params = parseTagParams(m.group(1).trim)
      val filename = params.get("title") getOrElse ""
      val alt = params.get("alt") getOrElse filename
      val serverpos = params.get("server-pos") map { "&server_pos=" +
        urlencode(_) } getOrElse ""
      val usernamepos = params.get("username-pos") map { "&username_pos=" +
        urlencode(_) } getOrElse ""
      val usenewvalues = params.get("use-new-values") map {
        _.toBoolean } getOrElse false
      val qserver = if (usenewvalues) newserver else oldserver
      val qusername = if (usenewvalues) newusername else oldusername
      
      val query = "?server=" + urlencode(qserver) + "&username=" +
        urlencode(qusername) + serverpos + usernamepos
      val replacement = "![" + alt + "](" + S.hostAndPath + "/images/" +
        filename + query + ")"
      
      newText = newText.substring(0, m.start - offset) +
        replacement + newText.substring(m.end - offset)
      offset += (m.end - m.start) - replacement.length
    }
    
    //replace server and username values
    newText = replaceValue(newText, usernameRegex, oldusername)
    newText = replaceValue(newText, newUsernameRegex, newusername)
    newText = replaceValue(newText, serverRegex, oldserver)
    newText = replaceValue(newText, newServerRegex, newserver)
    
    //process markdown markup
    val pegDown = new PegDownProcessor()
    Some(Unparsed(pegDown.markdownToHtml(newText)))
  }
}
