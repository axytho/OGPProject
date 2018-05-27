package testsuite;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

import org.junit.*;

import javaproject.*;
import javaproject.Recipe.Amount;
import quantity.LQuant;
import quantity.SQuant;



public class RecipeTest {
	
	static Recipe recipeForDisaster;
	
	static IngredientType MercurialAcid, Water, ImpGas, Garlic, BlackLotus, Mustard;
	
	static Recipe.Amount Merc, Wat, Imp, Gar, Black, Must;
	
	@Before
	public void setUpBeforeClass() {
		MercurialAcid = new IngredientType("Mercurial Acid", State.Liquid, new long[] {0 , 40}, 0.3);
		Water = new IngredientType("Water", State.Liquid, new long[] {0, 20}, 0);
		ImpGas = new IngredientType("Imp Gas", State.Liquid, new long[] {0 , 30}, 0.9);
		Garlic = new IngredientType("Garlic", State.Solid, new long[] {0 , 10}, 0.1);
		BlackLotus = new IngredientType("Black Lotus", State.Liquid, new long[] {0 , 40}, 0.7);
		Mustard = new IngredientType("Mustard", State.Liquid, new long[] {10, 0}, 0.2);
		ArrayList<Amount> recipeAmount = new ArrayList<Amount>();
		recipeAmount.add(recipeForDisaster.new Amount(3, LQuant.DROP, MercurialAcid));
		recipeAmount.add(recipeForDisaster.new Amount(1, LQuant.VIAL, Water));
		recipeAmount.add(recipeForDisaster.new Amount(2, LQuant.SPOON, ImpGas));
		recipeAmount.add(recipeForDisaster.new Amount(3, SQuant.SPOON, Garlic));
		recipeAmount.add(recipeForDisaster.new Amount(4, LQuant.DROP, BlackLotus));
		recipeAmount.add(recipeForDisaster.new Amount(1, LQuant.SPOON, Mustard));
		



	}
}
