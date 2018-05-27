package javaproject;

/**
 * A storage iterator aimed at returning the alchemic ingredients stored in a laboratory
 * 
 * 
 * @author Jonas
 *
 */

public interface StorageIterator {
	

	
	/**
	 * Return the ingredient currently selected
	 * 
	 * @return	The current item
	 * @throws	IllegalStateException
	 * 			This storage iterator is empty
	 */
	public int getCurrent();
	
	/**
	 * Return the number of remaining elements in this iterator (this element not included)
	 * 
	 * @return	The result is always positive or zero
	 * 			| result >= 0
	 */
	public int getNbElements();
	
	/**
	 * Advance to the next position
	 * @post	The new position is one greater than the current position
	 * 			| new.getNbElements() = old.getNbElements() - 1
	 * @throws	IllegalStateException
	 * 			There are no more ingredients left in this iterator
	 * 			| getNbElements() == 0
	 */
	public void advance();

	/**
	 * Set the current ingredient back to the first ingredient
	 */
	public void reset();
}
