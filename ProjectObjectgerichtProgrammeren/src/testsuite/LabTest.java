package testsuite;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collections;

import org.junit.Before;
import org.junit.Test;

import javaproject.*;
import javaproject.exception.*;
import quantity.*;

public class LabTest {
	static IngredientType Devilsdelight, Cat, Al, NH3NO3;
	
	static IngredientContainer BlueVial, RedVial, GoldSpoon, BlueBottle, GreenBottle, RedBottle, PurpleJug, ResultBarrel, BrownBarrel;
	
	static IngredientContainer SilverSpoon, GreenSachet, CyanBox, MagentaSack, RedLocker, RedLocker2;
	
	static AlchemicIngredient TodaysDD, DD1, CatinABox, GilesCat, OneCat, SecondCat, AlChest, AlChest2;
	
	static ArrayList<Integer> CatQuantity, ACat ,GillesCatQuantity, DevilsQuantity;

	static Device MyLittleCatOven, CatKettle, CatTrans, MyLittleDogFridge;
	
	static Laboratory lab;
	
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
		ACat = new ArrayList<Integer>(Collections.nCopies(7, 0));
		ACat.set(0, 1);
		ACat.set(2, 2);
		
		Al = new IngredientType("Aluminium Powder", State.Solid, new long[] {0, 23}, 0.40);
		NH3NO3 = new IngredientType("Ammonium Nitrate", State.Liquid, new long[] {0, 15}, 0.60);
		
		AlChest = new AlchemicIngredient(1, SQuant.CHEST, Al);
		AlChest2 = new AlchemicIngredient(1, SQuant.CHEST, Al);
		
		OneCat = new AlchemicIngredient(Cat, ACat);
		SecondCat = new AlchemicIngredient(Cat, ACat);
		// Set up containers
		BlueVial = new IngredientContainer("Blue Vial", LQuant.VIAL);
		RedVial = new IngredientContainer("Red Vial", LQuant.VIAL);
		GoldSpoon = new IngredientContainer("Gold Spoon", LQuant.VIAL);	
		GreenBottle = new IngredientContainer("Green Bottle", LQuant.BOTTLE, SecondCat);
		BlueBottle = new IngredientContainer("Blue Bottle", LQuant.BOTTLE, OneCat);
		PurpleJug = new IngredientContainer("Purple Jug", LQuant.JUG, DD1);
		BrownBarrel = new IngredientContainer("Brown Barrel", LQuant.BARREL);
		
		SilverSpoon = new IngredientContainer("Silver Spoon", SQuant.SPOON);
		GreenSachet = new IngredientContainer("Green Sachet", SQuant.SACHET);
		CyanBox = new IngredientContainer("Cyan Box", SQuant.BOX);
		MagentaSack = new IngredientContainer("Magenta Sack", SQuant.SACK);
		RedLocker = new IngredientContainer("Jonas' Locker", SQuant.CHEST, AlChest);
		RedLocker2 = new IngredientContainer("Jonas' Locker", SQuant.CHEST, AlChest2);
		
		lab = new Laboratory(3);
		CatTrans = new Transmogrifier(lab);
		MyLittleCatOven = new Oven(lab, new long[] {0, 300});
		CatKettle = new Kettle(lab);
		MyLittleDogFridge = new CoolingBox(lab, new long[] {40, 0});
	}
	
	@Test
	public void add_quantityDoubling() {
		lab.add(BlueBottle);
		lab.add(PurpleJug);
		lab.add(GreenBottle);
		// We've added two Vials and one drop of cat to this lab, let's see if we can get two vials back without an error
		lab.get("Cat", LQuant.VIAL, 2);
		lab.get("Cat", LQuant.DROP, 1);
	}
	
	@Test(expected = ExceedsStorageException.class)
	public void get_quantityDoubling$Illegal() {
		lab.add(BlueBottle);
		lab.add(PurpleJug);
		lab.add(GreenBottle);
		// We've added two Vials and one drop of cat to this lab, let's see if we can get two vials back without an error
		lab.get("Cat", LQuant.VIAL, 2);
		lab.get("Cat", LQuant.DROP, 2);
	}
	
	@Test(expected = NameNotFoundException.class)
	public void get_quantityDoubling$Illegal2() {
		lab.add(BlueBottle);
		lab.add(PurpleJug);
		lab.add(GreenBottle);
		// We've added two Vials and one drop of cat to this lab, let's see if we can get two vials back without an error
		lab.get("Dog", LQuant.VIAL, 2);
		lab.get("Cat", LQuant.DROP, 2);
	}
	
	@Test(expected = ExceedsContainerCapacityException.class)
	public void get_quantityDoubling$Illegal3() {
		lab.add(RedLocker);
		lab.add(RedLocker2);
		// We've added two Vials and one drop of cat to this lab, let's see if we can get two vials back without an error
		lab.get("Aluminium Powder", LQuant.VIAL, 2);
		IngredientContainer two = lab.get("Cat", LQuant.DROP, 2);
	}
	
	@Test(expected = EmptyContainerException.class)
	public void add_IllegalCase1() {
		lab.add(GoldSpoon);
	}
	
}
