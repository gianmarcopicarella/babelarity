package it.uniroma1.lcl.babelarity;

import java.util.Set;
/**
 * Definisce i metodi da implementare per relazionarsi con oggetti dello stesso tipo
 * @author gianpcrx
 * @since 1.0
 * @version 1.0
 * @param <T> Un tipo generico qualsiasi
 */
public interface LinkableObject<T> {
	/**
	 * Crea una relazione tra i due LinkableObject
	 * @param s LinkableObject di destinazione
	 * @param type Tipo di relazione 
	 */
	void linkTo(T s, String type);
	/**
	 * Ritorna tutti i nodi collegati a se stesso con relazione in types
	 * @param types Relazioni da prendere in considerazione
	 * @return un set di oggetti collegati a se stesso
	 */
	Set<T> getLinked(String... types);
	/**
	 * Ritorna tutti i nodi collegati a se stesso tranne quelli con relazione in types
	 * @param types Relazioni da non prendere in considerazione
	 * @return un set di oggetti collegati a se stesso
	 */
	Set<T> getLinkedExcept(String... types);
	/**
	 * Ritorna tutti i nodi collegati a se stesso con una distanza minore o uguale a quella specificata
	 * @param distance Distanza massima utilizzata nella ricerca
	 * @return Tutti i nodi collegati a se stesso con una distanza minore o uguale a quella specificata
	 */
	Set<T> getLinked(int distance);
		
}
