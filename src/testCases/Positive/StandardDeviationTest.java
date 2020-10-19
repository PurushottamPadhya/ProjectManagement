package testCases.Positive;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import model.handler.Handler;

/*
 * 
 * Unit testing to verify the standard deviation of all team team 
 * */

public class StandardDeviationTest {

	Handler handler;
	@Before
	public void setup() {
		handler = new Handler();
	}
	
	@Test
	public void testComputeStandardDeviationForSkillShortFall() {
		
		 
		 Map<String, Double> keyWithValues = new HashMap<String,Double>();
		
		 keyWithValues.put("S1", 2.0);
		 keyWithValues.put("S3", 3.0);
		 keyWithValues.put("S4", 2.5);
		 keyWithValues.put("S7", 1.5);
		assertEquals(0.55, handler.computeStandardDeviation(keyWithValues), 0.01);
		//fail("Not yet implemented");
	}
	
	@After
	public void tearDown() {
		handler = null;
	}

}
