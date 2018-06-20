package javaproject;

import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Stack;

import be.kuleuven.cs.som.annotate.*;
import be.kuleuven.cs.som.annotate.Model;
import be.kuleuven.cs.som.annotate.Raw;
import javaproject.Recipe.Amount;
import javaproject.exception.ExceedsContainerCapacityException;
import javaproject.exception.ExceedsStorageException;
import javaproject.exception.NameNotFoundException;

/**
 * 
 * @author Jonas
 *
 */

public class ExecutiveRecipe {

	
	/**
	 * Construct a new Executive recipe refering to a given recipe
	 * 
	 * @param	recipe
	 * 			The recipe which this Executive recipe is meant to help execute
	 * @post	The recipe of this executive recipe is set to the given recipe
	 * 			| new.getRecipe() == recipe
	 */
	public ExecutiveRecipe(Recipe recipe) {
		this.recipe = recipe;
	}

	/**
	 * The recipe which this Executive recipe is meant to help execute
	 */
	private Recipe recipe = null;
	
	/**
	 * Return the recipe which this Executive recipe is meant to help execute
	 */
	@Raw @Basic
	public Recipe getRecipe() {
		return this.recipe;
	}
	

	

		public void add(Laboratory lab) throws NameNotFoundException, ExceedsContainerCapacityException, ExceedsStorageException {
			push(lab.get(getCurrentAmount().getIngredientType().getName(), getCurrentAmount().getUnit(), getCurrentAmount().getQuantity()));
			nextAmount();
		}

	

		public void heat(Laboratory lab) throws IllegalStateException {
			lab.returnOven().changeTemperature(getLastCurrentElement().getContents().getTemperature());
			lab.returnOven().deltaTemperature(50);
			lab.returnOven().add(pop());
			lab.returnOven().execute();
			push(lab.returnOven().result());
		}

	

		
		public void cool(Laboratory lab) throws IllegalStateException {
			lab.returnFridge().changeTemperature(getLastCurrentElement().getContents().getTemperature());
			lab.returnFridge().deltaTemperature(-50);
			lab.returnFridge().add(pop());
			lab.returnFridge().execute();
			push(lab.returnFridge().result());
		}

	

		public void mix(Laboratory lab) throws IllegalStateException {
			while (getCurrentItems().size() > 0) {
				lab.returnKettle().add(pop());
			}
			lab.returnKettle().execute();
			push(lab.returnKettle().result());
		}

	
	
	/**
	 * The factor with which this recipe is multiplied
	 */
	private int factor = 1;
	
	/**
	 * Return the factor with which we multiply our ingredients
	 */
	@Raw @Basic
	public int getFactor() {
		return this.factor;
	}
	
	/**
	 * Set the factor with which we multiply our ingredients
	 * 
	 * @param	factor
	 * 			The factor with which the ingredients are multiplied
	 * @post	The factor is set to the given factor
	 * 			| new.getFactor() == factor
	 */
	@Model
	protected void setFactor(int factor) {
		this.factor = factor;
	}
	
	/**
	 * List containing the ingredients we've manufactured so far during the execution of a recipe
	 */
	private Stack<IngredientContainer> currentItems = new Stack<IngredientContainer>();
	
	/**
	 * Return a copy of the list of ingredients currently employed in making a recipe
	 */
	private ArrayList<IngredientContainer> getCurrentItems() {
		return new ArrayList<IngredientContainer>(currentItems);
	}
	
	/**
	 * Return the last element of current items
	 * 
	 * @return	The last element of the current items
	 * 			| getCurrentItems().get(getCurrentItems().size() - 1)
	 */
	public IngredientContainer getLastCurrentElement() {
		return getCurrentItems().get(getCurrentItems().size() - 1);
	}
	
	/**
	 * Push a new element to current items
	 * 
	 * @post	The ingredient is added to the current items
	 * 			| getCurrentItems().get(getCurrentItem().size() - 1) == ingredient
	 */
	@Basic
	public void push(IngredientContainer ingredient) {
		currentItems.push(ingredient);
	}
	
	/**
	 * Pop an element from the current ingredient container stack
	 * 
	 * @post	The first element is removed from the current ingredient container stack
	 * 			| old.getCurrentItems().get(I) == new.getCurrentItems().get(I - 1) 
	 * @return	The first element from the stack
	 * 			| currentItems.pop()
	 */
	@Basic
	public IngredientContainer pop() {
		return currentItems.pop();
	}
	

	
	/**
	 * The index of our amounts
	 */
	private int indexAmount = 0;
	
	/**
	 * Return the index of the current amount
	 */
	@Raw @Basic
	public int getIndexAmount() {
		return indexAmount;
	}
	
	/**
	 * Go to the next amount
	 */
	@Basic
	public void nextAmount() {
		indexAmount++;
	}
	

	/**
	 * Return the current amount
	 * 
	 * @return	Give the current amount 
	 * 			| result == getRecipe().getAmounts().get(getIndexAmount())
	 * @throws	NoSuchElementException
	 * 			There are no amounts left
	 * 			| getIndexAmount() >= getAmounts().size()
	 */
	public Amount getCurrentAmount() throws IndexOutOfBoundsException  {
		return getRecipe().getAmounts().get(getIndexAmount());
	}
	

	
	
	/**
	 * Return the used items to storage
	 * 
	 * @param	lab
	 * 			The laboratory to which we're returning our stuff
	 * @post	Our stack is empty
	 * 			| getCurrentItems().isEmpty()
	 * @effect	All remaining ingredient containers are added to the given lab
	 * 			| 
	 */
	protected void returnToStorage(Laboratory lab) {
		while (!getCurrentItems().isEmpty()) {
			lab.add(pop());
		}
	}
}
