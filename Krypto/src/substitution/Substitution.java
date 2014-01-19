package substitution;

import java.util.HashMap;
import java.util.Map;

public class Substitution {

	// Map<Character, Character> map;
	Map<Byte, Byte> map;

	public Substitution() {
		// map = new HashMap<Character, Character>();
		//
		// for (int i = 0; i <= 65535; i++) {
		// map.put((char) i, (char) (65535 - i));
		// }

		map = new HashMap<Byte, Byte>();
		for (int i = -128; i <= 127; i++) {
			map.put(new Byte((byte) i), new Byte((byte) (0 - i - 1)));
		}
	}

	public byte[] substitude(byte[] input) {
		byte[] ret = new byte[input.length];

		for (int i = 0; i < input.length; i++) {
			ret[i] = map.get(input[i]);
		}

		return ret;
	}

}
