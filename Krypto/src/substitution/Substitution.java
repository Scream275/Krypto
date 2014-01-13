package substitution;

import java.util.HashMap;
import java.util.Map;

public class Substitution {

	Map<Character, Character> map;

	public Substitution() {
		map = new HashMap<Character, Character>();

		for (int i = 0; i <= 65535; i++) {
			map.put((char) i, (char) (65535 - i));
		}
	}

	public String substitude(String input) {
		String ret = "";

		char[] temp = input.toCharArray();

		for (char c : temp) {
			ret += map.get(c);
		}

		return ret;
	}

	public char[] substitude(char[] input) {
		char[] ret = new char[input.length];

		for (int i = 0; i < input.length; i++) {
			ret[i] = map.get(input[i]);
		}

		return ret;
	}

}
