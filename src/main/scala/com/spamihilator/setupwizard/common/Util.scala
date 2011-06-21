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

import java.net.URLEncoder

/** A mixin for utility methods
  * @author Michel Kraemer */
trait Util {
  /** Translates a string into <code>application/x-www-form-urlencoded</code>
    * format using UTF-8 encoding */
  protected def urlencode(s: String) = URLEncoder.encode(s, "UTF-8")
}
