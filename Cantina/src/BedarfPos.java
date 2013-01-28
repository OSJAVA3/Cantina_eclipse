
/**
 * Ein Objekt der Klasse BedarfPos enthält den Gesamtbedarf (die Gesamtmenge und die Einheit) einer Zutat für
 * den Planungszeitraum.  
 * Die BedarfPos wird von der Einkaufsliste aggregiert. 
 * 
 * @author Rene Wiederhold 
 * @version 1.00
 */
public class BedarfPos implements Cloneable
{
	private String name;
	private float menge;
	private String einheit;


	/**
	 * Der Standard-Konstruktor des Objektes
	 * 
	 */
	public BedarfPos()
	{

	}


	/**
	 * Die Methode gibt den Names des Objektes zurück
	 * @return Der Name der Bedarfposition
	 */
	public String getName() {
		return name;
	}


	/**
	 * Die Methode gibt die Menge der Bedarfposition zurück
	 * @return Die Menge der Bedarfposition
	 */
	public float getMenge() {
		return menge;
	}


	/**
	 * Die Methode gibt die Einheit der Bedarfposition zurück
	 * @return Die Einheit der Bedarfsposition
	 */
	public String getEinheit() {
		return einheit;
	}


	/**
	 * Die Methode setzt den Namen der Bedarfposition
	 * @param name Der Name der Bedarfposition
	 */
	public void setName(String name) {
		this.name = name;
	}


	/**
	 * Die Methode setzt die Menge der Bedarfposition
	 * @param menge Die Menge der Einheit
	 */
	public void setMenge(float menge) {
		this.menge = menge;
	}


	/**
	 * Die Methode setzt die Einheit der Bedarfposition
	 * @param einheit Die Einheit
	 */
	public void setEinheit(String einheit) {
		this.einheit = einheit;
	}
	/** Klont das BedarfPos-Objekt als tiefe Kopie
	 * @return Das geklonte BedarfPos-Objekt
	 */
	public BedarfPos clone(){
		BedarfPos bp=new BedarfPos();
		bp.setName(getName());
		bp.setMenge(getMenge());
		bp.setEinheit(getEinheit());
		return bp;


	}
}