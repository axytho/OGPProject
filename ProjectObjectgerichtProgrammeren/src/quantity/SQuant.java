package quantity;

public enum SQuant implements Quant {
	PINCH 		(1),
	SPOON		(6),
	SACHET 		(7),
	BOX			(6),
	SACK		(3),
	CHEST		(10),
	STOREROOM	(5);
	
	/**
	 * Create a quantity with as value a given amount of the previous quantity
	 * @param	previousValue
	 */
	
	SQuant(int correspondingValue) {
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