package it.uniroma1.lcl.babelarity.exception;
/**
* Eccezione sollevata nel caso in cui la part-of-speech di due Synset è differenti
* @author gianpcrx
* @since 1.0
* @version 1.0
*/
public class DifferentPosException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Inizializza l'eccezione
	 */
	public DifferentPosException() {
		super("Different pos exception");
	}

}
