import javax.swing.*;

public class MainWin extends JFrame {
	private static final long serialVersionUID = 1L;

    static JTextArea ausgabe = new JTextArea("Kantina startet..."+"\n");

    
		public MainWin(){
            super("Cantina JAVA");                          	   // Text oben in der Leiste        
            
    	    ausgabe.setEditable(false);
    		JScrollPane scrollText = new JScrollPane (ausgabe);
    		scrollText.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
    		add(scrollText);

    		setSize(700,500);                                  // Fenster Größe
            setLocation(300,300);                              // Wo sich das Fenster beim starten befinden soll                     
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);    // Schließe Fenster und Programm beim beenden 
			setVisible(true);
		}
		
		public static void StringOut(String ausgabeText){
			ausgabe.append(ausgabeText);
		}

		public static void StringOutln(String ausgabeText){
			ausgabe.append(ausgabeText+"\n");
		}
		
		
}