package javaproject;

/**
 * A class of ovens
 * @author	Jonas
 *
 *	
 */

public class Oven extends TempDevice {
	
	/**
	 * Initialize a new Oven with a given temperature
	 * 
	 * @effect	The temperature of this box is changed to the given temperature
	 * 			| changeTemperature(temp)
	 * @effect	A temperature device is initialized
	 * 			| super()
	 */
	public Oven(long[] temp) {
		super();
		changeTemperature(temp);
	}

	/**
	 * Heat the given item
	 * 
	 * @effect	We execute this temperature device
	 * 			| super.execute()
	 * @effect	We heat the result until it is equal to the coldness of our heating box, the difference between our heating box and the temperature
	 * 			is negative (i.e. the result is hotter than our heating box, the temperature does not change)
	 * 			| getResult().heat(Double.max(AlchemicIngredient.differenceTemperature(getTemperature(), getResult().getTemperature()), 0) + getDelta())
	 */
	@Override
	public void execute() {
		super.execute();
		getResult().heat(Long.max(AlchemicIngredient.differenceTemperature(getTemperature(), getResult().getTemperature()), 0) + getDelta());
	}
	
	
	/**
	 * Give the possible variance of the oven
	 * 
	 * @return	A random double between minus -5% and 5% of the total temperature of the oven
	 * 			| ( this.getTemperature()[1]- this.getTemperature()[0]) * (Math.random() - 0.5)/10
	 */
	public long getDelta() {
		return  ( this.getTemperature()[1]- this.getTemperature()[0]) *  (long) ((Math.random() - 0.5)/10);
	}
	
}
