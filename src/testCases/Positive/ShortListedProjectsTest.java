package testCases.Positive;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import model.handler.Handler;

public class ShortListedProjectsTest {

	Handler handler;
	@Before
	public void setup() {
		handler = new Handler();
	}
	
	@Test
	public void testGetShortlistedProjectStudent() {
		assertEquals(5, handler.getShortlistedProjectStudent("").size(), 0.01);
	}

}
