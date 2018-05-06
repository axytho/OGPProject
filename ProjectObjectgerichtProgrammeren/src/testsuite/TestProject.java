package testsuite;

import static org.junit.Assert.*;

import org.junit.*;

import javaproject.*;

public class TestProject {
	
	static IngredientType Devilsdelight;
	
	@Before
	public void setUpBeforeClass() {
		Devilsdelight = new IngredientType("Devils Delight", State.Liquid);
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
		
	}

}
