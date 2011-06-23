To format your mail client tutorial you can use the
[Markdown markup language](http://daringfireball.net/projects/markdown/).
Markdown makes it easy to to format a text while still keeping it readable.
The text will be converted to HTML later. If the expressiveness of Markdown
is not enough for you, you can still add your own HTML tags.

Markdown quick reference
------------------------

Here are the most important elements of the Markdown markup language. Please
refer to the full [Markdown syntax](http://daringfireball.net/projects/markdown/syntax)
for more information.

### Headings

#### Level 1:

    Level 1
    =======

or

    # Level 1

#### Level 2:

    Level 2
    -------

or

    ## Level 2

#### Other levels:

You have to use the `#` character for further levels:

    ### Level 3
    #### Level 4

### Emphasize

*Emphasized* text:

    *Emphasized*

or

    _Emphasized_

**Strong** text:

    **Strong**

or

    __Strong__

### Lists

#### Unnumbered lists:

    * Item 1
    * Item 2
    * Item 3

or

    + Item 1
    + Item 2
    + Item 3

or

    - Item 1
    - Item 2
    - Item 3

#### Numbered lists:

    1. Item 1
    2. Item 2
    3. Item 3

### Links

Links consist of a text and a URI. The following code

    [Help](help.html)

produces a link back to this page: [Help](help.html)

Additional elements
-------------------

The Spamihilator Online Setup Wizard supports additional elements you can
use in your tutorials. These elements are called *short tags*. They are
enclosed in square brackets.

### [server] and [username]

These tags will be replaced by the server or the username respectively. You may
optionally use the `copy` parameter if you like to add a small icon that the
user can click in order to copy the server or the username to the clipboard.
This parameter can be `true` or `false`, depending on whether you want that
icon or not. The default is `false`.

#### Example 1

    **Server**: [server]  
    **Username**: [username]

will be replaced by

**Server**: pop.server.com  
**Username**: john

#### Example 2

    [username copy=true]

will be rendered to

<span class="text-with-clippy">john <span class="copy">
<object classid="clsid:d27cdb6e-ae6d-11cf-96b8-444553540000"
  width="110" height="16" id="clippy">
  <param name="movie" value="../toserve/clippy/clippy.swf" />
  <param name="allowScriptAccess" value="always" />
  <param name="quality" value="high" />
  <param name="scale" value="noscale" />
  <param NAME="FlashVars" value="text=john&label=copy&feedback=copied!" />
  <param name="bgcolor" value="#ffffff" />
  <embed src="../toserve/clippy/clippy.swf"
  width="110"
  height="16"
  name="clippy"
  quality="high"
  allowScriptAccess="always"
  type="application/x-shockwave-flash"
  pluginspage="http://www.macromedia.com/go/getflashplayer"
  FlashVars="text=john&label=copy&feedback=copied!"
  bgcolor="#ffffff"
  />
</object>
</span></span>

### [newserver] and [newusername]

These tags will be replaced by the new server and new username the user will
have to enter in his mail client in order to make it run with Spamihilator.

<em>Just like `[server]` and `[username]` these tags also support the `copy`
parameter. Here, the additional icon is particularly useful, because the user
will most likely want to copy the new values for his mail server and username
to the clipboard in order to paste them into his mail client's account
settings.</em>

#### Example 1

Assume the user entered `pop.server.com` as his mail server and `john` as his
username. The following code

    **New server**: [newserver]
    **New username**: [newusername]

will lead to the following output:

**New server**: localhost  
**New username**: pop.server.com&john

### [image]

The tutorial editor allows uploading screenshots. Each image will be attached
to the document and can be referenced with the `title` parameter of the
`[image]` short tag.

Before a screenshot is delivered to the user, it is searched for so-called
*placeholder areas*. These are areas completely filled with magenta (`#FF00FF`
or `rgb(255, 0, 255)`). The placeholders will be replaced by the server and
the username. By default, the server will be put into the first placeholder and
the username will be put into the second one.

The following image

![Screenshot of Thunderbird](../toserve/images/thunderbird_en_03.png)

will be rendered to

![Screenshot of Thunderbird](../toserve/images/thunderbird_en_03_rendered.png)

<em>Please highlight important areas in screenshots with a red frame having a
border width of 3 pixels.</em>

#### Parameters

##### title

The filename of the uploaded image. The following code

    [image title="thunderbird-1.png"]

will include the image with the filename `thunderbird-1.png` at the current
position in the document.

##### use-new-values

This parameter can be `true` or `false` depending on whether the placeholders
in the image should be replaced by the new values for server and username or
the original ones respectively. The default is `false`.

The following code

    [image title="thunderbird-1.png" use-new-values="true"]

will render the image with placeholders replaced by the new values.

##### server-pos and username-pos 

The numbers of the placeholders for the server and username respectively.
Counting starts at `0`. By default the server will be put into the first
placeholder (`0`) and the username will be put into the second placeholder
(`1`).

The following code

    [image title="thunderbird-1.png" username-pos=0 server-pos=1]

will replace the first placeholder in the image by the username and the
second placeholder by the server.

You may also use a negative number like `-1` to hide a variable completely:

    [image title="thunderbird-1.png" username-pos=0 server-pos=-1]

This code will put the username into the first placeholder, but the server
will be hidden.

Of course, these parameters can also be used in combination with
`use-new-values=true`.
