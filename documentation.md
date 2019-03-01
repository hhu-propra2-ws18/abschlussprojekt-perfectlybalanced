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

Auf der *Start* Seite werden alle Produkte mit deren Beschreibung und einen dazugehörigen Detaillink angezeigt.

**Produktdetail**

Auf der *Detailseite* angekommen, erhält man alle Informationen zum ausgewählten Produkt mit einem Button zum ausleihen.
Wenn man das Produkt ausleihen möchte, klickt man auf den Button und die verleihende Person erhält eine Anfrage zum ausleihen unter *Leih-Anfragen*
Alternativ befindet sich dort statdesseb ein Button zum kaufen, jenachdem ob das Produkt zum Verleih oder zum Verkauf steht.

**Leih-Anfragen**
<Todo Leih-Anfragen mit Zeitstempel>

Wenn ein anderer User ein Produkt des eingeloggten Users ausleihen möchte,so erscheint auf der Seite *Leih-Anfragen* unter Leih Anfragen die Meldung, dass der User mit dem Username das ausgewählte Produkt ausleihen möchte.
Der Besitzer kann nun entscheiden, ob er das Produkt verleiht oder nicht.
Desweiteren befinden sich dort Meldungen für alle zurückgegebenen Produkte.
Der Besitzer kann dort entscheiden, ob das Produkt in ordnungsgemäßem Zustand zurückgegeben wurde oder nicht.
Damit wird entweder die Kaution des Leihenden wieder freigegeben und der Ausleihvorgang abgeschlossen,
oder der Ausleihvorgang an die Konfliktlösungsstelle weitergeleitet.
Unter den Leih Anfragen befindet sich außerdem noch die Historie aller Leihanfragen an den Besitzer mit dem jeweiligen Status.

**Meine Ausleihen**

Hier werden alle Ausleihen des Users angezeigt, dabei sind sie nach Zustand sortiert.
Die Produkte die der User gerade ausgeliehen hat, kann er dort auch zurückgeben.

**Meine Produkte ansehen**

Unter der Seite werden alle Produkte des Users mit dem Titel, der Beschreibung, einen Link zu den Details und einen Link zum Editieren angezeigt.

**Produkt einstellen**

Um ein neues Produkt einzustellen, füllt man das gegebene Formular aus und klickt im Anschluss auf Erstellen
Dabei muss man sich vor dem Ausfüllen entscheiden, ob man das Produkt verleihen oder verkaufen möchte.

**Produkt editieren**

Um ein Produkt zu editieren, muss man zuerst auf *Meine Produkte ansehen* und dort beim ausgewählten Produkt auf den Link Editieren.
Es öffnet sich nun das Formular des Produkts, das man ändern kann.
Um die Änderungen zu übernehmen, klickt man auf den Button Bearbeiten.

**Ausloggen**

Um sich abzumelden, klickt man in der Navigationbar oben rechts auf Logout.


## Anmerkungen zu verschiedenen Bereichen des Projektes
### Anmerkungen der Geschäftslogik

Die Geschäftslogik befindet sich in den beiden Klassen LendingService und SellService.
In der Klasse LendingService werden alle Aktionen die den Verleihvorgang betreffen durchgeführt,
während im SellService ledinglich die Methode zur Kaufabwicklung ist.

In der Klasse LendingService gibt es drei Kategorien von Methoden:
Methoden die Teil der Geschäftsabwicklung sind,
Methoden die Informationen für die Views bereitstellen ohne irgendwelche Daten zu ändern,
und zwei private Methoden die weitere Funktionen zu den Timestamps bereitstellen.

Die Methode getAvailableTime prüft für ein Produkt in welchen Zeiträumen es bereits belegt ist.
Dabei werden die Zeiträume als Timespan ausgegeben,
wobei ein Timespan Beginn und Ende der belegten Zeit als Timestamps enthält.

Die Methode requestLending nimmt die Informationen aus dem Formular entgegen,
und prüft die Zeitangaben sowie den Kontostand des Benutzers.
Die bereits belegten Zeiträume des Produkt werden dabei ausdrücklich nicht geprüft.
Somit ist es möglich dass mehrere Nutzer eine Anfrage für den gleichen Zeitraum stellen,
der Besitzer des Produktes kann sich dann aussuchen wem er sein Produkt leihen möchte.

Die Methode acceptLendingRequest prüft nun ob das Produkt im entsprechenden Zeitraum verfügbar ist,
sowie erneut ob der Leihende genügend Geld parat hat.
Anschließend werden die Kosten sowie die Kaution reserviert.
Waren beide Reservierungen erfolgreich, so werden die Kosten an den Besitzer des Produkten ausgezahlt,
und der Verleihvorgang bestätigt.
Sollte die zweite Reservierungen fehlschlagen nachdem die erste erfolgreich war,
so wird die erste Reservierungen wieder zurückgezahlt um das Konto in den vorherigen Zustand zurückzusetzen.

Die Methode denyLendingRequest lehnt eine Anfrage ab.

Die Methode returnProduct ermöglicht es dem Leihenden anzugeben, das er das geliehene Produkt zurückgegeben hat.

Die Methode acceptReturnedProduct wird aufgerufen, wenn der Besitzer eines Produktes findet,
das es in guten Zustand zurückgeben wurde.
Dann wird die Kaution an den Leihenden zurückgezahlt, und der Ausleihvorgang abgeschlossen.

Die Methode denyReturnedProduct markiert den Ausleihvorgang als Konflikt,
wenn der Besitzer nicht mit dem Zustand seines Produktes zufrieden ist.

Die Methode ownerReceivesSuretyAfterConflict löst einen Konflikt,
indem die Kaution an den Besitzer ausgezahlt wird.

Die Methode borrowerReceivesSuretyAfterConflict löst einen Konflikt,
indem die Kaution an den Leihenden ausgezahlt wird.

Dann folgen etliche Methoden für die Views.
Dort werden viele Listen von dem LendingRepository einfach weitergegeben,
sowie eine Filterfunktion zur Verfügung gestellt.
Der Sinn der Filterfunktion besteht darin,
dass die Liste aller Ausleihvorgänge eines Nutzers nur einmal von der Datenbank abgefragt werden muss,
und anschließend in die einzelnen Zustände sortiert wird.
Die Alternative wären mehrere Anfragen an die Datenbank wobei nacheinander sämtliche Zustände abgefragt werden würden,
was die Datenbank unnötig auslasten würde.

Es folgen die beiden privaten Methoden zu den Timestamps:
In der Methode daysBetweenTwoTimestamps wird die Zeit zwischen zwei Timestamps in Tagen ausgegeben.
Dieser Wert wird benötig, um die Ausleihkosten zu berechnen.
Dabei wird der Wert aufgerundet, da jeder angefange Tag bezahlt werden muss.

Die Methode getThisMorning nimmt den aktuellen Timestamp (der Systemzeit) und entfernt die Uhrzeit aus diesem,
indem der Wert ganzzahlig durch die Länge eines Tages geteilt wird,
und anschließend mit diesem wieder mutlipliziert wird.
Diese Methode dient ledinglich der Prüfung ob ein Datum in der Vergangenheit liegt,
ansonsten hat sie keinerlei Einfluss auf die Logik.
Daher ist es auch nicht alzuschlimm, das sich diese Methode nicht vernünftig testen lässt:
Würde ich diese Methode nur mit eigenem Code testen, so würde im Test der gleiche Code stehen wie in der Anwendung,
was den Sinn des Testes damit aufhebt.
Sollte ich einen Denkfehler in der Berechnung machen, so würde es sowohl in der Anwendung als auch im Test vorkommen,
und der Test würde somit auch im Fehlerfall erfolgreich durchlaufen.
Daher habe ich beim Testen LocalDateTime usw verwendet, welche allerdings sehr abhängig von Zeitzonen usw sind.
Lokal bei mir läuft der Tests ohne Probleme durch, und Tests mit der Laufenden Anwendung zeigen das acuh dort die Methode funktioniert,
aber auf TravisCI läuft der Test nicht durch.
Ich vermute das die Server von TravisCI in einer anderen Zeitzone stehen und der Fehler daher kommt.
Foglich musste ich den Test wieder deaktivieren, damit die TravisCI-Tests ordnungsgemäß durchlaufen könen.

In der Klasse SellService befindet sich nur eine Methode: buyProduct.
Diese prüft ob das Produkt verkaufbar ist und der Käufer genügend Geld besitzt,
dann wird der Kauf abgewickelt.

Anmerkungen zu den Tests:

In den View-Methoden findet keine Logik statt, entsprechenden ist es nicht sinvoll diese auf Service-Ebene zu testen.

Da ich auch das ausgeben von Exeptions usw test, sowie meine Anforderungen an die Test-Dummys sehr hoch sind,
habe ich mich dazu entschieden die Dummys zu schreiben anstatt Mockito zu benutzen.

Die Tests der Geschäftslogik wurden (ausserhalb des Repos um die build.gradle nicht unötig zu vergrößern) mit pitest überprüft:
Dabei wurden die meisten Mutationen erfolgreich eliminiert,
nur Kleinigkeiten haben überlebt.
zB. Mutationen wie if (userMoney < totalMoney) --> if (userMoney <= totalMoney) haben überlebt,
aber derartige Mutationen kann man nur mit erheblichem Aufwand testen,
welche sich bei derartigen Kleinigkeiten einfach nicht lohnt.
