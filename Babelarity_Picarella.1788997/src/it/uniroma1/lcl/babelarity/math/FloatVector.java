package it.uniroma1.lcl.babelarity.math;

import java.util.Collection;
/**
 * Implementazione sul primitivo float della classe Vector
 * @since 1.0
 * @author gianpcrx
 * @version 1.0
 */
public class FloatVector extends Vector<Float> {
	private float[] array;
	/**
	 * Inizializza il Vettore senza elementi e con lunghezza 0
	 */
	public FloatVector() { super(); }
	/**
	 * Inizializza il vettore senza elementi e con lunghezza uguale a size
	 * @param size Lunghezza iniziale del Vettore
	 */
	public FloatVector(int size) { super(size); }
	/**
	 * Inizializza il vettore con una Collection
	 * @param c Collection da cui copiare i valori
	 */
	public FloatVector(Collection<Float> c) { super(c); }
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Float get(int arg0) {
		return array[arg0];
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Float set(int arg0, Float arg1) {
		return (array[arg0] = arg1);
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void copyArray(boolean inc, int startS, int startD, int endD) {
		float[] cpy = array;
		array = inc ? new float[1 + array.length * 2] : new float[1 + array.length / 2];
		for(int i = 0; i < startS - 1; i++) array[i] = cpy[i];
		for(int i = startS; i < endD; i++) array[startD + (i - startS)] = cpy[i];
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void intializeArray() {
		array = new float[size];
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected int getArraySize() {
		return array.length;
	}
}