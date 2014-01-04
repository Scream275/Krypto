package substitution;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.GZIPOutputStream;

import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;

public class MainClass {

	public static void main(String[] args) throws IOException {

		// Zu analysierender Input-String
		String input = "Text: Text (von lateinisch texere: weben/flechten) bezeichnet im "
				+ "nichtwissenschaftlichen Sprachgebrauch eine abgegrenzte, zusammenhaengende, "
				+ "meist schriftliche sprachliche Aeußerung, im weiteren Sinne auch nicht "
				+ "geschriebene, aber schreibbare Sprachinformation (beispielsweise eines "
				+ "Liedes, Films oder einer improvisierten Theaterauffuehrung).";
		
		Map<Character, Integer> map = getMap(input.toUpperCase());
		saveMapToFile(map, "haeufigkeitsanalyse_unedited");

		// Aufgabe 1.1.
		map = getMap(getEditedInput(input.toUpperCase()));
		saveMapToFile(map, "haeufigkeitsanalyse_edited");

		// Aufgabe 1.2.
		map = getMap(getZippedInput(input.toUpperCase()));
		saveMapToFile(map, "haeufigkeitsanalyse_zipped");

	}

	// Liefert anhand eines input-Strings einen String zurück, der alle
	// e's zyklisch durch x,y und q ersetzt und alle x,y und q's durch e ersetzt
	public static String getEditedInput(String input) {
		String output = "";
		int count = 1;
		for (Character c : input.toCharArray()) {
			if (c == 'E') {
				if (count % 3 == 1) {
					output = output.concat("Y");
				} else if (count % 3 == 2) {
					output = output.concat("X");
				} else {
					output = output.concat("Q");
				}
				count++;
			} else if (c == 'Y' || c == 'X' || c == 'Q')
				output = output.concat("E");
			else
				output = output.concat(c.toString());
		}
		return output;
	}

	public static String getZippedInput(String input) throws IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		GZIPOutputStream gzip = new GZIPOutputStream(out);
		gzip.write(input.getBytes("UTF-8"));
		gzip.close();
		return Base64.encode(out.toByteArray());
	}

	// Schreibt den Inhalt der map in die Datei mit dem Namen fileName
	public static void saveMapToFile(Map<Character, Integer> map,
			String fileName) {

		String help = "";
		Path target = Paths.get("./" + fileName + "_"
				+ System.currentTimeMillis() + ".txt");
		for (char c : map.keySet())
			help = help.concat(c + ";" + map.get(c) + "\n");
		try {
			Path file = Files.createFile(target);
			Files.write(file, help.getBytes(), StandardOpenOption.WRITE);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// Zählt alle Buchstaben-Vorkommen eines input-Strings und speichert
	// diesen in einer Map
	public static Map<Character, Integer> getMap(String input) {

		Map<Character, Integer> map = new HashMap<Character, Integer>();
		String help = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		for (char c : help.toCharArray()) {
			if (!map.containsKey(c))
				map.put(c, 0);
		}
		for (char c : input.toCharArray()) {
			if (!map.containsKey(c))
				map.put(c, 1);
			else
				map.put(c, map.get(c) + 1);
		}
		return map;
	}
}
