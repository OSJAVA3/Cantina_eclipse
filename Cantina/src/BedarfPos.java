
/**
 * Ein Objekt der Klasse BedarfPos enth�lt den Gesamtbedarf (die Gesamtmenge) einer Zutat f�r
 * den Planungszeitraum.  
 * Die BedarfPos wird von der Einkaufsliste aggregiert. 
 * 
 * @author Rene Wiederhold 
 * @version
 */
public class BedarfPos
{
    private String name;
    private float menge;
    private String einheit;
    

    /**
     * Dem Konstruktor des Objekts wird der Name der Zutat �bergeben.
     * 
     * @param name Der Name der Zutat
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
}