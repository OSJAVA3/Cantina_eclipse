import java.util.ArrayList;
/**
 * Die Rezeptverwaltung erstellt aus der Rezeptdatei die Objektabbildungen der Rezepte sowie deren Zutaten und
 * verwaltet diese. Sie stellt Methoden bereit um Referenzen auf bestimmte Rezept-Objekte 
 * zu �bergeben. Au�erdem kann sie auch die Hitliste einlesen und die Positionen den einzelnen Rezepten zuweisen.
 * 
 * Sie steht in Assoziation zur Verwaltungsklasse Lieferantenverwaltung, der Kantinenplanung sowie dem Kantinenplan.
 * Sie aggregiert die Objekte Rezept und Zutat.
 * 
 * @author  Lukas Krotki 
 * @version 1.0
 */
public class Rezeptverwaltung
{
	private String rezeptPfad;
	private String rezeptName;
	private Lieferantenverwaltung lieferantenVerw;
	private ArrayList<Rezept> rezeptListe;

	/** 
	 * Bereits bei der Konstruktion muss eine Referenz zur Lieferantenverwaltung �bergeben werden, 
	 * da diese zur Typpr�fung ben�tigt wird.
	 * 
	 * @param lieferantenverw Die zur Typpr�fung zu verwendende Lieferantenverwaltung
	 */ 
	public Rezeptverwaltung(Lieferantenverwaltung liefVerw)
	{
		lieferantenVerw = liefVerw;
		rezeptListe = new ArrayList<Rezept>();
	}

	/**
	 * Rezepte in Rezeptliste aufnehmen
	 * 
	 * @param: rezept Rezept auf Rezeptliste
	 */
	public void addRezept (Rezept rezept){
		rezeptListe.add (rezept);
	}

	/**
	 * Die Methode liest die Rezeptdatei ein erstellt Rezeptobjekte sowie Zutatenobjekte aus der eingelesenen Datei
	 * weist die Zutaten den Rezepten zu. Die Rezepte werden in die RezeptArrayList abgelegt.
	 * 
	 * @param   rezeptpfad Den Pfad zur Rezeptdatei.
	 * @return  Gibt True zur�ck, wenn die Rezepte vollst�ndig eingelesen wurden. 
	 *          Gibt False zur�ck, falls Fehler aufgetreten sind.   
	 */
	public boolean liesRezepte(String rezeptpfad) 
	{
		//Datei �ffnen
		Datei inFile = new Datei(rezeptpfad);
		inFile.openInFile_FS();


		// Abfrage, ob das Oeffen funktioniert hat
		if (!inFile.state()){
			// Ausgabe des Fehlers im Terminalfenster
			MainWin.StringOutln("Fehler beim �ffnen der Eingabedatei "+rezeptPfad);
			// Abbrechen der Methode
			return false;
		}

		//Rezept initialisieren
		Rezept rezept = null;
		//Datei-Schleife
		while (!inFile.eof()){
			// Zeile einer Datei einlesen
			String zeile = inFile.readLine_FS();
			//Debug-Print
			//MainWin.StringOutln(zeile);

			//Wenn aktuelle Zeile einen NullPointer enth�lt, wird gebrochen. 
			if (zeile != null){

				//Der CSVService macht aus den Eingabe-String (Zeile aus Datei) eine ArrayList, die die einzelnen Werte getrennt enth�lt
				ArrayList<String> fields = CSVService.getFields(zeile);

				//Debug-Print
				//MainWin.StringOutln("Zeile: "+zeilennummer+" Wert1: "+fields.get(0)+" Wert2: "+fields.get(1)+" Wert3: "+fields.get(2)+" Wert 4: " +fields.get(3));

				// pr�fe ob die eingelesene Zeile dem aktuellen Rezept entspricht
				if ( !fields.get(0).equals(rezeptName)) {

					//uebergebe der Variable rezeptNamen den String aus dem Feld 0 
					rezeptName = fields.get(0).toString();
                    
					//Verweist kein Nullpointer auf das rezept, wird der RezeptTyp gesetzt
					if ( rezept != null)
						setzeRezeptTyp(rezept);

					// erzeuge ein neues Objekt Rezept mit rezeptName als zu �bergebenden Parameter
					rezept = new Rezept(rezeptName);

					// Setze den Rezeptnamen -> nicht mehr ben�tigt, da schon als Parameter �bergeben
					//rezept.setName(fields.get(0).toString()); 


					//Rezept in Rezeptliste aufnehmen
					rezeptListe.add(rezept);

					//Debug Print
					//MainWin.StringOutln(rezept.getName());


				}
               
				//hinzuf�gen einer Zutat 
				rezept.addZutat(makeZutat(fields));
				
				//Debug-Print
				//MainWin.StringOutln("Zutat: " +fields.get(3).toString());

				// Zutatenobjekt erzeugen
				//Zutat zutat = new Zutat(fields.get(3).toString(), Float.valueOf(fields.get(1).toString().replace(",", ".")), fields.get(2).toString());
				// Zutat zu einem Rezept hinterlegen
				//rezept.addZutat( zutat );
				// Debug Print
				//MainWin.StringOutln(" Zutat: "+fields.get(3).toString());
			}

		}
		//Rezepttyp fuer das letzte Rezept, da sonst nicht veregeben wird
		setzeRezeptTyp (rezept);
		
		MainWin.StringOutln("Die Datei "+rezeptpfad+" wurde erfolgreich eingelesen");
		return true;
	}



	/**
	 * Methode zum setzen des Rezepttyps. 
	 * Durch die �bergebene Lieferantenverwaltung, erhalten die Zutaten sowie 
	 * Rezepte dar�ber Kenntnis welchen RezeptTyp diese entsprechen 
	 * 
	 * @param Rezept   
	 * @return  True f�r vollst�ndige Zuweisung, False f�r unvollst�ndig    
	 */
	private void setzeRezeptTyp (Rezept rezept){

		rezept.setRezeptTyp(lieferantenVerw);
	}

	/**
	 * Die Methode liest die Hitlistendatei ein und weist den im RezeptArrayList 
	 * enthaltenen Rezeptobjekten ihre Hitlistenposition zu.
	 * 
	 * @param hitlistenpfad Den Pfad zur Hitlistendatei  
	 * @return  True f�r vollst�ndige Zuweisung, False f�r unvollst�ndig    
	 */
	public boolean liesHitliste(String hitlistenPfad) 
	{
		//Datei �ffnen
		Datei inFile = new Datei(hitlistenPfad);
		inFile.openInFile_FS();


		// Abfrage, ob das Oeffen funktioniert hat
		if (!inFile.state()){
			// Ausgabe des Fehlers im Terminalfenster
			MainWin.StringOutln("Fehler beim �ffnen der Eingabedatei "+hitlistenPfad);
			// Abbrechen der Methode
			return false;
		}
		
		//Weise rezeptName null zu
		Rezept rezeptName = null;

		//Datei-Schleife
		while (!inFile.eof()){

			//zeilennummer++;

			// Zeile einer Datei einlesen
			String zeile = inFile.readLine_FS();

			//Debug-Print
			//MainWin.StringOutln(zeile);

			//Wenn aktuelle Zeile einen NullPointer enth�lt, wird gebrochen. 
			if (zeile != null){

				//Der CSVService macht aus den Eingabe-String (Zeile aus Datei) eine ArrayList, die die einzelnen Werte getrennt enth�lt
				ArrayList<String> fields = CSVService.getFields(zeile);

				//Debug-Print
				//MainWin.StringOutln("Zeile: "+zeilennummer+" Wert1: "+fields.get(0)+" Wert2: "+fields.get(1));


				// Uebergebe der Variable rezeptName den String aus dem Feld 1
				rezeptName = getRezeptWithName( fields.get(1).toString() );

				// Solange der Rezeptname auf keinen Nullpointer verweist, setze die Hitlistenposition aus dem Feld 0
				if ( rezeptName != null) {
					rezeptName.setHitlistenpos( fields.get(0) );

					// Debug Print
					//MainWin.StringOutln("Rezept: "+rezeptName.getName()+" Position: "+fields.get(0));
				} else
					//Debug Print
					MainWin.StringOutln("Rezept: "+fields.get(1)+" nicht gefunden.");

			}
		}
		MainWin.StringOutln("Die Datei "+hitlistenPfad+" wurde erfolgreich eingelesen");
		return true;

	}
	/** Pr�fen, ob der Rezeptname bereits in der Rezeptliste enhalten ist
	 * 
	 * @param rezeptName Name eines Rezeptes
	 * 
	 * @return null
	 */
	private Rezept getRezeptWithName( String rezeptName ) {
		// Durchsuche die rezeptListe und pr�fe, ob bereits ein Element aus der Liste den �bergebenen Rezeptnamen besitzt
		for(Rezept rezeptElement : rezeptListe ) {
			if(rezeptElement.getName().equals(rezeptName))
				// gebe das rezeptElement aus
				return rezeptElement;
		}

		return null;
	}

	/**Methode zur Objekterzeugung von Zutaten
	 * 
	 * @param fields
	 * @return zutat Es werden Zutaten erzeugt
	 */
	private Zutat makeZutat(ArrayList<String> fields) {
		Zutat zutat = new Zutat(
				fields.get(3).toString(),
				Float.valueOf(fields.get(1).toString().replace("," , ".")),
				fields.get(2)
				);

		return zutat;
	}

	/**
	 * Die Methode gibt ein zuf�lliges Fischrezept aus dem RezeptArrayList zur�ck.
	 * 
	 * @return Ein zuf�lliges Fischrezept 
	 */
	public Rezept gibFisch() 
	{
		//Erzeugt eine zuf�llige Ganzzahl 0<=x<Elemente in der Rezeptliste
		int rnd=new Double(Math.random() * rezeptListe.size()).intValue();
		//Holt das entsprechende Rezept aus der Liste
		Rezept rzp = rezeptListe.get(rnd);
		//Solang es kein Fischrezept ist, werden weitere Rezepte aufgerufen
		while (rzp.getTyp() != RezeptTyp.Fisch || rzp.getVerwendet()==true){
			rnd=new Double(Math.random() * rezeptListe.size()).intValue();
			rzp = rezeptListe.get(rnd);
		} 
		return rzp;
	}

	/**
	 * Die Methode gibt ein zuf�lliges Fleischrezept aus dem RezeptArrayList zur�ck.
	 * 
	 * @return       Ein zuf�lliges Fleischrezept
	 */
	public Rezept gibFleisch() 
	{
		//siehe gibFisch
		int rnd=new Double(Math.random() * rezeptListe.size()).intValue();
		Rezept rzp = rezeptListe.get(rnd);
		while (rzp.getTyp() != RezeptTyp.Fleisch || rzp.getVerwendet()==true){
			rnd=new Double(Math.random() * rezeptListe.size()).intValue();
			rzp = rezeptListe.get(rnd);
		} 
		return rzp;
	}

	/**
	 * Die Methode gibt ein zuf�lliges vegetarisches Rezept aus dem RezeptArrayList zur�ck.
	 * 
	 * @return       Ein zuf�lliges vegetetarisches Rezept 
	 */
	public Rezept gibVeggie() 
	{
		//siehe gibFisch
		int rnd=new Double(Math.random() * rezeptListe.size()).intValue();
		Rezept rzp = rezeptListe.get(rnd);
		while (rzp.getTyp() != RezeptTyp.Vegetarisches || rzp.getVerwendet()==true){
			rnd=new Double(Math.random() * rezeptListe.size()).intValue();
			rzp = rezeptListe.get(rnd);
		} 
		return rzp;
	}

	/**
	 * Die Methode gibt ein zuf�lliges Rezept aus dem RezeptArrayList zur�ck.
	 * 
	 * @return        Ein zuf�lliges Rezept 
	 */
	public Rezept gibRandom() 
	{
		//siehe gibFisch. Hier ohne Schleife, da ein vollkommen zuf�lliges Rezept ausreicht, ist keine Typ-Pr�fung n�tig
		int rnd = new Double(Math.random() * rezeptListe.size()).intValue();
		Rezept rzp = rezeptListe.get(rnd);
		while (rzp.getVerwendet()==true){
			rnd=new Double(Math.random() * rezeptListe.size()).intValue();
			rzp = rezeptListe.get(rnd);
		}
		return rzp;
	}

}
