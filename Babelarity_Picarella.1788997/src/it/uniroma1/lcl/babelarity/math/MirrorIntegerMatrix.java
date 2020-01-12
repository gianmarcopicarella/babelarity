package it.uniroma1.lcl.babelarity.math;
/**
 * Implementazione sul primitivo Int della classe Matrix
 * @since 1.0
 * @author gianpcrx
 * @version 1.0
 */
public class MirrorIntegerMatrix extends Matrix<Integer> {
	private int[][] mat;
	/**
	 * Inizializza la matrice
	 * @param rows Numero di righe
	 * @param cols Numero di colonne
	 */
	public MirrorIntegerMatrix(int rows, int cols) {
		super(cols, rows, 0);
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Integer set(int x, int y, Integer el) {
		int prev;
		if(y < x) {
			prev = mat[y][x - y];
			mat[y][x - y] = el;
		}
		else {
			prev = mat[x][y - x];
			mat[x][y - x] = el;
		}
		if(el > 0 && prev <= 0) nonzeroes += 2;
		else if(el <= 0 && prev > 0) nonzeroes -= 2;
		return prev;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Integer get(int x, int y) {
		return x < y ? mat[x][y - x] : mat[y][x - y];
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void increment(int x, int y, Integer el) {
		// TODO Auto-generated method stub
		if(y < x) mat[y][x - y]+= el;
		else mat[x][y - x]+= el;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Vector<Integer> get(int arg0) {
		IntegerVector row = new IntegerVector(getWidth());
		for(int x = 0; x < getWidth(); x++) row.set(x, get(x, arg0));
		return row;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void initializeMatrix() {
		mat = new int[height][];
		for(int i = 0; i < height; i++) mat[i] = new int[width - i];
	}
}