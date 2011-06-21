Spamihilator Online Setup Wizard
================================

This is the new online setup wizard for the free anti-spam filter
[Spamihilator](http://www.spamihilator.com). It helps users to configure their
email clients for the use with Spamihilator.

This application has been developed using [Scala](http://www.scala-lang.org)
and the [Lift web framework](http://liftweb.net).

Contributing
------------

Contributions to this application are very welcome. There are two ways to
contribute:

* Write a tutorial for a new mail client
* Add new features to the application itself

### Writing a new tutorial

TODO

### Developing

If you like to contribute new features to the setup wizard itself, simply fork
the GitHub repository, add your code and then send me a pull request.

You need [sbt](https://github.com/harrah/xsbt) in order to build the setup
wizard. Run the following command to create a deployable `war` file:

    sbt package

You may also run the wizard directly on your local machine:

    sbt jetty-run shell

The application will then available at
[http://localhost:8080/](http://localhost:8080/). The integrated web server
[Jetty](http://jetty.codehaus.org/jetty/) will keep running as long as you
don't leave the sbt shell with `exit`.

If you like to develop with [Eclipse](http://www.eclipse.org) and the
[Scala IDE](http://www.scala-ide.org) you may use the following command:

    sbt eclipse

This command creates the project files for Eclipse. You may repeat this command
later to update your classpath if needed. 

License
-------

The Spamihilator Online Setup Wizard has been released under the
[Apache License, Version 2.0](http://www.apache.org/licenses/LICENSE-2.0) (the
"License").

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.

Third party software notices
----------------------------

### Clippy

This product includes software distributed by Tom Preston-Werner under the MIT
License. 

Copyright (c) 2008 Tom Preston-Werner, [https://github.com/mojombo/clippy](https://github.com/mojombo/clippy)

Permission is hereby granted, free of charge, to any person obtaining
a copy of this software and associated documentation files (the
'Software'), to deal in the Software without restriction, including
without limitation the rights to use, copy, modify, merge, publish,
distribute, sublicense, and/or sell copies of the Software, and to
permit persons to whom the Software is furnished to do so, subject to
the following conditions:

The above copyright notice and this permission notice shall be
included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED 'AS IS', WITHOUT WARRANTY OF ANY KIND,
EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY
CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT,
TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.

### jQuery Caret Plugin

This product includes software distributed by C. F. Wong under the MIT
License.

Copyright (c) 2010 C. F., Wong, [http://jquery-plugin.buss.hk/](http://jquery-plugin.buss.hk/)

Permission is hereby granted, free of charge, to any person obtaining
a copy of this software and associated documentation files (the
"Software"), to deal in the Software without restriction, including
without limitation the rights to use, copy, modify, merge, publish,
distribute, sublicense, and/or sell copies of the Software, and to
permit persons to whom the Software is furnished to do so, subject to
the following conditions:

The above copyright notice and this permission notice shall be
included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.

### jQuery UI

This product includes software distributed by Paul Bakaus under the MIT
License.

Copyright (c) 2011 Paul Bakaus, [http://jqueryui.com/](http://jqueryui.com/)

Permission is hereby granted, free of charge, to any person obtaining
a copy of this software and associated documentation files (the
"Software"), to deal in the Software without restriction, including
without limitation the rights to use, copy, modify, merge, publish,
distribute, sublicense, and/or sell copies of the Software, and to
permit persons to whom the Software is furnished to do so, subject to
the following conditions:

The above copyright notice and this permission notice shall be
included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
