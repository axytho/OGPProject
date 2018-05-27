package testsuite;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ TestAlchemicIngredient.class, TestIngredientContainer.class, DeviceTest.class, LabTest.class })
public class AllTests {
	
}
