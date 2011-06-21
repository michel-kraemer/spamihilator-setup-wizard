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

import com.spamihilator.setupwizard.common.BCryptPasswordEncoder
import com.spamihilator.setupwizard.db.{CurrentUserAware, Database}
import com.spamihilator.setupwizard.model.Client
import net.liftweb._
import http._
import http.S.?
import util.Helpers._

/** Snippet for the change-password page
  * @author Michel Kraemer */
class ChangePassword extends CurrentUserAware {
  /** Renders this snippet */
  def render = {
    var oldpassword = ""
    var newpassword = ""
    var repeat = ""
      
    def process() {
      if (newpassword.isEmpty) {
        S.error(?("enter-password"))
        return
      }
      
      if (repeat.isEmpty)  {
        S.error(?("repeat-password"))
        return
      }
      
      if (newpassword != repeat) {
        S.error(?("do-not-match"))
        return
      }
      
      val user = Database.userDao.find(currentUsername) getOrElse {
        S.error(?("not-logged-in"))
        return
      }
      
      val encoder = new BCryptPasswordEncoder()
      if (!encoder.isPasswordValid(user.passwordHash, oldpassword, null)) {
        S.error(?("incorrect-old"))
        return
      }
      
      val newpasswordHash = encoder.encodePassword(newpassword, null)
      val user2 = user.copy(passwordHash = newpasswordHash)
      Database.userDao.update(user2)
      
      S.redirectTo("/admin/")
    }
    
    "#old" #> SHtml.password(oldpassword, oldpassword = _) &
    "#new" #> SHtml.password(newpassword, newpassword = _) &
    "#repeat" #> SHtml.password(repeat, repeat = _) &
    "#save-button" #> SHtml.onSubmitUnit(process) &
    "#save-button [value]" #> ?("save") &
    "#cancel-button" #> SHtml.onSubmitUnit(() => S.redirectTo("/admin")) &
    "#cancel-button [value]" #> ?("cancel")
  }
}
