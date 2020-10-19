package testCases.Negative;

import static org.junit.Assert.*;

import java.util.HashSet;
import java.util.Set;

import org.junit.BeforeClass;
import org.junit.Test;

import exceptions.ConflictException;
import exceptions.TeamLeaderException;
import model.handler.Handler;

public class ConflictTest {

	/*
	 * 
	 * Unit testing to verify the each team member has one and only one team leader
	 * */
	
	static Handler handler ;
	
	
    @BeforeClass
    public static void setUpClass() {
         handler = new Handler();
    }
	
    @Test(expected = ConflictException.class)
	public void testCheckForStudentConflict() throws ConflictException{
		Handler handler ;
		handler = new Handler();
		
		Set<String> selectedStudents;
		selectedStudents = new HashSet<String>();
		
		selectedStudents.add("S2");
		selectedStudents.add("S3");
		selectedStudents.add("S5");
		selectedStudents.add("S7");
		
		handler.checkForStudentConflict(selectedStudents, "NAUTO");
	}

}
