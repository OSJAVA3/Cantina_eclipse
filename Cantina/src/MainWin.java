import javax.swing.*;
/**
 * Die Klasse MainWin gibt die Ausgabe von Fehlermeldung beim Programmablauf aus.
 * Durch Bereitstellung einer Konsole wird auch Auskuft darüber erteilt, 
 * ob das einlesen der Dateien sowie die Ausgabe der Dateien fehlerfrei verlaufen ist.
 * 
 * Die Instanz der Klasse wird durch die Kantinenplanung erstellt.
 * Mit dieser steht sie in Assoziation.
 * 
 * @author  Lukas Krotki
 * @version 1.0
 */
public class MainWin extends JFrame {
	// Seriennummer für eine Serialisierung 
	private static final long serialVersionUID = 1L;
    // Erzeugung einer JTextArea
    static JTextArea ausgabe = new JTextArea("Cantina startet..."+"\n");

    /**
     * Der Konstruktor des MainWindow
     */
	public MainWin(){
		super("Cantina JAVA");                          	   // Text oben in der Leiste        
            
        ausgabe.setEditable(false);							   // Ausgabetext nicht änderbar
    	JScrollPane scrollText = new JScrollPane (ausgabe);    // Text soll scrollbar sein
    	scrollText.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
    	add(scrollText);

    	setSize(700,600);                                  // Fenster Größe
        setLocation(300,300);                              // Wo sich das Fenster beim starten befinden soll                     
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);    // Schließe Fenster und Programm beim beenden 
        setVisible(true);
	}
	/**
	 * Textausgabe im MainWindow ohne Zeilenumbruch.
	 *
	 * @param ausgabeText Text der ausgegeben werden soll
	 */
	public static void StringOut(String ausgabeText){
		ausgabe.append(ausgabeText);
	}
	/**
	 * Textausgabe im MainWindow mit Zeilenumbruch.
	 *
	 * @param ausgabeText Text der ausgegeben werden soll
	 */
	public static void StringOutln(String ausgabeText){
		ausgabe.append(ausgabeText+"\n");
	}
		
		
}