
/**
 * Die Objekte der Klasse Bauernhof erben alle ihre wesentlichen Attribute und Methoden von der Klasse Lieferant,
 * nur die Methode zur Ausgabe der Entfernung wird hier zusätzlich implementiert. Die Klasse Bauernhof ist eine 
 * Subklasse der Oberklasse Lieferant.
 * 
 * @author Rene Wiederhold
 * @version
 */
public class Bauernhof extends Lieferant
{
    private float entfernung; //Die Entfernung des Bauernhofes, welche mit dem pauschalen km-Satz multipliziert wird.

    /**
     * Konstruktor für Objekte der Klasse Bauernhof
     */
    public Bauernhof()
    {
        entfernung = 0;
    }
    /**
     * Gibt die Entfernung eins Bauernhof zurück. Dies wird für die Transportkostenberechnung innerhalb der 
     * Einkaufsliste benötigt.
     *
     * @return    Die Entfernung des Bauernhofes.
     */
    public float getEntfernung()
    {
        return entfernung;
    }
    /**
     * Setzt die Entfernung eines Bauernhofes.
     *
     * @param  entf  Die Entfernung des Bauernhofes
     */
    public void setEntfernung(float entf)
    {
    	this.entfernung=entf;
    }
}
