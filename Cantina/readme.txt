OSJAVA WS 2012/2013 Gruppe 3
Cantina - Kantinenplanungsspftware

Abgabe von Lukas Krotki und Rene Wiederhold

Installation und Start

-Entpacken Sie die Datei in ein beliebiges Verzeichnis
-Legen sie in den Ordner data/ die Hitlistendatei und die Datei mit den Rezepten
  (Sollten die Dateinnamen nicht hitliste.csv und rezepte.csv sein, ändern sie bitte die Dateinamen 
   ODER die Pfadangaben in der config.properties-Datei welche sich ebenfalls im Anwendungsordner befindet.
   Zur confog-properties weiter unten mehr.)
-Legen sie die verfügbaren Preislisten der Lieferanten in den Ordner lieferanten/
  (Die Dateinamen spielen hier keine Rolle, allerdings sollten sie keine Dateien in den Ordner ablegen, die keine 
   Lieferantendateien in dem vereinbarten Format darstellen. Sie können den Pfad auch über die config.properties ändern.)
-Öffnen sie die config.properties-Datei mit einem Editor. In dieser Datei sind wichtige Parameter für die Programm-
 ausführung hinterlegt, die im folgenden erläutert werden.
  
  Parameter          Bedeutung
  RezeptbuchPfad:    Hier können sie den Datei-Pfad zu dem Rezeptbuch ändern.
  HitlistenPfad:     Hier können sie den Datei-Pfad zur Hitlisten-Datei ändern.
  PreislistenOrdner: Hier können sie den Ordner-Pfad für den Ordner, der die Lieferantenpreislisten enthält ändern.
  kmSatz:            Hier können sie den Kilometersatz ändern, der bei der Lieferung von einem Bauernhof von 
                     der Spedition erhoben wird.
  AnzahlKantinen:    Hier können sie die Anzahl der Kantinenpläne, die erstellt werden sollen, ändern. Standartmäßig ist 
                     hier 2 eingestellt.
                     Bitte beachten sie folgenden Abschnitt:

                  FÜR JEDE KANTINE MUSS FOLGENDER ABSCHNITT VORHANDEN SEIN
                  Das #-Zeichen wird jeweils mit einer laufenden Nummer ersetzt, z.B. NameKantine3 für den Fall 
                  das 3 Speisepläne erstellt werden sollen.
  NameKantine#:      Name oder Standort der Kantine, später wird so auch der erstellte Kantinenplan benannt.
  AnzahlKantine#:    Anzahl der Gäste der Kantine bzw. Mitarbeiter am Standort.
 
-Starten sie die Cantina.jar über die JVM oder, falls sie dies in einer Windows-Umgebung ausführen, verwenden sie die start.bat

=> Das sich öffnede Fenster enthält grundsätzliche Informationen zum Ablauf und weißt am Ende auch die Gesamtkosten der 
   Bestellung inklusive der Lieferkosten aus.
=> Das Programm erstellt nun aus den Rezepten einen Speiseplan für 2 Kantinen (Voreinstellung) und speichert diese unter 
   den in der config.properties angegebenen Namen der Kantine, z.B. Essen.txt, in dem Anwendungsordner.
   Weiterhin wird die Einkaufsliste im Anwendungsordner als Einkaufsliste.txt gespeichert.

   Sollten sie Probleme mit der Formatierung der Ausgabedateien haben, verwenden sie bitte einen anderen Editor, als 
   den Windows-Editor, z.B. Notepad++ (http://notepad-plus-plus.org/)

Features

Das Programm erstellt zufällige Speisepläne aus der Rezept-Datei. Dabei stellt es sicher, das die nötigen Zutaten auch von 
den Lieferanten, deren Preislisten in dem Lieferantenordner liegen, beschafft werden können. Ein Rezept aus der Rezeptdatei 
kann dabei je Planungsperiode nur einmal über alle Kantinen vorkommen. 
Das Programm stellt sicher, dass jeden Tag ein Fleischgericht sowie ein vegetarisches Gericht angeboten wird. Weiterhin 
stellt es sicher, das pro Woche mindestens ein, höchstens aber 2 Fischgerichte angeboten werden, da eventuell nicht genügend
verschiedene Fischgerichte für mehr Tage vorhanden sind.

Nach der Kantinenplanerstellung wird sofort die Einkaufslistengenerierung initialisiert. Die Kostenoptimierung der Bestellung
ist wegen der unterschiedlichen Berechnung der Lieferkosten nicht trivial. Statt eines brute-force-Ansatzes (alle möglichen 
Bestellkombinationen ausrechnen und die günstigste nehmen) geht unser Programm etwas einfacher vor. 
Grundlage unseres Algorithmus ist die Annahme, dass es Bauernhöfe gibt, die Produkte so viel günstiger anbieten, dass es sich 
selbst in dem Fall lohnt diese dort zu bestellen, in denen sonst garnichts bei diesem Bauernhöfen gekauft worden wäre. Nach 
solchen Konstellationen wird zunächst gesucht (dabei werden auch Teilbestellungen, also Bestellungen, bei denen nicht der 
komplette Bedarf mit einer Bestellung abgedeckt werden kann, mit einbezogen.
Liegen diese Bestellpositionen vor, kann im folgenden der Lieferkostensatz dieser Bauernhöfe ignoriert werden und die optimale
Beschaffung gewählt werden.

Durch die Parametrisierung der wesentlichen Angaben, kann die Software schnell an veränderte Bedingungen angepasst werden, 
z.B. neue Standorte oder veränderte Mitarbeiterzahlen. 

Durch Zeitmangel nicht mehr aufgenommen werden konnte eine Funktion zum speichern und späteren laden von Kantinenplänen und 
ein GUI, in dem der vorgeschlagene Speiseplan noch modifiziert werden kann. 