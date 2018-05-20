package javaproject;



import be.kuleuven.cs.som.annotate.Basic;
import be.kuleuven.cs.som.annotate.Model;
import javaproject.exception.*;

public abstract class TempDevice extends Device {
	
	/**
	 * Initialize this temperature device
	 * 
	 * @effect	A device is initialized
	 * 			| super()
	 */
	public TempDevice() {
		
	}
	
	/**
	 * Heat or cool the given item
	 * 
	 * @post	The ingredient stack is now empty
	 * 			| new.getIngredients().isEmpty() == true
 	 * 
	 * @effect	We execute this device
	 * 			| super.execute()
	 */
	@Override
	public void execute() throws EmptyResultException {
		super.execute();
		setResult(pop());
		
	}

	/**
	 * Check whether a given number of ingredients is valid for this temperature device
	 * 
	 * @return	True if the number is less or equal than one
	 * 			| result == (number <= 1)
	 */
	@Override
	public boolean isValidNumberOfItems(int number) {
		return number <= 1;
	}
	/**
	 * The temperature of thiss temperature device
	 */
	
	private long[] temperature = {0, 0};
	
	/**
	 * Return the temperature of this temperature device
	 */
	public long[] getTemperature() {
		return this.temperature;
	}
	
	/**
	 * Set the temperature to the given temperature
	 * 
	 * @param	temperature
	 * 			The temperature which we want our temperature device to have
	 * @post	The new temperature is equal to the given temperature
	 * 			| new.getTemperature()	== temperature
	 */
	@Basic @Model
	private void setTemperature(long[] temperature) {
		this.temperature = temperature;
	}
	
	/**
	 * Change the temperature to the given temperature
	 * 
	 * @param	temperature
	 * 			The temperature which we want our temperature device to have
	 * @effect	The new temperature is set to the given temperature
	 * 			| setTemperature(temperature)
	 * @throws	IllegalArgumentException
	 * 			The given temperature is not valid
	 * 			| !isValidTemperature(temperature)
	 */
	
	public void changeTemperature(long[] temperature) {
		if (!AlchemicIngredient.isValidTemperature(temperature)) {
			throw new IllegalArgumentException("Not a valid temperature");
		}
		setTemperature(temperature);
	}

	
	

	
	
}
