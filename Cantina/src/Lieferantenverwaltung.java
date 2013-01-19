import java.util.ArrayList;
import java.util.Iterator;
import java.io.*;

/**
 * Die Klasse Lieferantenverwaltung erstellt und verwaltet die Objekte der
 * Klassen Lieferant, Artikel und Lebensmittel und aggregiert diese. Sie stellt
 * Methoden zur Verfügung, um die günstigsten Artikel der Lieferanten zu
 * berechnen, zu überprüfen, ob ausreichende Mengen an Zutaten beschafft werden
 * können und ob Tagesgerichte als Fleisch-, Fisch- oder vegetarische Gerichte
 * beschrieben werden können.
 * 
 * Die Rezeptverwaltung ist mit dieser Klasse assoziiert, da die
 * Rezeptverwaltung Methoden der Lieferantenverwaltung zur Erstellung ihrer
 * eigenen Datenschicht (Rezept, Zutat) verwendet. Die Einkaufsliste verwendet
 * die Lieferantenverwaltung um Artikelobjekte zu erhalten. Für die
 * Planungsdurchführung steht sie in Assoziation zu der Kantinenplanung, da die
 * Kantinenplanung die Lieferantenverwaltung konstruiert und die Erzeugung der
 * Datenschicht anstößt.
 * 
 * @author Rene Wiederhold
 * @version 0.01
 */
public class Lieferantenverwaltung {
	private ArrayList<Artikel> artList;
	private ArrayList<Lieferant> liefList;
	private ArrayList<Lebensmittel> lmList;

	/**
	 * Der Konstruktor der Lieferantenverwaltung
	 */
	public Lieferantenverwaltung() {
		artList = new ArrayList<Artikel>();
		liefList = new ArrayList<Lieferant>();
		lmList = new ArrayList<Lebensmittel>();
	}

	/**
	 * Die Methode prüft, von welchem Typ eine Zutat ist (Fleisch, Fisch,
	 * Veggie). Dies wird von der Rezeptverwaltung zur Erstellung der Rezepte
	 * verwendet.
	 * 
	 * @param zutat
	 *            Die zu prüfende Zutat
	 * @return Einen standardisierten String, der die Typ-Bezeichnung enthält.
	 */
	public RezeptTyp holeTyp(Zutat zutat){
		String typ = null;
		Iterator<Lebensmittel> it = lmList.iterator();
		while (it.hasNext()){
			Lebensmittel lm=it.next();
			if (lm.getName().equals(zutat.getName())){
				typ=lm.getTyp();
				break;
			}
		}
		if (typ == null)
			return RezeptTyp.Vegetarisches;
		else if (typ.equals("m"))
			return RezeptTyp.Fleisch;
		else if (typ.equals("f"))
			return RezeptTyp.Fisch;
		else
			return RezeptTyp.Vegetarisches;
	}

	/**
	 * Die Methode prüft, ob die benötigten Mengen Lebensmittel am Markt
	 * vorhanden sind, um ein Tagesgericht in der Planungs- periode anbieten zu
	 * können. Beim Aufruf wird die benötigte Menge vom zugehörigen
	 * Lebensmittel-Objekt abgezogen, sofern ausreichend. Dies wird vom
	 * Kantinenplan verwendet.
	 * 
	 * @param Tagesgericht
	 *            Das zu überprüfende Tagesgericht
	 * @return True, falls ausreichend Lebensmittel beschafft werden können,
	 *         ansonsten False
	 */

	public boolean lebensmittelVerfuegbar(Tagesgericht tagesgericht) {
		ArrayList<Zutat> zutatList= tagesgericht.getRezept().getZutaten();
		int c=0; //Zähler für die verfügbaren Lebensmittel
		
		//Zutatenschleife
		for (int i=0;i<zutatList.size();i++){
			Zutat z=zutatList.get(i);
			//Lebensmittelschleife
			for (Lebensmittel lm : lmList){
				if (lm.getName().equals(z.getName())) {
					// Differenz zwischen verfügbaren Lebensmitteln und benötigter Zutatmenge
					float diff=(lm.getMenge()-(z.getMenge()*tagesgericht.getMenge()));
					//Verfügbare Restmenge des Lebensmittels ist kleiner 0, Verfügbar: false
					if (diff<0){
						//Debug-Print
						System.out.println("Die Menge an "+z.getName()+" für "+tagesgericht.getRezept().getName()+" reicht nicht aus");
						tagesgericht.getRezept().setVerwendet(true);
						return false;
					}
					//Die Restmenge ist größer als 0
					else if (diff>=0){
						c++; //Lebensmittel verfügbar, Zähler erhöhen
					}
				}
			} //Ende Lebensmittelschleife
		} // Ende Zutatenschleife
		
		if (c==zutatList.size()){
			//Es sind alle Lebensmittel ausreichend verfügbar.
			for (int i=0;i<zutatList.size();i++){
				Zutat z=zutatList.get(i);
				//Lebensmittelschleife
				for (Lebensmittel lm : lmList){
					if (lm.getName().equals(z.getName())){
						lm.setMenge(lm.getMenge()-(z.getMenge()*tagesgericht.getMenge()));
						//Debug-Print
						//System.out.println("Menge von "+lm.getName()+" auf "+lm.getMenge()+" angepasst");
					}
				} //Ende Lebensmittelschleife
			} // Ende Zutatenschleife
			return true;
		}
		//falls ein Lebensmittel garnicht verfügbar ist, ist c<>Anzahl der Zutaten. Es wird false zurückgegeben.
		else {
			return false;
		}
	}

	/**
	 * Die Methode dient zum Aufbau der Lieferantenverwaltung. Sie erzeugt aus
	 * den Eingabe-Dateien die Bauernhof-, Grosshandel- und Artikelobjekte sowie
	 * die Lebensmittel-Objekte.
	 * 
	 * 
	 * @param String
	 *            Der Pfad zum Ordner, der die Lieferantenpreislisten enthält.
	 * @return True, falls die Lieferantendateien eingelesen werden konnten,
	 *         False, falls Probleme aufgetreten sind.
	 */
	public boolean readLiefFolder(String lieferantenOrdner) {
		File folder = new File(lieferantenOrdner);
		/* Debug-Print
		System.out.println("Angegebener Lieferantenordner ist ein Ordner: "
				+ folder.isDirectory());	*/

		String[] fileList = folder.list();
		if (folder.isDirectory()) {
			// Start der Ordner-Schleife
			for (int i = 0; i < fileList.length; i++) {
				// Datei öffnen
				if (readLiefFile(lieferantenOrdner + "//" + fileList[i]) == true) {
					// Debug-Print
					System.out.println("Die Datei " + fileList[i]
							+ " wurde erfolgreich eingelesen");
				} 
			}

			/*// Debug-Print aus der Artikelliste
			for (int j = 0; j < artList.size(); j++) {
				Artikel art = artList.get(j);
				System.out.println("Artikelname: " + art.getName());
				System.out.println("Gebindegröße: " + art.getGebindegroesse());
				System.out.println("Einheit: " + art.getEinheit());
				System.out.println("Einzelpreis: " + art.getPreis());
				System.out.println("Artikelanzahl: " + art.getArtikelanzahl());
				System.out.println("Lieferantname: "
						+ art.getLieferant().getLieferantenName());
			}
			// Debug-Print aus der Lieferantenliste
			for (int k = 0; k < liefList.size(); k++) {
				Lieferant lief = liefList.get(k);
				System.out.println(lief.getLieferantenName());
				System.out.println(lief.getClass().toString()
						.equals("class Bauernhof"));
			}
			//Debug-Print aus der Lebensmittelliste
			for (int l=0;l<lmList.size();l++){
				Lebensmittel lm=lmList.get(l);
				System.out.println("Name: "+lm.getName()+" Menge: "+lm.getMenge()+" Typ: "+lm.getTyp());
			}
			System.out.println(lmList.size());*/
		}
		return true;
	}

	/**
	 * Liest eine Lieferantendatei und schreibt die Lieferanten und Artikel in
	 * die zugehörigen ArrayLists
	 * 
	 * @param in
	 *            Der Pfad zur einzulesenden Lieferantendatei
	 * @return Einen booleschen Wert, ob die Datei erfolgreich eingelesen wurde.
	 */
	private boolean readLiefFile(String in) {

		Datei inFile = new Datei(in);
		inFile.openInFile_FS(); // öffnet den readbuffer

		// Abfrage, ob das Oeffen funktioniert hat
		if (!inFile.state()) {
			// Ausgabe des Fehlers im Terminalfenster
			System.out.println("Es ist ein Fehler beim Öffnen der Datei "+in+" aufgetreten");
			// Abbrechen der Methode
			return false;
		}

		// Zeilenzähler für den Import
		int zeilennummer = 0;

		// Datei-Schleife
		while (!inFile.eof()) {
			// Erhöhung des Zeilenzählers
			zeilennummer++;
			// Liest eine Zeile aus der Eingabedatei
			String zeile = inFile.readLine_FS();

			// Prüft ob zeile keinen NullPointer enthält.
			if (!(zeile == null)) {
				// Der CSVService macht aus den Eingabe-String (Zeile aus Datei)
				// eine ArrayList, die die einzelnen Werte getrennt enthält
				ArrayList<String> fields = CSVService.getFields(zeile);

				// Erste Zeile enthält Lieferanteninformationen
				if (zeilennummer == 1) {
					// Es liegt ein Grosshandel-Einkaufsliste vor
					if ((fields.get(0)).equals("Grosshandel")) {
						// Grosshandel erzeugen
						Grosshandel lieferant = new Grosshandel();

						// Namen setzen
						lieferant.setLieferantName((fields.get(1)).toString());
						lieferant.setKostensatz(Float.valueOf(
								(fields.get(2).replaceAll(",", ".")))
								.floatValue());

						liefList.add(lieferant);

						// Debug-Print
						// System.out.println(lieferant.getLieferantenName());
					}
					// Es liegt eine Bauernhof-Einkaufsliste vor.
					else if ((fields.get(0)).equals("Bauer")) {
						// Bauernhof erzeugen
						Bauernhof lieferant = new Bauernhof();

						// Namen und Entfernung setzen
						lieferant.setLieferantName((fields.get(1)).toString());
						lieferant.setEntfernung(Float.valueOf(
								(fields.get(2).replaceAll(",", ".")))
								.floatValue());

						// Lieferant der Lieferantenliste hinzufügen
						liefList.add(lieferant);

						// Debug-Print
						// System.out.println(lieferant.getLieferantenName());
					}
				} 
				else {
					// Artikel erzeugen und Lieferanten zuweisen, sofern Länge des Artikelnamen NICHT 0 ist ODER die verfügbaren Gebinde NICHT 0 sind
					if (!(fields.get(2).length() == 0) && !(fields.get(5).equals("0"))) {
						Artikel art = new Artikel(fields.get(2));
						art.setArikelanzahl(Integer.parseInt(fields.get(5)));
						art.setEinheit(fields.get(1));
						// Das Komma im String muss in einen Punkt umgewandelt
						// werden, sonst funktioniert der Typecast nicht.
						art.setPreis(Float.valueOf(
								(fields.get(4).replaceAll(",", ".")))
								.floatValue());
						art.setGebindegroesse(Float.valueOf(
								(fields.get(0).replaceAll(",", ".")))
								.floatValue());
						art.setLieferant(liefList.get(liefList.size() - 1));

						artList.add(art);
						addLebensmittel(art.getName(), art.getArtikelanzahl()
								* art.getGebindegroesse(),art.getEinheit(), fields.get(3));

					}
				}
			}

		}
		return true;
	}

	/**
	 * Die Methode gibt eine ArrayList zurück, die alle Artikel enthält, die den
	 * gleichen Namen haben, wie der übergebene String-Parameter. Dies wird zur
	 * Erzeugung der BestellPos-Objekte von der Einkaufsliste genutzt.
	 * 
	 * @param name
	 *            Die Bezeichnung einer Zutat
	 * @return Einen ArrayList, der die Referenzen zu allen Artikel-Objekte
	 *         enthält, deren Name mit dem Parameter übereinstimmen
	 */
	public ArrayList<Artikel> gibAlleArtikel(String name) {
		ArrayList<Artikel> result=new ArrayList<Artikel>();
		for (Artikel art:artList){
			if(art.getName().equals(name)){
				if (result.size()==0){
					result.add(art);
				}
				else if ( (result.get(result.size()-1).getPreis()/result.get(result.size()-1).getGebindegroesse()) < (art.getPreis()/art.getGebindegroesse()) ){
					result.add(art);						
				}
				else{
					for (int i=0;i<result.size();i++){
						if ( (result.get(i).getPreis()/result.get(i).getGebindegroesse()) > (art.getPreis()/art.getGebindegroesse())){
							result.add(i, art);
							break;
						}
					} //Ende result-Schleife
				}				
			}
		} //Ende Artikel-Schleife
		return result;
	}

	/**
	 * Die Methode prüft, ob in der Lebensmittelliste der Lieferantenverwaltung
	 * bereits ein Lebensmittel vorhanden ist, dessen Name mit dem übergegebenen
	 * String lm übereinstimmt. Ist dies der Fall wird nur die Menge m
	 * aufaddiert. Existiert noch kein Lebensmittel mit dem Namen lm, wird ein
	 * neues Lebensmittelobjekt erzeugt, die Menge m diesem Objekt zugewiesen
	 * und das Lebensmittel-Objekt in die Lebensmittelliste eingefügt. Außerdem
	 * wird der Typ eines Lebensmittels gesetzt, sofern ein neues
	 * Lebensmittelobjekt erzeugt werden muss.
	 * 
	 * @param lm
	 *            Der Name des Lebensmittel
	 * @param m
	 *            Die zu berücksichtigende Menge des Lebensmittel
	 * @param typ
	 *            Der Typ des Lebensmittel als standardisierter String. "m" für
	 *            Meat, "f" für Fisch, "" für vegetarisch.
	 */
	private void addLebensmittel(String lmName, float m, String einh, String typ) {
		//Wenn lmList leer ist, muss immer ein neues Lebensmittel hinzugefügt werden.
		if (lmList.isEmpty()){
			Lebensmittel firstLm = new Lebensmittel();
			firstLm.setName(lmName);
			firstLm.setTyp(typ);
			firstLm.setMenge(m);
			firstLm.setEinheit(einh);
			lmList.add(firstLm);
			//Debug-Print
			//System.out.println(lmName+" wurde der Liste als Erstes hinzugefügt");
		}
		else{
			//Es sind bereits Elemente in der lmList
			//Es muss geprüft werden ob ein Objekt gleichen Namens vorhanden ist
			//Hier wird eine "Iterator-Schleife verwendet
			Iterator<Lebensmittel> it=lmList.iterator();
			//Variable für die "Vorhanden"-Prüfung
			Boolean vorh=false;
			//Variable für das bereits in der List vorhandene Lebensmittel
			Lebensmittel vorhLm=new Lebensmittel();
			//Solang die List noch ein weiteres Element hat, läuft die Schleife
			while (it.hasNext()){
				//next() gibt das nächste Lebensmittel-Objekt
				Lebensmittel lmIt=it.next();
				//Stimmt der Name des Lebensmittel mit dem zu prüfenden überein
				//Wenn ja, dass Objekt temporär gespeichert und die Schleife gebrochen.
				if(lmIt.getName().equals(lmName)){
					vorh=true;
					vorhLm=lmIt;
					//Debug-Print
					//System.out.println(lmName+" ist in lmList vorhanden");
					break;
				}
				//Stimmt der übergebene Name des Lebensmittels nicht mit dem iterierten überein, bleibt vorh auf false
				else{
					vorh=false;
					//Debug-Print
					//System.out.println(lmName+" ist noch nicht in lmList vorhanden");
				}	
			} //while-Ende - Jetzt steht entweder vorh auf true und vorhLm enthält das LM-Objekt
			  //oder vorh steht auf false
			
			// Lebensmittel-Objekt schon vorhanden
			if(vorh==true){
				vorhLm.setMenge(vorhLm.getMenge()+m);
				//Debug-Print
				//System.out.println(lmName+"-Menge angepasst");
			}
			// Lebensmittel-Objekt noch nicht vorhanden
			else if(vorh==false){
				vorhLm.setName(lmName);
				vorhLm.setTyp(typ);
				vorhLm.setMenge(m);
				vorhLm.setEinheit(einh);
				lmList.add(vorhLm);
				//Debug-Print
				//System.out.println(lmName+" der Liste hinzugefügt!");
			}	
		} //else-Ende
	}

}
