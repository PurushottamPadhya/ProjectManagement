package milestone2;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import milestone1.RepeatedValueException;


/*
 * 
 * Unit testing to verify that if the student is already assigned on the another projects 
 * */

public class DuplicateTeamMemberTest {

	@Test(expected = RepeatedValueException.class)
	public void testCheckForDuplicateStudentAssign() throws  RepeatedValueException {
		
		Handler handler ;
		handler = new Handler();
		
		Map<String, Integer> addedStudents;
		addedStudents = new HashMap<String,Integer>();
		addedStudents.put("S10", 4);
		addedStudents.put("S3", 3);
		addedStudents.put("S4", 2);
		addedStudents.put("S7", 1);
		
		
		handler.checkForDuplicateStudentAssign("S10", addedStudents);

	}

}
