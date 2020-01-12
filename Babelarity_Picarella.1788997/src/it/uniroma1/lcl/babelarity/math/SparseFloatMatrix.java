package it.uniroma1.lcl.babelarity.math;
/**
 * Implementazione dell'algoritmo di compressione CSR sul primitivo Float della classe Matrix
 * @since 1.0
 * @author gianpcrx
 * @version 1.0
 */
public class SparseFloatMatrix extends Matrix<Float> {
	private float[] nnz;
	private short[] columIndex;
	private int[] ia;
	/**
	 * Inizializza la Matrice compressa secondo l'algoritmo CSR
	 * @param matrix Una matrice da cui inizializzare l'oggetto
	 */
	public SparseFloatMatrix(Matrix<Float> matrix) {
		super(matrix.getWidth(), matrix.size(), matrix.getNonZeroes());
		int counter = 0, iaI = 0, nnzC = 0;
		for(short y = 0; y < this.height; y++) {	
			Vector<Float> row = matrix.get(y);
			for(int x = 0; x < this.width; x++) {
				if(row.get(x) > 0) {
					this.nnz[nnzC] = row.get(x);
					this.columIndex[nnzC++] = (short) x;
					counter++;
				}
			}
			this.ia[iaI + 1] = ia[iaI++] + counter;
			counter = 0;
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public FloatVector get(int y) {
		FloatVector row = new FloatVector(this.width);
		for(int x = 0; x < this.width; x++) row.set(x, get(x, y));
		return row;
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Float set(int x, int y, Float el) {
		int numNNZ = Math.abs(ia[y + 1] - ia[y]);
		int startColIndex = this.ia[y];
		float prev = 0;
		for(int k = startColIndex; k < startColIndex + numNNZ; k++) {
			if(columIndex[k] == x) {
				if(nnz[k] <= 0 && el > 0) nonzeroes++;
				else if(nnz[k] > 0 && el <= 0) nonzeroes--;
				prev = nnz[k];
				nnz[k] = el;
				break;
			}
		}
		return prev;
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Float get(int x, int y) {
		int numNNZ = Math.abs(ia[y + 1] - ia[y]);
		int startColIndex = this.ia[y];
		for(int k = startColIndex; k < startColIndex + numNNZ; k++) {
			if(columIndex[k] == x) return nnz[k];
		}
		return 0.f;
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void increment(int x, int y, Float el) {
		set(x, y, get(x, y) + el);
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void initializeMatrix() {
		nnz = new float[nonzeroes];
		ia = new int[height + 1];
		columIndex = new short[nonzeroes];
	}
}
