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
    	ArrayList<Artikel> artList= lieferantenverw.gibAlleArtikel(bedarfPosList.get(0).getName());
    	for (Artikel art:artList){
    		System.out.println(art.getName()+" "+art.getPreis()+" "+art.getLieferant().getLieferantenName());
    	}
    	//makeVariante1();
    	
    	return true;
    }
    /**
     * Für die Erstellung der Einkaufsliste mit den Bestellpositionen wird zunächst eine Variante gerechnet, die versucht so viel es geht bei Bauernhöfen zu beschaffen.
     */
    private void makeVariante1(){
    	//Zunächst muss eine tiefe Kopie der BedarfPos-Objekte bzw. der bedarfPosList erstellt werden, auf der das Szenario rechnen kann, ohne die Original-Daten zu zerstören.
    	ArrayList<BedarfPos> bedarfListCopy=new ArrayList<BedarfPos>();
    	for (BedarfPos bedarf:bedarfPosList){
    		bedarfListCopy.add(bedarf.clone());
    	}
    	for (BedarfPos bedarf:bedarfListCopy){
    		Artikel art = getCheapestBauernhofArticle(bedarf);
    		
    		
    	}
    }
    
    /**
     * Die Methode gibt den Artikel mit dem günstigsten Einzelpreis zurück.
     * 
     * @param bedarf Ein BedarfPos-Objekt, für das der Artikel gesucht werden werden soll.
     * @return Einen Artikel
     */
    private ArrayList<Artikel> getAllArticles(BedarfPos bedarf) {
		ArrayList<Artikel> artList=lieferantenverw.gibAlleArtikel(bedarf.getName());
		Artikel cheapest=new Artikel();
		cheapest.setPreis(Float.MAX_VALUE);
		for (Artikel art:artList){
			if (art.getPreis()<cheapest.getPreis()){
				cheapest=art;
			}
		}
		return cheapest;
	}
    /**
     * Die Methode gibt den Artikel mit dem günstigsten Einzelpreis zurück, welcher von einem Bauernhof-Lieferanten angeboten wird.
     * 
     * @param bedarf Ein BedarfPos-Objekt, für das der Artikel gesucht werden werden soll.
     * @return Einen Artikel
     */
    private Artikel getCheapestBauernhofArticle(BedarfPos bedarf) {
		ArrayList<Artikel> artList=lieferantenverw.gibAlleArtikel(bedarf.getName());
		Artikel cheapest=new Artikel();
		cheapest.setPreis(Float.MAX_VALUE);
		for (Artikel art:artList){
			if (art.getPreis()<cheapest.getPreis() && art.getLieferant().getClass()==Bauernhof.class){
				cheapest=art;
			}
		}
		return cheapest;
	}
    /**
     * Die Methode gibt den Artikel mit dem günstigsten Einzelpreis zurück, welcher von einem Grosshandel angeboten wird.
     * 
     * @param bedarf Ein BedarfPos-Objekt, für das der Artikel gesucht werden werden soll.
     * @return Einen Artikel
     */
    private Artikel getCheapestGrosshandelArticle(BedarfPos bedarf) {
		ArrayList<Artikel> artList=lieferantenverw.gibAlleArtikel(bedarf.getName());
		Artikel cheapest=new Artikel();
		cheapest.setPreis(Float.MAX_VALUE);
		for (Artikel art:artList){
			if (art.getPreis()<cheapest.getPreis() && art.getLieferant().getClass()==Grosshandel.class){
				cheapest=art;
			}
		}
		return cheapest;
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

