package javaproject;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;

import be.kuleuven.cs.som.annotate.Basic;

public class RecipeBook {
	
	/**
	 * Make a copy of the given recipe book
	 * @param	recipeBook
	 * 			A hashtable containing all our recipes with corresponding page numbers
	 * @post	Our book is set to the given recipe book
	 * 			| new.getBook() == recipeBook.getBook()
	 */
	
	public RecipeBook(RecipeBook recipeBook) {
		this.book = recipeBook.getBook();
	}

	/**
	 * A hashtable containing all our recipes with corresponding page numbers
	 */
	
	private Hashtable<Integer, Recipe> book = new Hashtable<Integer, Recipe>();
	
	/**
	 * Return a copy of the hashtable containing our recipes and page numbers
	 */
	public Hashtable<Integer, Recipe> getBook() {
		return new Hashtable<Integer, Recipe>(book);
	}
	
	/**
	 * Add a recipe to our book
	 * 
	 * @param	recipe
	 * 			The recipe which is placed in our book
	 * @post	Put the recipe at the first page where there currently is no recipe
	 * 			| new.get( max(for each I in 1..getPageList()-1: I == getPageList().get(I-1)) + 1 ) == recipe
	 */
	public void addRecipe(Recipe recipe) {
		Integer pageNumber = 1;
		for (Integer key : getPageList()) {
			if (key != pageNumber) {
				book.put(pageNumber, recipe);
				return;
			}
			pageNumber++;
		}
		book.put(pageNumber, recipe);
	}
	
	/**
	 * Remove a recipe from the book at the given page
	 * 
	 * @post	The recipe at the given page is removed
	 * 			| getBook().contains(pageNumber) == false
	 */
	@Basic
	public void remove(int pageNumber) {
		book.remove(pageNumber);
	}

	/**
	 * Return a sorted list containing all pages with recipes
	 * 
	 * @return	A sorted list containing all pages with recipes
	 * 			| for each I in 0..result.size()-1:
	 * 			| 		Collections.list(getBook().keys()).contains(getBook().get(I))
	 * 			|		&& (result.get(I) < result.get(I + 1) || I == result.size()-1)
	 */
	private ArrayList<Integer> getPageList() {
		ArrayList<Integer> pageList = Collections.list(getBook().keys());
		Collections.sort(pageList);
		return pageList;
	}
	
}
