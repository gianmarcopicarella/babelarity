package it.uniroma1.lcl.babelarity.math;

import java.util.Collection;
/**
 * Implementazione sul primitivo Int della classe Vector
 * @since 1.0
 * @author gianpcrx
 * @version 1.0
 */
public class IntegerVector extends Vector<Integer> {
	private int[] array;
	/**
	 * Inizializza il Vettore senza elementi e con lunghezza 0
	 */
	public IntegerVector() { super(); }
	/**
	 * Inizializza il vettore senza elementi e con lunghezza uguale a size
	 * @param size Lunghezza iniziale del Vettore
	 */
	public IntegerVector(int size) { super(size); }
	/**
	 * Inizializza il vettore con una Collection
	 * @param c Collection da cui copiare i valori
	 */
	public IntegerVector(Collection<Integer> c) { super(c); }
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Integer get(int arg0) {
		return array[arg0];
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Integer set(int arg0, Integer arg1) {
		return (this.array[arg0] = arg1);
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void copyArray(boolean inc, int startS, int startD, int endD) {
		int[] cpy = array;
		this.array = inc ? new int[1 + array.length * 2] : new int[1 + array.length / 2];
		System.arraycopy(cpy, 0, array, 0, startS - 1);
		System.arraycopy(cpy, startS, array, startD, endD);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected int getArraySize() {
		return array.length;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void intializeArray() {
		array = new int[size];
	}
}