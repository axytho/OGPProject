package javaproject;
import be.kuleuven.cs.som.annotate.*;
import quantity.*;

/**
 * 
 * @author	Jonas & Frederik
 * @invar	Capacity must be a valid capacity
 * 			| isValidCapacity(capacity)
 * @invar	Contents must be a valid contents
 * 			| isValidContents(contents)
 */

public class IngredientContainer {
	// IngredientContainer("Glass Bottle", LQuant.BOTTLE)
	
	/**¨
	 * Create an empty ingredient container with a given name and capacity
	 * @param	name
	 * 			The name of this ingredient container
	 * @param	capacity
	 * 			The capacity of this ingredient container
	 * @pre		The capacity must be a valid capacity
	 * 			| isValidCapacity(capacity)
	 * @pre		The contents must be valid
	 * 			| isValidContents(contents)
	 * @effect	The capacity is set to the given capacity
	 * 			| setCapacity(capacity)
	 * @post	The new name is the given nam
	 * 			| new.getName()	= name
	 * @post	The new contents are null
	 * 			| new.getContents(
	 */
	public IngredientContainer(String name, Quant capacity) {
		this(name, capacity, null);
		
	}
	
	/**¨
	 * Create an empty ingredient container with a given name and capacity
	 * @param	name
	 * 			The name of this ingredient container
	 * @param	capacity
	 * 			The capacity of this ingredient container
	 * @param	content
	 * 			The content of this Ingredient Container
	 * @pre		The capacity must be a valid capacity
	 * 			| isValidCapacity(capacity)
	 * @pre		The contents must be valid
	 * 			| isValidContents(contents)
	 * @effect	The capacity is set to the given capacity
	 * 			| setCapacity(capacity)
	 * @post	The new name is the given nam
	 * 			| new.getName()	= name
	 * @effect	The content is set to the given content
	 * 			| setContent(content)
	 */
	public IngredientContainer(String name, Quant capacity, AlchemicIngredient content) {
		setCapacity(capacity);
		this.name = name;
		setContents(content);		
	}
	
	
	
	
	
	/**
	 * The name of this ingredient container
	 */
	public String name = "";
	
	/**
	 * Get the name of this ingredient container
	 */
	public String getName() {
		return this.name;
	}
	
	/**
	 * Empty the storage
	 * 
	 * @post	The storage is empty
	 * 			| new.getContents() == null
	 */
	public void empty() {
		setContents(null);
	}
	
	
	/**
	 * The contents of the storage
	 * 
	 * @param	content
	 * 			An amount of alchemic ingredient
	 * @pre		The contents must be valid
	 * 			| isValidContents(contents)
	 */
	@Raw
	private void setContents(AlchemicIngredient content) {
		this.content = content;
	}
	
	// [0, 0, 0, 1, 0, 1, 0]
	// [0, 0, 0, 0, 0, 0, 0]
	/**
	 * Checks whether the contents are valid
	 * 
	 * @return	True if the contents are less than the capacity, content agrees with the type of storage and the contents are not zero.
	 * 			| or if the given contents don't exist. (If they exist, they should have a quantity greater than zero though)
	 * 			| for index in 1..content.getSize():
	 * 			|		content.getItemAt() 
	 */
	
	public boolean canHaveAsContents(AlchemicIngredient content) {
		return content.fits(capacity) || content == null;
	}
	
	/**
	 * The contents of this Ingredient Container
	 */
	public AlchemicIngredient content = null;
	
	/**
	 * Return the contents of this Ingredient Container
	 */
	public AlchemicIngredient getContents() {
		return this.content;
	}

	
	/**
	 * The capacity
	 */
	
	public Quant capacity = null;
	
	/**
	 * @param	capacity
	 * 			The capacity which this container will get
	 * @pre		capacity must be a valid capacity
	 * 			| isValidCapacity(capacity)
	 * @post	the capacity of this container is now the given capacity
	 * 			| new.getCapacity() = capacity
	 * 
	 */
	@Model
	public void setCapacity(Quant capacity) {
		this.capacity = capacity;
	}
	
	/**
	 * Get the capacity of this IngredientContainer
	 */
	@Raw @Basic
	public Quant getCapacity() {
		return this.capacity;
	}

	
	/**
	 * Check whether this capacity is a valid capacity for this container
	 * @param 	capacity
	 * 			The liquid capacity to be checked
	 * @return	True if the given liquid capacity is not null or if there's already solid capcity
	 * 			| result == (capacity != null && capacity != LQuant.STOREROOM && capacity != SQuant.STOREROOM && capacity != LQuant.DROP 
				&& capacity != SQuant.PINCH)
	 */
	public static boolean isValidCapacity(Quant capacity) {
		return capacity != null && capacity != LQuant.STOREROOM && capacity != SQuant.STOREROOM && capacity != LQuant.DROP 
				&& capacity != SQuant.PINCH;
	}
	



}
