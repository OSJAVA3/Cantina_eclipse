import java.io.*;

/**
 * Die Klasse Datei soll in einfacherer Weise Strings als Zeilen ausgeben,
 * bzw einlesen koennen.
 * 
 * Beachten Sie, dass es fuer einige Aufgaben mehrere Methoden gibt, die sich
 * darin unterscheiden ob mit Java-Ausnahmen gearbeitet wird oder nicht. Dies
 * soll Sie in die Lage versetzen, die Datei-Klasse benutzen zu können, auch
 * wenn sie sich noch nicht mit dem Ausnahmekonzept auseinandergesetzt haben.
 * Hier wird dann stattdessen nur mit Fehlercodes gearbeitet.
 * 
 * Selbstverstaendlich duerfen Sie diese Klasse in ihrem Projekt nicht nur
 * benutzen, sondern auch nach Belieben veraendern!
 * 
 * 25.01.2013 Die Ausgabe der Fehlermeldungen erfolgt nun über die Methode MainWin.StringOutln
 * Dies ist für die Ausführung von außerhalb implementiert worden. 
 * 
 * @author  Patrick Veith et al., Lukas Krotki
 * @version 2.11 on 25.01.2013
 */
public class Datei
{
	// Attribute der Klasse Datei
	// Name der Datei
	// - Der Name der Datei darf Pfadinformationen enthalten, die aber 
	// nicht mit den Windows ueblichen Backslash angegeben werden duerfen,
	// sondern mit dem normalen Slash. (bsp. C:/temp/datei.txt)
	private String dName;
	// Objekt einer Java-Klasse zum Schreiben von Zeichenketten
	private PrintWriter dAus;
	// Objekt einer Java-Klasse zum Lesen von Zeichenketten
	private BufferedReader dEin;
	/**
	 * Enthaelt nach dem Aufruf einer Methode einen Fehlercode.
	 * 0 bedeutet, dass kein Fehler aufgetreten ist und ueber die 
	 * Methode errorMessage kann man eine Beschreibung eines 
	 * Fehlercodes erfragen.
	 */
	public int errorCode;
	// Enthaelt, wenn eine Eingabedatei ihr Ende erreicht true,
	// ansonsten enthaelt eof immer false.
	// Um dies sicherzustellen kann nicht von ausserhalb auf
	// dieses Attribut zugegriffen werden, sondern ein Zugriff
	// muss ueber die Methode eof erfolgen.
	private boolean eof;

	/**
	 * Konstruktor fuer Objekte der Klasse Datei
	 * Legt einen String mit dem Namen der zu bearbeitenden Datei an.
	 * @param in_name (String): Dateiname der benutzt werden soll.
	 */
	public Datei(String in_name)
	{   
		dName = new String(in_name);
		errorCode =0;
		eof=false;
	}

	/**
	 * Ueberprueft, ob das Ende einer Eingabedatei erreicht wurde.
	 *
	 * @return gibt true nur zurueck, wenn das Ende einer Eingabedatei 
	 *          erreicht wurde. Ansosnsten wird immer false zurueckgegeben.
	 */
	public boolean eof()
	{   
		return eof;
	}

	/**
	 * Oeffnet eine Ausgabedatei namens dName.
	 * 
	 * @return (int) - Fehlernummer, oder 0 fuer OK!
	 */
	public void openOutFile() throws IOException
	{
		eof=false;
		errorCode =0;
		dAus = new PrintWriter(new BufferedWriter(new FileWriter(dName)));
	}

	/**
	 * Oeffnet eine Ausgabedatei namens dName. (Variante mit Fehlercodes!)
	 * 
	 */
	public int openOutFile_FS()
	{
		eof=false;
		try {
			dAus = new PrintWriter(new BufferedWriter(new FileWriter(dName)));
			errorCode =0;
			return 0;
		}
		catch(Exception e)
		{
			MainWin.StringOutln(e.getMessage());
			errorCode =1;
			return 1;
		}
	}

	/**
	 * Schreibt einen String als Zeile in eine Datei.
	 * 
	 * @param  in_str - (String)Zeichenkette die in die Datei geschrieben 
	 *                  werden soll.
	 */
	public void writeLine(String in_str)
	{
		eof=false;
		errorCode =0;
		dAus.write(in_str+"\n");

	}

	/**
	 * Schreibt einen String als Zeile in eine Datei. (Variante mit Fehlercodes!)
	 * 
	 * @param  in_str - (String)Zeichenkette die in die Datei geschrieben 
	 *                  werden soll.
	 * @return (int) - Fehlernummer oder 0 fuer OK!
	 */
	public int writeLine_FS(String in_str)
	{
		eof=false;
		try {
			dAus.write(in_str+"\n");
			errorCode =0;
			return 0;
		}
		catch(Exception e)
		{
			MainWin.StringOutln(e.getMessage());
			errorCode =2;
			return 2;
		}
	}

	/**
	 * Schliesst eine Ausgabedatei.
	 * 
	 */
	public void closeOutFile()
	{
		errorCode =0;
		dAus.close();
	}

	/**
	 * Schliesst eine Ausgabedatei.  (Variante mit Fehlercodes!)
	 * 
	 * @return int - Fehlernummer oder 0 fuer OK
	 */
	public int closeOutFile_FS()
	{
		try {
			dAus.close();
			errorCode =0;
			return 0;
		}
		catch(Exception e)
		{
			MainWin.StringOutln(e.getMessage());
			errorCode =3;
			return 3;
		}
	}

	/**
	 * Oeffnet eine Eingabedatei namens dName.
	 * 
	 */
	public void openInFile() throws IOException
	{
		errorCode =0;
		eof = false;
		dEin = new BufferedReader(new FileReader(dName));
	}

	/**
	 * Oeffnet eine Eingabedatei namens dName. (Variante mit Fehlercodes!)
	 * 
	 * @return (int) - Fehlernummer, oder 0 fuer OK!
	 */
	public int openInFile_FS()
	{
		try {
			dEin = new BufferedReader(new FileReader(dName));
			errorCode =0;
			eof = false;
			return 0;
		}
		catch(Exception e)
		{
			MainWin.StringOutln(e.getMessage());
			errorCode =4;
			return 4;
		}
	}

	/**
	 * Liest eine Zeile aus einer Eingabedatei.
	 * 
	 * @return String - ausgelesene Zeile.
	 */
	public String readLine() throws IOException
	{
		String zw_in = dEin.readLine();
		if (zw_in == null){
			eof=true;
			return zw_in;
		}
		else{
			errorCode =0;
			return zw_in;
		}
	}


	/**
	 * Liest eine Zeile aus einer Eingabedatei. (Variante mit Fehlercodes!)
	 * Hier bitte darauf achten, das die Methode keinen Fehlercode zurueck gibt, 
	 * sondern -falls ein Fehler passiert) diesen nur ins Attribut errorCode schreibt.
	 * 
	 * @return String - ausgelesene Zeile.
	 */
	public String readLine_FS()
	{
		String zw_in;
		try {
			zw_in = dEin.readLine();
			if (zw_in == null){
				eof=true;
				return zw_in;
			}
			else{
				errorCode =0;
				return zw_in;
			}
		}
		catch(Exception e)
		{
			MainWin.StringOutln(e.getMessage());
			errorCode =5;
			return "";
		}
	}

	/**
	 * Schliesst eine Eingabedatei.
	 * 
	 */
	public void closeInFile()  throws IOException
	{
		errorCode =0;
		eof = false;
		dEin.close();
	}

	/**
	 * Schliesst eine Eingabedatei. (Variante mit Fehlercodes!)
	 * 
	 * @return int - Fehlernummer oder 0 fuer OK
	 */
	public int closeInFile_FS()
	{
		try {
			dEin.close();
			errorCode =0;
			eof = false;
			return 0;
		}
		catch(Exception e)
		{
			MainWin.StringOutln(e.getMessage());
			errorCode =6;
			return 6;
		}
	}

	/**
	 * Loescht eine Datei.
	 * 
	 */
	public int deleteFile() throws IOException
	{
		errorCode =0;
		File f = new File(dName);
		if (f.isFile()){
			f.delete();
		}
		else{
			errorCode=98;
		}
		return errorCode;

	}

	/**
	 * Loescht eine Datei. (Variante mit Fehlercodes!)
	 * 
	 * @return int - Fehlernummer oder 0 fuer OK
	 */
	public int deleteFile_FS()
	{
		try {
			File f = new File(dName);
			errorCode =0;
			if (f.isFile()){
				f.delete();
				return 0;  
			}
			else{
				errorCode=98;
				return 98;
			}
		}
		catch(Exception e)
		{
			MainWin.StringOutln(e.getMessage());
			errorCode =7;
			return 7;
		}
	}

	/**
	 * Gibt den aktuellen Zustand des Dateiobjektes zurueck. "true" bedeutet es sind
	 * keine Fehler aufgetreten, "false" bedeutet es sind Fehler aufgetreten, die 
	 * ueber den Fehlercode in errorCode genauer zu identifizieren sind. 
	 * Diese Methode ist -mit einer Ausnahme- nur bei Nutzung des integrierten 
	 * Exceptionhandlings (<Methodenname>_FS) sinnvoll nutzbar. Allein die Methode 
	 * "deleteFile()" schreibt einen Errorcode (fuer den Fall das die zu loeschende 
	 * Datei keine Datei ist).
	 * 
	 * @return boolean - Zustand des Dateiobjektes.
	 */
	public boolean state()
	{
		if (errorCode == 0)
			return true;
		else
			return false;
	}    

	/**
	 * Gibt einen Fehlerbeschreibung als Text aus.
	 * 
	 * @param  in_error - (int)Fehlernummer, die bei einem Methodenaufruf zurueck
	 *                    gegeben wurde
	 * @return String - Klartextbeschreibung des Fehlers!
	 */
	public String errorMessage(int in_error)
	{
		// Auswertung des uebergebenen int-Wertes
		switch (in_error){
		case 0: return "Ok!";
		case 1: return "(E) oeffnen des Ausgabefiles gescheitert.";
		case 2: return "(E) Schreiben einer Zeile gescheitert.";
		case 3: return "(E) Schliessen des Ausgabefiles gescheitert.";
		case 4: return "(E) oeffnen des Eingabefiles gescheitert.";
		case 5: return "(E) Lesen einer Zeile gescheitert.";
		case 6: return "(E) Schliessen des Eingabefiles gescheitert.";
		case 7: return "(E) Konnte Datei nicht loeschen.";
		case 8: return "(E) Konnte keine Datei waehlen.";
		case 98: return "(W) Es koennen nur Dateien geloescht werden.";
		default: return "(E) Nicht bekannter Fehler!";
		}
	}
}