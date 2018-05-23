package javaproject.exception;

import javaproject.*;

public class StorageCapacityException extends RuntimeException {
	
	


	/**
	 * This lab does not have the capacity for the given element
	 * @param	lab
	 * 			The laboratory to which we tried to add the ingredient container
	 * @param	container
	 * 			The container which we tried adding to the lab
	 * @post	The container of this exception is set to the given container
	 * 			| new.getContainer() = container
	 * @post	The laboratory of this exception is set to the given exception
	 * 			| new.getLaboratory() = lab 
	 */
	public StorageCapacityException(Laboratory lab, IngredientContainer container) {
		this.lab = lab;
		this.container = container;
	}
	
	/**
	 * The laboratory on which the exception was thrown
	 */
	private Laboratory lab = null;
	
	/**
	 * The ingredient container which we tried adding to the storage of this lab
	 */
	private IngredientContainer container = null;
	
	/**
	 * Return the laboratory
	 */
	public Laboratory getLaboratory() {
		return lab;
	}
	/**
	 * Return the Ingredient Container
	 */
	public IngredientContainer getIngredientContainer() {
		return container;
	}
	
	/**
	 * Serial version ID
	 */
	private static final long serialVersionUID = 1L;

}
