package javaproject;

import java.util.ArrayList;
import javaproject.ExecutiveRecipe.Instruction;
import quantity.Quant;

public class Recipe {
	

	
	public class Amount {
		
		/**
		 * Create a new amount with a given quantity, unit and type
		 * @param	quantity
		 * 			| The quantity of this amount
		 * @param	unit
		 * 			| The unit of this amount
		 * @param	type
		 * 			| The ingredient type of this amount
		 * @post	The quantity is set to the given quantity
		 * 			| new.getQuantity() == quantity
		 * @post	The unit is set to the given unit
		 * 			| new.getUnit() == unit
		 * @post	The type is set to the given type
		 * 			| new.getType()	== type
		 */
		public Amount(int quantity, Quant unit, IngredientType type) {
			this.quantity = quantity;
			this.unit = unit;
			this.type = type;
		}
		
		/**
		 * The quantity of this amount
		 */
		private int quantity = 0;
		
		/**
		 * The ingredient type of this amount
		 */
		private IngredientType type = null;
		
		/**
		 * The unit of this amount
		 */
		private Quant unit = null;
		
		/**
		 * Return the quantity of this amount
		 */
		public int getQuantity() {
			return this.quantity;
		}
		
		/**
		 * Return the ingredient type of this amount
		 */
		public IngredientType getIngredientType() {
			return this.type;
		}
		
		/**
		 * Return the unit of this amount
		 */
		public Quant getUnit() {
			return this.unit;
		}
	}
	
	
	// TODO: Everything to the bottom of this becomes a subclass
	
	
	

	/**
	 * List containing all our instructions
	 */
	public ArrayList<Instruction> instructions = new ArrayList<Instruction>();
	
	/**
	 * List containing all our amounts
	 */
	public ArrayList<Amount> amounts = new ArrayList<Amount>();
	
	/**
	 * Return the amounts
	 */
	public ArrayList<Amount> getAmounts() {
		return this.amounts;
	}
	
	/**
	 * Get the instructions
	 */
	public ArrayList<Instruction> getInstructions() {
		return this.instructions;
	}
	
	

}
