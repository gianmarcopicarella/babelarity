package it.uniroma1.lcl.babelarity.exception;
/**
 * Eccezione sollevata nel caso in cui non venga trovata una similarità adatta ai due oggetti da cofrontare
 * @author gianpcrx
 * @since 1.0
 * @version 1.0
 */
public class SimilarityNotFoundException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Inizializza l'eccezione
	 */
	public SimilarityNotFoundException() {
		super("Similarity not found");
	}
}
