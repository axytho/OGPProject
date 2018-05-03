package javaproject;


/**
 * @invar	Simple Name must be a valid name
 * 			| isValidSimpleName(getName())
 * 
 * @author Jonas
 *
 */
public class IngredientType {
	
	/**
	 * 
	 * @param	name
	 * 			| The name we want to give our Alchemic Ingredient
	 * @return	True if
	 * 			1) The name, if it consists of multiple words, must be separated by spaces
	 * 			2) Words may not contain numbers and characters other than ' and )
	 * 			3) A single word must contain at least 3 letters, multiple words at least 2
	 * 			4) Words must start with a Capital or a special character
	 * 			5) mixed and with are exceptions: they do not require a capital but are not allowed in simple names
	 * 			6) 
	 */
	
	public static boolean isValidSimpleName(String name) {
		return false;
	}

}
