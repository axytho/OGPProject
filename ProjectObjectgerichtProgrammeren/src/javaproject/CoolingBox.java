package javaproject;

/**
 * A class of coolboxes
 * @author	Jonas
 *
 *	
 */

public class CoolingBox extends TempDevice {
	
	/**
	 * Initialize a new Cooling box with a given temperature
	 * 
	 * @effect	The temperature of this box is changed to the given temperature
	 * 			| changeTemperature(temp)
	 * @effect	A temperature device is initialized
	 * 			| super()
	 */
	public CoolingBox(long[] temp) {
		super();
		changeTemperature(temp);
	}

	/**
	 * Cool the given item
	 * 
	 * @effect	We execute this temperature device
	 * 			| super.execute()
	 * @effect	We cool the result until it is equal to the coldness of our cooling box, the difference between our cooling box and the temperature
	 * 			is negative (i.e. the result is cooler than our cooling box, the temperature does not change)
	 * 			| getResult().cool(Double.max(AlchemicIngredient.differenceTemperature(getTemperature(), getResult().getTemperature()), 0))
	 */
	@Override
	public void execute() {
		super.execute();
		getResult().cool(Long.max(AlchemicIngredient.differenceTemperature(getTemperature(), getResult().getTemperature()), 0));
	}



}
