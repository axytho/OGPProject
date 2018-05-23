package javaproject;

import java.util.ArrayList;

import be.kuleuven.cs.som.annotate.Basic;
import be.kuleuven.cs.som.annotate.Raw;
import javaproject.exception.*;
import javaproject.exception.StorageCapacityException;

public class Laboratory {
	
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
		for (AlchemicIngredient ingredient : storage) {
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
	 * Return the size of the storage
	 */
	@Raw 
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
	 * Check whether the given ingredient can be put at the given index
	 */
	public boolean isValidIngredientFor(AlchemicIngredient ingredient, int index) {
		if (!isValidNewIngredient(ingredient)) {
			return false;
		}
		if (index < 0 || index >= getSize()) {
			return false;
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
		if (this.containsIngredientName(container.getContents())) {
			if (!this.isValidNewIngredient(container.getContents())) {
				throw new IllegalArgumentException("Cannot have an alchemic ingredient with an ingredient type whose name "
						+ "is equal to another ingredient in this laboratory, but whose type is not the same (i.e. an ingredient BlueCat named cat"
						+ "and an ingredient RedCat named cat cannot sit in the same directory)");
			}
			addExtraIngredient();
		} else {
			
		}
		addNewIngredient(container);
	}

	/**
	 * Add an ingredient whose type is already in storage
	 */
	private void addExtraIngredient() {
		// TODO Auto-generated method stub
		
	}

	/**
	 * Add an ingredient new to this storage
	 * @param container
	 */
	private void addNewIngredient(IngredientContainer container) {
		storage.add(container.getContents());	
		container.empty();
		bringToStandardTemp();
	}
	
	
	private void bringToStandardTemp() {
		// TODO Auto-generated method stub
		
	}


	/**
	 * Check whether the Alchemic Ingredient has an Ingredient Type with a valid name
	 *  (i.e. there is no other, different Ingredient type that has the same name)
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
	

	

}
