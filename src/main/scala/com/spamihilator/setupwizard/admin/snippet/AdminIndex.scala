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

import com.spamihilator.setupwizard.db.CurrentUserAware
import com.spamihilator.setupwizard.db.Database
import net.liftweb._
import http._
import util.Helpers._

/** The setup wizard administration page
  * @author Michel Kraemer */
class AdminIndex extends CurrentUserAware {
  /** Renders this snippet */
  def render = {
    val realName = {for (user <- Database.userDao.find(currentUsername))
      yield user.realName} getOrElse currentUsername
    val clients = Database.clientDao.find().toList.sortBy(c =>
      c.name + " " + c.version)
    ".login" #> realName &
    ".client *" #> clients.map {
      case x =>
        ".name" #> x.name &
        ".version" #> x.version &
        ".editlink [href]" #> ("/admin/edit.html?slug=" + x.slug)
    }
  }
}
