package testsuite;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collections;

import org.junit.Before;
import org.junit.Test;

import javaproject.AlchemicIngredient;
import javaproject.*;
import javaproject.exception.*;
import quantity.LQuant;
import quantity.SQuant;

public class DeviceTest {

	static IngredientType Devilsdelight, Cat;
	
	static IngredientContainer BlueVial, RedVial, GoldSpoon, BlueBottle, RedBottle, PurpleJug, ResultBarrel, BrownBarrel;
	
	static IngredientContainer SilverSpoon, GreenSachet, CyanBox, MagentaSack, RedLocker;
	
	static AlchemicIngredient TodaysDD, DD1, CatinABox, GilesCat, OneCat;
	
	static ArrayList<Integer> CatQuantity, ACat ,GillesCatQuantity, DevilsQuantity;

	static Device MyLittleCatOven, CatKettle, CatTrans;
	
	@Before
	public void setUpBeforeClassIngredients() {
		Devilsdelight = new IngredientType("Devils Delight", State.Liquid, new long[] {0 , 66}, 0.99);
		ArrayList<Integer> DoubleDevilQuantity = new ArrayList<Integer>(Collections.nCopies(7, 12));
		DevilsQuantity = new ArrayList<Integer>(Collections.nCopies(7, 0));
		DevilsQuantity.set(3, 6);
		DevilsQuantity.set(2, 2);
		DD1 = new AlchemicIngredient(Devilsdelight, DevilsQuantity);
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
		// Set up containers
		BlueVial = new IngredientContainer("Blue Vial", LQuant.VIAL);
		RedVial = new IngredientContainer("Red Vial", LQuant.VIAL);
		GoldSpoon = new IngredientContainer("Gold Spoon", LQuant.VIAL);
		BlueBottle = new IngredientContainer("Blue Bottle", LQuant.BOTTLE, OneCat);
		PurpleJug = new IngredientContainer("Purple Jug", LQuant.JUG, DD1);
		BrownBarrel = new IngredientContainer("Brown Barrel", LQuant.BARREL);
		SilverSpoon = new IngredientContainer("Silver Spoon", SQuant.SPOON);
		GreenSachet = new IngredientContainer("Green Sachet", SQuant.SACHET);
		CyanBox = new IngredientContainer("Cyan Box", SQuant.BOX);
		MagentaSack = new IngredientContainer("Magenta Sack", SQuant.SACK);
		RedLocker = new IngredientContainer("Jonas' Locker", SQuant.CHEST);
		CatTrans = new Transmogrifier();
		MyLittleCatOven = new Oven(new long[] {0, 300});
		CatKettle = new Kettle();		
	}
	
	@Test(expected = EmptyResultException.class)
	public void execute_IllegalCaseEmptyResult() {
		MyLittleCatOven.execute();
	}
	
	@Test(expected = EmptyContainerException.class)
	public void execute_IllegalCaseAddEmptyContainer() {
		MyLittleCatOven.add(RedVial);
	}
	
	@Test
	public void boilTheCat() {
		MyLittleCatOven.add(BlueBottle);
		MyLittleCatOven.execute();
		RedBottle = MyLittleCatOven.resultAfterReaction();
		assertEquals(RedBottle.getCapacity(), LQuant.BOTTLE);
		AlchemicIngredient Boilcat= RedBottle.getContents();
		assertTrue(Boilcat.getHotness() > 298 && Boilcat.getHotness()< 302);
		assertTrue(Boilcat.getVolatility() > 1500 && Boilcat.getVolatility()< 2300);
	}
	
	@Test
	public void mixTheCat() {
		CatKettle.add(BlueBottle);
		CatKettle.add(PurpleJug);
		CatKettle.execute();
		ResultBarrel = CatKettle.resultAfterReaction();
		assertEquals(ResultBarrel.getCapacity(), LQuant.BARREL);
		AlchemicIngredient MixedCat = ResultBarrel.getContents();
		assertEquals(MixedCat.getName(), "Cat mixed with Devils Delight");
		assertEquals(2 * MixedCat.getType().getVolatility(), OneCat.getType().getVolatility() + DD1.getType().getVolatility(), 0.01);
		assertEquals(2 * MixedCat.getCharVolatility(), OneCat.getCharVolatility() + DD1.getCharVolatility(), 0.01);
		assertEquals(MixedCat.getState(), State.Liquid);
		assertEquals(MixedCat.getNumberOf(LQuant.JUG), 1);
		assertEquals(MixedCat.getNumberOf(LQuant.BOTTLE), 0);
		assertEquals(MixedCat.getNumberOf(LQuant.VIAL), 1);
		assertEquals(MixedCat.getNumberOf(LQuant.DROP), 1);
		assertEquals(MixedCat.getType().getStandardTemperature(), OneCat.getType().getStandardTemperature());
		assertEquals(MixedCat.getHotness(), 63);
		assertEquals(MixedCat.getColdness(), 0);
	}
	
	@Test
	public void TransmogrifyThenMix() {
		CatTrans.add(PurpleJug);
		CatTrans.add(BlueBottle);
		CatTrans.execute();
		assertEquals(CatTrans.getResult().getState(), State.Solid);
		
		
	}
}
