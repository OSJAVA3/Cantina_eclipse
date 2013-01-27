import java.text.DecimalFormat;
import java.util.*;
import java.io.*;

/**
 * Die Klasse Kantinenplanung ist die auszuführende Klasse, welche die
 * Ablauflogik des Programms umfasst. Sie enthält neben der Main-Methode auch
 * Attribute für die wesentlichen Parameter, die zum Programmstart notwendig
 * sind. Diese werden über Java-Properties realisiert.
 * 
 * Für den Planungsdurchgang steht diese Klasse mit der Lieferantenverwaltung,
 * der Rezeptverwaltung, dem Kantinenplan und der Einkaufliste in Assoziation.
 * 
 * @author Rene Wiederhold
 * @version 1.0
 */
public class Kantinenplanung {

	// Die wesentlichen Parameter, welche für die Anwendung benötigt werden
	/** Die Anzahl der zu erstellenden Kantinenpläne */
	private int anzKantinen;
	/** Der relative oder absolute Pfad zur Rezepte-Datei */
	private String rezeptPfad;
	/** Der relative oder absolute Pfad zur Hitliste-Datei */
	private String hitListenPfad;
	/** Der relative oder absolute Pfad zum Ordner, welche die Preislisten der Lieferanten enthält */
	private String preisListenOrdner;

	// Attribute für die Verwaltungsklassen
	private Rezeptverwaltung rezeptverw;
	private Lieferantenverwaltung lieferantenverw;
	//Attribute für die Geschäftslogikklassen
	private Einkaufsliste einkaufsliste;
	private ArrayList<Kantinenplan> kantPlanList;


	/**
	 * Konstruktor für das Objekt.
	 */
	public Kantinenplanung() {
		new MainWin();
		MainWin.StringOutln("");
	}

	/**
	 * Führt das Programm aus und ruft die einzelnen Methoden gemäß der
	 * Ablauflogik auf.
	 * 
	 * @param String[] Übergegebene Parameter werden nicht verwendet.
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
		// Einen (oder zwei) Kantinenplan erzeugen und mit Tagesgerichten füllen
		app.startKantinenplanung();
		// Die Einkaufsliste für ALLE Kantinenpläne erzeugen
		app.startEinkaufsplanung();
		
		MainWin.StringOut("Guten Appetit");
	}

	

	/**
	 * Die Methode lädt die Parameter aus der Datei "config.properties" im
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

			MainWin.StringOutln("Anzahl der zu erstellenden Kantinenpläne: "+anzKantinen);
			MainWin.StringOutln("Pfad zur Rezept-Datei: "+rezeptPfad);
			MainWin.StringOutln("Pfad zur Hitlisten-Datei: "+hitListenPfad);
			MainWin.StringOutln("Pfad zum Lieferanten-Ordner: "+preisListenOrdner);
			MainWin.StringOutln("");
			return true;
			 
		} catch (IOException e) {
			MainWin.StringOutln(e.toString());
			MainWin.StringOutln("Die Datei config.properties konnte nicht gelesen werden. Prüfen Sie, ob sie im Anwendungsordner vorhanden ist.");
			return false;
		}
	}

	/**
	 * Die Methode stößt die Erstellung der Verwaltungsklassen (Lieferanten- und Rezeptverwaltung) und der darunterliegenden Datenschicht (Lieferant, Artikel, 
	 * Lebensmittel, Rezept, Zutat) an.
	 */
	public boolean startDateienEinlesen() {
		lieferantenverw = new Lieferantenverwaltung();
		if (lieferantenverw.readLiefFolder(preisListenOrdner)==false){
			return false;
		}
		// Übergabe der Lieferantenverwaltung im Konstruktor bei Rezeptverwaltung, wird für die Bestimmung der Rezepttypen benötigt.
		rezeptverw = new Rezeptverwaltung(lieferantenverw); 
		rezeptverw.liesRezepte(rezeptPfad);
		rezeptverw.liesHitliste(hitListenPfad);
		
		return true;
	}

	/**
	 * Die Methode stößt die Erstellung der Kantinenplänen für die gewählte Anzahl der Kantinen an und schreibt diese nach Erstellung 
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
			//Mit dem try-catch-Block werden zunächst Name und Anzahl der Mitarbeiter je Kantine aus der config.properties geholt
			try {
				Properties properties = new Properties();
				BufferedInputStream stream = new BufferedInputStream(new FileInputStream("config.properties"));
				properties.load(stream);
				stream.close();
				//Sollten die Daten in der config.properties fehlen oder fehlerhaft sein, wird die Schleife gebrochen.
				if (properties.getProperty("NameKantine"+i)==null || properties.getProperty("AnzahlKantine"+i)==null){
					MainWin.StringOutln("In der config.properties sind keine Daten für die "+i+". Kantine angegeben");
					break;
				}
				
				name = properties.getProperty("NameKantine"+i);
				anzMA = Integer.parseInt(properties.getProperty("AnzahlKantine"+i));
				
				//Kantinenplan anlegen, die Gerichte usw. erzeugen, die Ausgabedatei schreiben und der Liste hinzufügen
				Kantinenplan plan = new Kantinenplan(name, anzMA);
				plan.erzeugePlan(lieferantenverw, rezeptverw);
				plan.schreibeKantinenplan();
				kantPlanList.add(plan);
			} catch (IOException e) {
				//Lesefehler
				MainWin.StringOutln(e.toString());
				MainWin.StringOutln("Die Datei config.properties konnte nicht gelesen werden. Prüfen Sie, ob sie im Anwendungsordner vorhanden ist.");
			}
			catch (NumberFormatException e){
				//Datenfehler
				MainWin.StringOutln(e.toString());
				MainWin.StringOutln("In der config.properties konnte wegen eines falschen Zahlenformats nicht gelesen werden.");
				MainWin.StringOutln("Prüfen Sie, ob die numerischen Werte nur Ziffern enthalten.");
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
			//Über addKantinenplan werden die Tagesgerichte des Kantinenplans in Bedarfspositionen für die Einkaufsplanung umgesetzt.
			einkaufsliste.addKantinenplan(kantPlanList.get(i));
		}
		einkaufsliste.erzeugeEinkaufsliste(lieferantenverw);
		einkaufsliste.schreibeEinkaufsliste();
		
		DecimalFormat df = new DecimalFormat (",##0.00");
		Double gesamtkosten=einkaufsliste.getGesamtkosten();
		MainWin.StringOutln("");
		MainWin.StringOutln("Die Gesamtkosten für die Bestellung inklusive Lieferkosten betragen "+(df.format(gesamtkosten)));
		
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
