
/**
 * Die Klasse stellt Methoden zum Export von Kantinenplänen und Einkaufslisten bereit
 * 
 * Der Exporter steht in Assoziation zu den Klassen Kantinenplan und Einkaufliste, die er exportieren soll.
 * Der Export erfolgt im Planungslauf durch die Kantinenplanung.
 * 
 * Nicht genutzt, kann gelöscht werden
 * 
 * @author Lukas Krotki
 * @version 
 */
public class Exporter
{

	/**
	 * Konstruktor für den Exporter
	 */
	public Exporter()
	{
	}

	/**
	 * Exportiert einen Kantinenplan
	 * 
	 * @param  kantpl Ein Kantinenplan-Objekt
	 * @return     True, falls Export erfolgreich, false falls nicht. 
	 */
	public boolean expKantinenplanFile(Kantinenplan kantpl)
	{
		return true;
	}

	/**
	 * Exportiert eine Einkaufsliste
	 * 
	 * @param  einklist Ein Einkaufslisten-Objekt
	 * @return     True, falls Export erfolgreich, false falls nicht. 
	 */
	public boolean expEinkaufslisteFile(Einkaufsliste einklist)
	{
		return true;
	}
}
