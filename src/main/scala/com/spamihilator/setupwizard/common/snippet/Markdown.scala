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

package com.spamihilator.setupwizard.common.snippet

import net.liftweb.http.DispatchSnippet
import scala.xml.NodeSeq
import scala.xml.Text
import scala.xml.Unparsed
import org.pegdown.PegDownProcessor
import net.liftweb.http.LiftRules
import net.liftweb.http.S
import java.io.File

/** A snippet that renders Markdown markup. This snippet has only one
  * parameter: the name of a file containing the markup to render. If the
  * filename does not have the extension <code>.md</code>, it will automatically
  * be added. The snippet also handles the current locale. The file will be
  * searched in the directory of the calling snippet as well as in the
  * <code>templates-hidden</code> subdirectory.
  * 
  * Example: let's assume the current locale is <code>en_US</code> and the
  * calling snippet is in the <code>foo</code> directory. The snippet will
  * look for the following files in the given order:
  * <pre>
  * /foo/file_en_US.md
  * /foo/templates-hidden/file_en_US.md
  * /foo/file_en.md
  * /foo/templates-hidden/file_en.md
  * /foo/file.md
  * /foo/templates-hidden/file.md
  * </pre>
  * @author Michel Kraemer */
object Markdown extends DispatchSnippet {
  /** Dispatch function for this snippet */
  def dispatch: DispatchIt = {
    case file => render(file)
  }
  
  /** Appends suffixes to the given filename. Also takes care of the
    * extension <code>.md</code>. Adds this extension if needed
    * @param the filename of a markdown file (either with extension or not)
    * @param suffixes the list of suffixes to append to the filename (just
    * before the extension)
    * @return a list of filenames with suffixes and extension */
  private def appendSuffixes(file: String, suffixes: List[String]) = {
    val fo = if (file.toLowerCase.endsWith(".md"))
      file.substring(0, file.length - 3) else file
    suffixes map { s => fo + s + ".md" }
  }
  
  /** Tries to load the contents of the given file
    * @param path the path to the file to load
    * @return the file contents or <code>None</code> if the file
    * could not be found */
  private def doLoadMarkup(path: List[String]): Option[String] =
    LiftRules.loadResourceAsString("/" + path.mkString("/"))
  
  /** Tries to load the markup from the given file. Also searches the
    * <code>templates-hidden</code> subdirectory if the file does not exist
    * in the given path
    * @param path the path to search
    * @param file the filename (without path)
    * @return the file's contents or <code>None</code> if the file was
    * not found */
  private def loadMarkup(path: List[String], file: String): Option[String] =
    doLoadMarkup(path :+ file) orElse
    doLoadMarkup(path :+ "templates-hidden" :+ file)
  
  /** Tries to load the markup from one of the given files. Also searches
    * the <code>templates-hidden</code> subdirectory of the given path does
    * not contain the files.
    * @param path the path to search for the files
    * @param files the filenames (without path)
    * @return the contents of the first file found or <code>None</code> if
    * no matching file was found */
  private def loadMarkup(path: List[String], files: List[String]): Option[String] =
    (for {
      f <- files
      m <- loadMarkup(path, f)
    } yield m) headOption
  
  /** Renders the given markdown file
    * @param file the file to render */
  private def render(file: String)(ns: NodeSeq) = {
    val locale = S.locale
    val sls = List("_" + locale.toString, "_" + locale.getLanguage, "")
    
    //load markup from file
    val markup = (for {
      req <- S.request
      path = req.path.partPath.dropRight(1)
      m <- loadMarkup(path, appendSuffixes(file, sls))
    } yield m) getOrElse {
      throw new IllegalStateException("Could not find markdown file: " + file)
    }
    
    //render markup
    val pegDown = new PegDownProcessor()
    Unparsed(pegDown.markdownToHtml(markup))
  }
}
