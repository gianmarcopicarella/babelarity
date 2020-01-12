package it.uniroma1.lcl.babelarity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
/**
 * Implementazione di LinkableObject e LinguisticObject rappresentante un concetto in una rete semantica
 * @author gianpcrx
 * @since 1.0
 * @version 1.0
 */
public abstract class Synset implements LinguisticObject, LinkableObject<Synset> {
	private POS partOfSpeech;
	private String id;
	protected Set<String> lemmas;
	protected List<String> glosses;
	/**
	 * Inizializza il Synset
	 * @param id l'identificativo del Synset 
	 */
	public Synset(String id) { 
		this.id = id; 
		this.partOfSpeech = POS.getPos(id.charAt(id.length() - 1));
		this.lemmas = new HashSet<String>();
		this.glosses = new ArrayList<String>();
	}
	/**
	 * Restituisce l'id del Synset
	 * @return Una Stringa rappresentante l'id
	 */
	public String getId() { return this.id; }
	/**
	 * Restituisce il part-of-speech del Synset
	 * @return Un POS rappresentante il part-of-speech
	 */
	public POS getPOS() { return this.partOfSpeech; }
	/**
	 * Restituisce i lemmi contenuti nel synset
	 * @return Un Set di Stringhe rappresentanti i vari lemmi
	 */
	public Set<String> getLemmas() { return this.lemmas; }
	/**
	 * Restituisce le glosse contenute nel synset
	 * @return Una Lista di Stringhe rappresentante le varie glosse
	 */
	public List<String> getGlosses() { return this.glosses; }
	/**
	 * Aggiunge una lista di lemmi al Synset
	 * @param lemmas Lemmi da aggiungere
	 */
	abstract void addLemmas(List<String> lemmas);
	/**
	 * Aggiunge una lista di glosse al Synset
	 * @param glosses Glosse da aggiungere
	 */
	abstract void addGlosses(List<String> glosses);
	/**
	 * Ottiene un Set di Synset con distanza minore o uguale a quella specificata
	 * @param root Nodo attuale
	 * @param visited Nodi visitati
	 * @param distance Distanza di ricerca
	 * @return Un Set di Synset con distanza minore o uguale a quella specificata
	 */
	protected abstract Set<Synset> getLinkedRecursive(Synset root, Set<Synset> visited, int distance);
	
	@Override
	public boolean equals(Object o) {
		if(this == o) return true;
		if(o == null || o.getClass() != this.getClass()) return false;
		Synset s = (Synset)o;
		return this.partOfSpeech.equals(s.partOfSpeech) && this.id.equals(s.id) && this.lemmas.equals(s.lemmas);
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() { return Objects.hash(id, partOfSpeech); }
	/**
	 * {@inheritDoc}
	 */
	public Set<Synset> getLinked(int distance){ 
		return getLinkedRecursive(this, new HashSet<Synset>(), distance);
	}
}
