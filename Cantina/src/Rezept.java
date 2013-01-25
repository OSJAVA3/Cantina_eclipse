import java.util.*; // Importiert die Klasse ArrayList

/**
 * Die Klasse Rezept beschreibt Rezeptobjekte.
 * Sie steht mit den Zutaten in einer Kompositionsbeziehung, da ein Rezept ohne Zutaten nicht existieren kann. 
 * Sie steht in Assoziation zum Tagesgericht und wird zudem von der Rezeptverwaltung aggregiert,
 * welche die Rezeptobjekte verwaltet.
 * 
 * @author Lukas Krotki 
 * @version
 */

public class Rezept 
{
	/** Die ArrayList enthält alle Zutatenobjekte des Rezeptes */
	private ArrayList<Zutat> zutaten;
	/** Der Name des Rezeptes */
	private String name;
	/** Das Attribut verwendet wird mit FALSE initialisiert und auf TRUE gesetzt, falls das Rezept in den Speiseplan aufgenommen wird */
	private boolean verwendet;
	/** Die Hitlistenposition des Rezeptes */
	private int hitlistPos;
	/** Der Typ des Rezeptes (Fleisch, Fisch, Veggie) */
	private RezeptTyp typ;

	/**
	 * Konstruktor für Objekte der Klasse Rezept
	 * 
	 * @param name Der Name des Rezeptes
	 */
	public Rezept(String name)
	{
		this.name =name;
		zutaten = new ArrayList<Zutat>();
		hitlistPos = 0;
	}

	/**
	 * Setzt den Namen des Rezepts  
	 * 
	 * @param name Name des Rezepts
	 */
	public void setName (String name){
		this.name = name;
	}

	/**
	 * Gibt den Namen eines Rezepts zurück
	 * 
	 * @return name Name des Rezeptes
	 */
	public String getName(){
		return name;
	}

	/**
	 * Setzt die ArrayList für die Rezepte
	 * 
	 * @param zutaten ArrayList für die Zutaten
	 */
	public void addZutat(Zutat zutat){
		zutaten.add(zutat);
	}

	/**
	 * Gibt den ArrayList der Zutaten eines Rezepts zurück
	 * 
	 * @return  ArrayList mit Zutaten eines Rezeptes
	 */
	public ArrayList<Zutat> getZutaten(){
		return zutaten;
	}

	/**
	 * Setzt, ob ein Rezept verwendet wurde
	 * 
	 * @param verwendet Rezept wurde bereits verwendet
	 */
	public void setVerwendet (boolean verwendet){
		this.verwendet=verwendet;
	}

	/**
	 * Gibt den zurück, ob ein Rezept bereits verwendet wurde
	 * 
	 * @return Ob ein Rezept bereits verwendet wurde
	 */
	public boolean getVerwendet(){
		return verwendet;
	}

	/**
	 * Setzt die Hitlistenposition für ein Rezept  
	 * 
	 * @param hitlistenposition Postion eines Rezeptes auf der Hitliste
	 */
	public void setHitlistenpos ( String hitlistPosStr) {
		hitlistPos = Integer.valueOf( hitlistPosStr );

	}

	/**
	 * Gibt die Hitlistenpositon eines Rezeptes zurück
	 * 
	 * @return Postion des Rezeptes auf der Hitliste
	 */
	public int getHitlistenpos(){
		return hitlistPos;
	}

	/**
	 * Setzt den Typ des Rezeptes
	 * 
	 * @param typ Typ des Rezeptes (Fleisch, Fisch, Vegie)
	 */
	public void setRezeptTyp (Lieferantenverwaltung lieferantenVerw){
		RezeptTyp zutatTyp;
		// Alten Wert ueberschreiben und neuen Typ aufgrund der jetzigen Zutaten ermitteln
		typ = RezeptTyp.Vegetarisches;

		for( Zutat zutat : zutaten ) {
			zutatTyp = lieferantenVerw.holeTyp(zutat);
			if (zutatTyp != RezeptTyp.Vegetarisches)  {
				typ = zutatTyp;

				//Debug Print
				//MainWin.StringOutln(" # RezeptTyp: "+zutatTyp);

				break;
			}
		}

	}

	/**
	 * Gibt den Typ des Rezeptes wieder zurück
	 * 
	 * @return Der Typ des Rezeptes
	 */
	public RezeptTyp getTyp(){
		return typ;
	}
}
