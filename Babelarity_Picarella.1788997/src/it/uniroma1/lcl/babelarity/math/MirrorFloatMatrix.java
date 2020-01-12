package it.uniroma1.lcl.babelarity.math;
/**
 * Implementazione sul primitivo Float della classe Matrix
 * @since 1.0
 * @author gianpcrx
 * @version 1.0
 */
public class MirrorFloatMatrix extends Matrix<Float> {
	private float[][] mat;
	
	/**
	 * Inizializza la matrice
	 * @param rows Numero di righe
	 * @param cols Numero di colonne
	 */
	public MirrorFloatMatrix(int rows, int cols) {
		super(cols, rows, 0);
	}
	
	/*
	 * (non-Javadoc)
	 * @see it.uniroma1.lcl.babelarity.math.Matrix#initializeMatrix()
	 */
	@Override
	protected void initializeMatrix() {
		mat = new float[height][];
		for(int i = 0; i < height; i++) mat[i] = new float[width - i];
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Vector<Float> get(int y) {
		FloatVector row = new FloatVector(width);
		for(int x = 0; x < getWidth(); x++) row.set(x, get(x, y));
		return row;
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Float set(int x, int y, Float el) {
		float prev;
		if(y < x) {
			prev = mat[y][x - y];
			mat[y][x - y] = el;
		}
		else {
			prev = mat[x][y - x];
			mat[x][y - x] = el;
		}
		if(prev <= 0 && el > 0) nonzeroes += 2;
		else if(el <= 0 && prev > 0) nonzeroes -= 2;
		return prev;
		
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Float get(int x, int y) {
		return x < y ? mat[x][y - x] : mat[y][x - y];
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void increment(int x, int y, Float el) {
		if(y < x) mat[y][x - y]+= el;
		else mat[x][y - x]+= el;
	}
}