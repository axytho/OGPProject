package javaproject;

import java.util.ArrayList;
import java.util.EmptyStackException;
import java.util.Stack;

import be.kuleuven.cs.som.annotate.*;
import javaproject.exception.*;
import quantity.Quant;

/**
 * A device capable of performing operations on given Alchemic Ingredients (defensive programming)
 * 
 * @invar	This device must have a valid number of items
 * 			| isValidNumberOfItems(getIngredients().size())
 * @invar	Device must be in a laboratory
 * 			| isInLab()
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
	public Device(Laboratory lab) {
		this.lab = lab;
	}
	
	/**
	 * The lab in which this device is stationed
	 */
	private Laboratory lab = null;
	
	/**
	 * Return the lab in which this device is stationed
	 */
	public Laboratory getLab() {
		return this.lab;
	}
	
	/**
	 * Set the laboratory to the given laboratory
	 * 
	 * @param	lab
	 * 			The lab in which this device sits
	 * @post	The lab is set to the given lab
	 * 			| new.getLab() == lab
	 * @note	Does not verify the bi-directional relationship, but does check everything else about lab
	 */
	@Raw @Model
	private void setLab(Laboratory lab) throws IllegalArgumentException{
		if (!lab.hasProperIngredients()) {
			throw new IllegalArgumentException("Invalid Lab");
		}
		this.lab = lab;
	}
	
	/**
	 * Add a certain ingredient container
	 * 
	 * @param	container
	 * 			The container which we're adding
	 * @post	The content of the container is now stored in the device
	 * 			| new.getIngredients().contains(container) == true
	 * @post	If the device can only hold the current number of ingredients, the last ingredient in the device is replaced 
	 * 			by the ingredient in the container and the last ingredient is terminated.
	 * 			| new.getIngredients().contains(old.getIngredients().get(getIngredients().size() - 1) == false
	 * 			| && old.getIngredients().get(getIngredients().size() - 1).isTerminated() == true
	 * @throws	emptyContainerException
	 * 			The container you're adding is empty
	 * 			| container.getContents() == null
	 * @throws	IllegalArgumentException
	 * 			The alchemic ingredient you're trying to add has already been terminated
	 */
	
	
	public void add(IngredientContainer container) throws EmptyContainerException, IllegalArgumentException {
		if (container.getContents() == null) {
			throw new EmptyContainerException();
		}
		if (container.getContents().isTerminated()) {
			throw new IllegalArgumentException("Container's ingredient is terminated!");
		}
		if (!isValidNumberOfItems(getIngredients().size() +1)) {
			pop().terminate();
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
	 * 			(always a SPOON) will still be returned, else the largest available container will be filled with the alchemic ingredient
	 * 			| stuffInsideContainer(getResult())
	 */
	
	public IngredientContainer result() throws EmptyResultException {
		AlchemicIngredient alchemResult = getResult();
		if (alchemResult == null) {
			throw new EmptyResultException();
		}
		emptyResult();
		return stuffInsideContainer(alchemResult);

	}

	/**
	 * 
	 * @param	alchemResult
	 * 			The alchemic ingredient we're trying to get into the largest possible container
	 * @return	A container containing the Alchemic Ingredient created by the reaction
	 * 			But the container cannot be the smallest or largest unit, so if it fits the smallest unit, the second largest type
	 * 			(always a SPOON) will still be returned, else the largest available container will be filled with the alchemic ingredien
	 * 			| for each unit in  alchemResult.getState().getQuantities().subList(1, alchemResult.getSize()-1)
	 * 			|	if alchemResult.fits(unit) then result == new IngredientContainer("Result", unit, alchemResult)
	 * 			| else
	 * 			|	result.getName() == "Result"  && 
	 * 			|	result.getCapacity() == alchemResult.getState().getQuantities().get(alchemResult.getSize()-2)&&
	 * 			|	result.getContents().giveInLowestUnit() == alchemResult.getContents().convertToLowestUnit(
	 *			|			alchemResult.getHighestContainerQuantity() )
	 */
	public static IngredientContainer stuffInsideContainer(AlchemicIngredient alchemResult) {
		for (Quant unit : alchemResult.getState().getQuantities().subList(1, alchemResult.getSize()-1)) {
			if (alchemResult.fits(unit)) {
				return new IngredientContainer("Result", unit, alchemResult);
			}
		}
		Quant largestUnit = alchemResult.getHighestContainerQuantity();
		alchemResult.can(largestUnit);
		return new IngredientContainer("Result", largestUnit, alchemResult);
	}
	

	
	
	/**
	 * Return the result without deleting it
	 */
	@Raw @Basic
	protected AlchemicIngredient getResult() {
		return result;
	}
	
	/**
	 * Empty the result
	 * 
	 * @post	The result is now empty
	 * 			| getResult() == null 
	 */
	protected void emptyResult() {
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
