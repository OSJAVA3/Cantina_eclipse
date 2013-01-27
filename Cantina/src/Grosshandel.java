
/**
 * Die Objekte der Klasse Grosshandel erben alle ihre wesentlichen Attribute und Methoden von der Klasse Lieferant,
 * nur die Methode zur Ausgabe des Lieferkostensatz wird hier zusätzlich implementiert.
 * Die Klasse Grosshandel ist eine Subklasse der Oberklasse Lieferant.
 * 
 * @author Rene Wiederhold 
 * @version 1.00
 */
public class Grosshandel extends Lieferant
{
	private float kostensatz;     //Der Kostensatz, welcher pro bestellten Artikel fällig wird. 

	/**
	 * Konstruktor für Objekte der Klasse Grosshandel
	 */
	public Grosshandel()
	{
		kostensatz = 0;
	}

	/**
	 * Gibt den Lieferkostensatz eines Grosshandels zurück. Dies wird für die Transportkostenberechnung innerhalb der 
	 * Einkaufsliste benötigt.
	 *
	 * @return    Der Lieferkostensatz des Grosshandels.
	 */
	public float getLieferkostensatz()
	{
		return kostensatz;
	}

	/**
	 * Setzt den Lieferkostensatz eines Grosshandels.
	 *
	 * @param  liefko  Der Lieferkostensatz des Grosshandels
	 */
	public void setKostensatz(float liefko)
	{
		kostensatz=liefko;
	}

}
