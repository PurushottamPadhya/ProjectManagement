package testCases.Negative;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.BeforeClass;
import org.junit.Test;

import exceptions.ConflictException;
import exceptions.TeamLeaderException;
import model.handler.Handler;
import model.heuristic.Heuristic;
import model.project.Project;

public class ConflictOnSwapTest {

	static Handler handler;
	Heuristic heu;
	static List<Project> projects = new ArrayList<Project>();
	
	static Map<String, String> swappingStudents = new HashMap<String, String>();
	
	 @BeforeClass
	    public static void setUpClass() {
	         handler = new Handler();
	         swappingStudents.put("S2", "Pr10");
	         swappingStudents.put("S5", "Pr5");
	         projects = handler.getShortlistedProjectStudent("");
	        
	         
	    }
	@Test(expected = ConflictException.class)
	public void testSwapTwoStudents() throws ConflictException, TeamLeaderException{
		
		System.out.println(swappingStudents);
		heu.swapTwoStudents(swappingStudents, projects, "NAUTO");
		
	}

}
