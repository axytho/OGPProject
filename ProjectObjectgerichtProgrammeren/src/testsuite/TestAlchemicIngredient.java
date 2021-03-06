package testsuite;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collections;

import org.junit.*;

import javaproject.*;
import javaproject.AlchemicIngredient.Temperature;
import quantity.*;

public class TestAlchemicIngredient {
	
	static IngredientType Devilsdelight;
	
	static IngredientContainer JonasLocker;
	
	static AlchemicIngredient TodaysDD;
	

	
	
	@Before
	public void setUpBeforeClass() {
		Devilsdelight = new IngredientType("Devils Delight", State.Liquid, new long[] {0 , 66}, 0.99);
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
		assertTrue(IngredientType.isValidSimpleName("Rat�s Eye Fluid"));
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
		
		assertTrue(IngredientType.isValidSimpleName("Coke mixed with Beer"));
		assertTrue(IngredientType.isValidSimpleName("Coke mixed Beer"));
		assertTrue(IngredientType.isValidSimpleName("Cokemixed"));
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
	public void testTemperature1() {
		TodaysDD.heat(50);
		assertEquals( TodaysDD.getHotness(), 116);
		TodaysDD.cool(66);
		TodaysDD.cool(60);
		assertEquals(TodaysDD.getHotness(), 0);
		assertEquals(TodaysDD.getColdness(), 10);
		TodaysDD.heat(200000);
		assertEquals(  TodaysDD.getColdness(), 0);
		assertEquals(  TodaysDD.getHotness(), 10000);
		TodaysDD.cool(1000);
		TodaysDD.heat(Long.MAX_VALUE);
		assertTrue(Long.MAX_VALUE>=0);
		assertEquals(  TodaysDD.getHotness(), 10000);
		TodaysDD.heat((long) Math.pow(2, 65));
		assertEquals(  TodaysDD.getHotness(), 10000);
		TodaysDD.heat(Long.MAX_VALUE);
		assertEquals(  TodaysDD.getHotness(), 10000);
		assertEquals(  TodaysDD.getHotness(), 10000);
		AlchemicIngredient.setMaxTemperature(Long.MAX_VALUE);
		TodaysDD.cool(0);
		assertEquals(  TodaysDD.getHotness(), 10000);
		TodaysDD.cool(-200);
		assertEquals(  TodaysDD.getHotness(), 10000);
		TodaysDD.cool(Long.MIN_VALUE);
		assertEquals(  TodaysDD.getHotness(), 10000);
		TodaysDD.cool(Long.MAX_VALUE);
		assertEquals(  TodaysDD.getHotness(), 0);
		assertEquals(  TodaysDD.getColdness(), Long.MAX_VALUE-10000);
		TodaysDD.cool(Long.MAX_VALUE);
		assertEquals(  TodaysDD.getColdness(), Long.MAX_VALUE);
		TodaysDD.heat(0);
		assertEquals(  TodaysDD.getColdness(), Long.MAX_VALUE);
		TodaysDD.heat(-200);
		assertEquals(  TodaysDD.getColdness(), Long.MAX_VALUE);
		TodaysDD.cool(Long.MIN_VALUE);
		assertEquals(  TodaysDD.getColdness(), Long.MAX_VALUE);
	}
	
	@Test
	public void testTemperatureName() {
		assertEquals(TodaysDD.getTemperatureState(), Temperature.NONE);
		TodaysDD.heat(50);
		assertEquals(TodaysDD.getTemperatureState(), Temperature.HEATED);
		TodaysDD.cool(60);
		assertEquals(TodaysDD.getTemperatureState(), Temperature.COOLED);
	}
	
	@Test
	public void testVolatility() {
		assertTrue(TodaysDD.getCharVolatility() > 0.89);
	}
	
	@Test
	public void numberTests() {
		int three = 3;
		int two = 2;
		double threetwo = three / two;
		assertEquals(threetwo, 3/2, 0.0001);
	}

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

}
