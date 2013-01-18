import java.util.*;
import java.io.*;

/**
 * Die Klasse Kantinenplanung ist die auszuführende Klasse, welche die
 * Ablauflogik des Programms umfasst. Sie enthält neben der Main-Methode auch
 * Attribute für die wesentlichen Parameter, die zum Programmstart notwendig
 * sind. Diese werden über Java-Properties realisiert. Außerdem enthält sie die
 * Export-Methoden für die Kantinenplaene und die Einkaufsliste
 * 
 * Für den Planungsdurchgang steht diese Klasse mit der Lieferantenverwaltung,
 * der Rezeptverwaltung, dem Kantinenplan, der Einkaufliste und zuletzt dem
 * Exporter in Assoziation.
 * 
 * @author Rene Wiederhold
 * @version 0.01
 */
public class Kantinenplanung {

	// Die wesentlichen Parameter, welche für die Anwendung benötigt werden
	/** Die Anzahl der zu erstellenden Kantinenpläne */
	private int anzKantinen;
	/** Der relative oder absolute Pfad zur Rezepte-Datei */
	private String rezeptPfad;
	/** Der relative oder absolute Pfad zur Hitliste-Datei */
	private String hitListenPfad;
	/**
	 * Der relative oder absolute Pfad zum Ordner, welche die Preislisten der
	 * Lieferanten enthält
	 */
	private String preisListenOrdner;
	/** Die Pauschale, welche mit der Entfernung der Bauernhöfe multipliziert die Transportkosten ergibt */
	private float kmSatz;

	// Attribute für die Verwaltungsklassen
	private Rezeptverwaltung rezeptverw;
	private Lieferantenverwaltung lieferantenverw;
	//Attribute für die Geschäftslogikklassen
	private Einkaufsliste einkaufsliste;
	private ArrayList<Kantinenplan> kantPlanList;

	/**
	 * Konstruktor für das Objekt, bei Erstellung des Objektes werden die
	 * Parameter aus der config.properties eingelesen.
	 */
	public Kantinenplanung() {

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
		app.loadProperties();
		// Alle Dateien einlesen und Rezeptverwaltung, Lieferantenverwaltung
		// (inklusive Datenobjekten) erzeugen
		app.startDateienEinlesen();
		// Einen (oder zwei) Kantinenplan erzeugen und mit Tagesgerichten füllen
		app.startKantinenplanung();
		// Die Einkaufsliste für ALLE Kantinenpläne erzeugen
		app.startEinkaufsplanung();
		//app.schreibeKantinenplan();
	}

	/**
	 * Die Methode lädt die Parameter aus der Datei "config.properties" im
	 * Root-Ordner in die Attribute der Klasse.
	 */
	private void loadProperties() {
		try {
			Properties properties = new Properties();
			BufferedInputStream stream = new BufferedInputStream(
					new FileInputStream("config.properties"));
			properties.load(stream);
			stream.close();
			anzKantinen = Integer.parseInt(properties
					.getProperty("AnzahlKantinen"));
			rezeptPfad = properties.getProperty("RezeptbuchPfad");
			hitListenPfad = properties.getProperty("HitlistenPfad");
			preisListenOrdner = properties.getProperty("PreislistenOrdner");

			/*
			 * Debug-Print
			 * System.out.println("Anzahl der Kantinen: "+anzKantinen);
			 * System.out.println("Pfad zur Rezept-Datei: "+rezeptPfad);
			 * System.out.println("Pfad zur Hitlisten-Datei: "+hitListenPfad);
			 * System
			 * .out.println("Pfad zum Lieferanten-Ordner: "+preisListenOrdner);
			 */
		} catch (IOException e) {
			System.out.println(e.toString());
			System.out
					.println("Die Datei config.properties konnte nicht gelesen werden. Prüfen Sie, ob sie im Anwendungsordner vorhanden ist.");
		}
	}

	/**
	 * Hier wird die Steuerung des Datei-Einlesens, die Erstellung der
	 * Verwaltungsklassen und der Datenschicht hinterlegt. Die Parameter wurden
	 * bereits vorher aus der config.properties extrahiert.
	 */
	public void startDateienEinlesen() {
		lieferantenverw = new Lieferantenverwaltung();
		lieferantenverw.readLiefFolder(preisListenOrdner);

		rezeptverw = new Rezeptverwaltung(lieferantenverw); // Übergabe der Lieferantenverwaltung derzeit im Konstruktor bei Rezeptverwaltung
		rezeptverw.liesRezepte(rezeptPfad);
		rezeptverw.liesHitliste(hitListenPfad);
	}

	/**
	 * Hier wird die Steuerung der gesamten Kantinenplanerstellung hinterlegt.
	 * Die Parameter wurden bereits vorher aus der config.properties extrahiert.
	 */
	public void startKantinenplanung() {
		kantPlanList = new ArrayList<Kantinenplan>();
		int anzMA = 0;
		String name = null;
		for (int i = 1; i<=anzKantinen; i++) {
			try {
				Properties properties = new Properties();
				BufferedInputStream stream = new BufferedInputStream(new FileInputStream("config.properties"));
				properties.load(stream);
				stream.close();
				name = properties.getProperty("NameKantine"+i);
				anzMA = Integer.parseInt(properties.getProperty("AnzahlKantine"+i));	
			} catch (IOException e) {
				System.out.println(e.toString());
				System.out.println("Die Datei config.properties konnte nicht gelesen werden. Prüfen Sie, ob sie im Anwendungsordner vorhanden ist.");
			}
			Kantinenplan plan = new Kantinenplan(name, anzMA);
			plan.erzeugePlan(lieferantenverw, rezeptverw);
			kantPlanList.add(plan);
		}
	}



	/**
	 * Hier wird die Steuerung der gesamten Einkaufslistenerstellung hinterlegt.
	 * Die Parameter wurden bereits vorher aus der config.properties extrahiert.
	 */
	public void startEinkaufsplanung() {
		
		einkaufsliste = new Einkaufsliste();
		for (int i=0;i<kantPlanList.size();i++){
			//Über addKantinenplan werden die Tagesgerichte des Kantinenplans in Bedarfspositionen für die Einkaufsplanung umgesetzt.
			einkaufsliste.addKantinenplan(kantPlanList.get(i));
		}
		einkaufsliste.erzeugeEinkaufsliste(lieferantenverw);
		/*Debug-Print
		ArrayList<BedarfPos> bpl=einkaufsliste.getBedarfPosList();
		for(int i=0;i<bpl.size();i++){
			float m=bpl.get(i).getMenge();
			String e=bpl.get(i).getEinheit();
			String n=bpl.get(i).getName();
			System.out.println(m+" "+e+" "+n);
		}*/
	}	
	public void schreibeKantinenplan() {		
		for( Kantinenplan plan: kantPlanList ) {
			plan.schreibeKantinenplan();
		}
		
	}
}
