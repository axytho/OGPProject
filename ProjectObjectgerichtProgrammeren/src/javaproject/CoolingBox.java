package javaproject;

/**
 * A class of coolboxes
 * @author	Jonas
 *
 *	
 */

public class CoolingBox extends TempDevice {

	
	/**
	 * Initialize a new Cooling box with a given temperature and a given lab
	 * 
	 * @param	lab
	 * 			The laboratory in which our cooling box sits
	 * @param	temp
	 * 			The temperature to which we set the cooling box
	 * @effect	We initialize a new device which sits in a given laboratory
	 * 			| super(lab)
	 * @effect	The temperature of this box is changed to the given temperature
	 * 			| changeTemperature(temp)
	 * @effect	The lab's device is set to the given device
	 * 			| lab.setFridge(this)
	 */
	public CoolingBox(Laboratory lab, long[] temp) {
		super(lab);
		changeTemperature(temp);
		lab.setFridge(this);
	}
	
	/**
	 * Check the bidirectional relationship
	 * 
	 * @return	True if this cooling box has a specified lab and the lab has this cooling box as its cooling box
	 * 			| result == (getLab() != null && getLab().getFridge() == this)
	 * @note	Specification now closed
	 */
	@Override
	public boolean isInCorrectLab() {
		return (super.isInCorrectLab() && getLab().getFridge() == this);
	}
	
	/**
	 * Move this device to the given lab
	 * 
	 * @param	lab
	 * 			The lab to which we're moving the device
	 * @post	The old lab no longer has this device
	 * 			| old.getLab().getFridge() == null
	 * @post	The new lab now has this device
	 * 			| lab.getFridge() == this
	 * @effect	The super constructor sets this lab to the given lab
	 * 			| super.move(lab)
	 */
	@Override
	public void move(Laboratory lab) {
		getLab().setFridge(null);
		super.move(lab);
		lab.setFridge(this);
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
