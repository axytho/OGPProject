package javaproject;

/**
 * A class of ovens
 * @author	Jonas
 *
 *	
 */

public class Oven extends TempDevice {
	
	/**
	 * Initialize a new oven with a given temperature and a given lab
	 * 
	 * @param	lab
	 * 			The laboratory in which our oven sits
	 * @param	temp
	 * 			The temperature to which we set the oven
	 * @effect	We initialize a new device which sits in a given laboratory
	 * 			| super(lab)
	 * @effect	The temperature of this box is changed to the given temperature
	 * 			| changeTemperature(temp)
	 */
	public Oven(Laboratory lab, long[] temp) {
		super(lab);
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
