/**
 * Die Klasse Tagesgericht enth�lt neben dem Rezept auch 
 * die Anzahl der herzustellenden Gerichte und das Datum, 
 * an welchen Werktag der Planungsperiode es angeboten wird.
 * 
 * Sie wird vom Objekt Kantinenplan aggregiert und steht mit 
 * den Klassen Lieferantenverwaltung sowie dem Rezept in Assoziation,
 * die �ber den Kantinenplan hergestellt wird.
 * 
 * @author Lukas Krotki
 * @version 1.0
 */
public class Tagesgericht
{
	/** Das Rezept, auf das sich das Tagesgericht bezieht*/
	private Rezept rezept;
	/** Die herzustellende Menge des Tagesgericht, welche nach der Hitlistenposition vorraussichtlich abgesetzt wird.*/
	private int menge;
	/** Gibt als einfachen Integer an, an welchen Werktag der Planungsperiode das Tagesgericht hergestellt werden soll.*/
	private int datum;

	/**
	 * Konstruktor f�r Objekte der Klasse Tagesgericht
	 * 
	 * @param rezept Das Rezept des Tagesgerichts
	 */
	public Tagesgericht(Rezept rezept)
	{
		this.rezept = rezept;
	}

	/**
	 * Gibt das Rezept des Tagesgerichtes zur�ck.
	 *
	 * @return Das Rezept des Tagesgerichts
	 */
	public Rezept getRezept()
	{
		return rezept;
	}

	/**
	 * Gibt das Datum des Tagesgerichtes zur�ck.
	 *
	 * @return Das Datum des Tagesgerichts
	 */
	public int getDatum()
	{
		return datum;
	}

	/**
	 * Setzt das Datum des Tagesgerichtes.
	 *
	 * @param datum Das Datum des Tagesgerichts
	 */
	public void setDatum(int datum)
	{
		this.datum=datum;
	}
	
	/**
	 * Gibt die Absatztmenge des Tagesgerichtes zur�ck.
	 *
	 * @return Die Absatzmenge des Tagesgerichts
	 */
	public int getMenge()
	{
		return menge;
	}

	/**
	 * Setzt die Absatzmenge des Tagesgerichtes.
	 *
	 * @param menge Die Absatzmenge des Tagesgerichts
	 */
	public void setMenge(int menge)
	{
		this.menge=menge;
	}
}
