package javaproject;

import java.util.Arrays;
import java.util.List;

import be.kuleuven.cs.som.annotate.*;

/**
 * @invar	Simple Name must be a valid simpleName
 * 			| isValidSimpleName(getName())
 * 
 * @author Jonas
 *
 */
public class IngredientType {
	
	
	/**
	 * Initialize a new ingredient type with a given simpleName
	 * 
	 * @param 	name
	 * 			| The name of this Ingredient type
	 * @param	state
	 * 			| the state of this ingredient type
	 * @effect	The simpleName of the ingredient is set to the given simpleName
	 * 			| setName(simpleName)
	 * @post	The state of this ingredient type is set to the given state
	 * 			| new.getState() = state
	 */
	
	
	public IngredientType(String name, State state) {
		setName(name);
		this.state = state;
	}
	
	
	
	
	/**
	 * An array of words which are considered special
	 */
	
	private static final String[] specialWords = {"mixed", "with", "Heated", "Cooled"};
	

	
	/**
	 * A list containing the special words for easy access
	 */
	private static final List<String> specialWord  = Arrays.asList(specialWords);
	
	/**
	 * 
	 * @param	simpleName
	 * 			| The name we want to give our Alchemic Ingredient
	 * @return	True if
	 * 			1) The name, if it consists of multiple words, must be separated by a single space (not 2!!!)
	 * 			2) Words may not contain numbers and characters other than ' and )
	 * 			3) A single word must contain at least 3 letters, multiple words at least 2
	 * 			4) Words must start with a Capital or a special character
	 * 			5) mixed and with are exceptions: they do not require a capital but are not allowed in simple names
	 * 
	 * 			| False if 
	 * 			| (name.split(" ").length == 1 && name.trim().length() < 3) 
	 * 			| 		|| for some string in name.split(" ")
	 * 			|			string.length < 2 
	 * 			|			|| (!string.matches("[A-Z][a-z’']+"))z
	 * 			| 			|| (specialWord.contains(string))
	 */
	
	public static boolean isValidSimpleName(String name) {
		int count = 0;
		// Case 1
		for (String string : name.split(" ")) {
			//if not seperated by space, we should get one long string, which will then get picked out by virtue of having a capital in the middle
			// case 3 part 2
			if (string.length() < 2) {
				// Also returns false if there are 2 spaces
				return false;
			}
			// Case 4 and 2
			if (!string.matches("[A-Z][a-z’']+")) {
				return false;
			}
			// Case 5
			if (specialWord.contains(string)) {
				return false;
			}
			
			count += 1;
		}
		
		//Case 3 part 1)
		if (count == 1 && name.trim().length() < 3) {
			assert(!name.contains(" "));
			return false;
		}
		return true;
	}
	

	

	
	/**
	 * Name of the ingredient type
	 */
	public String simpleName = "";
	
	/**
	 * Sets the simpleName of the ingredient
	 * 
	 * @param	simpleName
	 * 			The simpleName to be given to this ingredient
	 * @post	The simpleName of this IngredientType is the given simpleName
	 * 			| this.getName() = simpleName
	 * @throws	IllegalArgumentException
	 * 			simpleName is not a valid simpleName
	 */
	@Model
	public void setName(String name) throws IllegalArgumentException {
		if (!IngredientType.isValidSimpleName(name)) {
			throw new IllegalArgumentException("Invalid simpleName!");
		}
		this.simpleName = name;
	}
	
	/**
	 * Get the simpleName of the ingredient type
	 */
	@Raw @Basic
	public String getName() {
		return this.simpleName;
	}
	

	
	/**
	 * The state of this ingredient
	 */
	public State state = null;
	
	/**
	 * Return the state of this Ingredient Type
	 */
	@Raw @Basic
	public State getState() {
		return state;
	}
	
//	public static void main(String[] args) {
//		System.out.println("Devils".matches("[A-Z][a-z]+"));
//	}

}
