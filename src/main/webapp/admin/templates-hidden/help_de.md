Zum Formatieren Ihrer Anleitung können Sie die
Markup-Sprache [Markdown](http://daringfireball.net/projects/markdown/)
benutzen. Mit Markdown ist es sehr einfach, einen Text zu formatieren und
ihn gleichzeitig gut lesbar zu halten. Der Text wird später in HTML konvertiert.
Wenn Ihnen die Möglichkeiten von Markdown nicht ausreichen, können Sie jederzeit
HTML-Tags verwenden. 

Schnelleinstieg in Markdown
---------------------------

Im Folgenden werden die wichtigsten Elemente von Markdown aufgelistet. Bitte
lesen Sie die vollständige [Markdown-Syntax](http://daringfireball.net/projects/markdown/syntax)
für weitere Informationen.

### Überschriften

#### Ebene 1:

    Ebene 1
    =======

oder

    # Ebene 1

#### Ebene 2:

    Ebene 2
    -------

oder

    ## Ebene 2

#### Weitere Ebenen:

Für weitere Ebenen müssen Sie das `#`-Zeichen verwenden:

    ### Ebene 3
    #### Ebene 4

### Hervorhebungen

*Hervorgehobener* Text:

    *Hervorgehoben*

oder

    _Hervorgehoben_

**Fetter** Text:

    **Fett**

oder

    __Fett__

### Listen

#### Einfache Listen:

    * Element 1
    * Element 2
    * Element 3

oder

    + Element 1
    + Element 2
    + Element 3

oder

    - Element 1
    - Element 2
    - Element 3

#### Nummerierte Listen:

    1. Element 1
    2. Element 2
    3. Element 3

### Links

Links setzen sich aus einem Text und einer URI zusammen. Der folgende Code

    [Hilfe](help.html)

erzeugt einen Link, der zurück zu dieser Seite führt: [Hilfe](help.html)

Weitere Elemente
----------------

Der Online-Setup-Assistent von Spamihilator unterstützt weitere Elemente, die
Sie in Ihren Anleitungen nutzen können. Diese Elemente werden *Short-Tags*
genannt. Sie werden durch eckige Klammern gekennzeichnet.

### [server] und [username]

Diese Tags werden durch den Server bzw. den Benutzernamen ersetzt. Sie können
optional den Parameter `copy` angeben, um ein kleines Icon hinzuzufügen, auf das
der Benutzer klicken kann, um den Server oder den Benutzernamen in die
Zwischenablage zu kopieren. Dieser Parameter kann die Werte `true` und `false`
annehmen, je nachdem, ob Sie das Icon wünschen oder nicht. Der Standardwert
ist `false`.

#### Beispiel 1

    **Server**: [server]  
    **Benutzernamen**: [username]

wird ersetzt durch

**Server**: pop.server.com  
**Benutzername**: john

#### Beispiel 2

    [username copy=true]

wird wiedergegeben als

<span class="text-with-clippy">john <span class="copy">
<object classid="clsid:d27cdb6e-ae6d-11cf-96b8-444553540000"
  width="110" height="16" id="clippy">
  <param name="movie" value="../toserve/clippy/clippy.swf" />
  <param name="allowScriptAccess" value="always" />
  <param name="quality" value="high" />
  <param name="scale" value="noscale" />
  <param NAME="FlashVars" value="text=john&label=kopieren&feedback=kopiert!" />
  <param name="bgcolor" value="#ffffff" />
  <embed src="../toserve/clippy/clippy.swf"
  width="110"
  height="16"
  name="clippy"
  quality="high"
  allowScriptAccess="always"
  type="application/x-shockwave-flash"
  pluginspage="http://www.macromedia.com/go/getflashplayer"
  FlashVars="text=john&label=kopieren&feedback=kopiert!"
  bgcolor="#ffffff"
  />
</object>
</span></span>

### [newserver] und [newusername]

Diese Tags werden durch den neuen Server bzw. den neuen Benutzernamen ersetzt,
den der Benutzer in seinem E-Mail-Programm angeben muss, damit dieses mit
Spamihilator funktioniert.

<em>Genauso wie `[server]` und `[username]` unterstützen diese Tags ebenfalls
den `copy`-Parameter. Hier ist das zusätzliche Icon besonders nützlich, da der
Benutzer sehr wahrscheinlich die neuen Werte für Server und Benutzername
in die Zwischenablage kopieren möchte, um sie dann in die Konteneinstellungen
seines E-Mail-Programms einzufügen.</em>

#### Beispiel 1

Angenommen der Benutzer hat `pop.server.com` als Server und `john` ale
Benutzername eingegeben. Der folgende Code

    **Neuer Server**: [newserver]
    **Neuer Benutzername**: [newusername]

führt zu folgender Ausgabe:

**Neuer Server**: localhost  
**Neuer Benutzername**: pop.server.com&john

### [image]

Der Editor für Anleitungen ermöglicht Ihnen das Hochladen von Screenshots. Jedes
Bild wird an das Dokument angehängt und kann mit dem `title`-Parameter des
`[image]`-Short-Tags referenziert werden.

Bevor ein Screenshot auf dem Bildschirm angezeigt wird, wird er nach so
genannten *Platzhalterbereichen* durchsucht. Dies sind Bereiche, die
vollständig mit der Farbe Magenta gefüllt sind (`#FF00FF` oder
`rgb(255, 0, 255)`). Die Platzhalter werden durch den Server und den
Benutzernamen ersetzt. Standardmäßig wird der Server in den ersten Platzhalter
eingefügt und der Benutzername in den zweiten.

Das folgende Bild

![Screenshot of Thunderbird](../toserve/images/thunderbird_de_03.png)

wird wiedergegeben als

![Screenshot of Thunderbird](../toserve/images/thunderbird_de_03_rendered.png)

<em>Bitte heben Sie wichtige Bereiche in Ihren Screenshots mit einem roten
Rand hervor, der 3 Pixel breit ist.</em>

#### Parameter

##### title

Der Dateiname des hochgeladenen Bilds. Der folgende Code

    [image title="thunderbird-1.png"]

fügt das Bild mit dem Dateinamen `thunderbird-1.png` an der aktuellen Position
im Dokument ein.

##### use-new-values

Dieser Parameter kann die Werte `true` und `false` annehmen, abhängig davon,
ob die Platzhalter im Bild durch die neuen Werte für Server und Benutzernamen
ersetzt werden sollen oder durch die ursprünglichen. Der Standardwert ist
`false`. 

Der folgende Code

    [image title="thunderbird-1.png" use-new-values="true"]

gibt das Bild wieder, wobei die Platzhalter durch die neuen Werte ersetzt
werden.

##### server-pos und username-pos 

Die Nummern der Platzhalter für den Server bzw. den Benutzernamen. Die
Zählung beginnt bei `0`. Standardmäßig wird der Server in den ersten
Platzhalter (`0`) eingefügt, der Benutzernamen in den zweiten Platzhalter (`1`).

Der folgende Code

    [image title="thunderbird-1.png" username-pos=0 server-pos=1]

ersetzt den ersten Platzhalter im Bild durch den Benutzernamen und den
zweiten durch den Server.

Sie können auch negative Zahlen wie zum Beispiel `-1` benutzen, um eine
Variable vollständig zu verstecken:

    [image title="thunderbird-1.png" username-pos=0 server-pos=-1]

Dieser Code fügt den Benutzernamen in den ersten Platzhalter ein. Der Server
wird jedoch versteckt.

Selbstverstädlich können diese Parameter auch in Verbindung mit
`use-new-values=true` benutzt werden.
