package it.uniroma1.lcl.babelarity.exception;
/**
* Eccezione sollevata nel caso in cui non venga trovato il lemma relativo ad una Word
* @author gianpcrx
* @since 1.0
* @version 1.0
*/
public class LemmaNotFoundException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Inizializza l'eccezione
	 */
	public LemmaNotFoundException() {
		super("Lemma not found");
	}
}
