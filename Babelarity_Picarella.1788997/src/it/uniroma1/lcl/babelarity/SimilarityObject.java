package it.uniroma1.lcl.babelarity;
/**
 * Definisce i metodi da implementare per una similarità tra oggetti dello stesso tipo
 * @author gianpcrx
 * @since 1.0
 * @version 1.0
 * @param <T> Tipo che estende l'interfaccia LinguisticObject
 */
public abstract class SimilarityObject<T extends LinguisticObject> {
	protected SemanticNetwork network;
	/**
	 * Inizializza la similarità
	 * @param network Una rete semantica da utilizzare per la similarità
	 */
	public SimilarityObject(SemanticNetwork network) {
		this.network = network;
	}
	/**
	 * Restituisce un double rappresentante la similarità tra i due oggetti
	 * @param o1 Primo oggetto da confrontare
	 * @param o2 Secondo oggetto da confrontare
	 * @return Un double rappresentante la similarità tra i due oggetti
	 */
	abstract double computeSimilarity(T o1, T o2);
}
