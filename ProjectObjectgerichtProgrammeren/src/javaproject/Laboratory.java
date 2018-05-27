package javaproject;

import java.util.ArrayList;

import be.kuleuven.cs.som.annotate.Basic;
import be.kuleuven.cs.som.annotate.Model;
import be.kuleuven.cs.som.annotate.Raw;
import javaproject.exception.*;
import quantity.*;

/**
 * A laboratory where we can store ingredient and devices and utilize them to execute recipes
 * 
 * @invar	Each laboratory must have proper ingredients in it
 * 			| hasProperIngredients()
 * @author Jonas && Frederik
 * 
 * @note	The names used to sort are given by getName()
 * 			We assume that if there's a special name for an ingredient, that's the name that is searched,
 * 			else the mixed name or simple name is used, depending on availability
 *
 */

public class Laboratory {
	
	/**
	 * Create a laboratory with a given amount of storerooms
	 * @param	storeroom
	 * 			The capacity of this laboratory
	 * @post	The capacity is set to the given amount of storerooms
	 * 			| new.getCapacity() == storeroom;
	 */
	
	public Laboratory(int storeroom) {
		this.capacity = storeroom;
	}
	
	/**
	 * Get an ingredient on name and return the demanded quantity
	 * 
	 * @param	name
	 * 			The name on which we're searching our ingredient
	 * @param	unit
	 * 			The unit of ingredient to be retrieved
	 * @param	amount
	 * 			The amount of unit to be retrieved
	 * @effect	The ingredients quantity is reduced by the given amount
	 * 			| getIngredientAt(find(name)).setQuantityTo( getIngredientAt(find(name)).giveInLowestUnit()
	 * 			|		- getIngredientAt(find(name)).convertToLowestUnit(unit) * amount  )
	 * @return	An ingredient container containing the requested element
	 * 			| result.getContents().giveInLowestUnit() == ingredient.convertToLowestUnit(unit) * amount
	 * @throws	NameNotFoundException
	 * 			There is no ingredient with that name
	 * 			| find(name) == -1
	 * @throws	ExceedsContainerCapacityException
	 * 			Amount asked exceeds valid amount for ingredient container
	 * 			| getIngredientAt(find(name)).convertToLowestUnit(unit) * amount 
	 * 			| 	> getIngredientAt(find(name)).convertToLowestUnit(getIngredientAt(find(name)).getHighestContainerQuantity())
	 * @throws	ExceedsStorageException
	 * 			Amount asked exceeds the amount in the storage of the lab
	 * 			| getIngredientAt(find(name)).giveInLowestUnit() < getIngredientAt(find(name)).convertToLowestUnit(unit) * amount
	 * @throws	IllegalArgumentException
	 * 			The unit requested does not exist for the state of this ingredient
	 * 			| getIngredientAt(find(name)) != unit.getType()
	 */
	public IngredientContainer get(String name, Quant unit, int amount) 
				throws NameNotFoundException, ExceedsContainerCapacityException, ExceedsStorageException, IllegalArgumentException {
		if (find(name) == -1 ) {
			throw new NameNotFoundException(name, this);
		}
		AlchemicIngredient ingredient = getIngredientAt(find(name));
		if (ingredient.getState() != unit.getType()) {
			throw new IllegalArgumentException("The unit requested does not exist for the state of this ingredient");
		}
		int amountInStorage = ingredient.giveInLowestUnit();
		int amountToWithdraw = ingredient.convertToLowestUnit(unit) * amount;
		if (amountToWithdraw > ingredient.convertToLowestUnit(ingredient.getHighestContainerQuantity())) {
			throw new ExceedsContainerCapacityException();
		}
		if (amountInStorage < amountToWithdraw) {
			throw new ExceedsStorageException(amountToWithdraw, amountInStorage);
		}

		AlchemicIngredient result = new AlchemicIngredient(ingredient);
		
		if (amountInStorage - amountToWithdraw != 0) {
			ingredient.setQuantityTo(amountInStorage - amountToWithdraw);
		} else {
			removeIngredientAt(find(name));
		}
		result.setQuantityTo(amountToWithdraw);
		return Device.stuffInsideContainer(result);
	}
	
	/**
	 * The capacity of this laboratory in storerooms
	 */
	private int capacity = 0;
	
	/**
	 * Return the capacity of this laboratory
	 */
	@Raw @Basic
	public int getCapacity() {
		return this.capacity;
	}
	
	/**
	 * Check whether the given storage can fit this laboratory
	 * 
	 * @return	True if the total storage taken up by the alchemic ingredient does not exceed the capacity
	 * 			| sum(for ingredient in storage: ingredient.giveInStoreRooms()) + extraIngredient.giveInStoreRooms() <= getCapacity())
	 */
	public boolean canHaveAsExtraContents(AlchemicIngredient extraIngredient) {
		int sum = 0;
		for (AlchemicIngredient ingredient : getStorage()) {
			sum += ingredient.giveInStoreRooms();
		}
		return sum + extraIngredient.giveInStoreRooms() <= getCapacity();
	}
	
	
	
	
	
	/**
	 * The storage of this laboratory
	 * 
	 * @invar	The storage is not null (can be empty though)
	 * 			| storage != null
	 * @invar	None of the items in the storage are null
	 * 			| for ingredient in storage :
	 * 			|	ingredient != null
	 * @invar	Each ingredient is ordered lexographically
	 * 			| for each I in 1..getSize() - 1
	 * 			|	getIngredientAt(I).isOrderedAfter(I - 1)
	 * @invar	There cannot be items with the same name in the storage
	 * 			| if containsIngredientName(ingredient1) && containsIngredientName(ingredient2) then ingredient1 == ingredient2
	 */
	private ArrayList<AlchemicIngredient> storage = new ArrayList<AlchemicIngredient>();
	
	/**
	 * Get a copy of the storage of this laboratory
	 */
	@Raw @Basic
	private ArrayList<AlchemicIngredient> getStorage() {
		return new ArrayList<AlchemicIngredient>(storage);
	}
	
	/**
	 * Return the number of ingredients stored
	 */
	@Raw @Basic
	public int getSize() {
		return getStorage().size();
	}
	
	/**
	 * Get the item at the given index
	 * 
	 * @throws	IndexOutOfBoundsException
	 * 			The index is smaller than zero of equal or larger than the size
	 * 			| index < 0  || index >= getSize()
	 */
	@Basic @Raw
	private AlchemicIngredient getIngredientAt(int index) throws IndexOutOfBoundsException {
		return storage.get(index);
	}
	
	/**
	 * 
	 * Check whether the given ingredient can be put at the given index (checker for ingredients which are not in list)
	 * @param	ingredient
	 * 			The ingredient we're checking
	 * @param	index
	 * 			The index at which we'd like to add our ingredient
	 * @return	False if the storage already contains the given ingredient or if it would not be valid at the given index
	 * 			| if (containsIngredientName(ingredient) || !isValidIngredientFor(ingredient, index)): result == false
	 */
	@Raw
	public boolean isValidIngredientForAdding(AlchemicIngredient ingredient, int index) {
		if (containsIngredientName(ingredient)) {
			// ingredient should not be added at an index if it's already in the storage
			return false;
		}
		return isValidIngredientFor(ingredient, index);  
	}
	
	/**
	 * Check whether the given ingredient is valid at the given index
	 * 
	 * @param	ingredient
	 * 			The ingredient we're checking
	 * @param	index
	 * 			The index at which we'd like to add our ingredient
	 * @return	False if the ingredient is not a valid ingredient
	 * 			| if (!isValidNewIngredient(ingredient)): result == false
	 * @return	False if the index is out of bounds
	 * 			| if (index < 0 || index > getSize()): result == false
	 * @return	False if it is NOT true that 
	 * 				either the ingredient equals zero or the ingredient at the index before the given index 
	 * 					is ordered before this ingredient 
	 * 				and either the index equals the size of the storage or the ingredient at this index
	 * 					is ordered after the given index
	 * 				and this laboratory does not contain this ingredient
	 * 				else if it does contain this ingredient
	 * 			False if it is NOT true that 
	 * 				either the ingredient equals zero or the ingredient at the index before the given index 
	 * 					is ordered before this ingredient 
	 * 				and either the index equals the size of the storage minus one or the ingredient after the given ingredient
	 * 					is ordered after the given given ingredient
	 * 				and this laboratory does not contain this ingredient
	 * 			| if (!containsIngredientName(ingredient)
	 * 			|	(index == 0 || getIngredientAt(index-1).isOrderedBefore(ingredient))
	 * 			|		&& (index == getSize() || getIngredientAt(index).isOrderedAfter(ingredient))
	 * 			| else
	 * 			| (index == 0 || getIngredientAt(index-1).isOrderedBefore(ingredient))			
	 *			|	&& (index == getSize() - 1 || getIngredientAt(index + 1).isOrderedAfter(ingredient))
	 */
	@Raw
	public boolean isValidIngredientFor(AlchemicIngredient ingredient, int index) {
		if (!isValidNewIngredient(ingredient)) {
			return false;
		}
		if (index < 0 || index > getSize()) {
			return false;
		}
		if (containsIngredientName(ingredient)) {
			return (index == 0 || getIngredientAt(index-1).isOrderedBefore(ingredient))			
					&& (index == getSize() - 1 || getIngredientAt(index + 1).isOrderedAfter(ingredient));
		}
		return (index == 0 || getIngredientAt(index-1).isOrderedBefore(ingredient))			
				&& (index == getSize() || getIngredientAt(index).isOrderedAfter(ingredient));  
	}
	
	/**
	 * Check whether the storage of this laboratory is correct
	 * 
	 * @return	Each ingredient of this laboratory is at its correct place and is a valid ingredient
	 * 			| result ==
	 * 			| 		for each I in 0..getSize() - 1:
	 * 			|			isValidIngredientFor(I, getIngredientAt(I)) && AlchemicIngredient.compareTemperature(getIngredientAt(index).getTemperature(), 
							getIngredientAt(index).getType().getStandardTemperature()) == 0
	 */
	public boolean hasProperIngredients() {
		for (int index = 0; index < getSize(); index++) {
			if (!isValidIngredientFor(getIngredientAt(index), index) 
					|| AlchemicIngredient.compareTemperature(getIngredientAt(index).getTemperature(), 
							getIngredientAt(index).getType().getStandardTemperature()) != 0) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * Remove the ingredient at the given index from this laboratory.
	 *
	 * @param 	index
	 *        	The index from the item to remove.
	 * @post	This laboratory no longer has the item at the given index as an item
	 * 			| !new.containsIngredientName(getIngredientAt(index).getName())
	 * @post  	All ingredients to the right of the removed ingredient
	 *        	are shifted left by 1 position.
	 *        	| for each I in index+1..getSize()-1:
	 *        	|   new.getIngredientAt(I-1) == old.getIngredientAt(I)
	 * @post  	The number of items has decreased by one
	 *        	| new.getSize() == getSize() - 1
	 */
	@Raw @Model 
	private void removeIngredientAt(int index) throws IndexOutOfBoundsException{
		storage.remove(index);
	}
	
	
	/**
	 * Add a certain ingredient container
	 * 
	 * @param	container
	 * 			The container which we're adding
	 * @post	The content of the container is now stored in the device
	 * 			| new.getIngredients().contains(container) == true
	 * @effect	The ingredients of our container are brought to standard temperature
	 * 			| bringToStandardTemp(container)
	 * @effect	The ingredients of our container are brought to standard state
	 * 			| bringToStandardState(container)
	 * @throws	EmptyContainerException
	 * 			The container you're adding is empty
	 * 			| container.getContents() == null
	 */
	
	public void add(IngredientContainer container) throws EmptyContainerException {
		if (container.getContents() == null) {
			throw new EmptyContainerException();
		}
		if (!canHaveAsExtraContents(container.getContents())) {
			throw new StorageCapacityException(this, container);
		}
		if (!this.isValidNewIngredient(container.getContents())) {
			throw new IllegalArgumentException("Not a valid new ingredient");
		}
		bringToStandardTemp(container);
		bringToStandardState(container);
		if (this.containsIngredientName(container.getContents())) {
			addExtraIngredient(container);
		} else {
			addNewIngredient(container);
		}
	}
	
	/**
	 * Add an ingredient resulting from a mix between 2 ingredients with the same name
	 * 
	 * @param	ingredient
	 * 			The ingredient we're adding
	 * @effect	The ingredient is at the correct index in the laboratory storage
	 * 			| addIngredientAt(findFit(container.getContents().getName()), container.getContents())
	 */
	@Raw
	private void addNewMixedIngredient(AlchemicIngredient ingredient) {
		addIngredientAt(findFit(ingredient.getName()), ingredient);	
	}
	
	/**
	 * Add a given ingredient at a given index
	 * 
	 * @post	The new ingredient is added at the given index
	 * 			| getStorage().get(index) == ingredient
	 * @throws	IllegalArgumentException
	 * 			The ingredient cannot be added at the correct location
	 * 			| !isValidIngredientForAdding(ingredient, index)
	 */
	@Basic @Model
	private void addIngredientAt(int index, AlchemicIngredient ingredient) {
		if (!isValidIngredientForAdding(ingredient, index)) {
			throw new IllegalArgumentException("This ingredient does not work at the given location");
		}
		storage.add(index, ingredient);
	}
	

	/**
	 * Add an ingredient whose type is already in storage
	 * 
	 * @throws	IllegalStateException
	 * 			This laboratory does not have a kettle
	 * 			| !hasKettle()
	 * @effect	The new ingredient is mixed with the existing ingredient and added to storage
	 *			|	finishBrew(mix(container, Device.stuffInsideContainer(getIngredientAt(find(container.getContents().getName())))))	
	 */
	@Raw
	private void addExtraIngredient(IngredientContainer container) throws IllegalStateException {
		// mixing gives the wrong characteristic volatility, but we set it later
		assert(containsIngredientName(container.getContents()));
		mixCompletely(container.getContents(), getIngredientAt(find(container.getContents().getName())));
	}
	
	/**
	 * Finish adding the ingredient
	 * 
	 * @param	alchemicIngredient
	 * 			The first ingredient
	 * @param	alchemicIngredient2
	 * 			The second ingredient 
	 * @effect	Finish the brew with the resulting container from mix and the two ingredient container
	 * 			| finishBrew(alchemicIngredient, alchemicIngredient2, mix(alchemicIngredient, alchemicIngredient2))
	 * @note	We get the contents from the containers before they're destroyed by mix
	 */
	@Raw
	private void mixCompletely(AlchemicIngredient alchemicIngredient, AlchemicIngredient alchemicIngredient2) {
		finishBrew(alchemicIngredient, alchemicIngredient2, mix(alchemicIngredient, alchemicIngredient2));
	}
	
	/**
	 * Finish adding the ingredient
	 * 
	 * @param	result
	 * 			The result to be added
	 * @param	newIngredient
	 * 			The new ingredient
	 * @param	oldIngredient
	 * 			The old ingredient
	 * @effect	Change the volatility of the given ingredient in the container and add to laboratory
	 * 			| changeContainerVolatilityToAverage(newIngredient, oldIngredient,  result)
	 * 			| addNewMixedIngredient(result)
	 */
	@Raw
	private void finishBrew(AlchemicIngredient newIngredient, AlchemicIngredient oldIngredient, AlchemicIngredient result) {
		changeContainerVolatilityToAverage(newIngredient, oldIngredient,  result);
		addNewMixedIngredient(result);	
	}
	

	
	/**
	 * Change the volatility of the ingredient in a container so it is the average of the 2 given ingredients
	 * 
	 * @param	ingredient1
	 * 			The first ingredient
	 * @param	ingredient2
	 * 			The second ingredient
	 * @param	result
	 * 			The result in which the to-be-changed ingredient resides
	 * @effect	The characteristic volatility of the result is set to the average of the two given ingredients
	 * 			|result.setCharacteristicVolatility( (
	 *			|	existingIngredient.getCharVolatility() *
	 *			|	existingIngredient.giveInLowestUnit() +
	 *			|	newIngredient.getCharVolatility() * newIngredient.giveInLowestUnit()  )
	 *			|	/ (existingIngredient.giveInLowestUnit() + newIngredient.giveInLowestUnit()))
	 */
	@Raw
	private void changeContainerVolatilityToAverage(AlchemicIngredient ingredient1, AlchemicIngredient ingredient2, AlchemicIngredient result) {
		result.setCharacteristicVolatility( (
				ingredient1.getCharVolatility() *
				ingredient1.giveInLowestUnit() +
				ingredient2.getCharVolatility() * ingredient2.giveInLowestUnit()  )
				/ (ingredient1.giveInLowestUnit() + ingredient2.giveInLowestUnit()));
	}
	
	/**
	 * Mix two ingredient containers with each other
	 * @param	alchemicIngredient
	 * 			The first ingredient
	 * @param	alchemicIngredient2
	 * 			The second ingredient
	 * @effect	The two ingredients are mixed after the kettle is cleared of previous ingredients and the old ingredient is removed from storage
	 * 			(and the result is requested, clearing the result of the kettle)
	 * 			|	removeIngredientAt(find(alchemicIngredient2.getContents().getName()))
	 * 			|	returnKettle().clear()
	 * 			|	returnKettle().add(ingredient1)
	 * 			|	returnKettle().add(ingredient2)
	 * 			|	returnKettle().execute()
	 * 			|	returnKettle().result() 
	 * @return	The result of the kettle
	 * 			| result == returnKettle().result()
	 */
	@Raw
	private AlchemicIngredient mix(AlchemicIngredient alchemicIngredient, AlchemicIngredient alchemicIngredient2) {
		removeIngredientAt(find(alchemicIngredient2.getName()));
		returnKettle().clear();
		returnKettle().add(alchemicIngredient);
		returnKettle().add(alchemicIngredient2);
		returnKettle().execute();
		return returnKettle().ingredientResult();
		
	}

	/**
	 * Add an ingredient new to this storage
	 * @param	ingredient
	 * 			The container we're adding
	 * @effect	The result of heating/cooling the container's ingredient  to standard temperature
	 * 			is at the correct index in the laboratory storage
	 * 			| addIngredientAt(findFit(container.getContents().getName()), container.getContents())
	 * @post	The container is empty
	 * 			| container.getContents() == null
	 */
	@Raw
	private void addNewIngredient(IngredientContainer container) {
		addIngredientAt(findFit(container.getContents().getName()), container.getContents());	
		container.empty();	
	}
	
	/**
	 * Bring the given container's ingredient to its standard temperature change the container to the resulting container of the temperature device
	 * 
	 * @param	container
	 * 			The container which contains the ingredient we're bringing to standard temperature
	 * @effect	The contents of the container are brought to standard temperature		
	 * 			| if (container.getContents().getTemperatureName() == AlchemicIngredient.Temperature.COOLED) 
	 *			| returnFridge().changeTemperature(container.getContents().getType().getStandardTemperature())
	 *			| returnFridge().add(container)
	 *			| returnFridge().execute()
	 *		    | container =  returnFridge().result()
	 *			|  else if (container.getContents().getTemperatureName() == AlchemicIngredient.Temperature.HEATED)
	 *			| returnOven().changeTemperature(container.getContents().getType().getStandardTemperature())
	 *			| returnOven().add(container)
	 *			| returnOven().execute()
	 *			| container = returnOven().result()
	 * @throws	IllegalStateException
	 * 			This laboratory does not have a Oven (if heating) or a Cooling Box (if cooling)
	 * 			| (AlchemicIngredient.compareTemperature(container.getContents().getTemperature(), 
	 *			| 			container.getContents().getType().getStandardTemperature()) < 0 && !hasValidFridge())
	 *			| || AlchemicIngredient.compareTemperature(container.getContents().getTemperature(), 
	 * 			|  			container.getContents().getType().getStandardTemperature()) > 0 && !hasValidOven())
	 */
	@Raw
	private void bringToStandardTemp(IngredientContainer container) throws IllegalStateException {
		if (container.getContents().getTemperatureState() == AlchemicIngredient.Temperature.COOLED) {
			returnFridge().changeTemperature(container.getContents().getType().getStandardTemperature());
			returnFridge().add(container);
			returnFridge().execute();
			container =  returnFridge().result();
		} else if (container.getContents().getTemperatureState() == AlchemicIngredient.Temperature.HEATED) {
			returnOven().changeTemperature(container.getContents().getType().getStandardTemperature());
			returnOven().add(container);
			returnOven().execute();
			container = returnOven().result();
		}
	}
	
	/**
	 * Bring the given container's ingredient to its standard temperature change the container to the resulting container of the temperature device
	 * 
	 * @param	container
	 * 			The container which contains the ingredient we're bringing to standard temperature
	 * @effect			
	 * 			| if (container.getContents().getTemperatureName() == AlchemicIngredient.Temperature.COOLED) 
	 *			| returnFridge().changeTemperature(container.getContents().getType().getStandardTemperature())
	 *			| returnFridge().add(container)
	 *			| returnFridge().execute()
	 *		    | container =  returnFridge().result()
	 *			|  else if (container.getContents().getTemperatureName() == AlchemicIngredient.Temperature.HEATED)
	 *			| returnOven().changeTemperature(container.getContents().getType().getStandardTemperature())
	 *			| returnOven().add(container)
	 *			| returnOven().execute()
	 *			| container = returnOven().result()
	 * @throws	IllegalStateException
	 * 			This laboratory does not have a Oven (if heating) or a Cooling Box (if cooling)
	 * 			| (AlchemicIngredient.compareTemperature(container.getContents().getTemperature(), 
	 *			| 			container.getContents().getType().getStandardTemperature()) < 0 && !hasFridge())
	 *			| || AlchemicIngredient.compareTemperature(container.getContents().getTemperature(), 
	 * 			|  			container.getContents().getType().getStandardTemperature()) > 0 && !hasOven())
	 */
	@Raw
	private void bringToStandardState(IngredientContainer container) throws IllegalStateException {
		if (container.getContents().getState() != container.getContents().getType().getState()) {
			returnTransmogrifier().add(container);
			returnTransmogrifier().execute();
			container =  returnTransmogrifier().result();
		} 
	}


	/**
	 * Check whether the Alchemic Ingredient has an Ingredient Type with a valid name
	 *  (i.e. there is no other, different Ingredient type that has the same name)
	 *  
	 * @param	newIngredient
	 *  		The ingredient to be checked
	 * @return	True if and only if there is no alchemic ingredient with a type that has the same name and alchemic ingredient is not terminated
	 * 			| result == !newIngredient.isTerminated() && (!containsIngredientName(newIngredient) 
	 * 			|		|| getIngredientAt(find(newIngredient.getName())).getType() == newIngredient.getType())
	 */
	@Raw
	public boolean isValidNewIngredient(AlchemicIngredient newIngredient) {
		return newIngredient.isValidIngredient() && (!containsIngredientName(newIngredient) 
					|| getIngredientAt(find(newIngredient.getName())).getType() == newIngredient.getType());
	}
	
	/**
	 * Check whether the Alchemic Ingredient has an Ingredient Type with the same name
	 * 
	 * @param	newIngredient
	 *  		The ingredient to be checked
	 * @return	True if and only if there is no alchemic ingredient with a type that has the same name
	 * 			| result == (find(newIngredient.getName()) != -1)
	 * 
	 * @note	Why do we call it containsIngredientName and not containsIngredient? Because we're searching on name, and not type!
	 */
	@Raw
	public boolean containsIngredientName(AlchemicIngredient newIngredient) {
		return find(newIngredient.getName()) != -1;
	}
	
	
	/**
	 * Find the ingredient with the given name and return the index, else return -1 (binary search implementation)
	 * 
	 * @param	name
	 * 			The name of the ingredient whose index we're trying to find
	 * @return	The index of name
	 * 			| if (for some I in 1..getSize()-1: getIngredientAt(I).getName() == name)
	 * 			|	then result == I
	 * 			| else
	 * 			|	result == -1
	 */
	@Raw
	public int find(String name) {
		int left = 0;
		int right = getSize()-1;
		int middle = left+right/2;
		int difference = 0;
		while (right >= left) {
			difference = getIngredientAt(middle).getName().compareTo(name);
			if (difference < 0) {
				left = middle + 1;
			} else if (difference > 0) {
				right = middle - 1;
			} else {
				return middle;
			}
			middle = left+right/2;
		}
		return -1;
	}
	
	/**
	 * Find where a given name fits
	 * 
	 * @param	name
	 * 			The name of the ingredient whose index we're trying to find
	 * @return	The index of the place behind which we should put our name 
	 * 			| ( name.isOrderedAfter(getIngredientAt(result - 1).getName()) || result == 0   )
	 * 			| 		 && ( result == getSize() || name.isOrderedBefore(getIngredientAt(result).getName())
	 * @return	0 is this list is empty
	 * 			| if (right == 0)
	 * 			|		result == 0
	 * @throws	IllegalArgumentException
	 * 			The given name is already in the database
	 * 			| find(name) != -1
	 */
	@Raw
	public int findFit(String name) throws IllegalArgumentException {
		int left = 0;
		int right = getSize();
		if (right == 0) {
			return 0;
		}
		int middle = (left+right)/2;
		int difference = 0;
		while (right >= left) {
			if (middle == getSize()) {
				// Because we want to be able to return getSize(), it must be possible for middle to be getSize(), and thus a seperate check is needed
				assert(getIngredientAt(middle - 1).getName().compareTo(name) < 0);
				return middle;
			}
			difference = getIngredientAt(middle).getName().compareTo(name);
			if (difference < 0) {
				left = middle + 1;
			} else if (difference > 0) {
				right = middle - 1;
			} else {
				throw new IllegalArgumentException("Name already in database");
			}
			middle = (left+right)/2;
		}
		// right = left - 1 and floor(right + left / 2) == middle
		assert(right == middle || right == -1 && middle == 0);
		return middle;
	}
	
	/**
	 * Return an iterator containing all the ingredients in the storage of this laboratory
	 * 
	 * 
	 */
	public StorageIterator getStorageIterator() {
		return new StorageIterator() {
			/**
			 * The current position of the iterator
			 */
			public int current = 0;

			/**
			 * Return the current element
			 * 
			 * @see	StorageIterator
			 * 
			 * @throws	IllegalStateException
			 * 			This iterator does not contain any elements
			 * 			| getStorage().size() == 0
			 */
			public int getCurrent() throws IllegalStateException {
				if (storageList.size() == 0) {
					throw new IllegalStateException("This iterator is empty!");
				}
				return current;
			}
			
			/**
			 * Return the number of elements remaining in this iterator
			 * 
			 * @see	StorageIterator
			 * 
			 * @return	The amount of elements left, minus the current element
			 * 			| result == getStorage().size() - getCurrent() - 1
			 */
			@Override
			public int getNbElements() {
				return storageList.size() - getCurrent() - 1;
			}

			/**
			 * Advance the storage iterator with one ingredient
			 * 
			 * @see		StorageIterator
			 * 
			 * @post	The new position is one greater than the current position
			 * 			| new.getNbElements() == old.getNbElements() - 1
			 */
			@Override
			public void advance() throws IllegalStateException {
				if (getNbElements() == 0) {
					throw new IllegalStateException("No more elements left in the iterator");
				}
				current += 1;
			}

			/**
			 * Reset the storage iterator to the first element
			 * 
			 * @see	StorageIterator
			 */
			@Override
			public void reset() {
				current = 0;
			}
			
			/**
			 * A copy of our storage list, listing all our Alchemic Ingredients
			 */
			private ArrayList<AlchemicIngredient> storageList = getStorage();			
		};
	}
	
	
	public String toString() {
		String sum = "";
		for (AlchemicIngredient ingredient : getStorage()) {
			sum += ingredient.getName() + ": "+ Double.toString(ingredient.giveInSpoons()) + " spoons, ";
		}
		return sum.substring(0, getSize() - 2);
	}
	
	/***************************************************************
	 * DEVICES (Bi-directional!)
	 ***************************************************************/
	
	
	/**
	 * The oven of our lab
	 */
	private Oven LabOven = null;
	
	/**
	 * The fridge of our lab
	 */
	
	private CoolingBox LabFridge = null;
	
	/**
	 * The transmogrifier of our lab
	 */
	private Transmogrifier LabTrans = null;
	
	/**
	 * The Kettle of our lab
	 */
	private Kettle LabKettle = null;
	
	/**
	 * Add a new device to this laboratory from a different laboratory
	 * 
	 * @param	device
	 * 			The device we're adding to this laboratory
	 * @effect	The device is moved to this laboratory
	 * 			| device.move(this)
	 */
	public void moveToHere(Device device) {
		device.move(this);
	}
	
	/**
	 * Return the oven of this lab
	 * 
	 * @throws	IllegalStateException
	 * 			There is no oven in this lab
	 * 			| !hasOven()
	 * @return	The oven
	 * 			| result == getOven()
	 */
	public Oven returnOven() {
		if (!hasValidOven()) {
			throw new IllegalStateException("This lab has no oven");
		}
		return getOven();
	}
	/**
	 * Return the cooling box of this lab
	 * 
	 * @throws	IllegalStateException
	 * 			There is no fridge in this lab
	 * 			| !hasFridge()
	 * @return	The fridge
	 * 			| result == getFridge()
	 */
	public CoolingBox returnFridge() {
		if (!hasValidFridge()) {
			throw new IllegalStateException("This lab has no fridge");
		}
		return getFridge();
	}
	/**
	 * Return the transmogrifier of this lab
	 * 
	 * @throws	IllegalStateException
	 * 			There is no transmogrifier in this lab
	 * 			| !hasTransmogrifier()
	 * @return	The Transmogrifier
	 * 			| result == getTransmogrifier()
	 */
	public Transmogrifier returnTransmogrifier() {
		if (!hasValidTransmogrifier()) {
			throw new IllegalStateException("This lab has no transmogrifier");
		}
		return getTransmogrifier();
	}
	
	/**
	 * Return the kettle of this lab
	 * 
	 * @throws	IllegalStateException
	 * 			There is no kettle in this lab
	 * 			| !hasKettle()
	 * @return	The kettle
	 * 			| result == getKettle()
	 */
	public Kettle returnKettle() {
		if (!hasValidKettle()) {
			throw new IllegalStateException("This lab has no kettle");
		}
		return getKettle();
	}
	
	/**
	 * Return the oven
	 */
	@Raw @Basic
	protected Oven getOven() {
		return this.LabOven;
	}
	
	/**
	 * Return the cooling box
	 */
	@Raw @Basic
	protected CoolingBox getFridge() {
		return this.LabFridge;
	}
	
	/**
	 * Return the kettle
	 */
	@Raw @Basic
	protected Kettle getKettle() {
		return this.LabKettle;
	}
	
	/**
	 * Return the transmogrifier
	 */
	@Raw @Basic
	protected Transmogrifier getTransmogrifier() {
		return this.LabTrans;
	}
	
	
	/**
	 * Set the oven in this lab
	 * 
	 * @param	oven
	 * 			The oven which we put in our lab
	 * @post	The lab oven is set to the given oven
	 * 			| new.returnOven() == oven
	 */
	@Model
	protected void setOven(Oven oven) {
		LabOven = oven;
	}
	
	/**
	 * Set the cooling box in this lab
	 * 
	 * @param	box
	 * 			The box which we put in our lab
	 * @post	The lab cooling box is set to the given box
	 * 			| new.returnFridge() == box
	 */
	@Model
	protected void setFridge(CoolingBox box) {
		LabFridge = box;
	}
	/**
	 * Set the kettle in this lab
	 * 
	 * @param	kettle
	 * 			The kettle which we put in our lab
	 * @post	The lab kettle is set to the given kettle
	 * 			| new.returnKettle() == kettle
	 */
	@Model
	protected void setKettle(Kettle kettle) {
		LabKettle = kettle;
	}
	
	/**
	 * Set the transmogrifier in this lab
	 * 
	 * @param	transmogrifier
	 * 			The transmogrifier which we put in our lab
	 * @post	The lab transmogrifier is set to the given transmogrifier
	 * 			| new.returnTransmogrifier() == transmogrifier
	 */
	@Model
	protected void setTransmogrifier(Transmogrifier transmogrifier) {
		LabTrans = transmogrifier;
	}
	
	/**
	 * Check whether this device has a valid oven
	 * 
	 * @return	This lab has an oven and that oven has this lab as its lab
	 * 			| hasOven && returnOven().isInCorrectLab()
	 */
	public boolean hasValidOven() {
		return hasOven() && getOven().isInCorrectLab();
	}
	
	/**
	 * Check whether this device has a valid Cooling box
	 * 
	 * @return	This lab has a fridge and that fridge has this lab as its lab
	 * 			| hasFridge && returnFridge().isInCorrectLab()
	 */
	public boolean hasValidFridge() {
		return hasFridge() && getFridge().isInCorrectLab();
	}
	
	/**
	 * Check whether this device has a valid Kettle
	 * 
	 * @return	This lab has an Kettle and that Kettle has this lab as its lab
	 * 			| hasKettle && returnKettle().isInCorrectLab()
	 */
	public boolean hasValidKettle() {
		return hasKettle() && getKettle().isInCorrectLab();
	}
	
	/**
	 * Check whether this device has a valid Transmogrifier
	 * 
	 * @return	This lab has an Transmogrifier and that Transmogrifier has this lab as its lab
	 * 			| hasTransmogrifier && returnTransmogrifier().isInCorrectLab()
	 */
	public boolean hasValidTransmogrifier() {
		return hasTransmogrifier() && getTransmogrifier().isInCorrectLab();
	}
	
	/**
	 * Check whether this lab has an oven
	 * @return	This oven is not null
	 * 			| LabOven != null
	 */
	@Raw
	public boolean hasOven() {
		return LabOven != null;
	}
	
	/**
	 * Check whether this lab has a fridge
	 * @return	This oven is not null
	 * 			| LabFridge != null
	 */
	@Raw
	public boolean hasFridge() {
		return LabFridge != null;
	}
	
	/**
	 * Check whether this lab has a transmogrifier
	 * @return	This oven is not null
	 * 			| LabTrans != null
	 */
	@Raw
	public boolean hasTransmogrifier() {
		return LabTrans != null;
	}
	
	/**
	 * Check whether this lab has a kettle
	 * @return	This oven is not null
	 * 			| LabKettle != null
	 */
	@Raw
	public boolean hasKettle() {
		return LabKettle != null;
	}
	
	
	
	
	
	
	/***************************************************************
	 * RECIPES
	 ***************************************************************/
	
	
	
	public void execute(Recipe recipe, int multiplier) {
		ExecutiveRecipe executive = new ExecutiveRecipe(recipe);
		executive.setFactor(multiplier);
		for (ExecutiveRecipe.Instruction instruction: executive.getRecipe().getInstructions()) {
			try {
				instruction.execute(this);
			} catch (ExceedsStorageException e1) {
				//System.err.println(String.valueOf(e1.getAmountAsked()) + " is more than " + String.valueOf(e1.getAmountAvailable()));
				executive.returnToStorage(this);
			} catch (IllegalStateException e2) {
				System.err.println("You do not have the necessary devices in your lab");
			} catch (ExceedsContainerCapacityException e3) {
				System.err.println("You are attempting to use a greater quantity than can fit in a barrel/chest");
			} catch (NameNotFoundException e4) {
				System.err.println("The ingredient described cannot be found in this laboratory");
			}
		}
		ExecutiveRecipe.Instruction mix = executive.new Mix();
		mix.execute(this);
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
