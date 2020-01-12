package it.uniroma1.lcl.babelarity.math;

import java.util.AbstractList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
/**
 * Vector definisce la struttura di un Vettore ad una dimensione con valori primitivi implementando AbstractList 
 * @author gianpcrx
 * @since 1.0
 * @version 1.0
 * @param <T> Tipo che estende Number
 */
public abstract class Vector<T extends Number> extends AbstractList<T> implements List<T> {
	protected int size;
	/**
	 * Inizializza il Vettore senza elementi e con lunghezza 0
	 */
	public Vector() { this(0); }
	/**
	 * Inizializza il vettore senza elementi e con lunghezza uguale a size
	 * @param size Lunghezza iniziale del Vettore
	 */
	public Vector(int size) {
		this.size = size;
		intializeArray();
	}
	/**
	 * Inizializza il vettore con una Collection
	 * @param c Collection da cui copiare i valori
	 */
	public Vector(Collection<T> c) {
		size = c.size();
		intializeArray();
		int k = 0;
		Iterator<T> i = c.iterator();
		while(i.hasNext()) set(k++, i.next());
	}
	
	/**
	 * 
	 * @return La lunghezza dell'array di primitivi utilizzato per mantenere i dati
	 */
	protected abstract int getArraySize();
	/**
	 * Inizializza l'array di primitivi
	 */
	protected abstract void intializeArray();
	/**
	 * Copia una porzione di array e, in caso, genera un nuovo array con lunghezza maggiore
	 * @param inc Indica se, in caso reinizializzazione dell'array, bisogna aumentare o diminuire la grandezza del nuovo array
	 * @param startS Index di inizio sorgente
	 * @param startD Index di inizio destinazione
	 * @param endD Index di fine destinazione
	 */
	protected abstract void copyArray(boolean inc, int startS, int startD, int endD);
	/**
	 * {@inheritDoc}
	 */
	@Override
	public int size() {
		return size;
	}
	
	/**
	 * Calcola la similarità del coseno tra Vettori
	 * @param v Un vettore di lunghezza uguale
	 * @return La similarità del coseno sottoforma di double
	 */
	public double cosineSimilarityWith(Vector<T> v) {
		double num = 0, dena = 0, denb = 0;
		for(int i = 0; i < this.size(); i++) {
			num += this.get(i).doubleValue() * v.get(i).doubleValue();
			dena += this.get(i).doubleValue() * this.get(i).doubleValue();
			denb += v.get(i).doubleValue() * v.get(i).doubleValue();
		}
		return (float) (num / (Math.pow(dena, 0.5) * Math.pow(denb, 0.5)));
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean add(T arg0) {
		if(size + 1 > getArraySize()) copyArray(true, 0, 0, size);
		set(size++, arg0);
		return true;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public  void add(int index, T el) {
		if(index == size - 1) add(el);
		else {
			copyArray(true, index, index + 1, size);
			set(index, el);
			size++;
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public T remove(int arg0) {
		T value = get(arg0);
		if(arg0 != size - 1) copyArray(true, arg0 + 1, arg0, size);
		if(getArraySize() - (--size) > getArraySize() / 2) copyArray(false, 0, 0, size);
		return value;
	}
}
