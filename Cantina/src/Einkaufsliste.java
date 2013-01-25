import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Properties;
/**
 * Die Klasse Einkaufsliste kann aus den Kantinenplan-Objekten eine Einkaufsliste mit einzelnen 
 * Einkaufslistenpositionen erzeugen. Dazu erzeugt sie als Zwischenschritt BedarfPos-Objekte 
 * für den Gesamtbedarf einer Zutat. Die Bedarfe werden dann in BestellPos-Objekte überführt.
 * 
 * Eine Einkaufsliste steht in einer Kompositionsbeziehung zur Bestellpositionen, weil eine Einkaufsliste nie 
 * ohne eine Bestellpsotion existieren kann. Sie verwaltet zudem die BedarfsPos und aggregeiert
 * diese. Sie aggregiert auch die Kantinenpläne um den Gesamtbedarf an Artikeln zu ermitteln, die als 
 * BedarfPos geführt werden. Die Assoziation zur Lieferantenverwaltung ermöglicht der Einkaufsliste die 
 * günstigsten Artikel zu ermitteln und aus der BedarfsPos die BestellPos zu generieren.
 * Für den Planungslauf steht sie in Assoziation zur Kantinenplanung. Zudem wird sie als Datei
 * exportiert, weshalb auch zum Exporter eine Assoziation gegeben ist. 
 * 
 * @author Rene Wiederhold 
 * @version
 */
public class Einkaufsliste
{
	/** Enthält die Objekte der Klasse BestellPos */
	private ArrayList<BestellPos> bestellPosList;
	/** Enthält die Objekte der Klasse BedarfPos */
	private ArrayList<BedarfPos> bedarfPosList;
	/** Enthält die Gesamtkosten der Bestellung inklusive der Transportkosten */
	private float gesamtkosten;
	/** Enthält die zu verwendende Lieferantenverwaltung */
	private Lieferantenverwaltung lieferantenverw;

	/**
	 * Der Konstruktor
	 */
	public Einkaufsliste()
	{

		bedarfPosList=new ArrayList<BedarfPos>();
		bestellPosList=new ArrayList<BestellPos>();
	}

	/**
	 * Die Methode fügt die benötigten Zutaten für einen Kantinenplan den Bedarfsposition-Objekten hinzu bzw. erstellt diese.
	 *
	 * @param  kantinenplan   Ein Kantinenplanobjekt
	 * @return     True für ein erfolgreiches hinzufügen, False falls ein Problem aufgetreten ist.
	 */
	public void addKantinenplan(Kantinenplan kantinenplan)
	{ 
		//Tagesgericht-Schleife
		for (int i=0;i<kantinenplan.getTagesgerichte().size();i++){
			Tagesgericht tg=kantinenplan.getTagesgerichte().get(i);
			int m=tg.getMenge();
			//Zutatenschleife
			for (int j=0;j<tg.getRezept().getZutaten().size();j++){
				Zutat z=tg.getRezept().getZutaten().get(j);
				String zname=z.getName();
				String zeinh=z.getEinheit();
				float zmenge=z.getMenge();

				if (bedarfPosList.isEmpty()){
					BedarfPos bPos=new BedarfPos();
					bPos.setName(zname);
					bPos.setEinheit(zeinh);
					bPos.setMenge(m*zmenge);
					bedarfPosList.add(bPos);
					//Debug-Print
				}
				else {
					//Es sind bereits Objekte in der bedarfPosList
					//Es muss geprüft werden ob ein Objekt gleichen Namens vorhanden ist
					//Hier wird eine "Iterator-Schleife verwendet
					Iterator<BedarfPos> it=bedarfPosList.iterator();
					//Variable für die "Vorhanden"-Prüfung
					Boolean vorh=false;
					//Variable für die bereits in der List vorhandene Bedarfposition
					BedarfPos vorhBP=new BedarfPos();
					//Solang die List noch ein weiteres Element hat, läuft die Schleife
					while (it.hasNext()){
						//next() gibt das nächste BedarfPos-Objekt
						BedarfPos bedarfPosIt=it.next();
						//Stimmt der Name der BedarfPos mit der zu prüfenden Zutat überein?
						//Wenn ja, dass Objekt temporär gespeichert und die Schleife gebrochen.
						if(bedarfPosIt.getName().equals(zname)){
							vorh=true;
							vorhBP=bedarfPosIt;
							break;
						}
						//Stimmt der übergebene Name der BedarfPos nicht mit dem iterierten überein, bleibt vorh auf false
						else{
							vorh=false;
						}	
					} //while-Ende - Jetzt steht entweder vorh auf true und vorhBP enthält das BedarfPos-Objekt oder vorh steht auf false
					// Lebensmittel-Objekt schon vorhanden
					if(vorh==true){
						vorhBP.setMenge(vorhBP.getMenge()+(m*zmenge));
						//Debug-Print
						//MainWin.StringOutln(zname+"-Menge von "+vorhBP.getMenge()+" auf "+(vorhBP.getMenge()+(m*zmenge))+" um "+(m*zmenge)+" angepasst");
					}
					// Lebensmittel-Objekt noch nicht vorhanden
					else if(vorh==false){
						vorhBP.setName(zname);
						vorhBP.setMenge(m*zmenge);
						vorhBP.setEinheit(zeinh);
						bedarfPosList.add(vorhBP);
						//Debug-Print
						//MainWin.StringOutln((m*zmenge)+" "+zeinh+" "+zname+" der Liste hinzugefügt!");
					}	
				}
			} //Ende Zutatenschleife
		} //Ende Tagesgericht-Schleife
		MainWin.StringOutln("Der Kantinenplan für "+kantinenplan.getStandort()+" wurde der Einkaufsliste hinzugefügt.");
	}

	/**
	 * @return the bedarfPosList
	 */
	public ArrayList<BedarfPos> getBedarfPosList() {
		return bedarfPosList;
	}
	/**
	 * Erzeugt aus den im KantinenplanArrayList enthaltenen Kantinenplänen eine Einkaufsliste
	 * Es muss vorher mindestens ein Kantinenplan über addKantinenplan referenziert worden sein.
	 * 
	 * @return     true, für eine erfolgreich erstellte Einkaufsliste, false, falls Fehler aufgetreten sind.
	 */
	public boolean erzeugeEinkaufsliste(Lieferantenverwaltung lieferantenverw)
	{
		this.lieferantenverw=lieferantenverw;
		bestellPosList=makeVariante2();
		berechneGesamtkosten();
		System.out.println(gesamtkosten);

		return true;
	}
	/**
	 * Für die Erstellung der Einkaufsliste mit den Bestellpositionen wird zunächst eine Variante gerechnet, die versucht so viel es geht bei Bauernhöfen zu beschaffen.
	 */
	public ArrayList<BestellPos> makeVariante1(){
		ArrayList<BedarfPos> bedarfListCopy=new ArrayList<BedarfPos>();
		ArrayList<BestellPos> bestellList=new ArrayList<BestellPos>();
		//Zunächst muss eine tiefe Kopie der BedarfPos-Objekte bzw. der bedarfPosList erstellt werden, auf der das Szenario rechnen kann, ohne die Original-Daten zu zerstören.
		for (BedarfPos bedarf:bedarfPosList){
			bedarfListCopy.add(bedarf.clone());
		}
		//Debug-Print
		System.out.println("Bedarfliste am Beginn");
		System.out.println("==============================================");
		for (BedarfPos bedarf:bedarfListCopy){
			System.out.println(bedarf.getMenge()+" "+bedarf.getEinheit()+" "+bedarf.getName());
		}
		//Schleife für alle BedarfPos-Objekte
		for (BedarfPos bedarf:bedarfListCopy){
			ArrayList<Artikel> artList=lieferantenverw.gibAlleArtikel(bedarf.getName());
			//artList enthält jetzt alle Artikel, die für die Beschaffung der BedarfPos in Frage kommen, der größe nach sortiert.
			for (Artikel art:artList){
				if (art.getLieferant().getClass()==Bauernhof.class){    				
					BestellPos bestellPos=new BestellPos();
					//Für die Bestimmung der nötigen Anzahl Gebinde muss quasi immer aufgerundet werden.
					//Hier wird zunächst Ganzzahldivision gerechnet, also der Rest "abgeschnitten".
					int anzGebinde=(int) (bedarf.getMenge()/art.getGebindegroesse());
					//Wenn Modulo größer 0 ist, muss ein Gebinde mehr beschafft werden.
					if (!(bedarf.getMenge()%art.getGebindegroesse()==0)){
						anzGebinde++;
					}
					//Reicht die Anzahl der Gebinde, die der Lieferant liefern kann nicht aus, um den kompletten Bedarf zu decken, wird nur die maximale beschaffbare Anzahl beschafft.
					if (anzGebinde>art.getArtikelanzahl()){
						anzGebinde=art.getArtikelanzahl();
					}
					//BestellPosition für Variante 1 schreiben und zur (temporären) Bestellliste hinzufügen.
					bestellPos.setMenge(anzGebinde);
					bestellPos.setArtikel(art);
					bestellList.add(bestellPos);
					//Die Bedarfsposition ist hierfür anzupassen
					float neuerBedarf=bedarf.getMenge()-anzGebinde*art.getGebindegroesse();
					//Wenn alles (und gegenfalls etwas zu viel) beschafft wurde, wird der Bedarf auf 0 gesetzt.
					if (neuerBedarf<=0){
						bedarf.setMenge(0);
						break;
					}

					//Ansonsten wird der neue Bedarf gesetzt.
					else bedarf.setMenge(neuerBedarf);   				
				}
			} //Ende Artikel-Schleife
		} //Ende BedarfPos-Schleife

		for (BedarfPos bedarf:bedarfListCopy){
			ArrayList<Artikel> artList=lieferantenverw.gibAlleArtikel(bedarf.getName());
			//artList enthält jetzt alle Artikel, die für die Beschaffung der BedarfPos in Frage kommen, der größe nach sortiert.
			for (Artikel art:artList){
				BestellPos bestellPos=new BestellPos();
				//Für die Bestimmung der nötigen Anzahl Gebinde muss quasi immer aufgerundet werden.
				//Hier wird zunächst Ganzzahldivision gerechnet, also der Rest "abgeschnitten".
				int anzGebinde=(int) (bedarf.getMenge()/art.getGebindegroesse());
				//Wenn Modulo größer 0 ist, muss ein Gebinde mehr beschafft werden.
				if (!(bedarf.getMenge()%art.getGebindegroesse()==0)){
					anzGebinde++;
				}
				//Reicht die Anzahl der Gebinde, die der Lieferant liefern kann nicht aus, um den kompletten Bedarf zu decken, wird nur die maximale beschaffbare Anzahl beschafft.
				if (anzGebinde>art.getArtikelanzahl()){
					anzGebinde=art.getArtikelanzahl();
				}
				//BestellPosition für Variante 1 schreiben und zur (temporären) Bestellliste hinzufügen.
				bestellPos.setMenge(anzGebinde);
				bestellPos.setArtikel(art);
				art.setArikelanzahl(art.getArtikelanzahl()-anzGebinde);
				bestellList.add(bestellPos);
				//Die Bedarfsposition ist hierfür anzupassen
				float neuerBedarf=bedarf.getMenge()-anzGebinde*art.getGebindegroesse();
				//Wenn alles (und gegenfalls etwas zu viel) beschafft wurde, wird der Bedarf auf 0 gesetzt.
				if (neuerBedarf<=0){
					bedarf.setMenge(0);
					break;
				}
				//Ansonsten wird der neue Bedarf gesetzt.
				else bedarf.setMenge(neuerBedarf);   	
			}
		}
		//Print Bestellliste der Variante 1
		for (BestellPos b:bestellList){
			Artikel a=b.getArtikel();
			MainWin.StringOutln(b.getMenge()+" Gebinde "+a.getName()+" a "+a.getGebindegroesse()+" bei "+a.getLieferant().getLieferantenName()+" kaufen.");
		}
		return bestellList;
	}
	private ArrayList<BestellPos> makeVariante2(){
		ArrayList<BedarfPos> bedarfListCopy=new ArrayList<BedarfPos>();
		ArrayList<BestellPos> bestellList=new ArrayList<BestellPos>();
		ArrayList<Bauernhof> bauernhofList=new ArrayList<Bauernhof>();
		Float kmSatz=getkmSatz();
		//Zunächst muss eine tiefe Kopie der BedarfPos-Objekte bzw. der bedarfPosList erstellt werden, auf der das Szenario rechnen kann, ohne die Original-Daten zu zerstören.
		for (BedarfPos bedarf:bedarfPosList){
			bedarfListCopy.add(bedarf.clone());
		}
		/*Debug-Print
    	System.out.println("Bedarfliste am Beginn");
    	System.out.println("==============================================");
    	for (BedarfPos bedarf:bedarfListCopy){
    		System.out.println(bedarf.getMenge()+" "+bedarf.getEinheit()+" "+bedarf.getName());
    	}*/
		for (BedarfPos bedarf:bedarfListCopy){
			/*
			 * Optionen:
			 * 1. Die komplette BedarfPos lässt sich von einem Artikel abdecken.
			 * 	- Grosshandel mit spezif. Lieferkosten ist billiger als Bauernhof netto. ->Grosshandel ist immer besser
			 * 	- Bauernhof mit spezif. Lieferkosten (volle Lieferkosten auf einen Artikel) ist billiger als Grosshandel mit spezif. Lieferkosten ->Bauernhof ist immer billiger.
			 * 	- Der Spielraum dazwischen hängt davon ab, wieviele andere Artikel noch beim Bauernhof gekauft werden, denn desto mehr Artikel, desto niedriger die spezif.Lieferkosten.
			 * 2. Die komplette BedarfPos lässt sich nicht komplett vom Bauernhof beschaffen, d.h. eine weitere Bestellung vom Grosshandel wird nötig.
			 * 	- 
			 * 	- Anteilige Bestellung beim Bauernhof inklusive Lieferkosten (volle Lieferkosten) und anteilige Bestellung beim Grosshandel inklusive Lieferkosten ist billiger als gesamte Bestellung beim Grosshandel ->Bestellung beim Bauernhof und beim Grosshandel auffüllen.
			 * 	- Gesamte Bestellung beim Grosshandel ist billiger als anteillige Bestellung beim Bauernhof OHNE Lieferkosten und anteilige Bestellung beim Grosshandel mit Lieferkosten ->Gesamte Bestellung beim Grosshandel ist billiger.
			 * 	- Der Spielraum dazwischen ist der Optimierungsbereich. 
			 * 	- 
			 */

			//Vollabdeckung Variablen
			Artikel cheapestBauerVoll=new Artikel();
			cheapestBauerVoll.setPreis(Float.MAX_VALUE);
			float cheapestBauerSummeNettoVoll=Float.MAX_VALUE;
			float cheapestBauerLieferkostenVoll=Float.MAX_VALUE;

			Artikel cheapestGHVoll=new Artikel();
			cheapestGHVoll.setPreis(Float.MAX_VALUE);
			float cheapestGHSummeNettoVoll=Float.MAX_VALUE;
			float cheapestGHLieferkostenVoll=Float.MAX_VALUE;

			//Teilabdeckung Variablen
			Artikel cheapestBauerTeil=new Artikel();
			cheapestBauerTeil.setPreis(Float.MAX_VALUE);
			float cheapestBauerSummeNettoTeil=Float.MAX_VALUE;
			float cheapestBauerLieferkostenTeil=Float.MAX_VALUE;
			int maxGebindeBauer=0;

			Artikel cheapestGHTeil=new Artikel();
			cheapestGHTeil.setPreis(Float.MAX_VALUE);
			float cheapestGHSummeNettoTeil=Float.MAX_VALUE;
			float cheapestGHLieferkostenTeil=Float.MAX_VALUE;
			int maxGebindeGH=0;

			ArrayList<Artikel> artList=lieferantenverw.gibAlleArtikel(bedarf.getName());
			for (Artikel art:artList){				

				int anzGebinde=(int) (bedarf.getMenge()/art.getGebindegroesse());
				//Wenn Modulo größer 0 ist, muss ein Gebinde mehr beschafft werden.
				if (!(bedarf.getMenge()%art.getGebindegroesse()==0)){
					anzGebinde++;
				}
				if (art.getArtikelanzahl()>=anzGebinde){
					//kompletter Bedarf ließe sich mit dem Artikel abdecken
					if (art.getLieferant().getClass()==Bauernhof.class){
						//Artikel ist Bauernhof
						Bauernhof bh=(Bauernhof) art.getLieferant();
						//Artikel + Lieferkosten des Bauernhofes ist kleiner als Artikel + Lieferkosten des aktuell billigsten Anbieters
						if ( (art.getPreis()*anzGebinde + (bh.getEntfernung()*kmSatz)) < (cheapestBauerSummeNettoVoll+cheapestBauerLieferkostenVoll) ){
							//Aktuell billigsten ersetzen
							cheapestBauerSummeNettoVoll= art.getPreis()*anzGebinde;
							cheapestBauerLieferkostenVoll=bh.getEntfernung()*kmSatz;
							cheapestBauerVoll=art;
						}
					}
					if (art.getLieferant().getClass()==Grosshandel.class){
						Grosshandel gh=(Grosshandel) art.getLieferant();
						//Artikel + Lieferkosten des Grosshandels ist kleiner als aktuell billigster Artikel + Lieferkosten
						if ( art.getPreis()*anzGebinde + gh.getLieferkostensatz() < cheapestGHSummeNettoVoll + cheapestGHLieferkostenVoll ){
							//billigsten ersetzen
							cheapestGHSummeNettoVoll= art.getPreis()*anzGebinde;
							cheapestGHLieferkostenVoll=gh.getLieferkostensatz();
							cheapestGHVoll=art;
						}
					}
				}
				else {
					//kompletter Bedarf ließe sich mit diesem Artikel nicht abdecken
					if (art.getLieferant().getClass()==Bauernhof.class){
						//Artikel ist Bauernhof
						Bauernhof bh=(Bauernhof) art.getLieferant();
						//Artikel + Lieferkosten des Bauernhofes ist kleiner als Artikel + Lieferkosten des aktuell billigsten Anbieters
						if ( (art.getPreis()*anzGebinde + (bh.getEntfernung()*kmSatz)) < (cheapestBauerSummeNettoTeil+cheapestBauerLieferkostenTeil) ){
							//Aktuell billigsten ersetzen
							cheapestBauerSummeNettoTeil= art.getPreis()*anzGebinde;
							cheapestBauerLieferkostenTeil=bh.getEntfernung()*kmSatz;
							cheapestBauerTeil=art;
							maxGebindeBauer=art.getArtikelanzahl();
						}
					}
					if (art.getLieferant().getClass()==Grosshandel.class){
						Grosshandel gh=(Grosshandel) art.getLieferant();
						//Artikel + Lieferkosten des Grosshandels ist kleiner als aktuell billigster Artikel + Lieferkosten
						if ( art.getPreis()*anzGebinde + gh.getLieferkostensatz() < cheapestGHSummeNettoTeil + cheapestGHLieferkostenTeil ){
							//billigsten ersetzen
							cheapestGHSummeNettoTeil= art.getPreis()*anzGebinde;
							cheapestGHLieferkostenTeil=gh.getLieferkostensatz();
							cheapestGHTeil=art;
							maxGebindeGH=art.getArtikelanzahl();
						}
					}
				}

			} //Artikel-Schleife Ende
			/*Debug-Print 
			System.out.println();
			System.out.println(bedarf.getName());
			System.out.println(bedarf.getMenge()+" "+bedarf.getEinheit());
			System.out.println("==================");
			if (!(cheapestBauerVoll.getLieferant()==null)){
				System.out.println("Billigster Bauernhof voll: "+cheapestBauerVoll.getLieferant().getLieferantenName());
				System.out.println("Summe: "+cheapestBauerSummeNettoVoll);
				System.out.println("Lieferkosten: "+cheapestBauerLieferkostenVoll);
			}
			if (!(cheapestGHVoll.getLieferant()==null)){
				System.out.println("Billigster Grosshandel voll: "+cheapestGHVoll.getLieferant().getLieferantenName());
				System.out.println("Summe: "+cheapestGHSummeNettoVoll);
				System.out.println("Lieferkosten: "+cheapestGHLieferkostenVoll);
			}
			if (!(cheapestBauerTeil.getLieferant()==null)){
				System.out.println("Billigster Bauernhof teil: "+cheapestBauerTeil.getLieferant().getLieferantenName());
				System.out.println("Summe: "+cheapestBauerSummeNettoTeil);
				System.out.println("Lieferkosten: "+cheapestBauerLieferkostenTeil);
				System.out.println("Maximal lieferbar: "+maxGebindeBauer);
			}
			if (!(cheapestGHTeil.getLieferant()==null)){
				System.out.println("Billigster Grosshandel teil: "+cheapestGHTeil.getLieferant().getLieferantenName());
				System.out.println("Summe: "+cheapestGHSummeNettoTeil);
				System.out.println("Lieferkosten: "+cheapestGHLieferkostenTeil);
				System.out.println("Maximal lieferbar: "+maxGebindeGH);
			}*/
			//Jetzt müssen alle Kombinationen, die sicher eine Entscheidung für einen Bauernhof bringen durchgegangen werden
			if (cheapestGHSummeNettoVoll+cheapestGHLieferkostenVoll < cheapestBauerSummeNettoVoll){
				//BauerVoll ist raus
				//Prüfung ob Teilbestellung Bauer + Auffüllung günstiger ist, als alles beim GH
				//Kosten Teilbestellung Bauer
				float teilbestellung = cheapestBauerTeil.getPreis()*maxGebindeBauer+cheapestBauerLieferkostenTeil;
				float zusaetzlichzubestellen = bedarf.getMenge() - (maxGebindeBauer*cheapestBauerTeil.getGebindegroesse());
				int anzZusaetzlicheGebinde=(int) zusaetzlichzubestellen;
				//Wenn Modulo größer 0 ist, muss ein Gebinde mehr beschafft werden.
				if (!(zusaetzlichzubestellen%cheapestGHVoll.getGebindegroesse()==0)){
					anzZusaetzlicheGebinde++;
				}
				if  ( (teilbestellung + anzZusaetzlicheGebinde*cheapestGHVoll.getPreis()+cheapestGHLieferkostenVoll) < (cheapestGHSummeNettoVoll+cheapestGHLieferkostenVoll)){
					//Definitiv beim Bauer die Teillieferung holen
					//Bauernhof in BauernhofListe merken zum späteren Abgleich
					System.out.println("1. Runde: Bei "+cheapestBauerVoll.getLieferant().getLieferantenName()+" Teilbestellung absetzen");
					BestellPos bp1=new BestellPos();
					cheapestBauerTeil.setArikelanzahl(0);
					bp1.setMenge(maxGebindeBauer);
					bp1.setArtikel(cheapestBauerTeil);
					bedarf.setMenge(bedarf.getMenge() - maxGebindeBauer*cheapestBauerTeil.getGebindegroesse());
					bestellList.add(bp1);
					if (!bauernhofList.contains(cheapestBauerTeil.getLieferant())){
						bauernhofList.add((Bauernhof) cheapestBauerTeil.getLieferant());
					}
				}
			}
			if (cheapestGHSummeNettoVoll+cheapestGHLieferkostenVoll > cheapestBauerSummeNettoVoll+cheapestBauerLieferkostenVoll){
				//GHVoll ist raus ->keine weiteren Prüfungen -> Alles beim BauernhofVoll
				//Bauernhof in BauernhofListe merken zum späteren Abgleich
				//System.out.println("1. Runde: Beim Bauernhof "+cheapestBauerVoll.getLieferant().getLieferantenName()+" alles kaufen");
				BestellPos bp=new BestellPos();
				//Gebindeanzahl bestimmen
				int anzGebinde=(int) (bedarf.getMenge()/cheapestBauerVoll.getGebindegroesse());
				//Wenn Modulo größer 0 ist, muss ein Gebinde mehr beschafft werden.
				if (!(bedarf.getMenge()%cheapestBauerVoll.getGebindegroesse()==0)){
					anzGebinde++;
				}
				cheapestBauerVoll.setArikelanzahl(cheapestBauerVoll.getArtikelanzahl()-anzGebinde);
				bp.setMenge(anzGebinde);
				bp.setArtikel(cheapestBauerVoll);
				bedarf.setMenge(bedarf.getMenge() - anzGebinde*cheapestBauerVoll.getGebindegroesse());
				if (bedarf.getMenge()<0){
					bedarf.setMenge(0);
				}
				bestellList.add(bp);
				if (!bauernhofList.contains(cheapestBauerVoll.getLieferant())){
					bauernhofList.add((Bauernhof) cheapestBauerVoll.getLieferant());
				}
			}			
		} //Ende Bedarf-Schleife
		//================================================================================================
		// Jetzt sind Bauernhöfe identifiziert, bei denen ohnehin bestellt wird, beim Abgleich der folgenden Bedarfspositionen können 
		//also die Lieferkosten dieser Bauernhöfe ignoriert werden.
		for (BedarfPos bedarf : bedarfListCopy) {
			if (bedarf.getMenge() > 0) {
				// Vollabdeckung Variablen
				Artikel cheapestBauerVoll = new Artikel();
				cheapestBauerVoll.setPreis(Float.MAX_VALUE);
				float cheapestBauerSummeNettoVoll = Float.MAX_VALUE;

				Artikel cheapestGHVoll = new Artikel();
				cheapestGHVoll.setPreis(Float.MAX_VALUE);
				float cheapestGHSummeNettoVoll = Float.MAX_VALUE;
				float cheapestGHLieferkostenVoll = Float.MAX_VALUE;

				// Teilabdeckung Variablen
				Artikel cheapestBauerTeil = new Artikel();
				cheapestBauerTeil.setPreis(Float.MAX_VALUE);
				float cheapestBauerSummeNettoTeil = Float.MAX_VALUE;
				int maxGebindeBauer = 0;

				Artikel cheapestGHTeil = new Artikel();
				cheapestGHTeil.setPreis(Float.MAX_VALUE);
				float cheapestGHSummeNettoTeil = Float.MAX_VALUE;
				float cheapestGHLieferkostenTeil = Float.MAX_VALUE;
				int maxGebindeGH = 0;

				ArrayList<Artikel> artList = lieferantenverw.gibAlleArtikel(bedarf.getName());
				for (Artikel art : artList) {
					int anzGebinde = (int) (bedarf.getMenge() / art.getGebindegroesse());
					// Wenn Modulo größer 0 ist, muss ein Gebinde mehr beschafft werden.
					if (!(bedarf.getMenge() % art.getGebindegroesse() == 0)) {
						anzGebinde++;
					}
					if (art.getArtikelanzahl() >= anzGebinde) {
						// kompletter Bedarf ließe sich mit dem Artikel abdecken
						if (art.getLieferant().getClass() == Bauernhof.class
								&& bauernhofList.contains(art.getLieferant())) {
							// Artikel ist Bauernhof
							// Kosten des Bauernhofes ist kleiner als Kosten des aktuell billigsten Anbieters
							if ((art.getPreis() * anzGebinde) < (cheapestBauerSummeNettoVoll)) {
								// Aktuell billigsten ersetzen
								cheapestBauerSummeNettoVoll = art.getPreis() * anzGebinde;
								cheapestBauerVoll = art;
							}
						}
						if (art.getLieferant().getClass() == Grosshandel.class) {
							Grosshandel gh = (Grosshandel) art.getLieferant();
							// Artikel + Lieferkosten des Grosshandels ist kleiner als aktuell billigster Artikel + Lieferkosten
							if (art.getPreis() * anzGebinde	+ gh.getLieferkostensatz() < cheapestGHSummeNettoVoll + cheapestGHLieferkostenVoll) {
								// billigsten ersetzen
								cheapestGHSummeNettoVoll = art.getPreis() * anzGebinde;
								cheapestGHLieferkostenVoll = gh.getLieferkostensatz();
								cheapestGHVoll = art;
							}
						}
					} else {
						// kompletter Bedarf ließe sich mit diesem Artikel nicht abdecken
						if (art.getLieferant().getClass() == Bauernhof.class && bauernhofList.contains(art.getLieferant())) {
							// Artikel ist Bauernhof
							// Artikel + Lieferkosten des Bauernhofes ist kleiner als Artikel + Lieferkosten des aktuell billigsten Anbieters
							if ((art.getPreis() * anzGebinde) < (cheapestBauerSummeNettoTeil)) {
								// Aktuell billigsten ersetzen
								cheapestBauerSummeNettoTeil = art.getPreis() * anzGebinde;
								cheapestBauerTeil = art;
								maxGebindeBauer = art.getArtikelanzahl();
							}
						}
						if (art.getLieferant().getClass() == Grosshandel.class) {
							Grosshandel gh = (Grosshandel) art.getLieferant();
							// Artikel + Lieferkosten des Grosshandels ist kleiner als aktuell billigster Artikel + Lieferkosten
							if (art.getPreis() * anzGebinde < cheapestGHSummeNettoTeil	+ cheapestGHLieferkostenTeil) {
								// billigsten ersetzen
								cheapestGHSummeNettoTeil = art.getPreis() * anzGebinde;
								cheapestGHLieferkostenTeil = gh.getLieferkostensatz();
								cheapestGHTeil = art;
								maxGebindeGH = art.getArtikelanzahl();
							}
						}
					}
				} // Ende Artikelschleife 2
				if (cheapestGHSummeNettoVoll + cheapestGHLieferkostenVoll < cheapestBauerSummeNettoVoll) {
					// BauerVoll ist raus
					// Prüfung ob Teilbestellungen vielleicht besser sind.
					// Kosten Teilbestellung Bauer
					float zusaetzlichzubestellen = bedarf.getMenge() - (maxGebindeBauer * cheapestBauerTeil.getGebindegroesse());
					int anzZusaetzlicheGebindeBauer = (int) zusaetzlichzubestellen;
					if (!(zusaetzlichzubestellen% cheapestGHVoll.getGebindegroesse() == 0)) {
						anzZusaetzlicheGebindeBauer++;
					}
					float teilbestellungBauer = cheapestBauerTeil.getPreis()*maxGebindeBauer + (anzZusaetzlicheGebindeBauer*cheapestGHVoll.getPreis()+cheapestGHLieferkostenVoll);

					// Kosten Teilbestellung GH
					zusaetzlichzubestellen = bedarf.getMenge() - (maxGebindeGH*cheapestGHTeil.getGebindegroesse());
					int anzZusaetzlicheGebindeGH = (int) zusaetzlichzubestellen;
					if (!(zusaetzlichzubestellen%cheapestGHVoll.getGebindegroesse() == 0)) {
						anzZusaetzlicheGebindeGH++;
					}

					float teilbestellungGH = cheapestGHTeil.getPreis()*maxGebindeGH+cheapestGHLieferkostenTeil + (anzZusaetzlicheGebindeGH*cheapestGHVoll.getPreis()+cheapestGHLieferkostenVoll);

					if ((teilbestellungBauer < cheapestGHSummeNettoVoll+cheapestGHLieferkostenVoll)	&& teilbestellungBauer < teilbestellungGH) {
						// Beim Bauer die Teillieferung holen
						System.out.println("Bei "+cheapestBauerTeil.getLieferant().getLieferantenName()+" Teilbestellung absetzen");
						BestellPos bp1 = new BestellPos();
						cheapestBauerTeil.setArikelanzahl(0);
						bp1.setMenge(maxGebindeBauer);
						bp1.setArtikel(cheapestBauerTeil);

						// Beim GH den Rest
						BestellPos bp2 = new BestellPos();
						cheapestGHVoll.setArikelanzahl(cheapestGHVoll.getArtikelanzahl() - anzZusaetzlicheGebindeBauer);
						bp2.setMenge(anzZusaetzlicheGebindeBauer);
						bp2.setArtikel(cheapestGHVoll);
						if (bedarf.getMenge() - maxGebindeBauer*cheapestBauerTeil.getGebindegroesse() - anzZusaetzlicheGebindeBauer*cheapestGHVoll.getGebindegroesse() > 0) {
							System.out.println("Problem bei Teillieferung vom Bauer");
						} else {
							bedarf.setMenge(0);
							bestellList.add(bp1);
							bestellList.add(bp2);
						}
					} else if ((teilbestellungGH < cheapestGHSummeNettoVoll+cheapestGHLieferkostenVoll)	&& teilbestellungGH < teilbestellungBauer) {
						// Beim GH Teillieferung machen
						System.out.println("Bei "+cheapestGHTeil.getLieferant().getLieferantenName()+" Teilbestellung absetzen");
						BestellPos bp1 = new BestellPos();
						cheapestGHTeil.setArikelanzahl(0);
						bp1.setMenge(maxGebindeGH);
						bp1.setArtikel(cheapestGHTeil);

						// Beim GH den Rest
						BestellPos bp2 = new BestellPos();
						cheapestGHVoll.setArikelanzahl(cheapestGHVoll.getArtikelanzahl() - anzZusaetzlicheGebindeGH);
						bp2.setMenge(anzZusaetzlicheGebindeGH);
						bp2.setArtikel(cheapestGHVoll);
						if (bedarf.getMenge() - maxGebindeBauer*cheapestBauerTeil.getGebindegroesse() - anzZusaetzlicheGebindeGH*cheapestGHVoll.getGebindegroesse() > 0) {
							System.out.println("Problem bei Teillieferung vom Grosshandel");
						} else {
							bedarf.setMenge(0);
							bestellList.add(bp1);
							bestellList.add(bp2);
						}
					} else {// Beim GH alles kaufen
						BestellPos bp = new BestellPos();
						// Gebindeanzahl bestimmen
						int anzGebinde = (int) (bedarf.getMenge() / cheapestGHVoll.getGebindegroesse());
						// Wenn Modulo größer 0 ist, muss ein Gebinde mehr
						// beschafft werden.
						if (!(bedarf.getMenge()%cheapestGHVoll.getGebindegroesse() == 0)) {
							anzGebinde++;
						}
						cheapestGHVoll.setArikelanzahl(cheapestGHVoll.getArtikelanzahl() - anzGebinde);
						bp.setMenge(anzGebinde);
						bp.setArtikel(cheapestGHVoll);
						bedarf.setMenge(bedarf.getMenge() - anzGebinde*cheapestGHVoll.getGebindegroesse());
						if (bedarf.getMenge() < 0) {
							bedarf.setMenge(0);
						}
						bestellList.add(bp);
					}
				}
				if (cheapestGHSummeNettoVoll + cheapestGHLieferkostenVoll > cheapestBauerSummeNettoVoll) {
					// GHVoll ist raus
					// Prüfung ob Teilbestellungen vielleicht besser sind.
					// Kosten Teilbestellung Bauer
					float zusaetzlichzubestellen = bedarf.getMenge() - (maxGebindeBauer * cheapestBauerTeil.getGebindegroesse());
					int anzZusaetzlicheGebindeBauer = (int) zusaetzlichzubestellen;
					if (!(zusaetzlichzubestellen% cheapestBauerVoll.getGebindegroesse() == 0)) {
						anzZusaetzlicheGebindeBauer++;
					}
					float teilbestellungBauer = cheapestBauerTeil.getPreis()*maxGebindeBauer + (anzZusaetzlicheGebindeBauer*cheapestBauerVoll.getPreis());

					// Kosten Teilbestellung GH
					zusaetzlichzubestellen = bedarf.getMenge() - (maxGebindeGH*cheapestGHTeil.getGebindegroesse());
					int anzZusaetzlicheGebindeGH = (int) zusaetzlichzubestellen;
					if (!(zusaetzlichzubestellen%cheapestBauerVoll.getGebindegroesse() == 0)) {
						anzZusaetzlicheGebindeGH++;
					}

					float teilbestellungGH = cheapestGHTeil.getPreis()*maxGebindeGH+cheapestGHLieferkostenTeil + (anzZusaetzlicheGebindeGH*cheapestBauerVoll.getPreis());

					if ((teilbestellungBauer < cheapestBauerSummeNettoVoll)	&& teilbestellungBauer < teilbestellungGH) {
						// Beim Bauer die Teillieferung holen
						System.out.println("Bei "+cheapestBauerTeil.getLieferant().getLieferantenName()+" Teilbestellung absetzen");
						BestellPos bp1 = new BestellPos();
						cheapestBauerTeil.setArikelanzahl(0);
						bp1.setMenge(maxGebindeBauer);
						bp1.setArtikel(cheapestBauerTeil);

						// Beim Bauer den Rest
						BestellPos bp2 = new BestellPos();
						cheapestBauerVoll.setArikelanzahl(cheapestBauerVoll.getArtikelanzahl() - anzZusaetzlicheGebindeBauer);
						bp2.setMenge(anzZusaetzlicheGebindeBauer);
						bp2.setArtikel(cheapestBauerVoll);
						if (bedarf.getMenge() - maxGebindeBauer*cheapestBauerTeil.getGebindegroesse() - anzZusaetzlicheGebindeBauer*cheapestBauerVoll.getGebindegroesse() > 0) {
							System.out.println("Problem bei Teillieferung vom Bauer");
						} else {
							bedarf.setMenge(0);
							bestellList.add(bp1);
							bestellList.add(bp2);
						}
					} else if ((teilbestellungGH < cheapestGHSummeNettoVoll+cheapestGHLieferkostenVoll)	&& teilbestellungGH < teilbestellungBauer) {
						// Beim GH Teillieferung machen
						System.out.println("Bei "+cheapestGHTeil.getLieferant().getLieferantenName()+" Teilbestellung absetzen");
						BestellPos bp1 = new BestellPos();
						cheapestGHTeil.setArikelanzahl(0);
						bp1.setMenge(maxGebindeGH);
						bp1.setArtikel(cheapestGHTeil);

						// Beim GH den Rest
						BestellPos bp2 = new BestellPos();
						cheapestBauerVoll.setArikelanzahl(cheapestBauerVoll.getArtikelanzahl() - anzZusaetzlicheGebindeGH);
						bp2.setMenge(anzZusaetzlicheGebindeGH);
						bp2.setArtikel(cheapestBauerVoll);
						if (bedarf.getMenge() - maxGebindeBauer*cheapestBauerTeil.getGebindegroesse() - anzZusaetzlicheGebindeGH*cheapestBauerVoll.getGebindegroesse() > 0) {
							System.out.println("Problem bei Teillieferung vom Grosshandel");
						} else {
							bedarf.setMenge(0);
							bestellList.add(bp1);
							bestellList.add(bp2);
						}
					} else {// Beim Bauer alles kaufen
						BestellPos bp = new BestellPos();
						// Gebindeanzahl bestimmen
						int anzGebinde = (int) (bedarf.getMenge() / cheapestBauerVoll.getGebindegroesse());
						// Wenn Modulo größer 0 ist, muss ein Gebinde mehr
						// beschafft werden.
						if (!(bedarf.getMenge()%cheapestBauerVoll.getGebindegroesse() == 0)) {
							anzGebinde++;
						}
						cheapestBauerVoll.setArikelanzahl(cheapestBauerVoll.getArtikelanzahl() - anzGebinde);
						bp.setMenge(anzGebinde);
						bp.setArtikel(cheapestBauerVoll);
						bedarf.setMenge(bedarf.getMenge() - anzGebinde*cheapestBauerVoll.getGebindegroesse());
						if (bedarf.getMenge() < 0) {
							bedarf.setMenge(0);
						}
						bestellList.add(bp);
					}
				}
			} // Ende if ! Bedarf=0
		} // Ende Bedarfschleife 2
		/* Debug-Print
		for (BestellPos bestell : bestellList) {
			System.out.println(bestell.getMenge() + " Gebinde a "
					+ bestell.getArtikel().getGebindegroesse() + " "
					+ bestell.getArtikel().getEinheit() + " "
					+ bestell.getArtikel().getName() + " bei "
					+ bestell.getArtikel().getLieferant().getLieferantenName()
					+ " kaufen.");
		}
		for (BedarfPos bedarf : bedarfListCopy) {
			System.out.println(bedarf.getName() + " " + bedarf.getMenge());
		}*/
		return bestellList;
	}
	/**
	 * Die Methode gibt den km-Satz in Euro-Cent zurück, welcher in der config.properties hinterlegt wurde.
	 * 
	 * @return Den km-Satz, welche in der config.properties der Anwendung angegeben ist, in Euro-Cent.
	 */
	private float getkmSatz(){
		float kmSatz = Float.MAX_VALUE;
		try{
			Properties properties = new Properties();
			BufferedInputStream stream = new BufferedInputStream(new FileInputStream("config.properties"));
			properties.load(stream);
			stream.close();
			kmSatz = Float.parseFloat(properties.getProperty("kmSatz"));
		} 
		catch (IOException e) {
			MainWin.StringOutln(e.toString());
			MainWin.StringOutln("Die Datei config.properties konnte nicht gelesen werden. Prüfen Sie, " +
					"ob sie im Anwendungsordner vorhanden ist.");
		}
		return kmSatz;
	}

	/**
	 * Berechnet die Gesamtkosten der Bestellung inklusive Transportkosten, die sich aus allen im BestellPosArrayList enthaltenen 
	 * Bestellpositionen ergibt und schreibt sie in das Attribut gesamtkosten, welches mit getGesamtkosten() ausgelesen werden
	 * kann.
	 */
	public void berechneGesamtkosten()
	{
		float gesamtkosten=0;
		float kmSatz=getkmSatz();
		ArrayList<Bauernhof> bauernhofList=new ArrayList<Bauernhof>();
		for (BestellPos b:bestellPosList){
			if(b.getArtikel().getLieferant().getClass()==Bauernhof.class){
				if (!(bauernhofList.contains(b.getArtikel().getLieferant()))){
					//BauernhofList enthält den Bauernhof noch nicht
					Bauernhof bauer=(Bauernhof) (b.getArtikel().getLieferant());
					bauernhofList.add(bauer);
					gesamtkosten=gesamtkosten+bauer.getEntfernung()*kmSatz;
				}
			}
			if (b.getArtikel().getLieferant().getClass()==Grosshandel.class){
				Grosshandel gh=(Grosshandel) (b.getArtikel().getLieferant());
				gesamtkosten=gesamtkosten+gh.getLieferkostensatz();
			}
			gesamtkosten=gesamtkosten+b.getMenge()*b.getArtikel().getPreis();

		}
		this.gesamtkosten=gesamtkosten;
	}

	/**
	 * Gibt die Gesamtkosten der Einkaufsliste zurück, die vorher mit berechneGesamtkosten() berechnet wurden.
	 *

	 * @return Die Gesamtkosten der Einkaufsliste.
	 */
	public float getGesamtkosten()
	{
		return gesamtkosten;
	}

	/**
	 * Liefert einen ArrayList zurück, der alle BestellPos-Objekte der Einkaufsliste enthält
	 * 
	 * @return Einen ArrayList der alle BestellPos-Objekte enthält
	 */
	public ArrayList<BestellPos> getBestellPos()
	{
		return bestellPosList;
	}
}

