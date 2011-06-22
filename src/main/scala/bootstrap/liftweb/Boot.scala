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

package bootstrap.liftweb

import com.spamihilator.setupwizard.rest.Images

import net.liftweb.http.Html5Properties
import net.liftweb.http.LiftRules
import net.liftweb.http.OnDiskFileParamHolder
import net.liftweb.http.ParsePath
import net.liftweb.http.Req
import net.liftweb.http.RewriteRequest
import net.liftweb.http.RewriteResponse
import net.liftweb.sitemap.Loc.Hidden
import net.liftweb.sitemap.Menu
import net.liftweb.sitemap.SiteMap

/** The application's boot loader
  * @author Michel Kraemer */
class Boot {
  /** Defines a sitemap of hidden entries just to make them accessible */
  def sitemap(): SiteMap = SiteMap(
    Menu.i("Home") / "index",
    Menu.i("Tutorial") / "tutorial" >> Hidden,
    Menu.i("Admin.Home") / "admin" / "index" >> Hidden,
    Menu.i("Admin.Profile.Password") / "admin" / "changepassword" >> Hidden,
    Menu.i("Admin.Edit.Tutorial") / "admin" / "edit" >> Hidden,
    Menu.i("Admin.Add.Client") / "admin" / "add" >> Hidden,
    Menu.i("Admin.Upload.Screenshots") / "admin" / "insertscreenshots" >> Hidden
  )
  
  def boot {
    LiftRules.addToPackages("com.spamihilator.setupwizard.home")
    LiftRules.addToPackages("com.spamihilator.setupwizard.admin")
    LiftRules.addToPackages("com.spamihilator.setupwizard.common")
    
    //force the request to be UTF-8
    LiftRules.early.append(_.setCharacterEncoding("UTF-8"))

    //use HTML5 for rendering
    LiftRules.htmlProperties.default.set((r: Req) =>
      new Html5Properties(r.userAgent))
    
    //write uploaded files to temporary directory
    LiftRules.handleMimeFile = OnDiskFileParamHolder.apply
    
    //set sitemap
    LiftRules.setSiteMapFunc(sitemap)
    
    //allow accessing "/admin" without trailing slash
    LiftRules.rewrite.append {
      case RewriteRequest(ParsePath(List("admin"), _, _, _), _, _) =>
        RewriteResponse("admin" :: "index" :: Nil, Map.empty[String, String])
    }
    
    //register REST services
    LiftRules.statelessDispatchTable.append(Images)
  }
}
