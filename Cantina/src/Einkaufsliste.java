import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Properties;
/**
 * Die Klasse Einkaufsliste kann aus den Kantinenplan-Objekten eine Einkaufsliste mit einzelnen 
 * Einkaufslistenpositionen erzeugen. Dazu erzeugt sie als Zwischenschritt BedarfPos-Objekte 
 * f�r den Gesamtbedarf einer Zutat. Die Bedarfe werden dann in BestellPos-Objekte �berf�hrt.
 * 
 * Eine Einkaufsliste steht in einer Kompositionsbeziehung zur Bestellpositionen, weil eine Einkaufsliste nie 
 * ohne eine Bestellpsotion existieren kann. Sie verwaltet zudem die BedarfsPos und aggregeiert
 * diese. Sie aggregiert auch die Kantinenpl�ne um den Gesamtbedarf an Artikeln zu ermitteln, die als 
 * BedarfPos gef�hrt werden. Die Assoziation zur Lieferantenverwaltung erm�glicht der Einkaufsliste die 
 * g�nstigsten Artikel zu ermitteln und aus der BedarfsPos die BestellPos zu generieren.
 * F�r den Planungslauf steht sie in Assoziation zur Kantinenplanung. Zudem wird sie als Datei
 * exportiert, weshalb auch zum Exporter eine Assoziation gegeben ist. 
 * 
 * @author Rene Wiederhold 
 * @version
 */
public class Einkaufsliste
{
    /** Enth�lt die Objekte der Klasse BestellPos */
    private ArrayList<BestellPos> bestellPosList;
    /** Enth�lt die Objekte der Klasse BedarfPos */
    private ArrayList<BedarfPos> bedarfPosList;
    /** Enth�lt die Gesamtkosten der Bestellung inklusive der Transportkosten */
    private float gesamtkosten;
    /** Enth�lt die zu verwendende Lieferantenverwaltung */
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
     * Die Methode f�gt die ben�tigten Zutaten f�r einen Kantinenplan den Bedarfsposition-Objekten hinzu bzw. erstellt diese.
     *
     * @param  kantinenplan   Ein Kantinenplanobjekt
     * @return     True f�r ein erfolgreiches hinzuf�gen, False falls ein Problem aufgetreten ist.
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
    				//Es muss gepr�ft werden ob ein Objekt gleichen Namens vorhanden ist
    				//Hier wird eine "Iterator-Schleife verwendet
    				Iterator<BedarfPos> it=bedarfPosList.iterator();
    				//Variable f�r die "Vorhanden"-Pr�fung
    				Boolean vorh=false;
    				//Variable f�r die bereits in der List vorhandene Bedarfposition
    				BedarfPos vorhBP=new BedarfPos();
    				//Solang die List noch ein weiteres Element hat, l�uft die Schleife
    				while (it.hasNext()){
    					//next() gibt das n�chste BedarfPos-Objekt
    					BedarfPos bedarfPosIt=it.next();
    					//Stimmt der Name der BedarfPos mit der zu pr�fenden Zutat �berein?
    					//Wenn ja, dass Objekt tempor�r gespeichert und die Schleife gebrochen.
    					if(bedarfPosIt.getName().equals(zname)){
    						vorh=true;
    						vorhBP=bedarfPosIt;
    						break;
    					}
    					//Stimmt der �bergebene Name der BedarfPos nicht mit dem iterierten �berein, bleibt vorh auf false
    					else{
    						vorh=false;
    					}	
    				} //while-Ende - Jetzt steht entweder vorh auf true und vorhBP enth�lt das BedarfPos-Objekt oder vorh steht auf false
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
    					//System.out.println((m*zmenge)+" "+zeinh+" "+zname+" der Liste hinzugef�gt!");
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
     * Erzeugt aus den im KantinenplanArrayList enthaltenen Kantinenpl�nen eine Einkaufsliste
     * Es muss vorher mindestens ein Kantinenplan �ber addKantinenplan referenziert worden sein.
     * 
     * @return     true, f�r eine erfolgreich erstellte Einkaufsliste, false, falls Fehler aufgetreten sind.
     */
    public boolean erzeugeEinkaufsliste(Lieferantenverwaltung lieferantenverw)
    {
    	this.lieferantenverw=lieferantenverw;
    	makeVariante1();
    	
    	return true;
    }
    /**
     * F�r die Erstellung der Einkaufsliste mit den Bestellpositionen wird zun�chst eine Variante gerechnet, die versucht so viel es geht bei Bauernh�fen zu beschaffen.
     */
    private void makeVariante1(){
    	ArrayList<BedarfPos> bedarfListCopy=new ArrayList<BedarfPos>();
    	ArrayList<BestellPos> bestellList=new ArrayList<BestellPos>();
    	//Zun�chst muss eine tiefe Kopie der BedarfPos-Objekte bzw. der bedarfPosList erstellt werden, auf der das Szenario rechnen kann, ohne die Original-Daten zu zerst�ren.
    	for (BedarfPos bedarf:bedarfPosList){
    		bedarfListCopy.add(bedarf.clone());
    	}
    	//Schleife f�r alle BedarfPos-Objekte
    	for (BedarfPos bedarf:bedarfListCopy){
    		ArrayList<Artikel> artList=lieferantenverw.gibAlleArtikel(bedarf.getName());
    		//artList enth�lt jetzt alle Artikel, die f�r die Beschaffung der BedarfPos in Frage kommen, der gr��e nach sortiert.
    		for (Artikel art:artList){
    			if (art.getLieferant().getClass()==Bauernhof.class){
    				BestellPos bestellPos=new BestellPos();
    				//F�r die Bestimmung der n�tigen Anzahl Gebinde muss quasi immer aufgerundet werden.
    				//Hier wird zun�chst Ganzzahldivision gerechnet, also der Rest "abgeschnitten".
    				int anzGebinde=(int) (bedarf.getMenge()/art.getGebindegroesse());
    				//Wenn Modulo gr��er 0 ist, muss ein Gebinde mehr beschafft werden.
    				if (!(bedarf.getMenge()%art.getGebindegroesse()==0)){
    					anzGebinde++;
    				}
    				//Reicht die Anzahl der Gebinde, die der Lieferant liefern kann nicht aus, um den kompletten Bedarf zu decken, wird nur die maximale beschaffbare Anzahl beschafft.
    				if (anzGebinde>art.getArtikelanzahl()){
    					anzGebinde=art.getArtikelanzahl();
    				}
    				//BestellPosition f�r Variante 1 schreiben und zur (tempor�ren) Bestellliste hinzuf�gen.
    				bestellPos.setMenge(anzGebinde);
    				bestellPos.setArtikel(art);
    				bestellList.add(bestellPos);
    				//Die Bedarfsposition ist hierf�r anzupassen
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
     * Die Methode gibt den km-Satz in Euro-Cent zur�ck, welcher in der config.properties hinterlegt wurde.
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
		System.out.println("Die Datei config.properties konnte nicht gelesen werden. Pr�fen Sie, " +
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
     * Gibt die Gesamtkosten der Einkaufsliste zur�ck, die vorher mit berechneGesamtkosten() berechnet wurden.
     *

     * @return Die Gesamtkosten der Einkaufsliste.
     */
    public float getGesamtkosten()
    {
        return gesamtkosten;
    }
    
    /**
     * Liefert einen ArrayList zur�ck, der alle BestellPos-Objekte der Einkaufsliste enth�lt
     * 
     * @return Einen ArrayList der alle BestellPos-Objekte enth�lt
     */
    public ArrayList<BestellPos> getBestellPos()
    {
        return bestellPosList;
    }
}

