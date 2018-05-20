package javaproject;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;


import be.kuleuven.cs.som.annotate.*;
import quantity.*;

/**
 * 
 * 
 * @invar	volatility must be valid
 * 			| isValidCharVolatility(getCharVolatility())
 * @invar	Temperature must be valid
 * 			| isValidTemperature(getTemperature())
 * @invar	Type must be valid
 * 			| isValidType()
 * @invar	Quantity must be valid
 * 			| isValidQuantity()
 * @invar	Quantity is carried over from smaller quantities to larger quantities
 * 			| isCarriedOver()
 * 
 * @author Jonas
 *
 */


public class AlchemicIngredient {
	
	
//	/**
//	 * Create a new Alchemic Ingredient Type
//	 * 
//	 * @param	Type
//	 * 			The type of this ingredient
//	 * @post	The type of this ingredient is set to the given type
//	 * 			If the type is not a valid type, the type is set to Aether, a default type signifying "nothing".
//	 * 			| setType(type)
//	 * @post	The quantity of this Alchemic ingredient is 0
//	 * 			| for (each index in 1..getSize())
//	 * 			|		getItemAt(index)  == 0
//	 * @post	The maximum temperature is set to 10000
//	 * 			| getMaxTemperature() == 10000
//	 */
//	public AlchemicIngredient(IngredientType type) {
//		if (isValidType(type)) {
//			this.type = type;
//		} else {
//			this.type = new IngredientType("Aether", State.Liquid, new long[] {0, 0} );
//		}
//		setQuantityToZero();
//		setState(getType().getState());
//		setMaxTemperature(10000);
//	}
	
	/**
	 * Create a new Alchemic Ingredient Type with a given Type
	 * 
	 * @param	type
	 * 			The type of this ingredient
	 * @param	quantity
	 * 			The quantity, represented in an arrayList
	 * @pre 	The quantity is a valid quantity
	 * 			| isValidQuantity(quantity)
	 * @post	The temperature is set to the standard temperature of the type, unless
	 * 			that temperature was above the maximum, in which case it is set to the maximum
	 * 			|	if (getType().getStandardTemperature()[0]>getMaxTemperature())
	 * 			|		this.getTemperature() == {getMaxTemperature() , 0}
	 * 			|	if (getType().getStandardTemperature()[1]>getMaxTemperature())
	 * 			|		this.getTemperature() == {0 , getMaxTemperature()}
	 * 			|  else
	 * 			| 		this.getTemperature() == getType().getStandardTemperature()
	 * @post	The quantity is validly carried over
	 * 			| isCarriedOver() == true
	 * @post	The type of this ingredient is set to the given type
	 * 			If the type is not a valid type, the type is set to Water, a default type signifying "nothing".
	 * 			| if (isValidType(type))
	 * 			|	new.getType() = type
	 * 			| else
	 * 			| 		this.type = new IngredientType("Water", State.Liquid, new long[] {0, 20}, 0)
	 * @post	The quantity of this Alchemic ingredient is to quantity
	 * 			| new.getQuantity() = quantity;
	 * @post	The maximum temperature is set to 10000
	 * 			| getMaxTemperature() == 10000
	 * @effect	The volatility is created
	 * 			| createVolatility()
	 * 
	 */
	// TODO: delete or keep: @note	This is private because we want a user to give us a quantity as a full barrel or something like that
	
	public AlchemicIngredient(IngredientType type, ArrayList<Integer> quantity) {
		if (isValidType(type)) {
			this.type = type;
		} else {
			this.type = new IngredientType("Water", State.Liquid, new long[] {0, 20}, 0);
		}
		setState(getType().getState());
		setQuantity(quantity);
		setMaxTemperature(10000);
		setHotness(getType().getStandardTemperature()[1]);
		setColdness(getType().getStandardTemperature()[0]);
		createVolatility();
	}
	
//	/**
//	 * Create a new alchemic ingredient with a given type and a quantity given in a solid unit
//	 * 
//	 * @param	type
//	 * 			The type of this ingredient
//	 * @param	unit
//	 * 			The unit of our alchemic ingredient
//	 * @post	The quantity is validly carried over
//	 * 			| isCarriedOver() == true
//	 * @post	The type of this ingredient is set to the given type
//	 * 			If the type is not a valid type, the type is set to Aether, a default type signifying "nothing".
//	 * 			| setType(type)
//	 * @post	The quantity of our alchemic ingredient is now equal to 1 unit of the given unit
//	 * 			| giveInLowestUnit(unit) == new.convertToLowestUnit()
//	 */
//	public AlchemicIngredient(IngredientType type, Quant unit) { 
//		if (isValidType(type)) {
//			this.type = type;
//		} else {
//			this.type = new IngredientType("Aether", State.Liquid);
//		}
//		
//		this(type, giveInLowestUnit(unit));
//	}
	
	/***************************************************************
	 * NAME CHECKS
	 ***************************************************************/
	
	/**
	 * Return the simple name of this Alchemic Ingredient
	 * 
	 * 
	 */
	public String getName() {
		if (getType().getName() == null) {
			return getMixedName();
		}
		return getType().getName();
	}
	
	
	
	/**
	 * A list containing all the ingredients mixed into this alchemic ingredient
	 */
	
	private ArrayList<IngredientType> mixList = new ArrayList<IngredientType>();
	
	/**
	 * Add an ingredient type to the mixList
	 * 
	 * @param	type
	 * 			The ingredient type which is added
	 * @post	An ingredient type is added to the mix list
	 * 			| new.get(old.size()) = type
	 */
	public void addToMixList(IngredientType type) {
		mixList.add(type);
	}
	
	/**
	 * Return a copy of the list of mixed ingredients
	 */
	@Basic @Raw
	protected ArrayList<IngredientType> getIngredientMixList() {
		return new ArrayList<IngredientType>(mixList);
	}
	
	/**
	 * Get a list of the alphabetically sorted names of all the types mixed in this alchemic ingredient
	 * 
	 * @return	A list of the alphabetically sorted names of all the types mixed in this alchemic ingredient
	 * 			| for each type, type2 in getMixList()
	 * 			|		result.contains(type.getName())
	 * 
	 */
	
	public ArrayList<String> createMixList() {
		ArrayList<String> resultList = new ArrayList<String>();
		for (IngredientType type : getIngredientMixList()) {
			resultList.add(type.getName());
		}
		Collections.sort(resultList);
		return resultList;
	}
	
	/**
	 * The special name of this Alchemic Ingredient
	 */
	private String specialName = null;
	
	/**
	 * Get the special name of this ingredient
	 */
	public String getSpecialName() {
		return this.specialName;
	}
	
	/**
	 * Set the special name of this Alchemic Ingredient
	 * 
	 * @param	name
	 * 			the special name of this ingredient
	 * @post	The new special name is equal to the given name
	 * 			| new.getSpecialName() == name
	 */		
	protected void setSpecialName(String name) {
		this.specialName = name;
	}
	
	/**
	 * Get the mixed name of this Alchemic Ingredient 
	 * 
	 * @return	A string with the special name up front, followed by the first name, followed by " mixed with " followed by a series
	 * 			of simple names of ingredients mixed in this ingredient, followed by " and ", followed by the last element
	 * 			| result == createMixList().get(0).concat(" mixed with ").concat(createMixList().get(1))
	 * 			|				+ sum(for I in 1..stringList.size() - 1: ", " + createMixList().get(I))
	 * 			|			+ " and " + createMixList().get(createMixList().size() - 1)
 	 */
	public String getMixedName() {
		ArrayList<String> stringList = createMixList();
		if (stringList.size() == 1) {
			return stringList.get(0);
		}
		String resultString = stringList.get(0).concat(" mixed with ").concat(stringList.get(1));
		if (stringList.size() == 2) {
			return resultString;
		}
		for (int i = 2; i < stringList.size() - 1; i++) {
			resultString = resultString.concat(", ").concat(stringList.get(i));
		}
		return resultString.concat(" and ").concat(stringList.get(stringList.size() - 1));
	}
	
	/**
	 * Return the total name if no special name is given
	 * 
	 * @return	The mixed name with prefixes added
	 * 			| getVolatilityName() + getTemperatureName() + " " + getMixedName()
	 * 
	 */
	public String getTotalNameWithoutSpecial() {
		return getVolatilityName() + getTemperatureName() + " " + getMixedName();
	}
	
	/**
	 * Return the full total name
	 * 
	 * @return	The full name, with special name attached
	 * 			| if (getSpecialName() != null)
	 * 			| 		result == getSpecialName() + " ("+  getTotalNameWithoutSpecial() + ")"
	 * 			| else
	 * 			|		result == getTotalNameWithoutSpecial()
	 */
	public String getTotalName() {
		if (getSpecialName() != null)
			return getSpecialName() + " ("+  getTotalNameWithoutSpecial() + ")";
		return getTotalNameWithoutSpecial();
	}
	
	//LEGACY CODE:
	// TODO: DELETE
	
	/**
	 * An array of adjectives which are considered special
	 */
	
	private static final String[] specialAdjectives = {"Heated", "Cooled"};
	
	/**
	 * A list containing the special adjectives for easy access
	 */
	private static final List<String> specialAdjective = Arrays.asList(specialAdjectives);
	
	// "Heated Red Eye Special (Heated Coke mixed with Beer, Cooled Water , Vodka, Martini, Cider and Tomato Juice)"
	// isValidSimpleName("Red Eye Special")
	// isValidTotalName("Red Eye Special (Heated Coke mixed with Beer, Water, Vodka, Martini, Cider and Tomato Juice)")
	// isValidSimpleName("Coke"), isValidSimpleName("Beer"), etc...
	// + check if valid way of constructing special name (comma's, and, mixed with at the right places)
	//
	
	/**
	 * 
	 * @param	name
	 * 			The total name
	 * @return	Returns true if there is only one opening bracket, if it ends with a closing bracket
	 * 			if the special name is a valid combined name and the simple mixed name is valid
	 * 			| true if (Arrays.asList(name.split("\\(", 2)).size() == 2 && name.endsWith(")") )
	 * 			| && isValidMixedName(Arrays.asList(name.split("\\(", 2)).get(0).trim())
	 * 			| && isValidMixedName(Arrays.asList(name.split("\\(", 2)).get(1).substring(0, Arrays.asList(name.split("(", 2)).get(1).length() - 1))))
	 */
	public static boolean isValidTotalName(String name) {
		List<String> splitList = Arrays.asList(name.split("\\(", 2));
		int length= splitList.get(1).length();
		return splitList.size() == 2 && name.endsWith(")") && isValidMixedName(splitList.get(0).trim())
				&& isValidMixedName(splitList.get(1).substring(0, length - 1));
	}
	
	/**
	 * Check if the simple mixed name is valid
	 * 
	 * @param	name
	 * 			The mixed name
	 * @return	True if there's a valid name before and after the "mixed with", after each comma and before and after the "and"
	 * 			true if
	 * 			isValidCombinedName(name)
	 * 			||
	 * 			for (each combinedName in Arrays.asList(name.split(","))) 
	 * 				isValidCombinedName(combinedName.trim())
	 * 					&& indexOf(combinedName) != 0 && indexOf(combinedName) != Arrays.asList(name.split(",")).length() - 1
	 * 				|| (Arrays.asList(combinedName.split("mixed with")).size() == 2 
	 * 						&& isValidCombinedName(Arrays.asList(combinedName.split("mixed with")).get(0).trim())
	 * 						&& isValidCombinedName(Arrays.asList(combinedName.split("mixed with")).get(1).trim())  
	 * 						&& indexOf(combinedName) == 0    )
	 * 				|| (Arrays.asList(combinedName.split("and")).size() == 2 
	 * 						&& isValidCombinedName(Arrays.asList(combinedName.split("and")).get(0).trim())
	 * 						&& isValidCombinedName(Arrays.asList(combinedName.split("and")).get(1).trim())  
	 * 						&& indexOf(combinedName) == Arrays.asList(name.split(",")).size() - 1    )
	 * 			
	 * 
	 */
	public static boolean isValidMixedName(String name) {
		if (isValidCombinedName(name)) {
			return true;
		}
		
		List<String> splitList = Arrays.asList(name.split(","));
		int count = 0;
		for (String combinedName : splitList) {
			List<String> mixList = Arrays.asList(combinedName.split("mixed with"));
			List<String> andList = Arrays.asList(combinedName.split("and"));	
			if (!(isValidCombinedName(combinedName.trim()) && count != 0 && count !=  splitList.size() - 1)
					&& !((mixList.size() == 2) && isValidCombinedName(mixList.get(0).trim()) && isValidCombinedName(mixList.get(1).trim())
							&& count == 0 )
					&& !((andList.size() == 2) && isValidCombinedName(andList.get(0).trim()) && isValidCombinedName(andList.get(1).trim())
							&& count == splitList.size() - 1 )
					) {
				return false;
			}		
			count += 1;
		}
		return true;
		
	}
	
	// "with Coke" invalid
	// ()
	// "Heated Coke" valid
	// 
	
	/**
	 * Splits possible combined name in adjective and simple name and check whether both are valid
	 * 
	 * @param	name
	 * 			The name to be split
	 * @return	True if the combined name is either just a simple name, or if the first part of the name is a correct adjective
	 * 			and the second part is a simple name and there is more than one part (to prevent index errors)
	 * 			| True if
	 * 			| 
	 * 			| IngredientType.isValidSimpleName(name) || ( Arrays.asList(name.split(" ", 2)) != 1 && specialAdjective.contains(Arrays.asList(name.split(" ", 2)).get(0)) 
	 * 			| && IngredientType.isValidName(Arrays.asList(name.split(" ", 2)).get(1)) )
	 */		
	public static boolean isValidCombinedName(String name) {
		List<String> splitList = Arrays.asList(name.split(" ", 2));
		return IngredientType.isValidSimpleName(name) || (splitList.size() > 1 && specialAdjective.contains(splitList.get(0)) 
				&& IngredientType.isValidSimpleName(splitList.get(1)) );
	}
	
	// END LEGACY CODE
	
	/***************************************************************
	 * QUANTITY
	 ***************************************************************/
	/**
	 * Set the quantity of this Ingredient to 0 for all units
	 * 
	 * @post	The quantity of this Alchemic ingredient is 0
	 * 			| new.getQuantity() = Collections.nCopies(7, 0);
	 */
	public void setQuantityToZero() {
		quant = getZeroQuantityList(getSize());
	}
	
	/**
	 * Return an array list containing only zeroes
	 * 
	 * @return	An arraylist containing only zeroes
	 * 			| ArrayList<Integer>(Collections.nCopies(getConversionList().size(), 0))
	 * 
	 * @note	Not a static function, because if the size of conversion list changed, it should still work
	 */
	public static ArrayList<Integer> getZeroQuantityList(int size) {
		return new ArrayList<Integer>(Collections.nCopies(size, 0));
	}
	
	/**
	 * Return a list giving the conversion factors between quantities
	 * 
	 * @return	result == getState().getQuantities()
	 */
	
	private List<Quant> getConversionList() {
		return getState().getQuantities();
	}
	
	// make copy of list
	
	/**
	 * Return the quantity of a certain unit
	 * 
	 * @pre		The given unit must be a unit that exists for this state
	 * 			| getConversionList().contains(unit)
	 * @return	The quantity of that unit
	 * 			| result == getItemAt(getConversionList().indexOf(unit))
	 */
	
	public int getNumberOf(Quant unit) {
		return getItemAt(getConversionList().indexOf(unit));
	}
	/**
	 * Return the item registered at the given position in this directory.
	 * 
	 * @param 	index
	 *        	The index of the item to be returned.
	 * @pre 	The given index is strictly positive and does not exceed the number
	 *         	of quanties registered 
	 *			| (index < 1) || (index > getNbItems())
	 */
	protected int getItemAt(int index) {
		return quant.get(index);
	}
	
	/**
	 * Set the quantity for a specific unit
	 * 
	 * @param	index
	 * 			The index of the unit to be set
	 * @param	value
	 * 			The value to which we set our unit
	 * @pre		The value must be more than zero
	 * 			| value > 0
	 * @post	The value at that index is now equal to the given value
	 * 			| new.getItemAt(index) == value
	 */
	private void setItemAt(int index, int value) {
		this.quant.set(index, value);
	}
	
	/**
	 * Get the number of different quantities
	 * 
	 * @return	the size of the quantity list
	 * 			| quant.size()
	 */
	public int getSize() {
		return quant.size();
	}
	
	/**
	 * Check whether the quantity is properly carried over
	 * 
	 * @return	True if and only if there are no amounts above the quantities given by getConversionList()
	 * 			| True if and only if
	 * 			| for (each index in 1..getSize()-1)
	 * 			|	getConversionList().get(index + 1).getCVal() >= getItemAt(index)
	 */
	public boolean isCarriedOver() {
		for (int index = 0; index < getSize()-1; index++) {
			if (getConversionList().get(index + 1).getCVal() < getItemAt(index)) {
				return false;
			}
		}
		return true;
	}
	
	
	/**
	 * A list representing the quantities of this Ingredient Type
	 */
	private ArrayList<Integer> quant = null;
	
	/**
	 * Carry over the quantities of this Ingredient
	 * 
	 * @post	We have properly carried over the quantities of this ingredient
	 * 			| isCarriedOver() == true
	 * 			
	 */
	
	@Raw
	public void carryOver() {
		for (int index = 0; index < getSize()-1; index++) {
			int quantity = getItemAt(index);
			int max = getConversionList().get(index + 1).getCVal();
			setItemAt(index, quantity % max);
			int carryOver = quantity / max;
			setItemAt(index+1, getItemAt(index+1) + carryOver);
		}
	}
	
	/**
	 * Set the quantity of this ingredient to the given quantity
	 * 
	 * @param	quantity
	 * 			the quantity to which we are setting this quantity
	 * @pre 	The quantity is a valid quantity
	 * 			| isValidQuantity(quantity)
	 * @post	The quantity is validly carried over
	 * 			| isCarriedOver() == true 
	 * @post	The quantity of this Alchemic ingredient is to quantity
	 * 			| new.getQuantity() = quantity;
	 * 
	 */
	@Model
	protected void setQuantity(ArrayList<Integer> quantity) {
		this.quant = new ArrayList<Integer>(quantity);
		carryOver();
	}
	
	/**
	 * Check whether this quantity is a valid quantity
	 * 
	 * @param	quantity
	 * 			The quantity to be checked
	 * @return	True if the quantity has the correct size and each quantity is more than zero
	 * 			and the quantity is not zero for some quantities and the quantity will not exceed int max value when converted to zero
	 * 			
	 * @note	(the amount of drops when all quantities are one is 11090, as checked by python: sum([numpy.prod(x[:i]) for i in range(len(x))])
	 * 			and x= [1, 8, 5, 3, 7, 12, 5], and the amount of pinches is even smaller, 8618)
	 * 			| result == 
	 * 			| for (each quantity in quant)
	 * 			| 		quantity >= 0 && quantity < Integer.MAX_VALUE/11091
	 * 			| && for (some quantity in quant)
	 * 			| 		quantity != 0
	 */
	public static boolean isValidQuantity(ArrayList <Integer> quant) {
		boolean result = false;
		for (Integer quantity : quant) {
			if (quantity < 0 || quantity > Integer.MAX_VALUE/11091) {
				return false;
			} else if (quantity > 0) {
				result = true;
			}
		}
		return result;
	}
	
	/**
	 * Check whether this quantity is a valid quantity for this alchemic ingredient
	 * 
	 * @param	quant
	 * 			The quantity to be checked
	 * @return	True if the quantity has the correct size and is valid
	 * 			| result == isValidQuantity(quant) && getSize() == getConversionList().size()
	 */

	public boolean canHaveAsQuantity(ArrayList<Integer> quant) {
		return isValidQuantity(quant) && getSize() == getConversionList().size();
	}

	
	/**
	 * Return a list showing each unit in its lowest unit (pinches or drops)
	 * 
	 * @return	A list containing the product of the previous factors
	 * 			| result.get(index) ==
	 * 			| result.get(index - 1) * getConversionList().getCVal(index)
	 */
	private ArrayList<Integer> factorialList() {
		ArrayList<Integer> factorialList = new ArrayList<Integer>();
		int product = 1;
		for (Quant quant : getConversionList()) {
			product *= quant.getCVal();
			factorialList.add(product);
		}
		return factorialList;
	}
	/**
	 * Give unit in lowest unit
	 * 
	 * @param	index
	 * 			The index that you're unit takes up (if you have a unit, use getConversionList().indexOf(unit))
	 * 
	 * @return	An integer representing the unit in the lowest quantity
	 * 			| factorialList().get(getConversionList().indexOf(unit))
	 */
	public Integer convertToLowestUnit(Quant unit) {
		return factorialList().get(getConversionList().indexOf(unit));
	}
	
	
	/**
	 * Check whether this Alchemic ingredient fits the given unit
	 * 
	 * @param	unit
	 * 			The unit which we're checking against
	 * 
	 * @return	False if and only if
	 * 			The unit is in a different state than the one this Alchemic Ingredient is in or
	 * 			if the unit of this Alchemic Ingredient is larger than the container
	 * 			| True if and only if
	 * 			| unit.getType() == getState() && 0 < giveInLowestUnit().compareTo(convertToLowestUnit(unit))
	 * 
	 * @note	We assume this class satisfies the invariants, and is thus properly carried over
	 *  			
	 */
	public boolean fits(Quant unit) {
		if (unit.getType() != getState()) {
			return false;
		}
		return 0 >= giveInLowestUnit().compareTo(convertToLowestUnit(unit));
	}
	
	/**
	 * Change the quantity so it fits the given unit and return this alchemic ingredient
	 * 
	 * @post	This alchemic ingredient is now equal to the unit
	 * 			| convertToLowestUnit(unit) == giveInLowestUnit()
	 */
	public void can(Quant unit) {
		if (!fits(unit)) {
			for (int index = 0; index < getSize(); index++) {
				setItemAt(index, 0);
				if (index == getConversionList().indexOf(unit)) {
					setItemAt(index, 1);
				}
			}
		}
	}
	
	
	/**
	 * Returns the entire quantity in the lowest quantity.
	 * 
	 * @return	The quantity in the lowest quantity
	 * 			| sum( for (index in 1..getSize())
	 * 			|			this.getItemAt(index) * factorialList.get(index)	)			
	 *
	 */
	
	public Integer giveInLowestUnit() {
		int sum = 0;
		ArrayList<Integer> factorialList = factorialList();
		for (int index = 0; index < getSize(); index++) {
			sum += this.getItemAt(index) * factorialList.get(index);
		}
		return sum;
	}
	
	/**
	 * Convert to spoons
	 * 
	 * @return	the quantity in spoons
	 * 			| giveInLowestUnit() / getConversionList()[1]
 	 */
	public double giveInSpoons() {
		return giveInLowestUnit() / getConversionList().get(1).getCVal();
	}
	
	/**
	 * Find the transmogrified quantity of the given ingredient
	 * 
	 * @result	An arrayList which contains in the first element the amount of ingredient in spoons multiplied with the amount
	 * 			the new amount makes up in the new state and round down to make an integer
	 * 			| if (i == 0) {
	 * 			|	result.get(i) == (int) (ingredient.giveInSpoons() * state.getQuantities().get(1).getCVal())
	 * 			| else
	 * 			|	result.get(i) == 0
	 */
	public ArrayList<Integer> getTransmogrifiedQuant(State state) {
		ArrayList<Integer> resultList = getZeroQuantityList(state.getQuantities().size());
		resultList.set(0, (int) (this.giveInSpoons() * state.getQuantities().get(1).getCVal()));
		return resultList;
	}
	


	/***************************************************************
	 * TYPE
	 ***************************************************************/
	
	private IngredientType type = null;
	
	/**
	 * Check if the ingredient Type is null
	 * @param	type
	 * 			| The type to be checked
	 * @return	True if type is not null
	 * 			| result == (type != null)
	 */

	public static boolean isValidType(IngredientType type)	{
		return type != null;
	}
	/**
	 * Get the type of this Alchemic ingredient
	 */
	
	public IngredientType getType() {
		return this.type;
	}
	
	
	/***************************************************************
	 * STATE
	 ***************************************************************/
	
	
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
	
	/**
	 * Set the state to the given state
	 * 
	 * @param	state
	 * 			The state to which this Alchemic Ingredient is set
	 * @post	The new state is equal to the given state
	 * 			| new.getState() == state
	 */
	@Raw @Model
	private void setState(State state) {
		this.state = state;
	}
	
	/***************************************************************
	 * TEMPERATURE
	 ***************************************************************/
	
	/**
	 * Temperature to array
	 * 
	 * @return	The temperature converted to an array
	 * 			| if (temperature < 0)
	 * 			|		result == new long[]{-temperature, 0}
	 * 			|  else 
	 * 			|		result == new long[]{0, temperature}
	 */
	public static long[] temperatureToArray(long temperature) {
		if (temperature < 0) {
			return new long[]{-temperature, 0};
		} else {
			return new long[]{0, temperature};
		}
	}
	
	/**
	 * The coldness of this ingredient
	 */
	
	private long coldness = 0;
	
	/**
	 * The hotness of this ingredient
	 */
	
	private long hotness = 0;
	
	/**
	 * The max value of coldness and hotness
	 */
	
	private static long maxvalue = 0;
	
	/**
	 * Return the max temperature
	 */
	@Basic @Raw
	public static long getMaxTemperature() {
		return maxvalue;
	}
	
	/**
	 * Check whether this is a valid max value
	 * @param	maxvalue
	 * 			The given max temperature
	 * @return	True if and only if the max value is less than Long.MAX_VALUE and is positive
	 * 			| result == (0 < maxvalue && maxvalue < Long.MAX_VALUE)
	 */
	public static boolean isValidMaxTemperature(long maxvalue) {
		return 0 < maxvalue && maxvalue < Long.MAX_VALUE;
	}
	
	/**
	 * Set the maximum temperature
	 * @param	maxTemp
	 * 			The new maxTemp
	 * @post	The new max temperature is the given max temperature unless it is not valid in which it is set to 0 if less than zero
	 * 			or set to max_value if more than maxvalue
	 *			| new.getMaxTemperature() == long.min(long.max(maxTemp, 0), Long.MAX_VALUE)
	 *			
	 */
	public static void setMaxTemperature(long maxTemp) {
		if (!isValidMaxTemperature(maxTemp)) {
			maxTemp = Long.min(maxTemp, Long.MAX_VALUE);
			maxTemp = Long.max(maxTemp, 0);
		}
		maxvalue = maxTemp;
	}
	
	/**
	 * Check whether coldness and hotness are valid
	 * 
	 * @param	temperature
	 * 			The temperature to be checked
	 * @return 	True if coldness and hotness are between 0 and the max value
	 * 			| temperature[0] < getMaxTemperature() && 0 < temperature[0] &&  temperature[1] < getMaxTemperature() && 0 < temperature[1]
	 *			| && (temperature[1] == 0 || temperature[0] == 0);
	 */
	public static boolean isValidTemperature(long[] temperature) {
		return temperature[0] < getMaxTemperature() && 0 <= temperature[0] &&  temperature[1] < getMaxTemperature() && 0 <= temperature[1]
				&& (temperature[1] == 0 || temperature[0] == 0);
	}
	
	/**
	 * Return the coldness
	 */
	@Basic @Raw
	public long getColdness() {
		return coldness;
	}
	
	/**
	 * Return the hotness
	 */
	@Basic @Raw
	public long getHotness() {
		return hotness;
	}
	
	/**
	 * Set the coldness
	 * 
	 * @param	coldness
	 * 			The coldness to which we're setting this
	 * @post	The new coldness is equal to the given coldness, unless the coldness is 
	 * 			out of bounds in which case we set it to the closest bound
	 * 			| new.getColdness() == long.min(long.max(coldness, 0), getMaxTemperature());
	 */
	@Model
	private void setColdness(long coldness) {
		this.coldness = Long.min(Long.max(coldness, 0), getMaxTemperature());
	}
	
	/**
	 * Set the hotness
	 * 
	 * @param	hotness
	 * 			The hotness to which we're setting this
	 * @post	The new hotness is equal to the given hotness, unless the hotness is 
	 * 			out of bounds in which case we set it to the closest bound
	 * 			| new.getHotness() == Long.min(Long.max(hotness, 0), getMaxTemperature());
	 */
	@Model
	private void setHotness(long hotness) {
		this.hotness = Long.min(Long.max(hotness, 0), getMaxTemperature());
	}
	

	
	/**
	 * Cool the ingredient
	 * 
	 * @param	temp
	 * 			The temperature with which we cool
	 * @effect	The hotness is increased with the given heat minus the coldness if the temperature is higher than the coldness,
	 * 			else the coldness is decrease with the temp
	 * 			| if (getHotness() > temp):
	 * 			| 		setColdness(0)
	 * 			| 		setHotness(getHotness() - temp)
	 * 			| else
	 * 			| 		setHotness(0)
	 * 			| 		setColdness(takeSumAndBringDownToMax(getColdness(), temp)  - getHotness())	
	 * @note	Cooling with -30 degrees is interpreted as doing nothing
	 * 			
	 * 			
	 */
	public void cool(long temp) {
		if (temp <= 0) {
			// Do nothing
		} else if (getHotness() > temp) {
			setHotness(getHotness() - temp);
			setColdness(0);
		} else {
			setColdness(takeSumAndBringDownToMax(getColdness(), temp)  - getHotness());
			setHotness(0);
		}
	}
	
	/**
	 * Check whether the sum of two longs will not exceed Long.MAX_VALUE
	 * 
	 * @param	term1
	 * 			The long which we're adding to the other long
	 * @param	term2
	 * 			The long which we're adding to the other long
	 * @effect	Take the sum of both terms, then if they are larger than Long.MAX_VALUE, set them to Long.MAX_VALUE
	 * 			|  (BigInteger.valueOf((long) term1).add(BigInteger.valueOf((long) term2))).min(BigInteger.valueOf(Long.MAX_VALUE)).longValue()
	 */
	public long takeSumAndBringDownToMax(long term1, long term2) {
		return (BigInteger.valueOf((long) term1).add(BigInteger.valueOf((long) term2))).min(BigInteger.valueOf(Long.MAX_VALUE)).longValue();
	}
	
	/**
	 * Heat the ingredient
	 * 
	 * @param	temp
	 * 			The temperature with which we heat
	 * 
	 * @effect	The hotness is increased with the given heat minus the coldness if the temperature is higher than the coldness,
	 * 			else the coldness is decrease with the temp
	 * 
	 * 			| if (getColdness() > temp):
	 * 			| 		setHotness(0)
	 * 			| 		setColdness(getColdness() - temp)
	 * 			| else
	 * 			| 		setColdness(0)
	 * 			| 		setColdness(takeSumAndBringDownToMax(getColdness(), temp)  - getHotness())
	 * @note	Heating with -30 degrees is interpreted as doing nothing
	 * 			
	 */
	public void heat(long temp) {
		if (temp <= 0) {
			// Do nothing
		} else if (getColdness() > temp) {
			setColdness(getColdness() - temp);
			setHotness(0);
		} else {
			setHotness(takeSumAndBringDownToMax(getHotness(), temp) - getColdness());
			setColdness(0);
		}
	}
	
	/**
	 * Return the temperature as an array containing the coldness had hotness
	 * 
	 * @return	An array containing coldness and hotness
	 * 			| result == (new long[] {getColdness(), getHotness()})
	 */
	public long[] getTemperature() {
		return new long[] {getColdness(), getHotness()};
	}
	
	
	/**
	 * Get the name of this Alchemic Ingredient which reflects the temperature
	 * 
	 * @return	"Heated" if the temperature is higher than standard, "Cooled" if lower, "" if equal
	 * 			| if (compareTemperature(getTemperature(),getType().getStandardTemperature()) == 1)
	 * 			|		result == "Heated"
	 * 			| else if (compareTemperature(getTemperature(),getType().getStandardTemperature()) == -1)
	 * 			| 		result == "Cooled"
	 * 			| else
	 * 			| 		result == ""
	 * 
	 */			
	public String getTemperatureName() {
		if (compareTemperature(getTemperature(),getType().getStandardTemperature()) > 0) {
			return "Heated";
		} else if (compareTemperature(getTemperature(),getType().getStandardTemperature()) < 0) {
			return "Cooled";
		} else
			assert (getHotness() == getType().getStandardTemperature()[1]);
			assert (getColdness() == getType().getStandardTemperature()[0]);
			return "";
	}
	
	
	/**
	 * Compare two temperatures
	 * 
	 * @param	temp1
	 * 			An array containing coolness and hotness
	 * @param	temp2
	 * 			An array containing coolness and hotness
	 * @return	0 if temp1 is equal to temp2
	 * 			positive if temp1 is greater than temp 2
	 * 			negative if temp1 is lesser than temp 2
	 * 			| if (temp1[1] > temp2[1] || temp1[0] < temp2[0])
	 * 			|		result > 0
	 * 			| else if (temp1[0] > temp2[0] || temp1[1] < temp2[1])
	 * 			| 		result < 0
	 * 			| else
	 * 			| 		result == 0
	 * 
	 */			
	public static long compareTemperature(long[] temp1, long[] temp2) {
		return differenceTemperature(temp1, temp2);
	}
	
	/**
	 * Give the difference between two temperatures
	 * 
	 * @param	temp1
	 * 			An array containing coolness and hotness
	 * @param	temp2
	 * 			An array containing coolness and hotness
	 * @return	The difference between both temperatures
	 * 			| temp1[1] - temp2[1] + temp2[0] - temp1[0]
	 * 
	 */			
	public static long differenceTemperature(long[] temp1, long[] temp2) {
		return temp1[1] - temp2[1] + temp2[0] - temp1[0];
	}
	
	

	/**
	 * Adjust the temperature to the given temperature through heating or cooling
	 * 
	 * @param	targetTemp
	 * 			the temperature which we want our alchemic ingredient to have
	 * @post	The new temperature equals the target temperature unless the target temperature is out of bounds
	 * 			| if AlchemicIngredient.isValidTemperature(targetTemp)
	 * 			|	new.getTemperature() == targetTemp
	 */
	public void changeTempTo(long[] targetTemp) {
		if (AlchemicIngredient.isValidTemperature(targetTemp)) {
			long difference = differenceTemperature(targetTemp, this.getTemperature());
			if (difference > 0) {
				heat(difference);
			} else {
				cool(difference);
			}
			
		}
	}
	
	
	
	
	
	
	/***************************************************************
	 * volatility
	 ***************************************************************/
	
	
	/**
	 * The characteristic volatility of this Alchemic Ingredient
	 */
	
	private double characteristicvolatility = 0;
	
	/**
	 * Return the characteristic volatility of this Alchemic Ingredient
	 */
	@Raw @Basic
	public double getCharVolatility() {
		return this.characteristicvolatility;
	}
	
	/**
	 * Set up the volatility (only used in constructor)
	 * 
	 * @post	The volatility is set the a float which differs at max by 10% of
	 * 			the theoretical volatility within the bounds of zero or one
	 * 			| Double.max(Double.min((getType().getVolality() * (1 + (Math.random() - 0.5)/5) )  ,1), 0);	
	 */
	@Raw
	private void createVolatility()	 {
		this.characteristicvolatility =
			Double.max(Double.min((getType().getVolatility() * (1 + (Math.random() - 0.5)/5) )  ,0.9999999), 0);	
	}
	/**
	 * Set a characteristic volatility for this AlchemicIngredient (used in Kettle)
	 * 
	 * @param	volatility
	 * 			| the volatility to which the characteristic volatility is set
	 * @post	The new characteristic volatility equals the given volatility
	 * 			| new.getCharVolatility() == volatility
	 * @throws	IllegalStateException
	 * 			The given volatility is not valid
	 * 			| !isValidCharVolatility(volatility)
	 */
	@Model
	protected void setCharacteristicVolatility(double volatility) throws IllegalStateException {
		if (!isValidCharVolatility(volatility)) {
			throw new IllegalStateException("Volatility must be between 0 and 1!");
		}
		this.characteristicvolatility = volatility;
	}
	
	/**
	 * Return the effective volatility
	 * 
	 * 
	 * 
	 * 
	 * TODO: change
	 * @return	A constant or increasing volatility the higher this temperature is above its standard temperature
	 * 			and a constant or decreasing volatility the lower it is under its standard temperature
	 * 			| if this.getHotness > getType().getStandardTemperature()[1]
	 * 			| 		result >=
	 * 			| result if this.getHotness < getType().getStandardTemperature()[1]
	 * 			
	 * @note	getStandardTemperature()[1] > 0
	 * 
	 * 
	 * 
	 *  // TODO: DELETE
//	 * @return	If this Ingredient is hotter than its standard temperature or equalt to its standard temperature, its volatility is given 
//	 *			by its characteristic volatility multiplied by a 100 divided by 10 multiplied with its standard hotness plus
//	 *			the amount it is heated above its standard hotness divided by its standard hotness multiplied with its characteristic volatility times 300
//	 *			else multiply the volatility of the standard temperature with characteristic volatility for every 100% decrease in temperature 			
//	 * 			| if (getHotness() > getType().getStandardTemperature()[1]) 
//	 *			| 	result ==   standardVol + 300 * (getHotness() - getType().getStandardTemperature()[1]) / getType().getStandardTemperature()[1] 
//	 *			|				* getCharVolatility();
//	 *			| else if (getHotness() < getType().getStandardTemperature()[1])
//	 *			|	result ==   standardVol * Math.pow(getCharVolatility(), (getType().getStandardTemperature()[1] - (getHotness() - getColdness())) 
//	 *			|				/ getType().getStandardTemperature()[1]);		
//	 *			| else 
//	 *			|	result == standardVol;
	 * 
	 * 
	 * 
	 */
	
	public double getVolatility() {
		if (!isValidCharVolatility(getCharVolatility())) {
			throw new IllegalStateException("Invalid characteristic volatility");
		}
		if (getHotness() > getType().getStandardTemperature()[1]) {
			return  getStandardVolatility() + 300 * (getHotness() - getType().getStandardTemperature()[1]) / getType().getStandardTemperature()[1] 
					* getCharVolatility();
		} else if (getHotness() < getType().getStandardTemperature()[1]) {
			return getStandardVolatility() * Math.pow(getCharVolatility(), (getType().getStandardTemperature()[1] - (getHotness() - getColdness())) 
					/ getType().getStandardTemperature()[1]);
			
		} else {
			return getStandardVolatility();
		}
	}
	
	/**
	 * Get the standard volatility
	 * 
	 * @return	The characteristic volatility times a 100 times the standard hotness
	 * 			| result == getCharVolatility() * 100 * getType().getStandardTemperature()[1]/10
	 */
	public double getStandardVolatility() {
		return getCharVolatility() * 100 * getType().getStandardTemperature()[1]/10;
	}
	
	/**
	 * Return the name of this volatility
	 * TODO: change!
	 */
	public String getVolatilityName() {
		
		
		return "helloWorld";
	}
	
	/**
	 * Check whether this volatility is a valid volatility
	 * 
	 * @return	The characteristic volatility is between 0 and 1.
	 * 			| 0 < getCharVolatility() && getCharVolatility() < 1
	 */
	public static boolean isValidCharVolatility(double volatility) {
		return 0 <= volatility && volatility < 1;
	}
	
	
	
	
	
	
	
	
	
	
	
	

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

}
