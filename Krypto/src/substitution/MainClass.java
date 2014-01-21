package substitution;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
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
		String input = null;
		try {
			input = readInput(args[0]);
		} catch (IndexOutOfBoundsException e) {
			System.err.println("Filename fehlt");
			System.exit(-1);
		}

		String base64Encoded = Base64.encode(input.toUpperCase().getBytes());

		Map<Character, Integer> map = getMap(new String(base64Encoded)
				.toUpperCase());
		saveMapToFile(map, "haeufigkeitsanalyse_unedited");

		// Aufgabe 1.1.
		map = getMap(getEditedInput(base64Encoded.toUpperCase()));
		saveMapToFile(map, "haeufigkeitsanalyse_edited");

		// Aufgabe 1.2.
		map = getMap(getZippedInput(base64Encoded.toUpperCase()).toUpperCase());
		saveMapToFile(map, "haeufigkeitsanalyse_zipped");

		// Aufgabe 1.3.
		map = getMap(getCBC(input));
		saveMapToFile(map, "haeufigkeitsanalyse_cbc");

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
		gzip.write(input.getBytes());
		gzip.close();
		return Base64.encode(out.toByteArray());
	}

	public static String getCBC(String input) {
		int BLOCKSIZE = 5;

		int toAdd = 0;
		if (input.length() % BLOCKSIZE != 0)
			toAdd = BLOCKSIZE - input.length() % BLOCKSIZE;

		for (int i = 0; i < toAdd; i++) {
			input += " ";
		}

		byte[] ca = input.getBytes(StandardCharsets.US_ASCII);
		byte[] ret = new byte[ca.length];

		byte[] iv = new byte[BLOCKSIZE];

		for (int i = 0; i < iv.length; i++) {
			iv[i] = (byte) Math.round(Math.random() * 255 - 128);
		}

		Substitution subs = new Substitution();

		for (int i = 0; i < ca.length / 5; i++) {
			byte[] temp = new byte[5];
			for (int j = 0; j < 5; j++) {
				temp[j] = (byte) (ca[5 * i + j] ^ iv[j]);
			}

			iv = subs.substitude(temp);

			for (int j = 0; j < 5; j++) {
				ret[5 * i + j] = iv[j];
			}
		}

		return Base64.encode(ret);
	}

	public static String readInput(String fileName) {
		File toOpen = new File(fileName);

		BufferedReader br;

		String input = "";

		try {
			br = new BufferedReader(new FileReader(toOpen));

			String temp;

			while ((temp = br.readLine()) != null) {
				input += temp;
			}

			br.close();
		} catch (FileNotFoundException e) {
			System.err.println("File not found...");
			System.exit(-1);
		} catch (IOException e) {
			e.printStackTrace();
		}

		return input;
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
