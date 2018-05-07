package testsuite;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collections;

import org.junit.*;

import javaproject.*;
import quantity.*;

public class TestProject {
	
	static IngredientType Devilsdelight;
	
	static IngredientContainer JonasLocker;
	
	static AlchemicIngredient TodaysDD;
	

	
	
	@Before
	public void setUpBeforeClass() {
		Devilsdelight = new IngredientType("Devils Delight", State.Liquid, new long[] {0 , 200});
		JonasLocker = new IngredientContainer("Jonas' Locker", SQuant.CHEST);
		ArrayList<Integer> DoubleDevilQuantity = new ArrayList<Integer>(Collections.nCopies(7, 12));
		TodaysDD = new AlchemicIngredient(Devilsdelight, DoubleDevilQuantity);
		
	}
	
	@Test
	public void testIngredientTypeString() {
		assertEquals(Devilsdelight.getName(), "Devils Delight");
	}

	@Test
	public void testIsValidSimpleName() {
		assertEquals("coke".substring(0, "coke".length() -1), "cok");
		assertTrue(IngredientType.isValidSimpleName("Rat’s Eye Fluid"));
		assertTrue(IngredientType.isValidSimpleName("Coke"));
		assertTrue(IngredientType.isValidSimpleName("Coaled Coke"));
		assertTrue(IngredientType.isValidSimpleName("Hot"));
		assertFalse(IngredientType.isValidSimpleName("Ht"));
		assertFalse(IngredientType.isValidSimpleName("Channel 5"));
		assertFalse(IngredientType.isValidSimpleName("ChanNel"));
		assertTrue(IngredientType.isValidSimpleName("Channel Five"));
		assertTrue(IngredientType.isValidSimpleName("Channel Five Zero"));
		assertFalse(IngredientType.isValidSimpleName("Channel five Zero"));
		assertFalse(IngredientType.isValidSimpleName("Channel F Zero"));
		assertFalse(IngredientType.isValidSimpleName("Chan_nel Five Zero"));
		assertTrue(IngredientType.isValidSimpleName("Channel Fi Zero"));
		
		assertTrue(IngredientType.isValidSimpleName("Hot "));
		assertTrue(IngredientType.isValidSimpleName("Hot"));
		
		assertFalse(IngredientType.isValidSimpleName("%Hot"));
		
		assertFalse(IngredientType.isValidSimpleName("Coke mixed with Beer"));
		assertFalse(IngredientType.isValidSimpleName("Coke mixed Beer"));
		assertTrue(IngredientType.isValidSimpleName("Cokemixed"));
	}
	
	@Test
	public void testIsValidMixedName()	{
		assertTrue(AlchemicIngredient.isValidMixedName("Heated Coke mixed with Beer, Cooled Water , Vodka, Martini, Cider and Tomato Juice"));
		assertFalse(AlchemicIngredient.isValidMixedName("Heated Coke mixed with Beer, Cooled Water mixed with Vodka, Martini, Cider and Tomato Juice"));
		assertFalse(AlchemicIngredient.isValidMixedName("Heated Coke mixed with Beer, Cooled Water , Vodka, martini, Cider and Tomato Juice"));
		assertFalse(AlchemicIngredient.isValidMixedName("Heated Coke mixed with Beer, Cooled Water , Vodka, Martini, Cider, Tomato Juice"));
		assertFalse(AlchemicIngredient.isValidMixedName("Heated Coke, Beer, Cooled Water , Vodka, Martini, Cider and Tomato Juice"));
		assertTrue(AlchemicIngredient.isValidMixedName("Heated Coke mixed with Beer"));
		assertTrue(AlchemicIngredient.isValidMixedName("Heated Coke"));
		assertTrue(AlchemicIngredient.isValidMixedName("Garlic mixed with Imp Gas, Mercurial Acid and Water"));
		
	}
	
	@Test
	public void testIsValidCombinedName() {
		assertTrue(AlchemicIngredient.isValidCombinedName("Heated Red Eye Special"));
		assertTrue(AlchemicIngredient.isValidCombinedName("Red Eye Special"));
		assertFalse(AlchemicIngredient.isValidCombinedName("Red Heated Eye Special"));
		assertFalse(AlchemicIngredient.isValidCombinedName("Heated red Eye Special"));
		assertFalse(AlchemicIngredient.isValidCombinedName("CoKe"));
	}
	
	@Test
	public void testIsValidTotalName() {
		assertTrue(AlchemicIngredient.isValidTotalName("Heated Red Eye Special (Heated Coke mixed with Beer,"
				+ " Cooled Water , Vodka, Martini, Cider and Tomato Juice)"));
		assertFalse(AlchemicIngredient.isValidTotalName("Heated Red Eye Special (Coke) (Heated Coke mixed with Beer, Vodka, Cider and Tomato Juice)"));
		assertTrue(AlchemicIngredient.isValidTotalName("Coke (Thee)"));
		assertFalse(AlchemicIngredient.isValidTotalName("CoKe (Thee)"));
		assertFalse(AlchemicIngredient.isValidTotalName("Coke (mixed with)"));
		assertFalse(AlchemicIngredient.isValidTotalName("Thee (Po)"));
	}
	
	@Test
	public void testIngredientContainer() {
		assertEquals(JonasLocker.getCapacity(), SQuant.CHEST);
	}
	
	@Test
	public void testQuantity() {
		assertTrue(TodaysDD.isCarriedOver());
		assertEquals(TodaysDD.getNumberOf(LQuant.STOREROOM), 14);
		assertEquals(TodaysDD.giveInLowestUnit(), new Integer(737868));
	}
	
	@Test
	public void testTemperature() {
		TodaysDD.heat(50);
		assertEquals(TodaysDD.getHotness(), 50);
		TodaysDD.cool(60);
		assertEquals(TodaysDD.getHotness(), 0);
		assertEquals(TodaysDD.getColdness(), 10);
		TodaysDD.heat(200000);
		assertEquals(TodaysDD.getColdness(), 0);
		assertEquals(TodaysDD.getHotness(), 10000);
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

}
