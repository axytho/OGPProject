package javaproject;
import be.kuleuven.cs.som.annotate.*;
import quantity.*;

/**
 * 
 * @author	Jonas & Frederik
 * @invar	Liquid capacity must be a valid capacity
 * 			| isValidLCapacity(capacity)
 * @invar	Solid capacity must be a valid capacity
 * 			| isValidSCapacity(capacity)
 */

public class IngredientContainer {
	// IngredientContainer("Glass Bottle", LQuant.BOTTLE)
	public IngredientContainer(String name, Quant capacity) {
		this.capacity = capacity;
	}
	

	

	
	/**
	 * The capacity of solid storage 1 barrel
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
	 * 			| result == (capacity != null)
	 */
	public boolean isValidCapacity(Quant capacity) {
		return capacity != null;
	}
	



}
