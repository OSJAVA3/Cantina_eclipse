import java.util.ArrayList;
/**
 * Die Klasse Kantinenplan repr�sentiert einen Kantinenplan im Rahmen der Planungsperiode.
 * Sie aggregiert die Tagesgerichtobjekte und erstellt auf Basis der Anzahl der 
 * Mitarbeiter je Kantine und Standort einen Plan mit insgesamt 45 Tagesgerichten.
 * Sie steht in Assoziation zu der Einkaufsliste um, durch die �bergabe eines
 * Kantinenplans, die Bestellpositonen zu generieren. Der Kantinenplan hat durch die Assoziation zur
 * Reezptverwaltung und Lieferantenverwaltung Zugriff auf deren Methoden um eine g�ltigen Kantinenplan
 * zu erstellen. Um den Export eine Kantinenplans in eine Datei zu erm�glichen steht der Kantinenplan 
 * auch in Assoziation zum Exporter. Damit ein Planungslauf von der Kantinenplanung durchgf�hrt werden kann, 
 * liegt auch mit dieser Klasse eine Assoziationsbeziehung vor.
 * 
 * @author Rene Wiederhold
 * @version
 */
public class Kantinenplan
{
    /** Der Standort, f�r den der Kantinenplan erzeugt wird */ 
    private String standort;
    /** Die Anzahl der Mitarbeiter, welche die Kantine am Standort besuchen*/
    private int anzMitarbeiter;
    /** Die ArrayList enth�lt die zum Speiseplan geh�renden Tagesgerichte */
    public ArrayList<Tagesgericht> tagesgerichtArrayList;
    
    // Die f�r die Kantinenplanerstellung notwendigen Lieferanten- und Rezeptverwaltungen
    private Lieferantenverwaltung lieferantenverw;
    private Rezeptverwaltung rezeptverw;

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
    	
    	//Debug-Print
    	System.out.println("Die Kantine "+standort+" mit "+anzMitarbeiter+" Mitarbeitern wurde erzeugt.");
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
        this.lieferantenverw=lieferantenverw;
        this.rezeptverw=rezeptverw;
    	
    	
        
        
        /*
        * F�r eine Woche, das ganze wird dann 2x wiederholt:
        * Von der Rezeptverwaltung werden 5 Fleischgericht-Rezepte und 5 vegetarische Rezepte angefordert, dann ein 
        * Fischgericht, die restlichen 4 mit zuf�lligen Rezepten aufgef�llt. Die 4 Teile werden in tempor�ren Arrays hinterlegt.
        * Nun werden alle Rezepte auf ihre Verf�gbarkeit bei der Lieferantenverwaltung gepr�ft. Falls etwas nicht verf�gbar ist,
        * muss neu angefordert und im tempor�ren Array ersetzt werden.  Vll. sollte man auch sicherstellen, dass nicht zu viele 
        * Fischgerichte �ber das Zufallsrezept reinkommen.
        * Dann werden die Tagesgerichtobjekte daraus erstellt. Als Datum wird 1-5 gesetzt, das Fischgericht bekommt immer
        * die 5 (bzw. 10 oder 15 f�r die 2. und 3. Woche). 
        * F�r die Tagesgerichte mit gleichem Datum k�nnen dann die Absatzmengen berechnet und in die Attribute geschrieben werden.
        * Die Tagesgerichtobjekte kommen, wenn alles passt in den tagesgerichtArrayList und die Rezepte werden dann auf 
        * "verwendet=true gesetzt. 
        */
       return true;
    }
    
    /**
     * Die Methode gibt eine ArrayList zur�ck, der alle Tagesgerichte des Kantinenplans enth�lt.
     * 
     * @return Eine ArrayList, der alle Tagesgerichte enth�lt.
     */
    public ArrayList<Tagesgericht> getTagesgerichte()
    {
        return tagesgerichtArrayList;
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
}