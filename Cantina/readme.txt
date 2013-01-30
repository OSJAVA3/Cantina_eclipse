OSJAVA WS 2012/2013 Gruppe 3
Cantina - Kantinenplanungssoftware

Abgabe von Lukas Krotki und Rene Wiederhold

1.	Allgemeines

Die Kantinenplanungssoftware dient der zur Erstellung von Speisepl�nen f�r eine Planungsperiode 
von 3 Wochen sowie Einkaufslisten f�r die VaWi GmbH unter Einbeziehung von Favoriten der Mitarbeiter
und Ber�cksichtigung von verf�gbaren Lebensmitteln am Markt. Es sollen die Gesamtkosten ausgegeben 
werden und Einkauflisten, in Bezug auf g�nstigste Lebensmittel sowie Transportkosten, erstellt werden.

Ziel des Programms ist die Ausgabe der folgenden Dateien, Daten.
-	Kantinenpl�nen
-	Einkaufslisten
-	Gesamtkosten

In der Konfigurationsdatei (config.properties) k�nnen die folgenden Einstellungen vorgenommen werden.
-	Pfadangaben zu Verzeichnissen 
-	Anzahl Kantinen sowie deren Namen
-	Anzahl Mitarbeiter
-	Kilometersatz in Euro

F�r einen Programmstart sind die folgenden Daten im CSV Dateiformat erforderlich. 
-	Rezeptdatei 
-	Hitlistendatei 
-	Lieferantendaten 

2.	Installation

�	Entpacken Sie die Datei �cantina.zip�  in ein beliebiges Verzeichnis
�	Nachdem Sie das ZIP Verzeichnis entpackt haben stehen Ihnen die 
    Verzeichnisse �data� und �lieferanten� zur Verf�gung.
�	Bitte legen Sie das Verzeichnis data/ die Hitlistendatei und die Datei mit den Rezepten ab
    (Sollten die Dateinamen nicht hitliste.csv und rezepte.csv sein, �ndern sie bitte die Dateinamen 
    oder die Pfadangaben in der Konfigurationsdatei welche sich ebenfalls im Anwendungsordner befindet.)
�	In das Verzeichnis lieferanten/ legen sie Bitte die verf�gbaren Preislisten der Lieferanten ab.
    (Die Dateinamen spielen hier keine Rolle, allerdings sollten sie keine Dateien in den Ordner ablegen, 
    die keine Lieferantendateien in dem vereinbarten Format darstellen. Sie k�nnen den Pfad auch �ber die
    Konfigurationsdatei �ndern.)
�	�ffnen sie die config.properties-Datei mit einem Editor. In dieser Datei sind wichtige Parameter f�r 
    die Programmausf�hrung hinterlegt, die im Folgenden erl�utert werden

Parameter		|	Bedeutung
---------------------------------------------------------------------------------------------------------------
RezeptbuchPfad		|	Hier k�nnen sie den Datei-Pfad zu dem Rezeptbuch �ndern.
---------------------------------------------------------------------------------------------------------------
HitlistenPfad		|	Hier k�nnen sie den Datei-Pfad zur Hitlisten-Datei �ndern.
---------------------------------------------------------------------------------------------------------------
PreislistenOrdner	|	Hier k�nnen sie den Ordner-Pfad f�r den Ordner, der die Lieferantenpreislisten 
			|	enth�lt �ndern.
---------------------------------------------------------------------------------------------------------------
kmSatz			|	Hier k�nnen sie den Kilometersatz �ndern, der bei der Lieferung von einem 
			|	Bauernhof von der Spedition erhoben wird.
---------------------------------------------------------------------------------------------------------------
AnzahlKantinen		|	Hier k�nnen sie die Anzahl der Kantinenpl�ne, die erstellt werden sollen, �ndern. 
			|	Standartm��ig ist hier 2 eingestellt.
---------------------------------------------------------------------------------------------------------------
NameKantine#		|	Name oder Standort der Kantine, sp�ter wird so auch der erstellte Kantinenplan benannt.
---------------------------------------------------------------------------------------------------------------
AnzahlKantine#		|	Anzahl der G�ste der Kantine bzw. Mitarbeiter am Standort.

Hinweis:
F�R JEDE KANTINE MUSS FOLGENDER ABSCHNITT VORHANDEN SEIN
Das #-Zeichen wird jeweils mit einer laufenden Nummer ersetzt, z.B. NameKantine3 f�r den Fall  das 3 Speisepl�ne
erstellt werden sollen. 
Damit eine Verarbeitung des Programms gew�hrleistet wird, ist bei einer Erweiterung von Kantinen in der 
Konfigurationsdatei (config.properties) sicherzustellen, dass gen�gend Rezepte in den eingelesenen Dateien 
enthalten sind. Die Rezepte m�ssen zudem in der Hitliste vorhanden sein. Nur so kann ein ausgewogener Speiseplan 
erstellt werden.

3.	Cantina - Ausf�hrung

Sie k�nnen die Planungssoftware unter Windows mit der Anwendung start.bat ausf�hren. Auf Unix/Linux/OSX Systemen 
erfolgt der Aufruf des Programms mit �java�, der Option �jar und dem Parameter �cantina.jar�, 
z.B. java �jar cantina.jar.  F�r die Ausf�hrung muss man sich in dem Verzeichnis der jar-Datei befinden. 

Das sich �ffnende Fenster enth�lt grunds�tzliche Informationen zum Ablauf und wei�t am Ende die Gesamtkosten 
der Bestellung inklusive der Lieferkosten aus.
Das Programm erstellt aus den zugrunde liegenden Rezeptdaten sowie Hitlistenpositionen einen Speiseplan 
f�r 2 Kantinen (Voreinstellung) und speichert diesen unter den in der Konfigurationsdatei (config.properties) 
angegebenen Namen der Kantine, z.B. Essen.txt, in dem Anwendungsordner.
Es erfolgt gleichzeitig die Ausgabe der Einkaufsliste als Textdatei (Einkaufsliste.txt) im Anwendungsordner.

Sollten sie Probleme mit der Formatierung der Ausgabedateien haben, verwenden sie bitte einen anderen Editor, 
als den Windows-Editor, z.B. Notepad++ (http://notepad-plus-plus.org/).

4.	Programmlogik und Features

Das Programm erstellt zuf�llige Speisepl�ne aus der Rezept-Datei. Es stellt sicher, dass die ben�tigten Zutaten 
von den Lieferanten, deren Preislisten im Lieferantenordner vorliegen, beschafft werden k�nnen. Ein Rezept aus 
der Rezeptdatei kann dabei je Planungsperiode nur einmal �ber alle Kantinen vorkommen. 
Das Programm stellt sicher, dass jeden Tag ein Fleischgericht sowie ein vegetarisches Gericht angeboten werden. 
Zudem wird sichergestellt, dass pro Woche mindestens ein, jedoch h�chstens zwei Fischgerichte angeboten werden,
da eventuell nicht gen�gend verschiedene Fischgerichte f�r mehr Tage vorhanden sind.

Nach der Kantinenplanerstellung wird sofort die Einkaufslistengenerierung initialisiert. Die Kostenoptimierung 
der Bestellung ist wegen der unterschiedlichen Berechnung der Lieferkosten nicht trivial. Statt eines 
brute-force-Ansatzes (alle m�glichen Bestellkombinationen zu berechnen und die g�nstigste nehmen) folgt das 
Programm einer einfacheren Logik. 
Grundlage des Algorithmus ist die Annahme, dass es Bauernh�fe gibt, die Produkte so viel g�nstiger anbieten, 
dass es sich selbst in den F�llen lohnt die Produkte dort zu bestellen, in denen sonst gar nichts bei diesen 
Bauernh�fen gekauft worden w�re. Zuerst wird also nach solchen Konstellationen gesucht. Dabei werden auch 
Teilbestellungen, also Bestellungen, bei denen nicht der komplette Bedarf mit einer Bestellung abgedeckt 
werden kann, mit einbezogen.
Liegen diese Bestellpositionen vor, kann im Folgenden der Lieferkostensatz dieser Bauernh�fe ignoriert werden 
und die optimale Beschaffung gew�hlt werden.

Durch die Parametrisierung der wesentlichen Angaben, kann die Software schnell an ver�nderte Bedingungen 
angepasst werden, z.B. neue Standorte oder ver�nderte Mitarbeiterzahlen. 

Durch Zeitmangel nicht mehr aufgenommen werden konnte eine Funktion zum Speichern und sp�teren laden von 
Kantinenpl�nen und ein GUI, in dem der vorgeschlagene Speiseplan noch modifiziert werden kann.
