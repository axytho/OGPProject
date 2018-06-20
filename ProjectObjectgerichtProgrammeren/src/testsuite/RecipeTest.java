package testsuite;


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
	
	static Device MyLittleCatOven, CatKettle, CatTrans, MyLittleDogFridge;
	
	static ExecutiveRecipe recipe;
	
	static Laboratory lab;
	
	static AlchemicIngredient HgSO4, H2O, IMP, GARLIC, BLACK, MUST;
	
	static IngredientContainer chest1, chest2, chest3, barrel4, chest5, chest6;
	
	@Before
	public void setUpBeforeClass() {
		recipeForDisaster = new Recipe(new ArrayList<Amount>(), new ArrayList<String>());
		recipe = new ExecutiveRecipe(recipeForDisaster);
		MercurialAcid = new IngredientType("Mercurial Acid", State.Liquid, new long[] {0 , 40}, 0.3);
		Water = new IngredientType("Water", State.Liquid, new long[] {0, 20}, 0);
		ImpGas = new IngredientType("Imp Gas", State.Liquid, new long[] {0 , 30}, 0.9);
		Garlic = new IngredientType("Garlic", State.Solid, new long[] {0 , 10}, 0.1);
		BlackLotus = new IngredientType("Black Lotus", State.Liquid, new long[] {0 , 40}, 0.7);
		Mustard = new IngredientType("Mustard", State.Liquid, new long[] {10, 0}, 0.2);
		HgSO4 = new AlchemicIngredient(4, LQuant.DROP, MercurialAcid);
		H2O = new AlchemicIngredient(1, LQuant.BARREL, Water);
		IMP = new AlchemicIngredient(1, LQuant.BARREL, ImpGas);
		GARLIC = new AlchemicIngredient(1, SQuant.CHEST, Garlic);
		BLACK = new AlchemicIngredient(1, LQuant.BARREL, BlackLotus);
		MUST = new AlchemicIngredient(1, LQuant.BARREL, Mustard);
		chest1 = new IngredientContainer("1", LQuant.BARREL, HgSO4);
		chest2 = new IngredientContainer("2", LQuant.BARREL, H2O);
		chest3 = new IngredientContainer("3", LQuant.BARREL, IMP);
		barrel4 = new IngredientContainer("4", SQuant.CHEST, GARLIC);
		chest5 = new IngredientContainer("5", LQuant.BARREL, BLACK);
		chest6 = new IngredientContainer("6", LQuant.BARREL, MUST);
		lab = new Laboratory(3);
		lab.add(chest1);
		lab.add(barrel4);
		lab.add(chest2);
		lab.add(chest3);
		lab.add(chest5);
		lab.add(chest6);
		ArrayList<Amount> recipeAmount = new ArrayList<Amount>();
		recipeAmount.add(recipeForDisaster.new Amount(3, LQuant.DROP, MercurialAcid));
		recipeAmount.add(recipeForDisaster.new Amount(1, LQuant.VIAL, Water));
		recipeAmount.add(recipeForDisaster.new Amount(2, LQuant.SPOON, ImpGas));
		recipeAmount.add(recipeForDisaster.new Amount(3, SQuant.SPOON, Garlic));
		recipeAmount.add(recipeForDisaster.new Amount(4, LQuant.DROP, BlackLotus));
		recipeAmount.add(recipeForDisaster.new Amount(1, LQuant.SPOON, Mustard));
		ArrayList<String> instructions = new ArrayList<String>();
		instructions.add("add");
		instructions.add("cool");
		instructions.add("add");
		instructions.add("heat");
		instructions.add("mix");
		instructions.add("add");
		instructions.add("cool");
		instructions.add("mix");
		instructions.add("add");
		instructions.add("heat");
		instructions.add("mix");
		instructions.add("heat");
		instructions.add("add");
		instructions.add("heat");
		instructions.add("add");
		instructions.add("mix");
		recipeForDisaster= new Recipe(recipeAmount, instructions);	
		CatTrans = new Transmogrifier(lab);
		MyLittleCatOven = new Oven(lab, new long[] {0, 300});
		CatKettle = new Kettle(lab);
		MyLittleDogFridge = new CoolingBox(lab, new long[] {40, 0});
	}
	
	@Test
	public void execute() {
		lab.execute(recipeForDisaster, 5);
	}
}
