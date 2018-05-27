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
	 * @effect	The lab's device is set to the given device
	 * 			| lab.setOven(this)
	 */
	public Oven(Laboratory lab, long[] temp) {
		super(lab);
		changeTemperature(temp);
		lab.setOven(this);
	}
	
	
	/**
	 * Move this device to the given lab
	 * 
	 * @param	lab
	 * 			The lab to which we're moving the device
	 * @post	The old lab no longer has this device
	 * 			| old.getLab().getOven() == null 
	 * @post	The new lab now has this device
	 * 			| lab.getOven() == this
	 * @effect	The super constructor sets this lab to the given lab
	 * 			| super.move(lab)
	 */
	@Override
	protected void move(Laboratory lab) {
		getLab().setOven(null);
		super.move(lab);
		lab.setOven(this);
	}
	
	/**
	 * Check the bidirectional relationship
	 * 
	 * @return	True if this oven has a specified lab and the lab has this oven as its oven
	 * 			| result == (getLab() != null && getLab().getOven() == this)
	 * @note	Specification now closed
	 */
	@Override
	public boolean isInCorrectLab() {
		return (super.isInCorrectLab() && getLab().getOven() == this);
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
