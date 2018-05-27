package javaproject;

import java.util.Arrays;
import java.util.List;

import be.kuleuven.cs.som.annotate.*;

/**
 * @invar	Simple Name must be a valid simpleName
 * 			| isValidSimpleName(getName())
 * @invar	Volatility must be a valid volatility
 * 			| isValidVolatility(getVolatility())
 * @invar	Standard temperature must be a valid temperature
 * 			| isValidTemperature(getStandardTemperature())
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
	 * @param	standardTemperature
	 * 			| The standard temperature of this ingredient type
	 * @param	volatility
	 * 			| The volatility of this ingredinet type
	 * @effect	The simpleName of the ingredient is set to the given simpleName
	 * 			| setName(simpleName)
	 * @post	The state of this ingredient type is set to the given state
	 * 			| new.getState() == state
	 * @post	The standard temperature is set to the given temperature if the given temperature is valid
	 * 			else, the standard temperature is set to [0, 1]
	 * @throws	IllegalArgumentException
	 * 			| The given name is invalid
	 * 			| !isValidSimpleName(name)
	 */
	
	
	public IngredientType(String name, State state, long[] standardTemperature, double volatility) {
		if (!isValidSimpleName(name)) {
			throw new IllegalArgumentException("Invalid Name!");
		}
		if (!isValidTemperature(standardTemperature)) {
			standardTemperature = new long[] {0, 1};
		}
		setName(name);
		this.state = state;
		setStandardTemperature(standardTemperature);
		this.volatility = volatility;
	}
	
	/**
	 * The standard temperature at which this type is usually used
	 */
	private long[] standardTemp = {0, 0};
	
	/**
	 * Return the standard temperature
	 */
	@Raw @Basic
	public long[] getStandardTemperature() {
		return this.standardTemp;
	}
	/**
	 * Set the standard temperature
	 * 
	 * @param	temp
	 * 			The temperature to which the standard temperature is set
	 * @post	The new temperature equals the given temperature
	 * 			| new.getStandardTemperature() == temperature
	 */
	@Model
	private void setStandardTemperature(long[] temperature) {
		this.standardTemp = temperature;
	}
	
	/**
	 * Check whether the temperature is valid
	 * 
	 * @param	temperature
	 * 			The temperature to be checker
	 * 
	 * @return	True if and only if the hotness is strictly greater than zero
	 * 			| temperature[1] > 0
	 */
	public static boolean isValidTemperature(long[] temperature) {
		return temperature[1] > 0;
	}
	
	
	/**
	 * The theoretical volality for this ingredient type
	 */
	private double volatility = 0;
	
	/**
	 * Return the theoretical volality of this ingredient type
	 */
	@Raw @Basic
	public double getVolatility() {
		return this.volatility;
	}
	
	
	/**
	 * Check the volatility
	 * 
	 * @param	volatility
	 * 			The volatility to be checked
	 * @return	True if the volatility is between 0 and 1
	 * 			| 0 < volatility && volatility < 1
	 * 
	 */
	public static boolean isValidVolatility(double volatility) {
		return 0 < volatility && volatility < 1;
	}
	
	/**
	 * An array of words which are considered special
	 */
	
	private static final String[] specialWords = {"Heated", "Cooled"};
	
	

	
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
	 * 			6) The simple name is null, which means that there is no simple name for this ingredient,
	 * 				only a name of mixed ingredients.
	 * 
	 * 			| False if 
	 * 			| (name.split(" ").length == 1 && name.trim().length() < 3) 
	 * 			| 		|| for some string in name.split(" ")
	 * 			|			string.length < 2 
	 * 			|			|| (!string.matches("[A-Z][a-z’']+") && string != "mixed" && string != "with")
	 * 			| 			|| (specialWord.contains(string))
	 */
	
	public static boolean isValidSimpleName(String name) {
		if (name == null) {
			return true;
		}
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
			if (!string.matches("[A-Z][a-z’']+") && !string.equals("mixed") && !string.equals("with")) {
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
	private String simpleName = "";
	
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
	private State state = null;
	
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
