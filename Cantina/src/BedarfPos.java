
/**
 * Ein Objekt der Klasse BedarfPos enthält den Gesamtbedarf (die Gesamtmenge) einer Zutat für
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
	 * Dem Konstruktor des Objekts
	 * 
	 */
	public BedarfPos()
	{

	}


	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}


	/**
	 * @return the menge
	 */
	public float getMenge() {
		return menge;
	}


	/**
	 * @return the einheit
	 */
	public String getEinheit() {
		return einheit;
	}


	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}


	/**
	 * @param menge the menge to set
	 */
	public void setMenge(float menge) {
		this.menge = menge;
	}


	/**
	 * @param einheit the einheit to set
	 */
	public void setEinheit(String einheit) {
		this.einheit = einheit;
	}
	/** Klont das BedarfPos-Objekt (tiefe Kopie)
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