
/**
 * Die Klasse beschreibt Zutaten durch Name, Menge und deren Einheit.
 * Instanzen der Klasse Zutat sind existenzabhängig von Rezeptobjekten, 
 * weshalb Sie eine Komposition der Rezpte darstellen.
 * 
 * @author Lukas Krotki
 * @version 1.0
 */
public class Zutat
{
	/** Der Name der Zutat */
	private String name;
	/** Die Masseinheit der Menge */
	private String einheit;
	/** Die benötigte Menge der Zutat */
	private float menge;

	/**
	 * Konstruktor für Objekte der Klasse Zutat
	 * 
	 * @param name Der Name der Zutat
	 * @param menge Die benötigte Menge der Zutat
	 * @param einheit Die Masseinheit der Menge
	 */
	public Zutat(String name, float menge, String einheit)
	{
		this.name=name;
		this.menge=menge;
		this.einheit=einheit;
	}

	/**
	 * Setzt den Namen der Zutat 
	 * @param zutatName 
	 * 
	 * @param name Der Name der Zutat  
	 */
	public void setName(String zutatName){

	}
	/**
	 * Gibt den Namen einer Zutat
	 * 
	 * @return Name der Zutat
	 */
	public String getName(){
		return name;
	}

	/**
	 * Setzt die Menge der Zutat 
	 * 
	 * @param menge Die Menge der Zutat
	 *        
	 */
	public void setMenge(float menge){

	}
	/**
	 * Gibt die Menge einer Zutat
	 * 
	 * @return Menge einer Zutat
	 */
	public float getMenge(){
		return menge;
	}
	/**
	 * Setzt die Einheit der Zutat 
	 * 
	 * @param einheit Die Einheit der Zutat
	 *        
	 */
	public void setEinheit (String einheit){

	}
	/**
	 * Gibt die Einheit einer Zutat
	 * 
	 * @return Einheit der Zutat
	 */
	public String getEinheit(){
		return einheit;
	}
}

