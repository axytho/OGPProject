package javaproject;

import java.util.Arrays;
import java.util.List;

/**
 * @invar	Volality must be valid
 * 			| isValidVolality(getVolality())
 * @invar	Temperature must be valid
 * 			| isValidTemperature(getVolality())
 * @invar	Volality must be valid
 * 			| isValidQuantity(getVolality())
 * 
 * @author Jonas
 *
 */

public class AlchemicIngredient {
	
	/**
	 * An array of adjectives which are considered special
	 */
	
	private static final String[] specialAdjectives = {"Heated", "Cooled"};
	
	/**
	 * A list containing the special adjectives for easy access
	 */
	private static final List<String> specialAdjective = Arrays.asList(specialAdjectives);
	
	// "Heated Red Eye Special (Heated Coke mixed with Beer, Cooled Water , Vodka, Martini, Cider and Tomato Juice)"
	// isValidSimpleName("Red Eye Special")
	// isValidTotalName("Red Eye Special (Heated Coke mixed with Beer, Water, Vodka, Martini, Cider and Tomato Juice)")
	// isValidSimpleName("Coke"), isValidSimpleName("Beer"), etc...
	// + check if valid way of constructing special name (comma's, and, mixed with at the right places)
	//
	
	/**
	 * 
	 * @param	name
	 * 			The total name
	 * @return	Returns true if there is only one opening bracket, if it ends with a closing bracket
	 * 			if the special name is a valid combined name and the simple mixed name is valid
	 * 			| true if (Arrays.asList(name.split("\\(", 2)).size() == 2 && name.endsWith(")") )
	 * 			| && isValidMixedName(Arrays.asList(name.split("\\(", 2)).get(0).trim())
	 * 			| && isValidMixedName(Arrays.asList(name.split("\\(", 2)).get(1).substring(0, Arrays.asList(name.split("(", 2)).get(1).length() - 1))))
	 */
	public static boolean isValidTotalName(String name) {
		List<String> splitList = Arrays.asList(name.split("\\(", 2));
		int length= splitList.get(1).length();
		return splitList.size() == 2 && name.endsWith(")") && isValidMixedName(splitList.get(0).trim())
				&& isValidMixedName(splitList.get(1).substring(0, length - 1));
	}
	
	/**
	 * Check if the simple mixed name is valid
	 * 
	 * @param	name
	 * 			The mixed name
	 * @return	True if there's a valid name before and after the "mixed with", after each comma and before and after the "and"
	 * 			true if
	 * 			isValidCombinedName(name)
	 * 			||
	 * 			for (each combinedName in Arrays.asList(name.split(","))) 
	 * 				isValidCombinedName(combinedName.trim())
	 * 					&& indexOf(combinedName) != 0 && indexOf(combinedName) != Arrays.asList(name.split(",")).length() - 1
	 * 				|| (Arrays.asList(combinedName.split("mixed with")).size() == 2 
	 * 						&& isValidCombinedName(Arrays.asList(combinedName.split("mixed with")).get(0).trim())
	 * 						&& isValidCombinedName(Arrays.asList(combinedName.split("mixed with")).get(1).trim())  
	 * 						&& indexOf(combinedName) == 0    )
	 * 				|| (Arrays.asList(combinedName.split("and")).size() == 2 
	 * 						&& isValidCombinedName(Arrays.asList(combinedName.split("and")).get(0).trim())
	 * 						&& isValidCombinedName(Arrays.asList(combinedName.split("and")).get(1).trim())  
	 * 						&& indexOf(combinedName) == Arrays.asList(name.split(",")).size() - 1    )
	 * 			
	 * 
	 */
	public static boolean isValidMixedName(String name) {
		if (isValidCombinedName(name)) {
			return true;
		}
		
		List<String> splitList = Arrays.asList(name.split(","));
		int count = 0;
		for (String combinedName : splitList) {
			List<String> mixList = Arrays.asList(combinedName.split("mixed with"));
			List<String> andList = Arrays.asList(combinedName.split("and"));	
			if (!(isValidCombinedName(combinedName.trim()) && count != 0 && count !=  splitList.size() - 1)
					&& !((mixList.size() == 2) && isValidCombinedName(mixList.get(0).trim()) && isValidCombinedName(mixList.get(1).trim())
							&& count == 0 )
					&& !((andList.size() == 2) && isValidCombinedName(andList.get(0).trim()) && isValidCombinedName(andList.get(1).trim())
							&& count == splitList.size() - 1 )
					) {
				return false;
			}		
			count += 1;
		}
		return true;
		
	}
	
	// "with Coke" invalid
	// ()
	// "Heated Coke" valid
	// 
	
	/**
	 * Splits possible combined name in adjective and simple name and check whether both are valid
	 * 
	 * @param	name
	 * 			The name to be split
	 * @return	True if the combined name is either just a simple name, or if the first part of the name is a correct adjective
	 * 			and the second part is a simple name and there is more than one part (to prevent index errors)
	 * 			| True if
	 * 			| 
	 * 			| IngredientType.isValidSimpleName(name) || ( Arrays.asList(name.split(" ", 2)) != 1 && specialAdjective.contains(Arrays.asList(name.split(" ", 2)).get(0)) 
	 * 			| && IngredientType.isValidName(Arrays.asList(name.split(" ", 2)).get(1)) )
	 */		
	public static boolean isValidCombinedName(String name) {
		List<String> splitList = Arrays.asList(name.split(" ", 2));
		return IngredientType.isValidSimpleName(name) || (splitList.size() > 1 && specialAdjective.contains(splitList.get(0)) 
				&& IngredientType.isValidSimpleName(splitList.get(1)) );
	}

	
}
