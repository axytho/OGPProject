package javaproject;



public class Transmogrifier extends Device {

	
	/**
	 * Initialize a new transmogrifier with a given temperature and a given lab
	 * 
	 * @param	lab
	 * 			The laboratory in which our transmogrifier sits
	 * @effect	We initialize a new device which sits in a given laboratory
	 * 			| super(lab)
	 */
	public Transmogrifier(Laboratory lab) {
		super(lab);
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
	 *			|		Integer nextQuantity = getResult().getTransmogrifiedQuant(State.Solid);
	 *			|		getResult().setState(State.Solid);
	 *			|		getResult().setQuantityTo(nextQuantity);
	 *			|	else 
	 *			|		Integer nextQuantity = getResult().getTransmogrifiedQuant(State.Liquid);
	 *			|		getResult().setState(State.Liquid);
	 *			|		getResult().setQuantityTo(nextQuantity);
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
