OSJAVA WS 2012/2013 Gruppe 3
Cantina - Kantinenplanungsspftware

Abgabe von Lukas Krotki und Rene Wiederhold

Installation und Start

-Entpacken Sie die Datei in ein beliebiges Verzeichnis
-Legen sie in den Ordner data/ die Hitlistendatei und die Datei mit den Rezepten
  (Sollten die Dateinnamen nicht hitliste.csv und rezepte.csv sein, �ndern sie bitte die Dateinamen 
   ODER die Pfadangaben in der config.properties-Datei welche sich ebenfalls im Anwendungsordner befindet.
   Zur confog-properties weiter unten mehr.)
-Legen sie die verf�gbaren Preislisten der Lieferanten in den Ordner lieferanten/
  (Die Dateinamen spielen hier keine Rolle, allerdings sollten sie keine Dateien in den Ordner ablegen, die keine 
   Lieferantendateien in dem vereinbarten Format darstellen. Sie k�nnen den Pfad auch �ber die config.properties �ndern.)
-�ffnen sie die config.properties-Datei mit einem Editor. In dieser Datei sind wichtige Parameter f�r die Programm-
 ausf�hrung hinterlegt, die im folgenden erl�utert werden.
  
  Parameter          Bedeutung
  RezeptbuchPfad:    Hier k�nnen sie den Datei-Pfad zu dem Rezeptbuch �ndern.
  HitlistenPfad:     Hier k�nnen sie den Datei-Pfad zur Hitlisten-Datei �ndern.
  PreislistenOrdner: Hier k�nnen sie den Ordner-Pfad f�r den Ordner, der die Lieferantenpreislisten enth�lt �ndern.
  kmSatz:            Hier k�nnen sie den Kilometersatz �ndern, der bei der Lieferung von einem Bauernhof von 
                     der Spedition erhoben wird.
  AnzahlKantinen:    Hier k�nnen sie die Anzahl der Kantinenpl�ne, die erstellt werden sollen, �ndern. Standartm��ig ist 
                     hier 2 eingestellt.
                     Bitte beachten sie folgenden Abschnitt:

                  F�R JEDE KANTINE MUSS FOLGENDER ABSCHNITT VORHANDEN SEIN
                  Das #-Zeichen wird jeweils mit einer laufenden Nummer ersetzt, z.B. NameKantine3 f�r den Fall 
                  das 3 Speisepl�ne erstellt werden sollen.
  NameKantine#:      Name oder Standort der Kantine, sp�ter wird so auch der erstellte Kantinenplan benannt.
  AnzahlKantine#:    Anzahl der G�ste der Kantine bzw. Mitarbeiter am Standort.
 
-Starten sie die Cantina.jar �ber die JVM oder, falls sie dies in einer Windows-Umgebung ausf�hren, verwenden sie die start.bat

=> Das sich �ffnede Fenster enth�lt grunds�tzliche Informationen zum Ablauf und wei�t am Ende auch die Gesamtkosten der 
   Bestellung inklusive der Lieferkosten aus.
=> Das Programm erstellt nun aus den Rezepten einen Speiseplan f�r 2 Kantinen (Voreinstellung) und speichert diese unter 
   den in der config.properties angegebenen Namen der Kantine, z.B. Essen.txt, in dem Anwendungsordner.
   Weiterhin wird die Einkaufsliste im Anwendungsordner als Einkaufsliste.txt gespeichert.

   Sollten sie Probleme mit der Formatierung der Ausgabedateien haben, verwenden sie bitte einen anderen Editor, als 
   den Windows-Editor, z.B. Notepad++ (http://notepad-plus-plus.org/)

Features

Das Programm erstellt zuf�llige Speisepl�ne aus der Rezept-Datei. Dabei stellt es sicher, das die n�tigen Zutaten auch von 
den Lieferanten, deren Preislisten in dem Lieferantenordner liegen, beschafft werden k�nnen. Ein Rezept aus der Rezeptdatei 
kann dabei je Planungsperiode nur einmal �ber alle Kantinen vorkommen. 
Das Programm stellt sicher, dass jeden Tag ein Fleischgericht sowie ein vegetarisches Gericht angeboten wird. Weiterhin 
stellt es sicher, das pro Woche mindestens ein, h�chstens aber 2 Fischgerichte angeboten werden, da eventuell nicht gen�gend
verschiedene Fischgerichte f�r mehr Tage vorhanden sind.

Nach der Kantinenplanerstellung wird sofort die Einkaufslistengenerierung initialisiert. Die Kostenoptimierung der Bestellung
ist wegen der unterschiedlichen Berechnung der Lieferkosten nicht trivial. Statt eines brute-force-Ansatzes (alle m�glichen 
Bestellkombinationen ausrechnen und die g�nstigste nehmen) geht unser Programm etwas einfacher vor. 
Grundlage unseres Algorithmus ist die Annahme, dass es Bauernh�fe gibt, die Produkte so viel g�nstiger anbieten, dass es sich 
selbst in dem Fall lohnt diese dort zu bestellen, in denen sonst garnichts bei diesem Bauernh�fen gekauft worden w�re. Nach 
solchen Konstellationen wird zun�chst gesucht (dabei werden auch Teilbestellungen, also Bestellungen, bei denen nicht der 
komplette Bedarf mit einer Bestellung abgedeckt werden kann, mit einbezogen.
Liegen diese Bestellpositionen vor, kann im folgenden der Lieferkostensatz dieser Bauernh�fe ignoriert werden und die optimale
Beschaffung gew�hlt werden.

Durch die Parametrisierung der wesentlichen Angaben, kann die Software schnell an ver�nderte Bedingungen angepasst werden, 
z.B. neue Standorte oder ver�nderte Mitarbeiterzahlen. 

Durch Zeitmangel nicht mehr aufgenommen werden konnte eine Funktion zum speichern und sp�teren laden von Kantinenpl�nen und 
ein GUI, in dem der vorgeschlagene Speiseplan noch modifiziert werden kann. 