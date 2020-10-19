package testCases.Negative;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import exceptions.TeamLeaderException;
import model.handler.Handler;

public class TeamLeaderLimitTest {
	
	
	/*
	 * 
	 * Unit testing to verify the each team member has one and only one team leader
	 * */
	
	static Handler handler ;
	
	
    @BeforeClass
    public static void setUpClass() {
         handler = new Handler();
    }

	@Test(expected = TeamLeaderException.class)
	public void testCheckForTeamLeaderIncludedOnly() throws TeamLeaderException {

		Handler handler ;
		handler = new Handler();
		
		Set<String> selectedStudents;
		selectedStudents = new HashSet<String>();
		
		selectedStudents.add("S1");
		selectedStudents.add("S3");
		selectedStudents.add("S4");
		selectedStudents.add("S7");
		
		handler.checkForTeamLeaderIncludedOnly(selectedStudents, "NAUTO");

	}
	
	@AfterClass
    public static void tearDownClass() {
        handler = null;
    }

}
