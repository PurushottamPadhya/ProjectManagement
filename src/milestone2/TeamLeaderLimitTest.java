package milestone2;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import milestone1.TeamLeaderException;

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
		
		Map<String, Integer> selectedStudents;
		selectedStudents = new HashMap<String,Integer>();
		
		selectedStudents.put("S1", 4);
		selectedStudents.put("S3", 3);
		selectedStudents.put("S4", 2);
		selectedStudents.put("S7", 1);
		
		
		handler.checkForTeamLeaderIncludedOnly(selectedStudents);


	}
	
	@AfterClass
    public static void tearDownClass() {
        handler = null;
    }

}
