package javaproject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import be.kuleuven.cs.som.annotate.*;
import quantity.*;

/**
 * @invar	Volality must be valid
 * 			| isValidVolality(getVolality())
 * @invar	Temperature must be valid
 * 			| isValidTemperature()
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
	
	
	/**
	 * Create a new Alchemic Ingredient Type
	 * 
	 * @param	Type
	 * 			The type of this ingredient
	 * @post	The type of this ingredient is set to the given type
	 * 			If the type is not a valid type, the type is set to Aether, a default type signifying "nothing".
	 * 			| setType(type)
	 * @post	The quantity of this Alchemic ingredient is 0
	 * 			| for (each index in 1..getSize())
	 * 			|		getItemAt(index)  == 0
	 * @post	The maximum temperature is set to 10000
	 * 			| getMaxTemperature() == 10000
	 */
	public AlchemicIngredient(IngredientType type) {
		if (isValidType(type)) {
			this.type = type;
		} else {
			this.type = new IngredientType("Aether", State.Liquid, new long[] {0, 0} );
		}
		setQuantityToZero();
		setState(getType().getState());
		setMaxTemperature(10000);
	}
	
	/**
	 * Create a new Alchemic Ingredient Type with a given Type
	 * 
	 * @param	type
	 * 			The type of this ingredient
	 * @param	quantity
	 * 			The quantity, represented in an arrayList
	 * @pre 	The quantity is a valid quantity
	 * 			| isValidQuantity(quantity)
	 * @post	The quantity is validly carried over
	 * 			| isCarriedOver() == true
	 * @post	The type of this ingredient is set to the given type
	 * 			If the type is not a valid type, the type is set to Aether, a default type signifying "nothing".
	 * 			| if (isValidType(type))
	 * 			|	new.getType() = type
	 * 			| else
	 * 			| 		this.type = new IngredientType("Aether", State.Liquid, new long[] {0, 0} )
	 * @post	The quantity of this Alchemic ingredient is to quantity
	 * 			| new.getQuantity() = quantity;
	 * @post	The maximum temperature is set to 10000
	 * 			| getMaxTemperature() == 10000
	 * 
	 */
	// TODO: delete or keep: @note	This is private because we want a user to give us a quantity as a full barrel or something like that
	
	public AlchemicIngredient(IngredientType type, ArrayList<Integer> quantity) {
		if (isValidType(type)) {
			this.type = type;
		} else {
			this.type = new IngredientType("Aether", State.Liquid, new long[] {0, 0} );
		}
		setQuantity(quantity);
		setState(getType().getState());
		setMaxTemperature(10000);
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
	
	/***************************************************************
	 * QUANTITY
	 ***************************************************************/
	/**
	 * Set the quantity of this Ingredient to 0 for all units
	 * 
	 * @post	The quantity of this Alchemic ingredient is 0
	 * 			| new.getQuantity() = Collections.nCopies(7, 0);
	 */
	@Raw
	private void setQuantityToZero() {
		quant = new ArrayList<Integer>(Collections.nCopies(getConversionList().size(), 0));
	}
	
	/**
	 * Return a list giving the conversion factors between quantities
	 * 
	 * @return	result == getType().getState().getQuantities()
	 */
	
	private List<Quant> getConversionList() {
		return getType().getState().getQuantities();
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
	
	private void setQuantity(ArrayList<Integer> quantity) {
		this.quant = quantity;
		carryOver();
	}
	
	/**
	 * Check whether this quantity is a valid quantity
	 * 
	 * @param	quantity
	 * 			The quantity to be checked
	 * @return	True if the quantity has the correct size and each quantity is more than zero
	 * 			| result == (quantity.size() == getConversionList().size()) &&
	 * 			| for (each quantity in quant)
	 * 			| 		quanity >= 0
	 */
	public boolean isValidQuantity(ArrayList <Integer> quant) {
		if (getSize() != getConversionList().size()) {
			return false;
		}
		for (int quantity : quant) {
			if (quantity < 0) {
				return false;
			}
		}
		return true;
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
	 * Check whether this quantity fits the given unit
	 * 
	 * @param	unit
	 * 			The unit which we're checking against
	 * 
	 * @return	False if and only if
	 * 			The unit is for a different state than the one this Alchemic Ingredient is in or
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
	 * Returns the entire quantity in the lowest quantity.
	 * 
	 * @return	The quantity in the lowest quantity
	 * 
	 * @post	dot product of factorialList() and this quantity list
	 * 			| sum( for (index in 1..getSize())
	 * 			|			this.getItemAt(index) * factorialList.get(index)	)
	 */
	
	public Integer giveInLowestUnit() {
		int sum = 0;
		ArrayList<Integer> factorialList = factorialList();
		for (int index = 0; index < getSize(); index++) {
			sum += this.getItemAt(index) * factorialList.get(index);
		}
		return sum;
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
	private State getState() {
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
	
	private long maxvalue = 0;
	
	/**
	 * Return the max temperature
	 */
	@Basic @Raw
	public long getMaxTemperature() {
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
	 *			| new.getMaxTemperature() == Long.min(Long.max(maxTemp, 0), Long.MAX_VALUE)
	 *			
	 */
	private void setMaxTemperature(long maxTemp) {
		if (!isValidMaxTemperature(maxTemp)) {
			maxTemp = Long.min(maxTemp, Long.MAX_VALUE);
			maxTemp = Long.max(maxTemp, 0);
		}
		maxvalue = maxTemp;
	}
	
	/**
	 * Check whether coldness and hotness are valid
	 * 
	 * @return 	True if coldness and hotness are between 0 and the max value
	 * 			| coldness < maxValue && 0 < coldness &&  hotness < maxValue && 0 < hotness && (hotness == 0 || coldness == 0)
	 */
	public boolean isValidTemperature() {
		return getColdness() < getMaxTemperature() && 0 < getColdness() &&  getHotness() < getMaxTemperature() && 0 < getHotness()
				&& (getHotness() == 0 || getColdness() == 0);
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
	 * 			| new.getColdness() == Long.min(Long.max(coldness, 0), getMaxTemperature());
	 */
	@Raw @Model
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
	@Raw @Model
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
	 * 			| 		setColdness(getColdness() - getHotness() + temp)	
	 * @note	Cooling with -30 degrees is interpreted as heating with 30 degrees, if you "cool" with less than - Long.MAX_INT,
	 * 			
	 */
	public void cool(long temp) {
		if (getHotness() > temp) {
			setHotness(getHotness() - temp);
			setColdness(0);
		} else {
			setColdness(getColdness() - getHotness() + temp);
			setHotness(0);
		}
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
	 * 			| 		setHotness(getHotness() - getColdness() + temp)	
	 * @note	Cooling with -30 degrees is interpreted as heating with 30 degrees, if you "cool" with less than - Long.MAX_INT,
	 * 			
	 */
	public void heat(long temp) {
		if (getColdness() > temp) {
			setColdness(getColdness() - temp);
			setHotness(0);
		} else {
			setHotness(getHotness() - getColdness() + temp);
			setColdness(0);
		}
	}
	
	/**
	 * Return the temperature as an array containing the coldness had hotness
	 * 
	 * @return	An array containing coldness and hotness
	 * 			| result == (new Long[] {getColdness(), getHotness()})
	 */
	public Long[] getTemperature() {
		return new Long[] {getColdness(), getHotness()};
	}
	
	
	

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

}
