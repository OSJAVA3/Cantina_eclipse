import java.io.*; // Aufgrund des Einlesens der Datei
import java.util.ArrayList;
/**
 * Die Rezeptverwaltung erstellt aus der Rezeptdatei die Objektabbildungen der Rezepte und 
 * verwaltet diese. Sie stellt Methoden bereit um Referenzen auf bestimmte Rezept-Objekte 
 * zu �bergeben. Au�erdem kann sie auch die Hitliste einlesen und die Positionen den einzelnen Rezepten zuweisen.
 * 
 * Sie steht in Assoziation zur Verwaltungsklasse Lieferantenverwaltung, der Kantinenplanung sowie dem Kantinenplan.
 * Sie aggregiert die Objekte Rezept und Zutat.
 * 
 * @author Lukas Krotki 
 * @version 
 */
public class Rezeptverwaltung
{
    /** Die ArrayList Rezeptliste enth�lt die Referenz zu allen erzeugten Rezeptobjekten, ist also eine Rezeptliste*/
    private ArrayList<Rezept> rezeptListe;    
    /** Die Lieferantenverwaltung wird der Rezeptverwaltung �bergeben und wird als Objekt �bergeben*/
    private Lieferantenverwaltung lieferantenverw;
    /** Angabe des Rezeptpfades */
    private String rezeptPfad;
    /** Angabe des Hitlistenpfades */
    private String hitlistenPfad;

    /** Bereits bei der Konstruktion muss eine Referenz zur Lieferantenverwaltung �bergeben werden, da diese zur Typpr�fung ben�tigt wird.
     * 
     * @param lieferantenverw Die zur Typpr�fung zu verwendende Lieferantenverwaltung
     */
    public Rezeptverwaltung() //parameter entfernt zum testen (Lieferantenverwaltung lieferantenverw)
    {
     
    }

    /**
     * Die Methode liest die Rezeptdatei ein, erstellt Rezeptobjekte und Zutatenobjekte daraus und weist die Zutaten den Rezepten zu. Dann werden die Rezepte in den RezeptArrayList gelegt.
     * 
     * @param   rezeptpfad Den Pfad zur Rezeptdatei.
     * @return  Gibt True zur�ck, wenn die Rezepte vollst�ndig eingelesen wurden. Gibt False zur�ck, falls Fehler aufgetreten sind.   
     */
    public boolean liesRezepte(String rezeptpfad) 
    {
    	//Datei �ffnen
		Datei inFile = new Datei(rezeptpfad);
		inFile.openInFile_FS();
		int zeilennummer = 0;
        
		// Abfrage, ob das Oeffen funktioniert hat
        if (!inFile.state()){
            // Ausgabe des Fehlers im Terminalfenster
            System.out.println("Fehler beim �ffnen der Eingabedatei "+rezeptPfad);
            // Abbrechen der Methode
            return false;
        }

        //Datei-Schleife
        while (!inFile.eof()){
        	
        	//Zeilenz�hler erh�hen
        	zeilennummer = zeilennummer++; 
        	// Zeile einer Datei einlesen
        	String zeile = inFile.readLine_FS();
        	//Debug-Print
        	//System.out.println(zeile);
        	
   	
        	//Wenn aktuelle Zeile einen NullPointer enth�lt, wird gebrochen. 
        	if (!(zeile==null)){
        		
        		//Der CSVService macht aus den Eingabe-String (Zeile aus Datei) eine ArrayList, die die einzelnen Werte getrennt enth�lt
				ArrayList<String> fields = Kantinenplanung.CSVService.getFields(zeile);
        		
				//Debug-Print
				System.out.println("Zeile: "+zeilennummer+" Wert1: "+fields.get(0)+" Wert2: "+fields.get(1)+" Wert3: "+fields.get(2)+" Wert4: "+fields.get(3));
				/*
        		// Zeilenschleife zum einlesen jeder Zeile der Datei
        		for (int k = 0; k < zeile.length(); k++) {
        			// gibt die Zeichen an der Indexstelle k
        			char ch = zeile.charAt(k);
        			// sobald die Zeichkette ein Zeilenende erreicht, setzte Feldstart negiert (true)
        			if (ch == '\"') {
        				feldStart = !feldStart;
        			}
        			// ansonsten Trenne die Strings, wenn Felstart negiert und "," vorhanden
        			else if ( (ch == ',' && !feldStart) ) {
        				//Debug-Print
            	    	//System.out.println(k);
        				zeile = zeile.replaceAll("\"", ""); //entfernt die Anf�hrungszeichen aus den String
            	    	//System.out.println(zeile.substring(0,k));
        				

        			}
        			
        		}
        		*/
        	}
        }
        return true;
        }
       
        
        
      
        		
    

       	   
    
    
    /**
     * Die Methode liest die Hitlistendatei ein und weist den im RezeptArrayList enthaltenen Rezeptobjekten ihre Hitlistenposition zu.
     * 
     * @param hitlistenpfad Den Pfad zur Hitlistendatei  
     * @return  True f�r vollst�ndige Zuweisung, False f�r unvollst�ndig    
     */
    public boolean liesHitliste(String hitlistenPfad) //
    {
        return true;
    }
    
    /**
     * Die Methode gibt ein zuf�lliges Fischrezept aus dem RezeptArrayList zur�ck.
     * 
     * @return Ein zuf�lliges Fischrezept 
     */
    public Rezept gibFisch() 
    {
        Rezept x = new Rezept("a"); 
        return x;
    }
   
    /**
     * Die Methode gibt ein zuf�lliges Fleischrezept aus dem RezeptArrayList zur�ck.
     * 
     * @return       Ein zuf�lliges Fleischrezept
     */
    public Rezept gibFleisch() 
    {
        Rezept x=new Rezept("a");
        return x;
    }
    
    /**
     * Die Methode gibt ein zuf�lliges vegetarisches Rezept aus dem RezeptArrayList zur�ck.
     * 
     * @return       Ein zuf�lliges vegetetarisches Rezept 
     */
    public Rezept gibVeggie() 
    {
        Rezept x=new Rezept("a");
        return x;
    }
    
    /**
     * Die Methode gibt ein zuf�lliges Rezept aus dem RezeptArrayList zur�ck.
     * 
     * @return        Ein zuf�lliges Rezept 
     */
    public Rezept gibRandom() 
    {
        Rezept x=new Rezept("a");
        return x;
    }
}
