import java.util.ArrayList;
/**
 * Die Klasse Kantinenplan repräsentiert einen Kantinenplan im Rahmen der Planungsperiode.
 * Sie aggregiert die Tagesgerichtobjekte und erstellt auf Basis der Anzahl der 
 * Mitarbeiter je Kantine und Standort einen Plan mit insgesamt 45 Tagesgerichten.
 * Sie steht in Assoziation zu der Einkaufsliste um, durch die Übergabe eines
 * Kantinenplans, die Bestellpositonen zu generieren. Der Kantinenplan hat durch die Assoziation zur
 * Reezptverwaltung und Lieferantenverwaltung Zugriff auf deren Methoden um eine gültigen Kantinenplan
 * zu erstellen. Damit ein Planungslauf von der Kantinenplanung durchgführt werden kann, 
 * liegt auch mit dieser Klasse eine Assoziationsbeziehung vor.
 * 
 * @author Rene Wiederhold
 * @version 1.00
 */
public class Kantinenplan
{
	/** Der Standort, für den der Kantinenplan erzeugt wird */ 
	private String standort;
	/** Die Anzahl der Mitarbeiter, welche die Kantine am Standort besuchen*/
	private int anzMitarbeiter;
	/** Die ArrayList enthält die zum Speiseplan gehörenden Tagesgerichte */
	public ArrayList<Tagesgericht> tgArrayList;

	/**
	 * Konstruktor für Objekte der Klasse Kantinenplan
	 * 
	 * @param name Der Name der Kantine/des Standortes
	 * @param anzMA Die Anzahl der Mitarbeiter des Standortes.
	 */
	public Kantinenplan(String name, int anzMA)
	{
		this.standort=name;
		this.anzMitarbeiter=anzMA;
		tgArrayList=new ArrayList<Tagesgericht>();

		//Debug-Print
		MainWin.StringOutln("");
		MainWin.StringOutln("Der Kantinenplan für "+standort+" mit "+anzMitarbeiter+" Mitarbeitern wurde erzeugt.");
	}

	/**
	 * Die Methode füllt den Speiseplan mit Tagesgerichten, die sie aus Rezepten erzeugt. Die Rezepte erhält 
	 * sie von der als Parameter zugewiesenen Rezeptverwaltung. Um die Verfügbarkeit der Zutaten am Markt sicher-
	 * zustellen, wird auf die Lieferantenverwaltung zurückgegriffen.
	 * 
	 * @param lieferantenverw Die Lieferantenverwaltung, welche die Verfügbarkeitsinformation zur Verfügung stellt.
	 * @param rezeptverw Die Rezeptverwaltung, die die Rezepte für den Speiseplan zur Verfügung stellen kann. 
	 * @return True, für erfolgreiche Erstellung, False falls Fehler aufgetreten sind.       
	 */
	public boolean erzeugePlan(Lieferantenverwaltung lieferantenverw, Rezeptverwaltung rezeptverw)
	{
		// Anzahl der Öffnungstage pro Woche
		int tageProWoche = 5;
		//Anzahl der Wochen pro Planungszeitraum
		int wochenProPlan = 3;

		for (int w=0;w<wochenProPlan;w++){
			/*Week 1 */
			// Fischrezept-Zähler
			int fishCnt=0;
			//enthält die 5 Fleischrezepte, die pro Woche mindestens angeboten werden sollen
			Rezept[] tmpMeatArr=new Rezept[tageProWoche];	
			//enthält die 5 vegetarischen Rezepte, die pro Woche mindestens angeboten werden sollen
			Rezept[] tmpVeggieArr=new Rezept[tageProWoche];
			//enthält 5 zufällige Rezepte, die pro Woche mindestens angeboten werden sollen, wobei min. eines ein Fischgericht sein muss
			Rezept[] tmpRndArr=new Rezept[tageProWoche];

			Tagesgericht[] meatTGArr=new Tagesgericht[tageProWoche];
			Tagesgericht[] veggieTGArr=new Tagesgericht[tageProWoche];
			Tagesgericht[] rndTGArr=new Tagesgericht[tageProWoche];

			// temporären Wochen-Plan erzeugen
			for (byte i=0; i<tageProWoche;i++){
				tmpMeatArr[i]=rezeptverw.gibFleisch();
				tmpVeggieArr[i]=rezeptverw.gibVeggie();
				tmpRndArr[i]=rezeptverw.gibRandom();
				//Wenn das Zufallsrezept ein Fischrezept ist, wird der Zähler erhöht.
				if(tmpRndArr[i].getTyp()==RezeptTyp.Fisch){
					/*Sollte der Zähler größer als 2 sein (das Zufallsgericht also das 3. Fischgericht in der Woche),  Dies geschieht so oft, bis ein 
	        		Nicht-Fischgericht vorgeschlagen wird. Da das Zufallsgericht ersetzt wird, wird natürlich auch der Fischgericht-Zähler wieder reduziert*/
					if (fishCnt>2){
						do{
							tmpRndArr[i]=rezeptverw.gibRandom();
						}
						while (tmpRndArr[i].getTyp()==RezeptTyp.Fisch);
					}
					else {
						fishCnt++;
					}
				}
				/*Sollte beim letzten Durchlauf (der 5.) der Zufall nicht für mindestens ein Fischgericht gesorgt haben (Zähler also = 0), dann
	        	wird explizit ein Fischgericht aufgerufen. Das sorgt natürlich dafür, dass die Wahrscheinlichkeit, dass es christlich-traditionell 
	        	am Freitag Fisch gibt, recht hoch ist. Dies kann hier aber auch auf einen anderen Tag gelegt werden, denn letztlich ist es egal, 
	        	welcher Tag überschrieben wird. */
				if (i==4 && fishCnt==0){
					tmpRndArr[i]=rezeptverw.gibFisch();
					//Debug-Print
					//MainWin.StringOutln("Immernoch kein Fischgericht. Ersetze mit "+tmpRndArr[i].getName());
				}
				// Verfügbarkeits-Prüfung
				//Tagesgerichte erstellen
				meatTGArr[i]=new Tagesgericht(tmpMeatArr[i]);
				veggieTGArr[i]=new Tagesgericht(tmpVeggieArr[i]);
				rndTGArr[i]=new Tagesgericht(tmpRndArr[i]);

				if (tmpMeatArr[i].getHitlistenpos() < tmpVeggieArr[i].getHitlistenpos() && 
						tmpMeatArr[i].getHitlistenpos() < tmpRndArr[i].getHitlistenpos()){
					//Fleischgericht hat höchste Hitlistenposition
					//Mengen setzen
					meatTGArr[i].setMenge(new Double((anzMitarbeiter*1.5)/2).intValue());
					veggieTGArr[i].setMenge(new Double((anzMitarbeiter*1.5)/4).intValue());
					rndTGArr[i].setMenge(new Double((anzMitarbeiter*1.5)/4).intValue());
					//Verfügbarkeit prüfen
					if (lieferantenverw.lebensmittelVerfuegbar(meatTGArr[i]) && lieferantenverw.lebensmittelVerfuegbar(veggieTGArr[i])
							&& lieferantenverw.lebensmittelVerfuegbar(rndTGArr[i])){
						//Alle Zutatmengen für die Tagesgerichte des Tags i sind verfügbar
						//Rezepte auf verwendet setzen
						tmpMeatArr[i].setVerwendet(true);
						tmpVeggieArr[i].setVerwendet(true);
						tmpRndArr[i].setVerwendet(true);
						//Datum setzen
						meatTGArr[i].setDatum((w*tageProWoche)+i+1);
						veggieTGArr[i].setDatum((w*tageProWoche)+i+1);
						rndTGArr[i].setDatum((w*tageProWoche)+i+1);
						//zur ArrayList hinzufügen
						tgArrayList.add(meatTGArr[i]);
						tgArrayList.add(veggieTGArr[i]);
						tgArrayList.add(rndTGArr[i]);
					} else {
						//Mindestens für ein Tagesgericht sind die Mengen nicht verfügbar
						/*Wenn das getestete Zufallsgericht ein Fischgericht war, muss der Fischgerichtzähler natürlich auch wieder vor der Neu-
	    				planung reduziert werden */
						if (tmpRndArr[i].getTyp()==RezeptTyp.Fisch){
							fishCnt--;
						}
						i--;
					}
				}
				else if (tmpVeggieArr[i].getHitlistenpos() < tmpMeatArr[i].getHitlistenpos() && 
						tmpVeggieArr[i].getHitlistenpos() < tmpRndArr[i].getHitlistenpos()){
					//Vegetarisches Gericht hat höchste Hitlistenposition
					//Mengen setzen
					meatTGArr[i].setMenge(new Double((anzMitarbeiter*1.5)/4).intValue());
					veggieTGArr[i].setMenge(new Double((anzMitarbeiter*1.5)/2).intValue());
					rndTGArr[i].setMenge(new Double((anzMitarbeiter*1.5)/4).intValue());
					//Verfügbarkeit prüfen
					if (lieferantenverw.lebensmittelVerfuegbar(meatTGArr[i]) && lieferantenverw.lebensmittelVerfuegbar(veggieTGArr[i])
							&& lieferantenverw.lebensmittelVerfuegbar(rndTGArr[i])){
						//Alle Zutatmengen für die Tagesgerichte des Tags i sind verfügbar
						//Rezepte auf verwendet setzen
						tmpMeatArr[i].setVerwendet(true);
						tmpVeggieArr[i].setVerwendet(true);
						tmpRndArr[i].setVerwendet(true);
						//Datum setzen
						meatTGArr[i].setDatum((w*tageProWoche)+i+1);
						veggieTGArr[i].setDatum((w*tageProWoche)+i+1);
						rndTGArr[i].setDatum((w*tageProWoche)+i+1);
						//zur ArrayList hinzufügen
						tgArrayList.add(meatTGArr[i]);
						tgArrayList.add(veggieTGArr[i]);
						tgArrayList.add(rndTGArr[i]);
					} else {
						//Mindestens für ein Tagesgericht sind die Mengen nicht verfügbar
						//Wenn das getestete Zufallsgericht ein Fischgericht war, muss der Fischgerichtzähler natürlich auch wieder vor der Neu-
						//planung reduziert werden
						if (tmpRndArr[i].getTyp()==RezeptTyp.Fisch){
							fishCnt--;
						}
						i--;
					}
				}
				else if (tmpRndArr[i].getHitlistenpos() < tmpMeatArr[i].getHitlistenpos() && 
						tmpRndArr[i].getHitlistenpos() < tmpVeggieArr[i].getHitlistenpos()){
					//Zufallsgericht hat höchste Hitlistenposition
					//Mengen setzen
					meatTGArr[i].setMenge(new Double((anzMitarbeiter*1.5)/4).intValue());
					veggieTGArr[i].setMenge(new Double((anzMitarbeiter*1.5)/4).intValue());
					rndTGArr[i].setMenge(new Double((anzMitarbeiter*1.5)/2).intValue());
					//Verfügbarkeit prüfen
					if (lieferantenverw.lebensmittelVerfuegbar(meatTGArr[i]) && lieferantenverw.lebensmittelVerfuegbar(veggieTGArr[i])
							&& lieferantenverw.lebensmittelVerfuegbar(rndTGArr[i])){
						//Alle Zutatmengen für die Tagesgerichte des Tags i sind verfügbar
						//Rezepte auf verwendet setzen
						tmpMeatArr[i].setVerwendet(true);
						tmpVeggieArr[i].setVerwendet(true);
						tmpRndArr[i].setVerwendet(true);
						//Datum setzen
						meatTGArr[i].setDatum((w*tageProWoche)+i+1);
						veggieTGArr[i].setDatum((w*tageProWoche)+i+1);
						rndTGArr[i].setDatum((w*tageProWoche)+i+1);
						//zur ArrayList hinzufügen
						tgArrayList.add(meatTGArr[i]);
						tgArrayList.add(veggieTGArr[i]);
						tgArrayList.add(rndTGArr[i]);
					} else {
						//Mindestens für ein Tagesgericht sind die Mengen nicht verfügbar
						//Wenn das getestete Zufallsgericht ein Fischgericht war, muss der Fischgerichtzähler natürlich auch wieder vor der Neu-
						//planung reduziert werden
						if (tmpRndArr[i].getTyp()==RezeptTyp.Fisch){
							fishCnt--;
						}
						i--;
					}
				}
				else {
					//Durch den Zufallsmodus kann auch 2x dasselbe Rezept vorgeschlagen werden, da ja zum Zeitpunkt des Aufrufes die Vorgänger noch nicht gesetzt sind.
					//Dann wird ein neuer Planungstag initialisiert
					i--;
					//Debug_Print
					//MainWin.StringOutln("Problem bei den Hitlistenpositionen");
				}	
			} //Ende temporärer Wochenplanschleife
		}
		/*Debug-Print

        for (int i=0;i<tgArrayList.size();i++){
        	String dOffset="";
        	String hOffset="";
        	if(tgArrayList.get(i).getDatum()<10){dOffset=" ";}
        	if(tgArrayList.get(i).getRezept().getHitlistenpos()<100){hOffset=" ";}
        	if(tgArrayList.get(i).getRezept().getHitlistenpos()<10){hOffset="  ";}
        	MainWin.StringOutln("Datum: "+dOffset+tgArrayList.get(i).getDatum()+" Hitlistenpos.: "+hOffset+tgArrayList.get(i).getRezept().getHitlistenpos()
        			+" Menge: "+tgArrayList.get(i).getMenge()+" Typ: "+tgArrayList.get(i).getRezept().getTyp()+" Gericht: "+tgArrayList.get(i).
        			getRezept().getName());
        } */
		return true;
	}
	/**
	 * Die Methode gibt eine ArrayList zurück, der alle Tagesgerichte des Kantinenplans enthält.
	 * 
	 * @return Eine ArrayList, der alle Tagesgerichte enthält.
	 */
	public ArrayList<Tagesgericht> getTagesgerichte()
	{
		return tgArrayList;
	}
	/**
	 * Gibt den Namen der Kantine zurück.
	 * 
	 * @return Der Name der Kantine.
	 */
	public String getStandort()
	{
		return standort;
	}
	/**
	 * Gibt die Anzahl der Mitarbeiter am Standort zurück.
	 * 
	 * @return Der Name der Kantine.
	 */
	public int getAnzMA()
	{
		return anzMitarbeiter;
	}
	public void schreibeKantinenplan() {
		Datei planDatei = new Datei( standort+".csv");
		//MainWin.StringOutln("Schreibe Datei "+standort+".csv ...");
		if (planDatei.openOutFile_FS()==0) {
			planDatei.writeLine("Kantinenplan für "+standort);
			String ausgabeZeile;
			for( Tagesgericht tgGericht: tgArrayList ) {
				String dOffset="";
				String hOffset="";
				//Tag Offset für Datum kleiner als 10:
				if(tgGericht.getDatum()<10){dOffset=" ";}
				//Hitlistenposition Offset: 3 Zeichen reserviert
				if(tgGericht.getRezept().getHitlistenpos()<100){hOffset=" ";}
				if(tgGericht.getRezept().getHitlistenpos()<10){hOffset="  ";}
				//Ausgabezeile zusammensetzen
				ausgabeZeile = "Datum: "+dOffset+tgGericht.getDatum()+" Hitlistenpos.: "+hOffset+tgGericht.getRezept().getHitlistenpos()
						+" Menge: "+tgGericht.getMenge()+" Gericht: "+tgGericht.
						getRezept().getName()+" Typ: "+tgGericht.getRezept().getTyp();
				if( planDatei.writeLine_FS(ausgabeZeile) != 0) {
					MainWin.StringOutln("Fehler beim Schreiben der planZeile"+tgGericht.getRezept().getName());
					break;
				}
			}
			if( planDatei.closeOutFile_FS()!=0)
				MainWin.StringOutln("Fehler beim Schließen der Ausgabedatei");
		} else 
			MainWin.StringOutln("Die Ausgabedatei kann nicht geöffnet werden.");
		MainWin.StringOutln("Ausgabe des Kantinenplans in "+System.getProperty("user.dir")+" als "+standort+".csv");
	}
}