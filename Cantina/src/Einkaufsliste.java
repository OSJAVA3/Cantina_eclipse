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
    					//System.out.println(zname+"-Menge von "+vorhBP.getMenge()+" auf "+(vorhBP.getMenge()+(m*zmenge))+" um "+(m*zmenge)+" angepasst");
    				}
    				// Lebensmittel-Objekt noch nicht vorhanden
    				else if(vorh==false){
    					vorhBP.setName(zname);
    					vorhBP.setMenge(m*zmenge);
    					vorhBP.setEinheit(zeinh);
    					bedarfPosList.add(vorhBP);
    					//Debug-Print
    					//System.out.println((m*zmenge)+" "+zeinh+" "+zname+" der Liste hinzugefügt!");
    				}	
    			}
    		} //Ende Zutatenschleife
    	} //Ende Tagesgericht-Schleife
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
    	makeVariante1();
    	
    	return true;
    }
    /**
     * Für die Erstellung der Einkaufsliste mit den Bestellpositionen wird zunächst eine Variante gerechnet, die versucht so viel es geht bei Bauernhöfen zu beschaffen.
     */
    private void makeVariante1(){
    	ArrayList<BedarfPos> bedarfListCopy=new ArrayList<BedarfPos>();
    	ArrayList<BestellPos> bestellList=new ArrayList<BestellPos>();
    	//Zunächst muss eine tiefe Kopie der BedarfPos-Objekte bzw. der bedarfPosList erstellt werden, auf der das Szenario rechnen kann, ohne die Original-Daten zu zerstören.
    	for (BedarfPos bedarf:bedarfPosList){
    		bedarfListCopy.add(bedarf.clone());
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
    				if (neuerBedarf<=0) bedarf.setMenge(0);
    				//Ansonsten wird der neue Bedarf gesetzt.
    				else bedarf.setMenge(neuerBedarf);   				
    			}
    		} //Ende Artikel-Schleife	
    	} //Ende BedarfPos-Schleife
    	for (BestellPos b:bestellList){
    		Artikel a=b.getArtikel();
    		System.out.println(b.getMenge()+" Gebinde "+a.getName()+" bei "+a.getLieferant().getLieferantenName()+" kaufen.");
    	}
    }
  
    /**
     * Die Methode gibt den km-Satz in Euro-Cent zurück, welcher in der config.properties hinterlegt wurde.
     * 
     * @return Den km-Satz, welche in der config.properties der Anwendung angegeben ist, in Euro-Cent.
     */
    private int getkmSatz(){
    	int kmSatz = Integer.MAX_VALUE;
    	try{
    		Properties properties = new Properties();
    		BufferedInputStream stream = new BufferedInputStream(new FileInputStream("config.properties"));
    		properties.load(stream);
    		stream.close();
    		kmSatz = Integer.parseInt(properties.getProperty("kmSatz"));
    	} 
    	catch (IOException e) {
		System.out.println(e.toString());
		System.out.println("Die Datei config.properties konnte nicht gelesen werden. Prüfen Sie, " +
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

