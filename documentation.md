# Verleihplattform - Perfectlybalanced
## Grundlegende Funktionen
### Unsere Anwendung ermöglicht es Produkte zu leihen und zu verleihen.

**Start der Anwendung**

Sobald man die Anwendung <mit docker-compose up> gestartet hat, 
lässt sich unsere Anwendung mit dem Link http://localhost:8080/ öffnen.
Mit diesem Link gelangt man auf die Startseite unserer Anwendung,
auf der alle verfügbaren Produkte angezeigt werden.
Um weitere Interaktionen durchzuführen, muss man eingeloggt sein.
Falls man noch keinen Account besitzt, kann man in der Navigationbar oben Rechts auf Registrierung und sich registrieren lassen.
Nach der Registrierung wird man automatisch angemeldet. Falls man bereits einen Account besitzt, so kann man sich unter Login anmelden. 
Nachdem man sich angemeldet hat, wird man auf seine Profilseite weitergeleitet
Wenn man angemeldet ist kann man in der Navigationbar zwischen allen Produkten, den Produkten des Users, Produkte einstellen, Leih-Anfragen und dem Profil wechseln.

**Profile**

Auf der *Profilseite* stehen alle Informationen zum eingeloggten User.
Außerdem wird das ProPay Konto mit dem Kontostand und der Möglichkeit Geld auf das Konto einzuzahlen angezeigt.

**Produkte ansehen**

Auf der *Produkte ansehen* Seite werden alle Produkte mit deren Beschreibung und einen dazugehörigen Detaillink angezeigt.

**Produktdetail**

Auf der *Detailseite* angekommen, erhält man alle Informationen zum ausgewählten Produkt mit einem Button zum ausleihen.
Wenn man das Produkt ausleihen möchte, klickt man auf den Button und die verleihende Person erhält eine Anfrage zum ausleihen unter *Leih-Anfragen*

**Leih-Anfragen**
<Todo Leih-Anfragen mit Zeitstempel>

Wenn ein anderer User ein Produkt des eingeloggten Users ausleihen möchte,so erscheint auf der Seite *Leih-Anfragen* unter Leih Anfragen die Meldung, dass der User mit dem Username das ausgewählte Produkt ausleihen möchte.
Der Besitzer kann nun entscheiden, ob er das Produkt verleiht oder nicht.
Unter den Leih Anfragen befindet sich außerdem noch die Historie aller Leihanfragen an den Besitzer mit dem Status.

**Meine Produkte ansehen**

Unter der Seite werden alle Produkte des Users mit dem Titel, der Beschreibung, einen Link zu den Details und einen Link zum Editieren angezeigt. 

**Produkt einstellen**

Um ein neues Produkt einzustellen, füllt man das gegebene Formular aus und klickt im Anschluss auf Erstellen

**Produkt editieren**

Um ein Produkt zu editieren, muss man zuerst auf *Meine Produkte ansehen* und dort beim ausgewählten Produkt auf den Link Editieren.
Es öffnet sich nun das Formular des Produkts, das man ändern kann.
Um die Änderungen zu übernehmen, klickt man auf den Button Bearbeiten.

**Ausloggen**

Um sich abzumelden, klickt man in der Navigationbar oben rechts auf Logout.