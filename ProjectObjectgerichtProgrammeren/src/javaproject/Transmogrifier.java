package javaproject;



public class Transmogrifier extends Device {

	
	/**
	 * Initialize a new transmogrifier with a given temperature and a given lab
	 * 
	 * @param	lab
	 * 			The laboratory in which our transmogrifier sits
	 * @effect	We initialize a new device which sits in a given laboratory
	 * 			| super(lab)
	 * @effect	The lab's device is set to the given device
	 * 			| lab.setTransmogrifier(this)
	 */
	public Transmogrifier(Laboratory lab) {
		super(lab);
		lab.setTransmogrifier(this);
	}
	
	/**
	 * Check the bidirectional relationship
	 * 
	 * @return	True if this transmogrifier has a specified lab and the lab has this transmogrifier as its transmogrifier
	 * 			| result == (getLab() != null && getLab().getTransmogrifier() == this)
	 * @note	Specification now closed
	 */
	@Override
	public boolean isInCorrectLab() {
		return (super.isInCorrectLab() && getLab().getTransmogrifier() == this);
	}
	
	/**
	 * Move this device to the given lab
	 * 
	 * @param	lab
	 * 			The lab to which we're moving the device
	 * @post	The old lab no longer has this device
	 * 			| old.getLab().getTransmogrifier() == null
	 * @post	The new lab now has this device
	 * 			| lab.getTransmogrifier() == this
	 * @effect	The super constructor sets this lab to the given lab
	 * 			| super.move(lab)
	 * 
	 */
	@Override
	protected void move(Laboratory lab) {
		getLab().setTransmogrifier(null);
		super.move(lab);
		lab.setTransmogrifier(this);
	}
	
	/**
	 * Toggle the state of the ingredient
	 * 
	 * @effect	We execute this device
	 * 			| super.execute()
	 * @post	The ingredient list is now empty
	 * 			| getIngredients().isEmpty() == true
	 * @effect	Depending on the state, the quantity of the result is set to the proper transmogrified quantity
	 * 			| 	if (getResult().getState() == State.Liquid)
	 *			|		getResult().setState(State.Solid);
	 *			|	else 
	 *			|		getResult().setState(State.Liquid);
	 * @post	The quantity is set to the transmogrified quantity
	 * 			| 	if (getResult().getState() == State.Liquid)
	 * 			|		new.getResult().getQuantity() == getResult().getTransmogrifiedQuant(State.Solid)
	 * 			| 	else
	 * 			|		new.getResult().getQuantity() == getResult().getTransmogrifiedQuant(State.Liquid)
	 */
	@Override
	public void execute() {
		super.execute();
		setResult(pop());
		if (getResult().getState() == State.Liquid) {
			Integer nextQuantity = getResult().getTransmogrifiedQuant(State.Solid);
			getResult().setState(State.Solid);
			getResult().setQuantityTo(nextQuantity);
		} else {
			Integer nextQuantity = getResult().getTransmogrifiedQuant(State.Liquid);
			getResult().setState(State.Liquid);
			getResult().setQuantityTo(nextQuantity);
		}
	}

	/**
	 * Check whether a given number of ingredients is valid for this transmogrifier
	 * 
	 * @return	True if the number is less or equal than one
	 * 			| result == (number <= 1)
	 */
	@Override
	public boolean isValidNumberOfItems(int number) {
		return number <= 1;
	}
	


}
