package milestone2;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/*
 * 
 * Unit testing to verify the standard deviation of all team team 
 * */

public class StandardDeviationForSkillShortFallTest {

	Handler handler;
	@Before
	public void setup() {
		handler = new Handler();
	}
	
	@Test
	public void testComputeStandardDeviationForSkillShortFall() {
		
		assertEquals(3.048, handler.computeStandardDeviationForSkillShortFall(), 0.01);
		//fail("Not yet implemented");
	}
	
	@After
	public void tearDown() {
		handler = null;
	}

}
