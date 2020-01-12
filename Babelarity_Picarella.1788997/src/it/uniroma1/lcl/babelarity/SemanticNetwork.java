package it.uniroma1.lcl.babelarity;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
/**
 * Definisce i metodi e l'implementazione base di una rete semantica
 * @author gianpcrx
 * @since 1.0
 * @version 1.0
 */
public abstract class SemanticNetwork {
	protected SimilarityObject<Word> lexical;
	protected SimilarityObject<Synset> semantic;
	protected SimilarityObject<Document> document;
	/**
	 * Restituisce l’insieme di synset che contengono tra i loro sensi la word
	 * @param word Parola da utilizzare nella ricerca
	 * @return Restituisce l’insieme di synset che contengono tra i loro sensi la word
	 */
	public List<Synset> getSynsets(String word){
		return getSynsetsAsStream(word).collect(Collectors.toList());
	}
	/**
	 * Restituisce il synset relativo all’id specificato
	 * @param id Identificativo del Synset richiesto
	 * @return Restituisce il synset relativo all’id specificato
	 */
	abstract Synset getSynset(String id);
	/**
	 * Restituisce tutti i Synset contenuti nella rete semantica
	 * @return Restituisce tutti i Synset contenuti nella rete semantica
	 */
	abstract Set<Synset> getSynsets();
	/**
	 * Restituisce uno Stream di synset che contengono tra i loro sensi la word
	 * @param word Parola da utilizzare nella ricerca
	 * @return Restituisce uno Stream di synset che contengono tra i loro sensi la word
	 */
	abstract Stream<Synset> getSynsetsAsStream(String word);
	/**
	 * Restituisce tutti i lemmi contenuti nella rete semantica
	 * @return Restituisce tutti i lemmi contenuti nella rete semantica
	 */
	abstract Set<String> getLemmas();
	/**
	 * Restituisce uno o più lemmi associati alla parola flessa fornita in input
	 * @param word Parola da utilizzare nella ricerca
	 * @return Restituisce uno o più lemmi associati alla parola flessa fornita in input
	 */
	abstract List<String> getLemmas(String word);
	/**
	 * Calcola la similarità tra due LinguisticObject
	 * @param o1 Primo elemento da confrontare
	 * @param o2 Primo elemento da confrontare
	 * @return Un double rappresentante la similarità tra i due LinguisticObject
	 */
	abstract double computeSimilarity(LinguisticObject o1, LinguisticObject o2);
	/**
	 * Imposta la similarità lessicale
	 * @param strategy Un'implementazione della similarità tra Word
	 */
	public void setLexicalSimilarityStrategy(SimilarityObject<Word> strategy) { 
		lexical = strategy; 
	}
	/**
	 * Imposta la similarità semantica
	 * @param strategy Un'implementazione della similarità tra Synset
	 */
	public void setSemanticSimilarityStrategy(SimilarityObject<Synset> strategy) { 
		semantic = strategy; 
	}
	/**
	 * Imposta la similarità tra documenti
	 * @param strategy Un'implementazione della similarità tra Document
	 */
	public void setDocumentSimilarityStrategy(SimilarityObject<Document> strategy) { 
		document = strategy; 
	}
}
