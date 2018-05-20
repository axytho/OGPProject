package javaproject;

import java.util.ArrayList;
import java.util.EmptyStackException;
import java.util.Stack;

import be.kuleuven.cs.som.annotate.*;
import javaproject.exception.*;
import quantity.Quant;

/**
 * A device capable of performing operations on given Alchemic Ingredients
 * 
 * @invar	This device must have a valid number of items
 * 			| isValidNumberOfItems(getIngredients().size())
 * @author	Jonas
 * 				
 */

public abstract class Device {
	
	/**
	 * Create a new device
	 * 
	 * @post	Device storage is empty
	 * 			| new.isEmpty() == true
	 * @post	The result is null
	 * 			| new.getResult() == null
	 */
	public Device() {
		
	}
	
	/**
	 * Add a certain ingredient container
	 * 
	 * @param	container
	 * 			The container which we're adding
	 * @post	The content of the container is now stored in the device
	 * 			| new.getIngredients().contains(container) == true
	 * @post	If the device can only hold the current number of ingredients, the last ingredient in the device is replaced by the ingredient in the container
	 * 			| new.getIngredients().contains(old.getIngredients().get(getIngredients().size() - 1) == false
	 * @throws	emptyContainerException
	 * 			The container you're adding is empty
	 * 			| container.getContents() == null
	 */
	
	public void add(IngredientContainer container) throws EmptyContainerException {
		if (container.getContents() == null) {
			throw new EmptyContainerException();
		}
		if (!isValidNumberOfItems(getIngredients().size() +1)) {
			pop();
		} 
		deviceStorage.push(container.getContents());	
		container.empty();
	}
	
	/**
	 * Pop the last ingredient added from the device
	 * 
	 * @effect	Pop the last Alchemic Ingredient from the stack
	 * 			| deviceStorage.pop()
	 * 
	 * @throws	EmptyStackException
	 * 			The stack is empty
	 * 			| getIngredients().isEmpty() == true
	 */
	protected AlchemicIngredient pop() throws EmptyStackException {
		return deviceStorage.pop();
	}
	
	/**
	 * Clear the stack
	 * 
	 * @post	The device storage is cleared
	 * 			| new.isEmpty() == true
	 */
	protected void clear() {
		deviceStorage.clear();
	}
	
	
	
	/**
	 * Check whether the container can be added
	 * 
	 * @return	True if and only if the container is not null
	 * 			| container.getContents() != null
	 */
	@Raw
	public static boolean isValidContainer(IngredientContainer container) {
		return container.getContents() != null;
	}
	
	/**
	 * Set of all the ingredient containers added to the input
	 */
	private Stack<AlchemicIngredient> deviceStorage = new Stack<AlchemicIngredient>(); 
	
	/**
	 * The result of the device
	 */
	private AlchemicIngredient result = null;
	
	/**
	 * Return a list of the stack of ingredients currently in the input of the device
	 * 
	 * @return	result == new ArrayList<AlchemicIngredient>(deviceStorage)
	 */
	protected ArrayList<AlchemicIngredient> getIngredients() {
		return new ArrayList<AlchemicIngredient>(deviceStorage); 
	}
	
	/**
	 * Return the result of a reaction (and remove it from the device)
	 * 
	 * @post	The result is now empty
	 * 			| getResult() == null
	 * @return	A container containing the Alchemic Ingredient created by the reaction
	 * 			But the container cannot be the smallest or largest unit, so if it fits the smallest unit, the second largest type
	 * 			(always a SPOON) will still be returned, if it does not fit a barrel, the rest is thrown away and a filled barrel is returned
	 * 			| for each unit in  getResult().getState().getQuantities().subList(1, result.getSize()-1)
	 * 			|	if getResult().fits(unit) then result == new IngredientContainer("Result", unit, getResult())
	 * 			| else
	 * 			|	result.getName() == "Result"  && 
	 * 			|	result.getCapacity() == alchemResult.getState().getQuantities().get(alchemResult.getSize()-2)&&
	 * 			|	result.getContents().giveInLowestUnit() == result.getContents().convertToLowestUnit(
				|			alchemResult.getState().getQuantities().get(alchemResult.getSize()-2) )
	 */
	
	public IngredientContainer resultAfterReaction() throws EmptyResultException {
		AlchemicIngredient alchemResult = getResult();
		if (alchemResult == null) {
			throw new EmptyResultException();
		}
		emptyResult();
		for (Quant unit : alchemResult.getState().getQuantities().subList(1, alchemResult.getSize()-1)) {
			if (alchemResult.fits(unit)) {
				return new IngredientContainer("Result", unit, alchemResult);
			}
		}

		Quant largestUnit = alchemResult.getState().getQuantities().get(alchemResult.getSize()-2);
		alchemResult.can(largestUnit);
		return new IngredientContainer("Result", largestUnit, alchemResult);

	}
	
	
	/**
	 * Return the result without deleting it
	 */
	@Raw @Basic
	public AlchemicIngredient getResult() {
		return result;
	}
	
	/**
	 * Empty the result
	 * 
	 * @post	The result is now empty
	 * 			| getResult() == null 
	 */
	public void emptyResult() {
		setResult(null);
	}
	
	/**
	 * Set the result
	 * 
	 * @param	result
	 * 			The new result of this Device
	 * @post	The result is now equal to the new result
	 * 			| new.getResult == result
	 */
	@Model
	protected void setResult(AlchemicIngredient result) {
		this.result = result;
	}
	
	/**
	 * Check whether the device is empty
	 * 
	 * @return	True if the device is empty
	 * 			| result == deviceStorage.empty()
	 */
	public boolean isEmpty() {
		return deviceStorage.empty();
	}
	
	/**
	 * Execute an operation
	 * 
	 * @throws	EmptyStackException
	 * 			The stack is empty
	 * 			| isEmpty() == true
	 * @throws	Illegal state exception
	 * 			There are too many ingredients for this device
	 * 			| !isValidNumberOfIngredients(getIngredients().size())
	 */
	public void execute() throws EmptyResultException, IllegalStateException {
		if (this.isEmpty()) {
			throw new EmptyResultException();
		}
		if (!isValidNumberOfItems(getIngredients().size())) {
			throw new IllegalStateException("Too many ingredients for this device");
		}
	}
	
	/**
	 * Check whether this device has a valid number of items
	 */
	public abstract boolean isValidNumberOfItems(int number);
	
	
	
}
