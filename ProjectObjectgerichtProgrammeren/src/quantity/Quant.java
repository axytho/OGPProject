package quantity;

import javaproject.State;

public interface Quant {

	/**
	 * The value of the previous quantity
	 */
	public int cVal = 0;
	
	/**
	 * Return the value of the previous quantity
	 */
	public int getCVal();
	
	/**
	 * Get the state of this quantity
	 */
	public State getType();
}
