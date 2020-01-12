package it.uniroma1.lcl.babelarity;

import java.util.HashMap;
import java.util.Map;
/**
 * Definisce i vari part-of-speech possibili per un Synset
 * @author gianpcrx
 * @since 1.0
 * @version 1.0
 */
public enum POS {
	NOUN, ADV, ADJ, VERB;
	private static Map<Character, POS> pos;
	static {
		pos = new HashMap<Character, POS>();
		pos.put('n', NOUN);
		pos.put('v', VERB);
		pos.put('r', ADV);
		pos.put('a', ADJ);
	}
	/**
	 * 
	 * @param c Identificatore del part-of-speech da ottenere
	 * @return Il part-of-speech corrispondente a c
	 */
	static POS getPos(char c) {
		return pos.get(c);
	}
}
