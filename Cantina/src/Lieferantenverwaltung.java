import java.util.Vector;
import java.io.*;

/**
 * Die Klasse Lieferantenverwaltung erstellt und verwaltet die Objekte der Klassen Lieferant, Artikel und Lebensmittel und aggregiert diese. 
 * Sie stellt Methoden zur Verf�gung, um die g�nstigsten Artikel der Lieferanten zu berechnen, zu �berpr�fen, ob ausreichende Mengen
 * an Zutaten beschafft werden k�nnen und ob Tagesgerichte als Fleisch-, Fisch- oder vegetarische Gerichte beschrieben werden k�nnen.
 * 
 * Die Rezeptverwaltung ist mit dieser Klasse assoziiert, da die Rezeptverwaltung Methoden der Lieferantenverwaltung zur Erstellung 
 * ihrer eigenen Datenschicht (Rezept, Zutat) verwendet.
 * Die Einkaufsliste verwendet die Lieferantenverwaltung um Artikelobjekte zu erhalten.
 * F�r die Planungsdurchf�hrung steht sie in Assoziation zu der Kantinenplanung, da die Kantinenplanung die Lieferantenverwaltung konstruiert
 * und die Erzeugung der Datenschicht anst��t. 
 * 
 * @author Rene Wiederhold 
 * @version 0.01
 */
public class Lieferantenverwaltung{ 
    private Vector<Artikel> artikelVector;
    private Vector<Lieferant> lieferantVector;
    private Vector<Lebensmittel> lebensmittelVector;
    
    /**
     *  Der Konstruktor der Lieferantenverwaltung
     */
    public Lieferantenverwaltung (){
        
    }

    /**
     * Die Methode pr�ft, von welchem Typ eine Zutat ist (Fleisch, Fisch, Veggie). Dies wird von der Rezeptverwaltung zur Erstellung 
     * der Rezepte verwendet.
     * 
     * @param  zutat Die zu pr�fende Zutat
     * @return  Einen standardisierten String, der die Typ-Bezeichnung enth�lt.
     */
    public String holeTyp(Zutat zutat)
    {
        // tragen Sie hier den Code ein
        String typ = "FLEISCH";
       return typ;
    }

    /**
     * Die Methode pr�ft, ob die ben�tigten Mengen Lebensmittel am Markt vorhanden sind, um ein Tagesgericht in der Planungs-
     * periode anbieten zu k�nnen. Beim Aufruf wird die ben�tigte Menge vom zugeh�rigen Lebensmittel-Objekt abgezogen, sofern
     * ausreichend. Dies wird vom Kantinenplan verwendet.
     * 
     * @param   Tagesgericht Das zu �berpr�fende Tagesgericht
     * @return  True, falls ausreichend Lebensmittel beschafft werden k�nnen, ansonsten False
     */
    
    public boolean lebensmittelVerfuegbar(Tagesgericht tagesgericht)
    {
        return true;
    }

    /**
     * Die Methode dient zum Aufbau der Lieferantenverwaltung. Sie erzeugt aus den Eingabe-Dateien die Bauernhof-, Grosshandel-
     * und Artikelobjekte sowie die Lebensmittel-Objekte. 
     * 
     * 
     * @param  String Der Pfad zum Ordner, der die Lieferantenpreislisten enth�lt.
     * @return      True, falls die Lieferantendateien eingelesen werden konnten, False, falls Probleme aufgetreten sind.
     */
    public boolean liesLieferantenDateien(String lieferantenOrdner)
    {
    	File folder = new File(lieferantenOrdner);
    	//Debug-Print
    	System.out.println("Angegebener Lieferantenordner ist ein Ordner: "+folder.isDirectory());
        return true;
    }
    
    /**
     * Die Methode gibt einen Vector zur�ck, der alle Artikel enth�lt, die den gleichen Namen haben, wie der �bergebene
     * String-Parameter. Dies wird zur Erzeugung der BestellPos-Objekte von der Einkaufsliste genutzt.
     *
     * @param  name Die Bezeichnung einer Zutat
     * @return     Einen Vector, der die Referenzen zu allen Artikel-Objekte enth�lt, deren Name mit dem Parameter 
     *             �bereinstimmen
     */
    public Vector<Artikel> gibAlleArtikel(String name)
    {
        
        return new Vector<Artikel>();
    }

}
