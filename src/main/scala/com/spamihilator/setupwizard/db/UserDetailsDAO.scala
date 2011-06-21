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

import org.springframework.security.core.authority.GrantedAuthorityImpl
import org.springframework.security.core.userdetails._
import java.util.Arrays

/** An implementation of Spring's {@link UserDetailsService} which loads
  * user objects from the database, so Spring Security can run its
  * authentication mechanisms against them
  * @author Michel Kraemer */
class UserDetailsDAO extends UserDetailsService {
  override def loadUserByUsername(username: String): User = {
    val u = Database.userDao.find(username) getOrElse {
      throw new UsernameNotFoundException("Could not find user: " + username,
          username)
    }
    new User(username, u.passwordHash, true, true, true, true,
        Arrays.asList(new GrantedAuthorityImpl(u.role)))
  }
}
