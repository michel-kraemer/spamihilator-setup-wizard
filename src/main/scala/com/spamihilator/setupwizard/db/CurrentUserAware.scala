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

package com.spamihilator.setupwizard.db

import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.Authentication

/** Provides methods to access the current user (can be used as a mixin)
  * @author Michel Kraemer */
trait CurrentUserAware {
  /** @return the current authenticated principal */
  protected def currentAuthentication: Option[Authentication] = {
    val ctx = SecurityContextHolder.getContext
    if (ctx == null) {
      return None
    }
    
    val auth = ctx.getAuthentication
    if (auth == null) {
      return None
    }
    
    Some(auth)
  }
  
  /** @return the identity of the current principal */
  protected def currentPrincipal = {
    currentAuthentication match {
      case Some(auth) => {
        val p = auth.getPrincipal
        if (p != null) Some(p) else None
      }
      case _ => None
    }
  }
  
  /** @return the username of the current principal */
  protected def currentUsername = {
    currentPrincipal match {
      case Some(ud: UserDetails) => ud.getUsername
      case Some(p) => p.toString
      case _ => ""
    }
  }
  
  /** @return true if the user is currently logged in */
  protected def isLoggedIn = 
    currentPrincipal.exists(_.isInstanceOf[UserDetails])
}
