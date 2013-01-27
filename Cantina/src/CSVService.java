import java.util.ArrayList;
import java.util.Iterator;
/**
 * Die Klasse CSVService stellt Methoden zur Transformation von "comma-separated values"-Strings bereit
 * @author Rene Wiederhold
 * @version 1.00
 */
public class CSVService {
	/**
	 * getFields wandelt einen String (eine Zeile einer .csv-Datei in eine ArrayList um, die die einzelnen
	 * Werte als Felder enthält.
	 * 
	 * @param csv Der Eingabestring (Zeile aus einer .csv-Datei)
	 * @return Eine ArrayList, die die einzelnen Felder als String enthält.
	 */
	public static ArrayList<String> getFields(String csv){

		ArrayList<String> fields = new ArrayList<String>();
		boolean fieldStarted = false;
		StringBuffer tmpBuffer = new StringBuffer();
		for (int i = 0; i < csv.length(); i++) {
			char ch = csv.charAt(i);
			if (ch == '\"'){
				fieldStarted = !fieldStarted;
			} else if ((ch == ',' && !fieldStarted)) {
				fields.add((tmpBuffer.toString()).replaceAll("\"",""));
				tmpBuffer = new StringBuffer();
			} else {
				tmpBuffer.append(ch);
			}
		}
		fields.add((tmpBuffer.toString().replaceAll("\"","")));
		return fields;
	}
	/**
	 * getString wandelt eine ArrayList, welche Strings enthält, in eine CSV-Zeile (Ausgabe-String) um.
	 * 
	 * @param arr Eine ArrayList, die String-Objekte enthält.
	 * @return Die CSV-Zeile
	 */
	public static String getString(ArrayList<String> arr){
		String csv=new String();
		Iterator<String> it = arr.iterator();
		while (it.hasNext()){
			String tmp=it.next();

			csv=csv+"\""+tmp+"\",";
		}
		return csv;
	}
}
