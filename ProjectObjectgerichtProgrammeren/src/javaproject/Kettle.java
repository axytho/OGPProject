package javaproject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import be.kuleuven.cs.som.annotate.Model;
import javaproject.exception.EmptyContainerException;
import quantity.*;

public class Kettle extends Device {
	
	
	/**
	 * Initialize a new kettle which stands in a given laboratory
	 * 
	 * @param	lab
	 * 			The laboratory in which our kettle sits
	 * @effect	We initialize a new device which sits in a given laboratory
	 * 			| super(lab)
	 * @effect	The lab's device is set to the given device
	 * 			| lab.setKettle(this)
	 */
	public Kettle(Laboratory lab) {
		super(lab);
		lab.setKettle(this);
	}
	
	/**
	 * Check the bidirectional relationship
	 * 
	 * @return	True if this kettle has a specified lab and the lab has this kettle as its kettle
	 * 			| result == (getLab() != null && getLab().getKettle() == this)
	 * @note	Specification now closed
	 */
	@Override
	public boolean isInCorrectLab() {
		return (super.isInCorrectLab() && getLab().getKettle() == this);
	}
	
	/**
	 * Move this device to the given lab
	 * 
	 * @param	lab
	 * 			The lab to which we're moving the device
	 * @post	The old lab no longer has this devic
	 * 			| old.getLab().getKettle() == null
	 * @post	The new lab now has this device
	 * 			| lab.getKettle() == this
	 * @effect	The super constructor sets this lab to the given lab
	 * 			| super.move(lab)
	 */
	@Override
	protected void move(Laboratory lab) {
		getLab().setKettle(null);
		super.move(lab);
		lab.setKettle(this);
	}

	/**
	 * Mix the ingredients in the kettle
	 * 
	 * @effect	The ingredient list is cleared
	 * 			| clear()
	 * @post	The result of this device is set to a new Alchemic Ingredient
	 * 			| getResult() == new AlchemicIngredient(getType(), findQuantity())
	 * @effect	The ingredients of this kettle are added to the mix list of the new ingredient
	 * 			| addMixList(getResult())
	 * @post	The characteristic volatility is set to the correct characteristic volatility
	 * 			| getResult().getCharVolatility() == findCharacteristicVolatility()
	 * @post	The temperature is set to the correct temperature
	 * 			| getResult().getTemperature() == findTemperature(getResult())
	 * @effect	We execute this device
	 * 			| super.execute()
	 * @effect	We terminate all ingredients
	 * 			| terminateAll()
	 */
	@Override
	public void execute() {		
		super.execute();
		IngredientType type = getType();
		AlchemicIngredient ingredient = new AlchemicIngredient(type, findQuantity());
		ingredient.setCharacteristicVolatility(findCharacteristicVolatility());
		ingredient.changeTempTo(findTemperature(ingredient));
		addMixList(ingredient);
		setResult(ingredient);
		terminateAll();
		clear();
	}
	/**
	 * Terminate all ingredients
	 * 
	 * @post	Each ingredient in our storage is terminated
	 * 			| for each ingredient in getIngredients():
	 * 			|		ingredient.isTerminated() == true
	 */
	@Model
	private void terminateAll() {
		for (AlchemicIngredient ingredient : getIngredients()) {
			ingredient.terminate();
		}
	}
	
	/**
	 * Get the type of the result
	 * 
	 * @return	A new ingredient type if you mix two different ingredients, else the same ingredient
	 * 			| for each ingredient in getIngredients():
	 * 			| 	if ingredient.getType() == getIngredients().get(0).getType():
	 * 			|		result == getIngredients().get(0).getType()
	 * 			| 	else
	 * 			|		result == new IngredientType(null, findState(), findStandardTemperature(), findTheoreticalVolatility())
	 * 
	 */
	@Model
	private IngredientType getType() {
		IngredientType firstType = getIngredients().get(0).getType();
		for (AlchemicIngredient ingredient : getIngredients()) {
			if (firstType != ingredient.getType()) {
				return new IngredientType(null, findState(), findStandardTemperature(), findTheoreticalVolatility());
			}
		}
		return firstType;
		
	}
	
	
	/**
	 * Mix the ingredients in the kettle, and give a new name to the result
	 * 
	 * @param	specialName
	 * 			The name to be given to the result
	 * @throws	IllegalArgumentException
	 * 			The given name is not a valid name
	 * 			| !IngredientType.isValidSimpleName(specialName)
	 */
	public void execute(String specialName) throws IllegalArgumentException {
		if (!IngredientType.isValidSimpleName(specialName)) {
			throw new IllegalArgumentException("Invalid special name");
		}
		execute();
		getResult().setSpecialName(specialName);
	}

	/**
	 * Add the mix of distinct ingredient types to the ingredient
	 * 
	 * @param	result
	 * 			The result of our kettle
	 * @post	The mix list contains every type of every ingredient in the kettle, but each only once
	 * 			| for each ingredient in getIngredients():
	 *			|	for each ingredientType in ingredient.getIngredientMixList():
	 *			| 		result.getIngredientMixList().contains(ingredientType)
	 */
	public void addMixList(AlchemicIngredient result) {
		Set<IngredientType> resultSet = new HashSet<IngredientType>();
		for (AlchemicIngredient ingredient: getIngredients()) {
			resultSet.addAll(ingredient.getIngredientMixList());
			resultSet.add(ingredient.getType());
		}
		for (IngredientType ingredientType : resultSet) {
			result.addToMixList(ingredientType);
		}
	}
	
	
	
	/**
	 * Return whether the given number of items is valid
	 * 
	 * @return	Always true, a kettle can take as many items as we want
	 * 			| result == true
	 */
	@Override
	public boolean isValidNumberOfItems(int number) {
		return true;
	}
	
	/**
	 * Find the state of the ingredient that comes from the kettle
	 * 
	 * 
	 * @return	The result is set to the state of the ingredient for which the temperature is closest to [0, 20]
	 * 			If there are two ingredients equally close to [0, 20], the state is set to the state of the ingredient
	 * 			who's state is liquid (i.e. if they both have the same state, nothing change, else if the new state is liquid then
	 *  		the state is set to that state)
	 *  		| if (
	 *  		| 	for each ingredient in getIngredients()	
	 *  		| 			if ( Math.abs(AlchemicIngredient.differenceTemperature(ingredient.getType().getStandardTemperature(), new double[] {0,20}))
	 *  		|			<= for each otherIngredient in getIngredients()
	 *  		|					Math.abs(AlchemicIngredient.differenceTemperature(otherIngredient.getType().getStandardTemperature(), new double[] {0,20})) )
	 *  		|			then ingredient.getState() == State.Solid
	 *  		|	) then result ==  State.Solid
	 *  		|	 else result == State.Liquid
	 * 
	 * @note	If the first elements is the element closest to [0,20], then it takes the state of this element,
	 * 			because the program compares the first element to itself on the first iteration, so it sees them as equally close to [0,20]
	 * 			but it still takes the state they both share
	 * 
	 */
	@Model
	private State findState() {
		AlchemicIngredient minimum = getIngredients().get(0);
		double minimumDifference = Math.abs(AlchemicIngredient.differenceTemperature(minimum.getType().getStandardTemperature(), new long[] {0,20}));
		for (AlchemicIngredient ingredient : getIngredients()) {
			double difference = Math.abs(AlchemicIngredient.differenceTemperature(ingredient.getType().getStandardTemperature(), new long[] {0,20}));
			if (difference < minimumDifference) {
				minimumDifference = difference;
				minimum = ingredient;
			} else if (difference == minimumDifference && minimum.getState() == State.Solid && ingredient.getState() != minimum.getState()) {
				minimum = ingredient;
			}
		}
		return minimum.getState();
	}
	
	/**
	 * Find the standard temperature of the new alchemic ingredient
	 * 
	 * @return	The result is the temperature of the ingredient closest to [0, 20]
	 * 			If there are two ingredients equally close to [0, 20] the hottest is taken.
	 *  		| for each minimumIngredient in (
	 *  		| 	for each ingredient in getIngredients()	
	 *  		| 			if ( Math.abs(AlchemicIngredient.differenceTemperature(ingredient.getType().getStandardTemperature(), new double[] {0,20}))
	 *  		|			<= for each otherIngredient in getIngredients()
	 *  		|					Math.abs(AlchemicIngredient.differenceTemperature(otherIngredient.getType().getStandardTemperature(), new double[] {0,20})) )
	 *  		|			then ingredient)
	 *  		|	)
	 *  		| 	AlchemicIngredient.compareTemperature(minimumIngredient.getType().getStandardTemperature(), result.getType().getStandardTemperature()) > 0
	 */
	@Model
	private long[] findStandardTemperature() {
		AlchemicIngredient minimum = getIngredients().get(0);
		long minimumDifference = Math.abs(AlchemicIngredient.differenceTemperature(minimum.getType().getStandardTemperature(), new long[] {0,20}));
		for (AlchemicIngredient ingredient : getIngredients()) {
			long difference = Math.abs(AlchemicIngredient.differenceTemperature(ingredient.getType().getStandardTemperature(), new long[] {0,20}));
			if (difference < minimumDifference) {
				minimumDifference = difference;
				minimum = ingredient;
			} else if (difference == minimumDifference && AlchemicIngredient.compareTemperature(minimum.getType().getStandardTemperature(), 
					ingredient.getType().getStandardTemperature()) < 0 ) {
				minimum = ingredient;
			}
		}
		return minimum.getType().getStandardTemperature();	
	}
	

	
	
	/**
	 * Find the temperature of the result depending on the spoons, we ignore the effect of pinches or drops on the temperature
	 * on the total amount because these are lost in the mixing process.
	 * 
	 * @return	The weighted mean of the temperature over the spoons
	 * 			| AlchemicIngredient.temperatureToArray(sum(for ingredient in getIngredients(): ingredient.giveInSpoons() * (ingredient.getHotness()-ingredient.getColdness()))
	 * 			|		/ ( result.giveInSpoons()) )
	 */
	public long[] findTemperature(AlchemicIngredient result) {
		double sum = 0;
		for (AlchemicIngredient ingredient : getIngredients()) {
			sum += ingredient.giveInSpoons() * (ingredient.getHotness()-ingredient.getColdness());
		}
		long temperature =   (long) ( sum/ result.giveInSpoons());
		return AlchemicIngredient.temperatureToArray(temperature);		
	}
	

	
	/**
	 * Find the characteristic volatility of the result
	 * 
	 * @return	The result is given by the average characteristic volatility of all the alchemic ingredients
	 * 			| sum( for ingredient in getIngredients(): ingredient.getCharVolatility()) / getIngredients().size()
	 */
	public double findCharacteristicVolatility() {
		double sum = 0;
		for (AlchemicIngredient ingredient : getIngredients()) {
			sum += ingredient.getCharVolatility();
		}
		return sum / getIngredients().size() ;
	}
	
	/**
	 * Find the theoretical volatility of the result
	 * 
	 * @return	The result is given by the average theoretical volatility of all the alchemic ingredients
	 * 			| sum( for ingredient in getIngredients(): ingredient.getType().getVolatility()) / getIngredients().size()
	 */
	public double findTheoreticalVolatility() {
		double sum = 0;
		for (AlchemicIngredient ingredient : getIngredients()) {
			sum += ingredient.getType().getVolatility();
		}
		return sum / getIngredients().size();
	}
	
	/**
	 * Check if all elements are liquid
	 * 
	 * @return	True if all elements are in a liquid state
	 * 			| if (
	 * 			| for each element in list:
	 * 			|		element.getState() == State.Liquid
	 * 			|	) 
	 * 			| result == true
	 */
	public boolean allLiquid() {
		for (AlchemicIngredient ingredient: getIngredients()) {
			if (ingredient.getState() == State.Solid) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * Check if all elements are solid
	 * 
	 * @return	True if all elements are in a solid state
	 * 			| if (
	 * 			| for each element in list:
	 * 			|		element.getState() == State.Solid
	 * 			|	) 
	 * 			| result == true
	 */
	public boolean allSolid() {
		for (AlchemicIngredient ingredient: getIngredients()) {
			if (ingredient.getState() == State.Liquid) {
				return false;
			}
		}
		return true;
	}
	

	
	/**
	 * Find the quantity of the result
	 * 
	 * 
	 * 
	 * @return	if everything is liquid, result equals a list containing the uncarried amount of pinches of all the ingredients
	 * 			| if (for each ingredient in getIngredients()
	 * 			|			ingredient.getState() == State.Liquid)
	 * 			|   then result.get(0) == sum(for each ingredient in getIngredients(): ingredient.giveInLowestUnit())
	 * 			|			&& for index in 1..10: result.get(index) == 0
	 * 			| if (for each ingredient in getIngredients()
	 * 			|			ingredient.getState() == State.Solid)
	 * 			|   then result.get(0) == sum(for each ingredient in getIngredients(): ingredient.giveInLowestUnit())
	 * 			|			&& for index in 1..10: result.get(index) == 0   
	 * 			| else
	 * 			|		result.get(0) == 0 && for index in 2..10: result.get(index) == 0
	 * 			|			&&	result.get(1) == sum(for each ingredient in getIngredients(): if (ingredient.getState() == State.Liquid):
	 * 			|					ingredient.giveInLowestUnit()) / 8 + sum(for each ingredient in getIngredients(): 
	 * 			|					if (ingredient.getState() == State.Solid): ingredient.giveInLowestUnit()) / 6
	 * 			|				+ (   if (ingredient.getState() == State.Liquid):
	 * 			|					ingredient.giveInLowestUnit()) % 8 + sum(for each ingredient in getIngredients(): 
	 * 			|					if (ingredient.getState() == State.Solid): ingredient.giveInLowestUnit()) % 6	
	 * 			|				) / 6
	 * 					
	 */
	@Model
	private ArrayList<Integer> findQuantity() {
		ArrayList<Integer> resultList = new ArrayList<Integer>(Collections.nCopies(findState().getQuantities().size(), 0));
		int liquidVolume = 0;
		int solidMass = 0;
		for (AlchemicIngredient ingredient: getIngredients()) {
			if (ingredient.getState() == State.Liquid) {
				liquidVolume += ingredient.giveInLowestUnit();
			} else  {
				solidMass += ingredient.giveInLowestUnit();
			}
		}
		if (liquidVolume == 0) {
			resultList.set(0, solidMass);
			resultList.set(1, 0);
		} else if (solidMass == 0) {
			resultList.set(0, liquidVolume);
			resultList.set(1, 0);
		} else {
			resultList.set(0, 0);
			resultList.set(1, liquidVolume / LQuant.SPOON.getCVal() + solidMass / SQuant.SPOON.getCVal() 
				+ (liquidVolume % LQuant.SPOON.getCVal() * SQuant.SPOON.getCVal() + solidMass % SQuant.SPOON.getCVal() * LQuant.SPOON.getCVal()) 
				/ (LQuant.SPOON.getCVal() * SQuant.SPOON.getCVal())							);
		}
		
		return resultList;
	}
	
	/**
	 * Add a certain ingredient
	 * 
	 * @param	ingredient
	 * 			The container which we're adding
	 * @post	The content of the container is now stored in the device
	 * 			| new.getIngredients().contains(container) == true
	 * @post	If the device can only hold the current number of ingredients, the last ingredient in the device is replaced 
	 * 			by the ingredient in the container and the last ingredient is terminated.
	 * 			| new.getIngredients().contains(old.getIngredients().get(getIngredients().size() - 1) == false
	 * 			| && old.getIngredients().get(getIngredients().size() - 1).isTerminated() == true
	 * @throws	IllegalArgumentException
	 * 			The alchemic ingredient you're trying to add has already been terminated
	 * 			| container.getContents().isTerminated()
	 * @throws	IllegalArgumentException
	 * 			This device does not sit in a valid lab
	 * 			| !isInCorrectLab()
	 */
	@Model
	protected void add(AlchemicIngredient ingredient) throws EmptyContainerException, IllegalArgumentException {
		if (!isInCorrectLab()) {
			throw new IllegalArgumentException("This device is not in the correct lab!");
		}
		if (ingredient.isTerminated()) {
			throw new IllegalArgumentException("Container's ingredient is terminated!");
		}
		if (!isValidNumberOfItems(getIngredients().size() +1)) {
			pop().terminate();
		} 
		push(ingredient);	
	}
	
	
	

}
