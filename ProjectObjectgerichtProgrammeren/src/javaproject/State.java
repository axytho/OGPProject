package javaproject;

import java.util.Arrays;
import java.util.List;

import quantity.*;

/**
 * The state which an ingredient can be in
 */
public enum State {
	Liquid, Solid;
	
	/**
	 * Return the conversion factors of quantity
	 * 
	 * @return	A List containing the conversion factors depending on the state
	 * 			| if (this == liquid): (result == Arrays.asList(LQuant.values()))
	 * 			| if (this == solid): (result ==  Arrays.asList(SQuant.values()))
	 */
	
	public List<Quant> getQuantities() {
		if (this == Liquid) {
			return Arrays.asList(LQuant.values());
		} else {
			return Arrays.asList(SQuant.values());
		}
				
	}
}
