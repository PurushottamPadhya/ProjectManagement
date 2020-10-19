package testCases.Positive;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import model.heuristic.Heuristic;
import model.project.Project;



public class SortingProjectWithCompetency {

	
	Heuristic heu;
	List<Project> unsorted = new ArrayList<Project>();
	
	List<Project> sorted = new ArrayList<Project>();
	
	@Before
	public void setup() {
		heu = new Heuristic();
		
		// unsorted list
		Project p1 = new Project();
		p1.setID("Pr1" );
		p1.setSkillCompetency(4.0);
		unsorted.add(p1);
		
		Project p2 = new Project();
		p2.setID("Pr2" );
		p2.setSkillCompetency(1.0);
		unsorted.add(p2);
		
		
		Project p3 = new Project();
		p3.setID("Pr3" );
		p3.setSkillCompetency(2.0);
		unsorted.add(p3);
		
		Project p4 = new Project();
		p4.setID("Pr4" );
		p4.setSkillCompetency(3.0);
		unsorted.add(p4);

		
	}
	
	@Test
	public void testSortingProjectWithCompetency() {
		
		assertEquals("Pr1", heu.sortingProjectWithCompetency(unsorted).get(0).getID());
	}

}
