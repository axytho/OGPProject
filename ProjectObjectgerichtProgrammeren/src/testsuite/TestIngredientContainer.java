package testsuite;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collections;

import org.junit.*;

import javaproject.*;
import quantity.*;

public class TestIngredientContainer {
	static IngredientType Devilsdelight, Cat;
	
	static IngredientContainer BlueVial, RedVial, GoldSpoon, BlueBottle, PurpleJug, BrownBarrel;
	
	static IngredientContainer SilverSpoon, GreenSachet, CyanBox, MagentaSack, RedLocker;
	
	static AlchemicIngredient TodaysDD, CatinABox, GilesCat, OneCat;
	
	static ArrayList<Integer> CatQuantity, ACat;

	static ArrayList<Integer> GillesCatQuantity;
	
	
	@Before
	public void setUpBeforeClassIngredients() {
		Devilsdelight = new IngredientType("Devils Delight", State.Liquid, new long[] {0 , 66}, 0.99);
		ArrayList<Integer> DoubleDevilQuantity = new ArrayList<Integer>(Collections.nCopies(7, 12));
		TodaysDD = new AlchemicIngredient(Devilsdelight, DoubleDevilQuantity);
		Cat = new IngredientType("Cat", State.Liquid, new long[] {0, 37}, 0.7);
		CatQuantity = new ArrayList<Integer>(Collections.nCopies(7, -1));
		GillesCatQuantity = new ArrayList<Integer>(Collections.nCopies(7, Integer.MAX_VALUE));
		ACat = new ArrayList<Integer>(Collections.nCopies(7, 0));
		ACat.set(0, 1);
		ACat.set(2, 2);
		CatinABox = new AlchemicIngredient(Cat, CatQuantity);
		GilesCat = new AlchemicIngredient(Cat, GillesCatQuantity);
		OneCat = new AlchemicIngredient(Cat, ACat);
	}
	
	@Before
	public void setUpBeforeClassContainers() {
		BlueVial = new IngredientContainer("Blue Vial", LQuant.VIAL);
		RedVial = new IngredientContainer("Red Vial", LQuant.VIAL);
		GoldSpoon = new IngredientContainer("Gold Spoon", LQuant.VIAL);
		BlueBottle = new IngredientContainer("Blue Bottle", LQuant.BOTTLE, OneCat);
		PurpleJug = new IngredientContainer("Purple Jug", LQuant.JUG);
		BrownBarrel = new IngredientContainer("Brown Barrel", LQuant.BARREL);
		SilverSpoon = new IngredientContainer("Silver Spoon", SQuant.SPOON);
		GreenSachet = new IngredientContainer("Green Sachet", SQuant.SACHET);
		CyanBox = new IngredientContainer("Cyan Box", SQuant.BOX);
		MagentaSack = new IngredientContainer("Magenta Sack", SQuant.SACK);
		RedLocker = new IngredientContainer("Jonas' Locker", SQuant.CHEST);
	}
	
	@Test
	public void putCatInBox() {
		assertFalse(AlchemicIngredient.isValidQuantity(CatQuantity));
		assertFalse(AlchemicIngredient.isValidQuantity(GillesCatQuantity));
		assertFalse(CyanBox.canHaveAsContents(OneCat));
		assertTrue(BlueBottle.canHaveAsContents(OneCat));
		assertFalse(IngredientContainer.isValidCapacity(SQuant.STOREROOM));
	}
}
