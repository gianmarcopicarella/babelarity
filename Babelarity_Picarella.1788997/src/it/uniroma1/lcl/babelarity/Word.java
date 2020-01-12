package it.uniroma1.lcl.babelarity;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import it.uniroma1.lcl.babelarity.utils.Utils;

/**
 * Implementazione di LinguisticObject utilizzata come wrapper di stringhe
 * @author gianpcrx
 * @since 1.0
 * @version 1.0
 */
public class Word implements LinguisticObject {
	/**
	 * StopWords utilizzate per filtrare un testo
	 */
	public static Set<String> stopWords;
	private static Map<String, Word> instances;
	private static Path stopWordsPath = Paths.get("english-stop-words-large.txt");
	
	static {
		instances = new HashMap<String, Word>();
		stopWords = new HashSet<String>();
		Utils.forEachFileLine(stopWords::add, stopWordsPath.toFile());
	}
	/**
	 * Restituisce un oggetto Word rappresentante la stringa presa in input
	 * @param s La stringa da utilizzare nella creazione della Word
	 * @return La Word rappresentante la stringa presa in input
	 */
	public static Word fromString(String s) {
		if(!instances.containsKey(s)) instances.put(s, new Word(s));
		return instances.get(s);
	}
	
	private String s;
	private Word(String s) {
		this.s = s;
	}
	/**
	 * Restituisce i lemmi corrispondenti alla Word
	 * @return Restituisce i lemmi corrispondenti alla Word
	 */
	public List<String> getLemmas() {
		List<String> lemmas = MiniBabelNet.getInstance().getLemmas(s);
		if(lemmas != null) return lemmas;
		else if(MiniBabelNet.getInstance().getLemmas().contains(s)) return List.of(s);
		return null;
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return this.s;
	}
}
