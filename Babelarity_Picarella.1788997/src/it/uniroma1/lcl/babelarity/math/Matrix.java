package it.uniroma1.lcl.babelarity.math;

import java.util.AbstractList;
import java.util.List;
/**
* Vector definisce la struttura di una Matrix a due dimensioni con valori primitivi implementando AbstractList di List 
* @author gianpcrx
* @since 1.0
* @version 1.0
* @param <T> Tipo che estende Number
*/
public abstract class Matrix<T extends Number> extends AbstractList<List<T>> implements List<List<T>> {
	protected int width, height, nonzeroes;
	/**
	 * Inizializza la matrice 
	 * @param width Numero colonne della matrice
	 * @param height Numero righe della matrice
	 * @param nonzeroes Numero di valori maggiori di zero
	 */
	public Matrix(int width, int height, int nonzeroes) {
		this.width = width;
		this.height = height;
		this.nonzeroes = nonzeroes;
		initializeMatrix();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public int size() { return height; }
	/**
	 * 
	 * @return Il numero di colonne
	 */
	public int getWidth() { return width; }
	/**
	 * 
	 * @return Il numero di valori diversi da zero
	 */
	public int getNonZeroes() { return nonzeroes; }
	/**
	 * Setta un valore alla posizione x, y
	 * @param x Posizione nella colonna
	 * @param y Posizione nella riga
	 * @param el Elemento da inserire
	 * @return Ritorna il valore precedente alla posizione x, y
	 */
	public abstract T set(int x, int y, T el);
	/**
	 * Ritorna il valore nella posizione x, y
	 * @param x Index della colonna
	 * @param y Index della riga
	 * @return Il valore nella posizione x, y
	 */
	public abstract T get(int x, int y);
	/**
	 * Ritorna il Vettore nella posizione y
	 * @param y Index del vettore da prelevare
	 * @return Il Vettore che rappresenta la riga in posizione y
	 */
	public abstract Vector<T> get(int y);
	/**
	 * Incrementa il valore alla posizione x, y con el
	 * @param x Index della colonna
	 * @param y Index della riga
	 * @param el Valore da utilizzare nell'incremento
	 */
	public abstract void increment(int x, int y, T el);
	/**
	 * Inizializza la matrice di primitivi
	 */
	protected abstract void initializeMatrix();
}
