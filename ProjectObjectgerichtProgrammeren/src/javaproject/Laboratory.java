package javaproject;

import java.util.ArrayList;

import be.kuleuven.cs.som.annotate.Basic;
import be.kuleuven.cs.som.annotate.Model;
import be.kuleuven.cs.som.annotate.Raw;
import javaproject.exception.*;
import javaproject.exception.StorageCapacityException;
import quantity.*;

/**
 * A laboratory where we can store ingredient and devices and utilize them to execute recipes
 * 
 * @invar	Each laboratory must have proper ingredients in it
 * 			| hasProperIngredients()
 * @author Jonas && Frederik
 *
 */

public class Laboratory {
	// TODO: make it work for special name/mixed name
	
	/**
	 * Create a laboratory with a given amount of storerooms
	 * @param	storeroom
	 * 			The capacity of this laboratory
	 * @post	The capacity is set to the given amount of storerooms
	 * 			| this.capacity = storeroom;
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
	 * @throws	IllegalArgumentException
	 * 			Amount asked exceeds valid amount for ingredient container
	 * 			| getIngredientAt(find(name)).convertToLowestUnit(unit) * amount 
	 * 			| 	> getIngredientAt(find(name)).convertToLowestUnit(getIngredientAt(find(name)).getHighestContainerQuantity())
	 * @throws	IllegalArgumentException
	 * 			Amount asked exceeds the amount in the storage of the lab
	 * 			| getIngredientAt(find(name)).giveInLowestUnit() < getIngredientAt(find(name)).convertToLowestUnit(unit) * amount
	 */
	public IngredientContainer get(String name, Quant unit, int amount) throws NameNotFoundException, IllegalArgumentException, ExceedsStorageException {
		if (find(name) == -1 ) {
			throw new NameNotFoundException(name, this);
		}
		AlchemicIngredient ingredient = getIngredientAt(find(name));
		int amountInStorage = ingredient.giveInLowestUnit();
		int amountToWithdraw = ingredient.convertToLowestUnit(unit) * amount;
		if (amountToWithdraw > ingredient.convertToLowestUnit(ingredient.getHighestContainerQuantity())) {
			throw new ExceedsContainerCapacityException();
		}
		if (amountInStorage < amountToWithdraw) {
			throw new ExceedsStorageException(amountToWithdraw, amountInStorage);
		}

		AlchemicIngredient result = new AlchemicIngredient(ingredient);
		ingredient.setQuantityTo(amountInStorage - amountToWithdraw);
		result.setQuantityTo(amountToWithdraw);
		return Device.stuffInsideContainer(result);
	}
	
	/**
	 * The capacity of this laboratory in storerooms
	 */
	public int capacity = 0;
	
	/**
	 * Return the capacity of this laboratory
	 */
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
	public AlchemicIngredient getIngredientAt(int index) throws IndexOutOfBoundsException {
		return storage.get(index);
	}
	
	/**
	 * Check whether the given ingredient can be put at the given index (checker for ingredients which are not in list)
	 * 
	 * @param	ingredient
	 * 			The ingredient we're checking
	 * @param	index
	 * 			The index at which we'd like to add our ingredient
	 * @return	False if the ingredient is not a valid ingredient
	 * 			| if (!isValidNewIngredient(ingredient)): result == false
	 * @return	False if the index is out of bounds
	 * 			| if (index < 0 || index > getSize()): result == false
	 * @return	False if the storage already contains the given ingredient
	 * 			| if (containsIngredientName(ingredient)): result == false
	 * @return	False if it is NOT true that 
	 * 				either the ingredient equals zero or the ingredient at the index before the given index 
	 * 					is ordered before this ingredient 
	 * 				and either the index equals the size of the storage or the ingredient at this index
	 * 					is ordered after the given index
	 * 			| (index == 0 || getIngredientAt(index-1).isOrderedBefore(ingredient))			
	 *			|	&& (index == getSize() || getIngredientAt(index).isOrderedAfter(ingredient))
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
			// ingredient should not be added at an index if it's already in the storage
			return false;
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
	 * @throws	emptyContainerException
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
			throw new IllegalArgumentException("Cannot have an alchemic ingredient with an ingredient type whose name "
					+ "is equal to another ingredient in this laboratory, but whose type is not the same (i.e. an ingredient BlueCat named cat"
					+ "and an ingredient RedCat named cat cannot sit in the same directory)");
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
	 * Add a given ingredient at a given index
	 * 
	 * @post	The new ingredient is added at the given index
	 * 			| getStorage().get(index) == ingredient
	 * @throws	IllegalArgumentException
	 * 			The ingredient cannot be added at the correct location
	 * 			| !isValidIngredientFor(ingredient, index)
	 */
	@Basic @Model
	private void addIngredientAt(int index, AlchemicIngredient ingredient) {
		if (!isValidIngredientFor(ingredient, index)) {
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
	 * @effect 	We add both the contents of our container and the ingredient with the same name to a kettle and mix
	 * 			| getKettle().add(Device.stuffInsideContainer(getIngredientAt(find(container.getContents().getName()))))
	 * 			| 		getKettle().add(container);
	 *			|	getKettle().execute();
	 *			|	container = getKettle().result();
	 *			|	container.getContents().setCharacteristicVolatility( (
	 *			|	getIngredientAt(find(container.getContents().getName())).getCharVolatility() *
	 *			|	getIngredientAt(find(container.getContents().getName())).giveInLowestUnit() +
	 *			|	container.getContents().getCharVolatility() * container.getContents().giveInLowestUnit()  )
	 *			|	/ (getIngredientAt(find(container.getContents().getName())).giveInLowestUnit() 
	 *			|							+ container.getContents().giveInLowestUnit()));
	 *
	 */
	private void addExtraIngredient(IngredientContainer container) throws IllegalStateException {
		// mixing gives the wrong characteristic volatility, but we set it later
		assert(containsIngredientName(container.getContents()));
		AlchemicIngredient newIngredient = container.getContents();
		AlchemicIngredient existingIngredient = getIngredientAt(find(container.getContents().getName()));
		getKettle().add(Device.stuffInsideContainer(existingIngredient));
		getKettle().add(container);
		getKettle().execute();
		container = getKettle().result();
		container.getContents().setCharacteristicVolatility( (
				existingIngredient.getCharVolatility() *
				existingIngredient.giveInLowestUnit() +
				newIngredient.getCharVolatility() * newIngredient.giveInLowestUnit()  )
				/ (existingIngredient.giveInLowestUnit() + newIngredient.giveInLowestUnit()));
				
	}

	/**
	 * Add an ingredient new to this storage
	 * @param	container
	 * 			The container we're adding
	 * @effect	The result of heating/cooling the container's ingredient  to standard temperature
	 * 			is at the correct index in the laboratory storage
	 * 			| addIngredientAt(findFit(container.getContents().getName()), container.getContents())
	 * @post	The container is empty
	 * 			| container.getContents() == null
	 */
	private void addNewIngredient(IngredientContainer container) {
		addIngredientAt(findFit(container.getContents().getName()), container.getContents());	
		container.empty();	
	}
	
	/**
	 * Bring the given container's ingredient to its standard temperature change the container to the resulting container of the temperature device
	 * 
	 * @param	container
	 * 			The container which contains the ingredient we're bringing to standard temperature
	 * @effect			
	 * 			| if (container.getContents().getTemperatureName() == AlchemicIngredient.Temperature.COOLED) 
	 *			| getFridge().changeTemperature(container.getContents().getType().getStandardTemperature())
	 *			| getFridge().add(container)
	 *			| getFridge().execute()
	 *		    | container =  getFridge().result()
	 *			|  else if (container.getContents().getTemperatureName() == AlchemicIngredient.Temperature.HEATED)
	 *			| getOven().changeTemperature(container.getContents().getType().getStandardTemperature())
	 *			| getOven().add(container)
	 *			| getOven().execute()
	 *			| container = getOven().result()
	 * @throws	IllegalStateException
	 * 			This laboratory does not have a Oven (if heating) or a Cooling Box (if cooling)
	 * 			| (AlchemicIngredient.compareTemperature(container.getContents().getTemperature(), 
	 *			| 			container.getContents().getType().getStandardTemperature()) < 0 && !hasFridge())
	 *			| || AlchemicIngredient.compareTemperature(container.getContents().getTemperature(), 
	 * 			|  			container.getContents().getType().getStandardTemperature()) > 0 && !hasOven())
	 */
	
	private void bringToStandardTemp(IngredientContainer container) throws IllegalStateException {
		if (container.getContents().getTemperatureState() == AlchemicIngredient.Temperature.COOLED) {
			getFridge().changeTemperature(container.getContents().getType().getStandardTemperature());
			getFridge().add(container);
			getFridge().execute();
			container =  getFridge().result();
		} else if (container.getContents().getTemperatureState() == AlchemicIngredient.Temperature.HEATED) {
			getOven().changeTemperature(container.getContents().getType().getStandardTemperature());
			getOven().add(container);
			getOven().execute();
			container = getOven().result();
		}
	}
	
	/**
	 * Bring the given container's ingredient to its standard temperature change the container to the resulting container of the temperature device
	 * 
	 * @param	container
	 * 			The container which contains the ingredient we're bringing to standard temperature
	 * @effect			
	 * 			| if (container.getContents().getTemperatureName() == AlchemicIngredient.Temperature.COOLED) 
	 *			| getFridge().changeTemperature(container.getContents().getType().getStandardTemperature())
	 *			| getFridge().add(container)
	 *			| getFridge().execute()
	 *		    | container =  getFridge().result()
	 *			|  else if (container.getContents().getTemperatureName() == AlchemicIngredient.Temperature.HEATED)
	 *			| getOven().changeTemperature(container.getContents().getType().getStandardTemperature())
	 *			| getOven().add(container)
	 *			| getOven().execute()
	 *			| container = getOven().result()
	 * @throws	IllegalStateException
	 * 			This laboratory does not have a Oven (if heating) or a Cooling Box (if cooling)
	 * 			| (AlchemicIngredient.compareTemperature(container.getContents().getTemperature(), 
	 *			| 			container.getContents().getType().getStandardTemperature()) < 0 && !hasFridge())
	 *			| || AlchemicIngredient.compareTemperature(container.getContents().getTemperature(), 
	 * 			|  			container.getContents().getType().getStandardTemperature()) > 0 && !hasOven())
	 */
	
	private void bringToStandardState(IngredientContainer container) throws IllegalStateException {
		if (container.getContents().getState() != container.getContents().getType().getState()) {
			getTransmogrifier().add(container);
			getTransmogrifier().execute();
			container =  getTransmogrifier().result();
		} 
	}


	/**
	 * Check whether the Alchemic Ingredient has an Ingredient Type with a valid name
	 *  (i.e. there is no other, different Ingredient type that has the same name)
	 *  
	 * @param	newIngredient
	 *  		The ingredient to be checked
	 * @return	True if and only if there is no alchemic ingredient with a type that has the same name
	 * 			| result == (!containsIngredientName(newIngredient) 
	 * 			|		|| getIngredientAt(find(newIngredient.getName())).getType() == newIngredient.getType())
	 */
	public boolean isValidNewIngredient(AlchemicIngredient newIngredient) {
		return (!containsIngredientName(newIngredient) || getIngredientAt(find(newIngredient.getName())).getType() == newIngredient.getType());
	}
	
	/**
	 * Check whether the Alchemic Ingredient has an Ingredient Type with the same name
	 * 
	 * @param	newIngredient
	 *  		The ingredient to be checked
	 * @return	True if and only if there is no alchemic ingredient with a type that has the same name
	 * 			| result == (find(newIngredient.getName()) != -1)
	 */
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
		assert(right == middle);
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
			 * 			| new.getNbElements() = old.getNbElements() - 1
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
	
	
	// TODO: make it return a prettier quantity and write code
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
	Oven LabOven = null;
	
	/**
	 * The fridge of our lab
	 */
	
	CoolingBox LabFridge = null;
	
	/**
	 * The transmogrifier of our lab
	 */
	Transmogrifier LabTrans = null;
	
	/**
	 * The Kettle of our lab
	 */
	Kettle LabKettle = null;
	
	/**
	 * Return the oven of this lab
	 * 
	 * @throws	IllegalStateException
	 * 			There is no oven in this lab
	 * 			| !hasOven()
	 */
	public Oven getOven() {
		if (!hasOven()) {
			throw new IllegalStateException("This lab has no oven");
		}
		return this.LabOven;
	}
	/**
	 * Return the cooling box of this lab
	 * 
	 * @throws	IllegalStateException
	 * 			There is no fridge in this lab
	 * 			| !hasFridge()
	 */
	public CoolingBox getFridge() {
		if (!hasFridge()) {
			throw new IllegalStateException("This lab has no fridge");
		}
		return this.LabFridge;
	}
	/**
	 * Return the transmogrifier of this lab
	 * 
	 * @throws	IllegalStateException
	 * 			There is no transmogrifier in this lab
	 * 			| !hasTransmogrifier()
	 */
	public Transmogrifier getTransmogrifier() {
		if (!hasTransmogrifier()) {
			throw new IllegalStateException("This lab has no transmogrifier");
		}
		return this.LabTrans;
	}
	
	/**
	 * Return the kettle of this lab
	 * 
	 * @throws	IllegalStateException
	 * 			There is no kettle in this lab
	 * 			| !hasKettle()
	 */
	public Kettle getKettle() {
		if (!hasKettle()) {
			throw new IllegalStateException("This lab has no kettle");
		}
		return this.LabKettle;
	}
	
	/**
	 * Put an oven in this lab
	 */
	public void intializeOven() {
		LabOven = new Oven(this, new long[] {0, 0});
	}
	/**
	 * Put a cooling box in this lab
	 */
	public void intializeFridge() {
		LabFridge = new CoolingBox(this, new long[] {0, 0});
	}
	/**
	 * Put a kettle in this lab
	 */
	public void initializeKettle() {
		LabKettle = new Kettle(this);
	}
	
	/**
	 * Put a transmogrifier in this lab
	 */
	public void intializeTrans() {
		LabTrans = new Transmogrifier(this);
	}
	
	
	/**
	 * Check whether this lab has an oven
	 * @return	This oven is not null
	 * 			| LabOven != null
	 */
	public boolean hasOven() {
		return LabOven != null;
	}
	
	/**
	 * Check whether this lab has a fridge
	 * @return	This oven is not null
	 * 			| LabFridge != null
	 */
	public boolean hasFridge() {
		return LabFridge != null;
	}
	
	/**
	 * Check whether this lab has a transmogrifier
	 * @return	This oven is not null
	 * 			| LabTrans != null
	 */
	public boolean hasTransmogrifier() {
		return LabTrans != null;
	}
	
	/**
	 * Check whether this lab has a kettle
	 * @return	This oven is not null
	 * 			| LabKettle != null
	 */
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
