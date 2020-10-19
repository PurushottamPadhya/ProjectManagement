package testCases.Positive;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import model.handler.Handler;


/*
 * 
 * Unit testing to verify that Skill Competency is calculated correctly
 * */

public class AverageSkillCompetencyTest {

	
	Handler handler;
	@Before
	public void setup() {
		handler = new Handler();
	}
	
	
	
	@Test
	public void testGetAverageSkillCompetancy() {
		
		
		assertEquals(2.25, handler.getAverageSkillCompetancy("Pr5"), 0.001);
		
	}
	

	@After
	public void tearDown() {
		
	}
	

}
