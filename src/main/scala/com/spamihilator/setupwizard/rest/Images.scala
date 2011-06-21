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

package com.spamihilator.setupwizard.rest

import com.spamihilator.setupwizard.db.Database
import net.liftweb._
import common._
import http._
import rest._
import java.awt.{Color, Graphics2D, Rectangle}
import java.awt.image.BufferedImage
import java.io.ByteArrayOutputStream
import javax.imageio.ImageIO
import scala.annotation.tailrec

/** A rest service providing screenshot for mail client tutorials. Screenshots
  * are requested by their title:
  * 
  * {{{
  * /images/<title>
  * }}}
  * 
  * The service searches the image for areas having the color
  * <code>#ff00ff</code>. Such areas will be filled with a white background
  * color and one of two variables. The first area will be filled with the
  * server variable and the second one with the username variable. Both
  * variables can be passed as service
  * parameters:
  * <ul>
  * <li><strong>server</strong><br />
  *   The server variable</li>
  * <li><strong>username</strong><br />
  *   The username variable</li>
  * <li><strong>server_pos</strong><br />
  *   A number denoting which area should be filled with the server variable
  *   (defaults to 0)</li>
  * <li><strong>username_pos</strong><br />
  *   A number denoting which area should be filled with the username variable
  *   (defaults to 1)</li>
  * </ul> */
object Images extends RestHelper {
  /** Checks if the given pixel is a placeholder pixel with the color
    * <code>#ff00ff</code> */
  private def isPlaceholder(bi: BufferedImage, x: Int, y: Int) =
    ((bi.getRGB(x, y) & 0xFFFFFF) == 0xFF00FF)
  
  /** Finds the right-most pixel of a placeholder area
    * @param x the x ordinate of the pixel where the search should start
    * @param y the y position
    * @return the x ordinate of the right-most pixel in the placeholder area */
  @tailrec
  private def findRight(bi: BufferedImage, x: Int, y: Int): Int =
    if (x < bi.getWidth - 1 && isPlaceholder(bi, x + 1, y))
      findRight(bi, x + 1, y)
    else
      x
  
  /** Finds the lowest pixel of a placeholder area
    * @param x the x ordinate of the pixel where the search should start
    * @param y the y position
    * @return the y ordinate of the lowest pixel in the placeholder area */
  @tailrec
  private def findBottom(bi: BufferedImage, x: Int, y: Int): Int =
    if (y < bi.getHeight - 1 && isPlaceholder(bi, x, y + 1))
      findBottom(bi, x, y + 1)
    else
      y
  
  /** Finds the bottom right pixel of a placeholder area
    * @param x the x ordinate of the pixel where the search should start
    * @param y the y position
    * @return the coordinate the placeholder area's bottom right pixel */
  private def findBottomRight(bi: BufferedImage, x: Int, y: Int): (Int, Int) = {
    val right = findRight(bi, x, y)
    val bottom = findBottom(bi, right, y)
    (right, bottom)
  }
  
  /** Draws a variable. Tries to find the area where the variable should be put
    * by parsing the request parameter <code>name_pos</code>. If this parameter
    * is empty, the method will use a default area denoted by the
    * <code>defaultpos</code> parameter.
    * @param name the variable's name
    * @param defaultpos a number denoting the area where the variable should
    * be put
    * @param g the image
    * @param areas the coordinates of all placeholder areas in this image */
  private def drawVariable(name: String, defaultpos: Int, g: Graphics2D,
      areas: List[(Int, Int, Int, Int)]) {
    val pos = S.param(name + "_pos") map { _.toInt } getOrElse defaultpos
    if (pos >= 0 && pos < areas.length) {
      for (t <- S.param(name)) {
        val a = areas(pos)
        g.setClip(new Rectangle(a._1 + 3, a._2, a._3 - 4, a._4))
        g.drawString(t, a._1 + 3, a._2 + a._4 - 4)
        g.setClip(null)
      }
    }
  }
  
  /** Prepares an image before sending it to the client. Replaces all
    * placeholder areas by the respective variables. 
    * @param bi the image */
  private def prepareImage(bi: BufferedImage) {
    val g = bi.createGraphics
    g.setColor(Color.WHITE)
    
    var areas = List.empty[(Int, Int, Int, Int)]
    
    for (y <- 0 to bi.getHeight - 1) {
      for (x <- 0 to bi.getWidth - 1) {
        if (isPlaceholder(bi, x, y)) {
          val bottomRight = findBottomRight(bi, x, y)
          val w = bottomRight._1 - x + 1
          val h = bottomRight._2 - y + 1
          g.fillRect(x, y, w, h)
          areas :+= (x, y, w, h)
        }
      }
    }
    
    g.setColor(Color.BLACK)
    drawVariable("server", 0, g, areas)
    drawVariable("username", 1, g, areas)
  }
  
  serve {
    case "images" :: image :: Nil Get req =>
      //get filename including extension (image does not contain the extension)
      val filename = req.uri.substring(req.uri.lastIndexOf("/") + 1)
      val ext = {
        val dot = filename.lastIndexOf('.')
        if (dot == -1)
          ".png"
        else
          filename.substring(dot + 1)
      }
      
      ((for (img <- Database.screenshotDao.find(filename)) yield {
        val bi = ImageIO.read(img.getInputStream)
        prepareImage(bi)
        val writer = ImageIO.getImageWritersBySuffix(ext).next
        val baos = new ByteArrayOutputStream()
        val ios = ImageIO.createImageOutputStream(baos)
        writer.setOutput(ios)
        writer.write(bi)
        InMemoryResponse(baos.toByteArray, Nil, Nil, 200)
      }) getOrElse {
        NotFoundResponse("Unknown image")
      }): LiftResponse
  }
}
