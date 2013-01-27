/**
 * Die abstrakte Klasse Lieferant beinhaltet den Namen der Lieferanten. 
 * Verwaltet werden die Lieferantenobjekte von der Klasse Lieferantenverwaltung die diese aggregiert. 
 * Objekte der Klasse Lieferant k�nnen nicht erzeugt werden, da die Klasse Lieferant eine abstrakte Klasse ist.
 * Sie f�hrt die Klassen Bauernhof und Grosshandel als Subklassen.
 * 
 * @author Rene Wiederhold
 * @version 1.00
 */
public abstract class Lieferant
{
	private String lieferantName;

	public Lieferant(){

	}
	/**
	 * Setzt den Namen eines Lieferanten
	 * 
	 * @param  lieferantName Der Name des Lieferanten
	 */
	public void setLieferantName(String name)
	{
		lieferantName=name;
	}
	/**
	 * Gibt den Namen des Lieferanten zur�ck
	 *
	 * @return     Der Name des Lieferanten
	 */
	public String getLieferantenName() 
	{
		return lieferantName;
	}
}
