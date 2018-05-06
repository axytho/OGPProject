package quantity;
/**
 * List  all liquid Quantities
 */
public enum LQuant implements Quant{
	DROP 		(1),
	SPOON		(8),
	VIAL 		(5),
	BOTTLE		(3),
	JUG			(7),
	BARREL		(12),
	STOREROOM	(5);
	
	/**
	 * Create a quantity with as value a given amount of the previous quantity
	 * @param	previousValue
	 */
	
	LQuant(int correspondingValue) {
		this.cVal = correspondingValue;
	}
	/**
	 * The value of the previous quantity
	 */
	public int cVal = 0;
	
	/**
	 * Return the value of the previous quantity
	 */
	public int getCVal() {
		return this.cVal;
	}
	
}