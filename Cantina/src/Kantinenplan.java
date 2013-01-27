import java.util.ArrayList;
/**
 * Die Klasse Kantinenplan repr�sentiert einen Kantinenplan im Rahmen der Planungsperiode.
 * Sie aggregiert die Tagesgerichtobjekte und erstellt auf Basis der Anzahl der 
 * Mitarbeiter je Kantine und Standort einen Plan mit insgesamt 45 Tagesgerichten.
 * Sie steht in Assoziation zu der Einkaufsliste um, durch die �bergabe eines
 * Kantinenplans, die Bestellpositonen zu generieren. Der Kantinenplan hat durch die Assoziation zur
 * Reezptverwaltung und Lieferantenverwaltung Zugriff auf deren Methoden um eine g�ltigen Kantinenplan
 * zu erstellen. Damit ein Planungslauf von der Kantinenplanung durchgf�hrt werden kann, 
 * liegt auch mit dieser Klasse eine Assoziationsbeziehung vor.
 * 
 * @author Rene Wiederhold
 * @version 1.00
 */
public class Kantinenplan
{
	/** Der Standort, f�r den der Kantinenplan erzeugt wird */ 
	private String standort;
	/** Die Anzahl der Mitarbeiter, welche die Kantine am Standort besuchen*/
	private int anzMitarbeiter;
	/** Die ArrayList enth�lt die zum Speiseplan geh�renden Tagesgerichte */
	public ArrayList<Tagesgericht> tgArrayList;

	/**
	 * Konstruktor f�r Objekte der Klasse Kantinenplan
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
		MainWin.StringOutln("Der Kantinenplan f�r "+standort+" mit "+anzMitarbeiter+" Mitarbeitern wurde erzeugt.");
	}

	/**
	 * Die Methode f�llt den Speiseplan mit Tagesgerichten, die sie aus Rezepten erzeugt. Die Rezepte erh�lt 
	 * sie von der als Parameter zugewiesenen Rezeptverwaltung. Um die Verf�gbarkeit der Zutaten am Markt sicher-
	 * zustellen, wird auf die Lieferantenverwaltung zur�ckgegriffen.
	 * 
	 * @param lieferantenverw Die Lieferantenverwaltung, welche die Verf�gbarkeitsinformation zur Verf�gung stellt.
	 * @param rezeptverw Die Rezeptverwaltung, die die Rezepte f�r den Speiseplan zur Verf�gung stellen kann. 
	 * @return True, f�r erfolgreiche Erstellung, False falls Fehler aufgetreten sind.       
	 */
	public boolean erzeugePlan(Lieferantenverwaltung lieferantenverw, Rezeptverwaltung rezeptverw)
	{
		// Anzahl der �ffnungstage pro Woche
		int tageProWoche = 5;
		//Anzahl der Wochen pro Planungszeitraum
		int wochenProPlan = 3;

		for (int w=0;w<wochenProPlan;w++){
			/*Week 1 */
			// Fischrezept-Z�hler
			int fishCnt=0;
			//enth�lt die 5 Fleischrezepte, die pro Woche mindestens angeboten werden sollen
			Rezept[] tmpMeatArr=new Rezept[tageProWoche];	
			//enth�lt die 5 vegetarischen Rezepte, die pro Woche mindestens angeboten werden sollen
			Rezept[] tmpVeggieArr=new Rezept[tageProWoche];
			//enth�lt 5 zuf�llige Rezepte, die pro Woche mindestens angeboten werden sollen, wobei min. eines ein Fischgericht sein muss
			Rezept[] tmpRndArr=new Rezept[tageProWoche];

			Tagesgericht[] meatTGArr=new Tagesgericht[tageProWoche];
			Tagesgericht[] veggieTGArr=new Tagesgericht[tageProWoche];
			Tagesgericht[] rndTGArr=new Tagesgericht[tageProWoche];

			// tempor�ren Wochen-Plan erzeugen
			for (byte i=0; i<tageProWoche;i++){
				tmpMeatArr[i]=rezeptverw.gibFleisch();
				tmpVeggieArr[i]=rezeptverw.gibVeggie();
				tmpRndArr[i]=rezeptverw.gibRandom();
				//Wenn das Zufallsrezept ein Fischrezept ist, wird der Z�hler erh�ht.
				if(tmpRndArr[i].getTyp()==RezeptTyp.Fisch){
					/*Sollte der Z�hler gr��er als 2 sein (das Zufallsgericht also das 3. Fischgericht in der Woche),  Dies geschieht so oft, bis ein 
	        		Nicht-Fischgericht vorgeschlagen wird. Da das Zufallsgericht ersetzt wird, wird nat�rlich auch der Fischgericht-Z�hler wieder reduziert*/
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
				/*Sollte beim letzten Durchlauf (der 5.) der Zufall nicht f�r mindestens ein Fischgericht gesorgt haben (Z�hler also = 0), dann
	        	wird explizit ein Fischgericht aufgerufen. Das sorgt nat�rlich daf�r, dass die Wahrscheinlichkeit, dass es christlich-traditionell 
	        	am Freitag Fisch gibt, recht hoch ist. Dies kann hier aber auch auf einen anderen Tag gelegt werden, denn letztlich ist es egal, 
	        	welcher Tag �berschrieben wird. */
				if (i==4 && fishCnt==0){
					tmpRndArr[i]=rezeptverw.gibFisch();
					//Debug-Print
					//MainWin.StringOutln("Immernoch kein Fischgericht. Ersetze mit "+tmpRndArr[i].getName());
				}
				// Verf�gbarkeits-Pr�fung
				//Tagesgerichte erstellen
				meatTGArr[i]=new Tagesgericht(tmpMeatArr[i]);
				veggieTGArr[i]=new Tagesgericht(tmpVeggieArr[i]);
				rndTGArr[i]=new Tagesgericht(tmpRndArr[i]);

				if (tmpMeatArr[i].getHitlistenpos() < tmpVeggieArr[i].getHitlistenpos() && 
						tmpMeatArr[i].getHitlistenpos() < tmpRndArr[i].getHitlistenpos()){
					//Fleischgericht hat h�chste Hitlistenposition
					//Mengen setzen
					meatTGArr[i].setMenge(new Double((anzMitarbeiter*1.5)/2).intValue());
					veggieTGArr[i].setMenge(new Double((anzMitarbeiter*1.5)/4).intValue());
					rndTGArr[i].setMenge(new Double((anzMitarbeiter*1.5)/4).intValue());
					//Verf�gbarkeit pr�fen
					if (lieferantenverw.lebensmittelVerfuegbar(meatTGArr[i]) && lieferantenverw.lebensmittelVerfuegbar(veggieTGArr[i])
							&& lieferantenverw.lebensmittelVerfuegbar(rndTGArr[i])){
						//Alle Zutatmengen f�r die Tagesgerichte des Tags i sind verf�gbar
						//Rezepte auf verwendet setzen
						tmpMeatArr[i].setVerwendet(true);
						tmpVeggieArr[i].setVerwendet(true);
						tmpRndArr[i].setVerwendet(true);
						//Datum setzen
						meatTGArr[i].setDatum((w*tageProWoche)+i+1);
						veggieTGArr[i].setDatum((w*tageProWoche)+i+1);
						rndTGArr[i].setDatum((w*tageProWoche)+i+1);
						//zur ArrayList hinzuf�gen
						tgArrayList.add(meatTGArr[i]);
						tgArrayList.add(veggieTGArr[i]);
						tgArrayList.add(rndTGArr[i]);
					} else {
						//Mindestens f�r ein Tagesgericht sind die Mengen nicht verf�gbar
						/*Wenn das getestete Zufallsgericht ein Fischgericht war, muss der Fischgerichtz�hler nat�rlich auch wieder vor der Neu-
	    				planung reduziert werden */
						if (tmpRndArr[i].getTyp()==RezeptTyp.Fisch){
							fishCnt--;
						}
						i--;
					}
				}
				else if (tmpVeggieArr[i].getHitlistenpos() < tmpMeatArr[i].getHitlistenpos() && 
						tmpVeggieArr[i].getHitlistenpos() < tmpRndArr[i].getHitlistenpos()){
					//Vegetarisches Gericht hat h�chste Hitlistenposition
					//Mengen setzen
					meatTGArr[i].setMenge(new Double((anzMitarbeiter*1.5)/4).intValue());
					veggieTGArr[i].setMenge(new Double((anzMitarbeiter*1.5)/2).intValue());
					rndTGArr[i].setMenge(new Double((anzMitarbeiter*1.5)/4).intValue());
					//Verf�gbarkeit pr�fen
					if (lieferantenverw.lebensmittelVerfuegbar(meatTGArr[i]) && lieferantenverw.lebensmittelVerfuegbar(veggieTGArr[i])
							&& lieferantenverw.lebensmittelVerfuegbar(rndTGArr[i])){
						//Alle Zutatmengen f�r die Tagesgerichte des Tags i sind verf�gbar
						//Rezepte auf verwendet setzen
						tmpMeatArr[i].setVerwendet(true);
						tmpVeggieArr[i].setVerwendet(true);
						tmpRndArr[i].setVerwendet(true);
						//Datum setzen
						meatTGArr[i].setDatum((w*tageProWoche)+i+1);
						veggieTGArr[i].setDatum((w*tageProWoche)+i+1);
						rndTGArr[i].setDatum((w*tageProWoche)+i+1);
						//zur ArrayList hinzuf�gen
						tgArrayList.add(meatTGArr[i]);
						tgArrayList.add(veggieTGArr[i]);
						tgArrayList.add(rndTGArr[i]);
					} else {
						//Mindestens f�r ein Tagesgericht sind die Mengen nicht verf�gbar
						//Wenn das getestete Zufallsgericht ein Fischgericht war, muss der Fischgerichtz�hler nat�rlich auch wieder vor der Neu-
						//planung reduziert werden
						if (tmpRndArr[i].getTyp()==RezeptTyp.Fisch){
							fishCnt--;
						}
						i--;
					}
				}
				else if (tmpRndArr[i].getHitlistenpos() < tmpMeatArr[i].getHitlistenpos() && 
						tmpRndArr[i].getHitlistenpos() < tmpVeggieArr[i].getHitlistenpos()){
					//Zufallsgericht hat h�chste Hitlistenposition
					//Mengen setzen
					meatTGArr[i].setMenge(new Double((anzMitarbeiter*1.5)/4).intValue());
					veggieTGArr[i].setMenge(new Double((anzMitarbeiter*1.5)/4).intValue());
					rndTGArr[i].setMenge(new Double((anzMitarbeiter*1.5)/2).intValue());
					//Verf�gbarkeit pr�fen
					if (lieferantenverw.lebensmittelVerfuegbar(meatTGArr[i]) && lieferantenverw.lebensmittelVerfuegbar(veggieTGArr[i])
							&& lieferantenverw.lebensmittelVerfuegbar(rndTGArr[i])){
						//Alle Zutatmengen f�r die Tagesgerichte des Tags i sind verf�gbar
						//Rezepte auf verwendet setzen
						tmpMeatArr[i].setVerwendet(true);
						tmpVeggieArr[i].setVerwendet(true);
						tmpRndArr[i].setVerwendet(true);
						//Datum setzen
						meatTGArr[i].setDatum((w*tageProWoche)+i+1);
						veggieTGArr[i].setDatum((w*tageProWoche)+i+1);
						rndTGArr[i].setDatum((w*tageProWoche)+i+1);
						//zur ArrayList hinzuf�gen
						tgArrayList.add(meatTGArr[i]);
						tgArrayList.add(veggieTGArr[i]);
						tgArrayList.add(rndTGArr[i]);
					} else {
						//Mindestens f�r ein Tagesgericht sind die Mengen nicht verf�gbar
						//Wenn das getestete Zufallsgericht ein Fischgericht war, muss der Fischgerichtz�hler nat�rlich auch wieder vor der Neu-
						//planung reduziert werden
						if (tmpRndArr[i].getTyp()==RezeptTyp.Fisch){
							fishCnt--;
						}
						i--;
					}
				}
				else {
					//Durch den Zufallsmodus kann auch 2x dasselbe Rezept vorgeschlagen werden, da ja zum Zeitpunkt des Aufrufes die Vorg�nger noch nicht gesetzt sind.
					//Dann wird ein neuer Planungstag initialisiert
					i--;
					//Debug_Print
					//MainWin.StringOutln("Problem bei den Hitlistenpositionen");
				}	
			} //Ende tempor�rer Wochenplanschleife
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
	 * Die Methode gibt eine ArrayList zur�ck, der alle Tagesgerichte des Kantinenplans enth�lt.
	 * 
	 * @return Eine ArrayList, der alle Tagesgerichte enth�lt.
	 */
	public ArrayList<Tagesgericht> getTagesgerichte()
	{
		return tgArrayList;
	}
	/**
	 * Gibt den Namen der Kantine zur�ck.
	 * 
	 * @return Der Name der Kantine.
	 */
	public String getStandort()
	{
		return standort;
	}
	/**
	 * Gibt die Anzahl der Mitarbeiter am Standort zur�ck.
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
			planDatei.writeLine("Kantinenplan f�r "+standort);
			String ausgabeZeile;
			for( Tagesgericht tgGericht: tgArrayList ) {
				String dOffset="";
				String hOffset="";
				//Tag Offset f�r Datum kleiner als 10:
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
				MainWin.StringOutln("Fehler beim Schlie�en der Ausgabedatei");
		} else 
			MainWin.StringOutln("Die Ausgabedatei kann nicht ge�ffnet werden.");
		MainWin.StringOutln("Ausgabe des Kantinenplans in "+System.getProperty("user.dir")+" als "+standort+".csv");
	}
}