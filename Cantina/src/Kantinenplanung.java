import java.text.DecimalFormat;
import java.util.*;
import java.io.*;

/**
 * Die Klasse Kantinenplanung ist die auszuf�hrende Klasse, welche die
 * Ablauflogik des Programms umfasst. Sie enth�lt neben der Main-Methode auch
 * Attribute f�r die wesentlichen Parameter, die zum Programmstart notwendig
 * sind. Diese werden �ber Java-Properties realisiert.
 * 
 * F�r den Planungsdurchgang steht diese Klasse mit der Lieferantenverwaltung,
 * der Rezeptverwaltung, dem Kantinenplan und der Einkaufliste in Assoziation.
 * 
 * @author Rene Wiederhold
 * @version 1.0
 */
public class Kantinenplanung {

	// Die wesentlichen Parameter, welche f�r die Anwendung ben�tigt werden
	/** Die Anzahl der zu erstellenden Kantinenpl�ne */
	private int anzKantinen;
	/** Der relative oder absolute Pfad zur Rezepte-Datei */
	private String rezeptPfad;
	/** Der relative oder absolute Pfad zur Hitliste-Datei */
	private String hitListenPfad;
	/** Der relative oder absolute Pfad zum Ordner, welche die Preislisten der Lieferanten enth�lt */
	private String preisListenOrdner;

	// Attribute f�r die Verwaltungsklassen
	private Rezeptverwaltung rezeptverw;
	private Lieferantenverwaltung lieferantenverw;
	//Attribute f�r die Gesch�ftslogikklassen
	private Einkaufsliste einkaufsliste;
	private ArrayList<Kantinenplan> kantPlanList;


	/**
	 * Konstruktor f�r das Objekt.
	 */
	public Kantinenplanung() {
		new MainWin();
		MainWin.StringOutln("");
	}

	/**
	 * F�hrt das Programm aus und ruft die einzelnen Methoden gem�� der
	 * Ablauflogik auf.
	 * 
	 * @param String[] �bergegebene Parameter werden nicht verwendet.
	 */
	public static void main(String[] args) {
		// Erzeugen des Application-Objektes
		Kantinenplanung app = new Kantinenplanung();
		// Erste Parameter lesen
		if (app.loadProperties()==false){
			MainWin.StringOutln("Programm beendet");
			return;
		}
		// Alle Dateien einlesen und Rezeptverwaltung, Lieferantenverwaltung (inklusive Datenobjekten) erzeugen
		if (app.startDateienEinlesen()==false){
			MainWin.StringOutln("Programm beendet");
			return;
		}
		// Einen (oder zwei) Kantinenplan erzeugen und mit Tagesgerichten f�llen
		app.startKantinenplanung();
		// Die Einkaufsliste f�r ALLE Kantinenpl�ne erzeugen
		app.startEinkaufsplanung();
		
		MainWin.StringOut("Guten Appetit");
	}

	

	/**
	 * Die Methode l�dt die Parameter aus der Datei "config.properties" im
	 * Root-Ordner in die Attribute der Klasse.
	 */
	private boolean loadProperties() {
		try {
			Properties properties = new Properties();
			BufferedInputStream stream = new BufferedInputStream(
					new FileInputStream("config.properties"));
			properties.load(stream);
			stream.close();
			anzKantinen = Integer.parseInt(properties.getProperty("AnzahlKantinen"));
			rezeptPfad = properties.getProperty("RezeptbuchPfad");
			hitListenPfad = properties.getProperty("HitlistenPfad");
			preisListenOrdner = properties.getProperty("PreislistenOrdner");

			MainWin.StringOutln("Anzahl der zu erstellenden Kantinenpl�ne: "+anzKantinen);
			MainWin.StringOutln("Pfad zur Rezept-Datei: "+rezeptPfad);
			MainWin.StringOutln("Pfad zur Hitlisten-Datei: "+hitListenPfad);
			MainWin.StringOutln("Pfad zum Lieferanten-Ordner: "+preisListenOrdner);
			MainWin.StringOutln("");
			return true;
			 
		} catch (IOException e) {
			MainWin.StringOutln(e.toString());
			MainWin.StringOutln("Die Datei config.properties konnte nicht gelesen werden. Pr�fen Sie, ob sie im Anwendungsordner vorhanden ist.");
			return false;
		}
	}

	/**
	 * Die Methode st��t die Erstellung der Verwaltungsklassen (Lieferanten- und Rezeptverwaltung) und der darunterliegenden Datenschicht (Lieferant, Artikel, 
	 * Lebensmittel, Rezept, Zutat) an.
	 */
	public boolean startDateienEinlesen() {
		lieferantenverw = new Lieferantenverwaltung();
		if (lieferantenverw.readLiefFolder(preisListenOrdner)==false){
			return false;
		}
		// �bergabe der Lieferantenverwaltung im Konstruktor bei Rezeptverwaltung, wird f�r die Bestimmung der Rezepttypen ben�tigt.
		rezeptverw = new Rezeptverwaltung(lieferantenverw); 
		rezeptverw.liesRezepte(rezeptPfad);
		rezeptverw.liesHitliste(hitListenPfad);
		
		return true;
	}

	/**
	 * Die Methode st��t die Erstellung der Kantinenpl�nen f�r die gew�hlte Anzahl der Kantinen an und schreibt diese nach Erstellung 
	 * in die Kantinenplanliste der Kantinenplanung. Dazu liest sie aus der config.properties die Informationen zu Name und Anzahl der Mitarbeiter. 
	 * Sollte die config.properties diese Daten nicht (oder falsch formatiert) haben, wird die 
	 */
	public void startKantinenplanung() {
		//Variablen initialisieren
		kantPlanList = new ArrayList<Kantinenplan>();
		int anzMA = 0;
		String name = null;
		//Kantinenplan-Schleife
		for (int i = 1; i<=anzKantinen; i++) {
			//Mit dem try-catch-Block werden zun�chst Name und Anzahl der Mitarbeiter je Kantine aus der config.properties geholt
			try {
				Properties properties = new Properties();
				BufferedInputStream stream = new BufferedInputStream(new FileInputStream("config.properties"));
				properties.load(stream);
				stream.close();
				//Sollten die Daten in der config.properties fehlen oder fehlerhaft sein, wird die Schleife gebrochen.
				if (properties.getProperty("NameKantine"+i)==null || properties.getProperty("AnzahlKantine"+i)==null){
					MainWin.StringOutln("In der config.properties sind keine Daten f�r die "+i+". Kantine angegeben");
					break;
				}
				
				name = properties.getProperty("NameKantine"+i);
				anzMA = Integer.parseInt(properties.getProperty("AnzahlKantine"+i));
				
				//Kantinenplan anlegen, die Gerichte usw. erzeugen, die Ausgabedatei schreiben und der Liste hinzuf�gen
				Kantinenplan plan = new Kantinenplan(name, anzMA);
				plan.erzeugePlan(lieferantenverw, rezeptverw);
				plan.schreibeKantinenplan();
				kantPlanList.add(plan);
			} catch (IOException e) {
				//Lesefehler
				MainWin.StringOutln(e.toString());
				MainWin.StringOutln("Die Datei config.properties konnte nicht gelesen werden. Pr�fen Sie, ob sie im Anwendungsordner vorhanden ist.");
			}
			catch (NumberFormatException e){
				//Datenfehler
				MainWin.StringOutln(e.toString());
				MainWin.StringOutln("In der config.properties konnte wegen eines falschen Zahlenformats nicht gelesen werden.");
				MainWin.StringOutln("Pr�fen Sie, ob die numerischen Werte nur Ziffern enthalten.");
			}
		}
	}



	/**
	 * Hier wird die Steuerung der gesamten Einkaufslistenerstellung hinterlegt.
	 * Die Parameter wurden bereits vorher aus der config.properties extrahiert.
	 */
	public void startEinkaufsplanung() {
		MainWin.StringOutln("");

		einkaufsliste = new Einkaufsliste();
		for (int i=0;i<kantPlanList.size();i++){
			//�ber addKantinenplan werden die Tagesgerichte des Kantinenplans in Bedarfspositionen f�r die Einkaufsplanung umgesetzt.
			einkaufsliste.addKantinenplan(kantPlanList.get(i));
		}
		einkaufsliste.erzeugeEinkaufsliste(lieferantenverw);
		einkaufsliste.schreibeEinkaufsliste();
		
		DecimalFormat df = new DecimalFormat (",##0.00");
		Double gesamtkosten=einkaufsliste.getGesamtkosten();
		MainWin.StringOutln("");
		MainWin.StringOutln("Die Gesamtkosten f�r die Bestellung inklusive Lieferkosten betragen "+(df.format(gesamtkosten)));
		
		/*Debug-Print
		ArrayList<BedarfPos> bpl=einkaufsliste.getBedarfPosList();
		for(int i=0;i<bpl.size();i++){
			float m=bpl.get(i).getMenge();
			String e=bpl.get(i).getEinheit();
			String n=bpl.get(i).getName();
			MainWin.StringOutln(m+" "+e+" "+n);
		}*/
	}	
}
