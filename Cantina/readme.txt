OSJAVA WS 2012/2013 Gruppe 3
Cantina - Kantinenplanungssoftware

Abgabe von Lukas Krotki und Rene Wiederhold

1.	Allgemeines

Die Kantinenplanungssoftware dient der zur Erstellung von Speiseplänen für eine Planungsperiode 
von 3 Wochen sowie Einkaufslisten für die VaWi GmbH unter Einbeziehung von Favoriten der Mitarbeiter
und Berücksichtigung von verfügbaren Lebensmitteln am Markt. Es sollen die Gesamtkosten ausgegeben 
werden und Einkauflisten, in Bezug auf günstigste Lebensmittel sowie Transportkosten, erstellt werden.

Ziel des Programms ist die Ausgabe der folgenden Dateien, Daten.
-	Kantinenplänen
-	Einkaufslisten
-	Gesamtkosten

In der Konfigurationsdatei (config.properties) können die folgenden Einstellungen vorgenommen werden.
-	Pfadangaben zu Verzeichnissen 
-	Anzahl Kantinen sowie deren Namen
-	Anzahl Mitarbeiter
-	Kilometersatz in Euro

Für einen Programmstart sind die folgenden Daten im CSV Dateiformat erforderlich. 
-	Rezeptdatei 
-	Hitlistendatei 
-	Lieferantendaten 

2.	Installation

•	Entpacken Sie die Datei „cantina.zip“  in ein beliebiges Verzeichnis
•	Nachdem Sie das ZIP Verzeichnis entpackt haben stehen Ihnen die 
    Verzeichnisse „data“ und „lieferanten“ zur Verfügung.
•	Bitte legen Sie das Verzeichnis data/ die Hitlistendatei und die Datei mit den Rezepten ab
    (Sollten die Dateinamen nicht hitliste.csv und rezepte.csv sein, ändern sie bitte die Dateinamen 
    oder die Pfadangaben in der Konfigurationsdatei welche sich ebenfalls im Anwendungsordner befindet.)
•	In das Verzeichnis lieferanten/ legen sie Bitte die verfügbaren Preislisten der Lieferanten ab.
    (Die Dateinamen spielen hier keine Rolle, allerdings sollten sie keine Dateien in den Ordner ablegen, 
    die keine Lieferantendateien in dem vereinbarten Format darstellen. Sie können den Pfad auch über die
    Konfigurationsdatei ändern.)
•	Öffnen sie die config.properties-Datei mit einem Editor. In dieser Datei sind wichtige Parameter für 
    die Programmausführung hinterlegt, die im Folgenden erläutert werden

Parameter		|	Bedeutung
---------------------------------------------------------------------------------------------------------------
RezeptbuchPfad		|	Hier können sie den Datei-Pfad zu dem Rezeptbuch ändern.
---------------------------------------------------------------------------------------------------------------
HitlistenPfad		|	Hier können sie den Datei-Pfad zur Hitlisten-Datei ändern.
---------------------------------------------------------------------------------------------------------------
PreislistenOrdner	|	Hier können sie den Ordner-Pfad für den Ordner, der die Lieferantenpreislisten 
			|	enthält ändern.
---------------------------------------------------------------------------------------------------------------
kmSatz			|	Hier können sie den Kilometersatz ändern, der bei der Lieferung von einem 
			|	Bauernhof von der Spedition erhoben wird.
---------------------------------------------------------------------------------------------------------------
AnzahlKantinen		|	Hier können sie die Anzahl der Kantinenpläne, die erstellt werden sollen, ändern. 
			|	Standartmäßig ist hier 2 eingestellt.
---------------------------------------------------------------------------------------------------------------
NameKantine#		|	Name oder Standort der Kantine, später wird so auch der erstellte Kantinenplan benannt.
---------------------------------------------------------------------------------------------------------------
AnzahlKantine#		|	Anzahl der Gäste der Kantine bzw. Mitarbeiter am Standort.

Hinweis:
FÜR JEDE KANTINE MUSS FOLGENDER ABSCHNITT VORHANDEN SEIN
Das #-Zeichen wird jeweils mit einer laufenden Nummer ersetzt, z.B. NameKantine3 für den Fall  das 3 Speisepläne
erstellt werden sollen. 
Damit eine Verarbeitung des Programms gewährleistet wird, ist bei einer Erweiterung von Kantinen in der 
Konfigurationsdatei (config.properties) sicherzustellen, dass genügend Rezepte in den eingelesenen Dateien 
enthalten sind. Die Rezepte müssen zudem in der Hitliste vorhanden sein. Nur so kann ein ausgewogener Speiseplan 
erstellt werden.

3.	Cantina - Ausführung

Sie können die Planungssoftware unter Windows mit der Anwendung start.bat ausführen. Auf Unix/Linux/OSX Systemen 
erfolgt der Aufruf des Programms mit „java“, der Option –jar und dem Parameter „cantina.jar“, 
z.B. java –jar cantina.jar.  Für die Ausführung muss man sich in dem Verzeichnis der jar-Datei befinden. 

Das sich öffnende Fenster enthält grundsätzliche Informationen zum Ablauf und weißt am Ende die Gesamtkosten 
der Bestellung inklusive der Lieferkosten aus.
Das Programm erstellt aus den zugrunde liegenden Rezeptdaten sowie Hitlistenpositionen einen Speiseplan 
für 2 Kantinen (Voreinstellung) und speichert diesen unter den in der Konfigurationsdatei (config.properties) 
angegebenen Namen der Kantine, z.B. Essen.txt, in dem Anwendungsordner.
Es erfolgt gleichzeitig die Ausgabe der Einkaufsliste als Textdatei (Einkaufsliste.txt) im Anwendungsordner.

Sollten sie Probleme mit der Formatierung der Ausgabedateien haben, verwenden sie bitte einen anderen Editor, 
als den Windows-Editor, z.B. Notepad++ (http://notepad-plus-plus.org/).

4.	Programmlogik und Features

Das Programm erstellt zufällige Speisepläne aus der Rezept-Datei. Es stellt sicher, dass die benötigten Zutaten 
von den Lieferanten, deren Preislisten im Lieferantenordner vorliegen, beschafft werden können. Ein Rezept aus 
der Rezeptdatei kann dabei je Planungsperiode nur einmal über alle Kantinen vorkommen. 
Das Programm stellt sicher, dass jeden Tag ein Fleischgericht sowie ein vegetarisches Gericht angeboten werden. 
Zudem wird sichergestellt, dass pro Woche mindestens ein, jedoch höchstens zwei Fischgerichte angeboten werden,
da eventuell nicht genügend verschiedene Fischgerichte für mehr Tage vorhanden sind.

Nach der Kantinenplanerstellung wird sofort die Einkaufslistengenerierung initialisiert. Die Kostenoptimierung 
der Bestellung ist wegen der unterschiedlichen Berechnung der Lieferkosten nicht trivial. Statt eines 
brute-force-Ansatzes (alle möglichen Bestellkombinationen zu berechnen und die günstigste nehmen) folgt das 
Programm einer einfacheren Logik. 
Grundlage des Algorithmus ist die Annahme, dass es Bauernhöfe gibt, die Produkte so viel günstiger anbieten, 
dass es sich selbst in den Fällen lohnt die Produkte dort zu bestellen, in denen sonst gar nichts bei diesen 
Bauernhöfen gekauft worden wäre. Zuerst wird also nach solchen Konstellationen gesucht. Dabei werden auch 
Teilbestellungen, also Bestellungen, bei denen nicht der komplette Bedarf mit einer Bestellung abgedeckt 
werden kann, mit einbezogen.
Liegen diese Bestellpositionen vor, kann im Folgenden der Lieferkostensatz dieser Bauernhöfe ignoriert werden 
und die optimale Beschaffung gewählt werden.

Durch die Parametrisierung der wesentlichen Angaben, kann die Software schnell an veränderte Bedingungen 
angepasst werden, z.B. neue Standorte oder veränderte Mitarbeiterzahlen. 

Durch Zeitmangel nicht mehr aufgenommen werden konnte eine Funktion zum Speichern und späteren laden von 
Kantinenplänen und ein GUI, in dem der vorgeschlagene Speiseplan noch modifiziert werden kann.
