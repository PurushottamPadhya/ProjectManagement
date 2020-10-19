package model.heuristic;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;

import customAlert.CustomAlert;
import exceptions.ConflictException;
import exceptions.RepeatedValueException;
import exceptions.TeamLeaderException;
import milestone1.ShortlistProject;
import milestone1.utility;
import model.handler.Handler;
import model.preference.Preference;
import model.project.Project;
import model.student.Student;

public class Heuristic {

	private final static int SWAPLIMIT = 10;

	// sorting of shortlisted projects according to skill competency

	Handler handler = new Handler();

	// sorting of project with the competency
	public List<Project> sortingProjectWithCompetency(List<Project> shortListedProjects) {

		Collections.sort(shortListedProjects, (c1, c2) -> {

			Double value1 = c1.getSkillCompetency();
			Double value2 = c2.getSkillCompetency();

			if (value1 < value2) {
				return 1;
			} else {
				return -1;
			}
		});

		return shortListedProjects;
	}

	// sorting of project with the preference percentage
	public List<Project> sortingProjectWithPreference(List<Project> shortListedProjects) {

		List<Project> sorted = new ArrayList<Project>();
		sorted = shortListedProjects;
		Collections.sort(sorted, (c1, c2) -> {

			Double value1 = c1.getPreferencePercentage();
			Double value2 = c2.getPreferencePercentage();

			if (value1 < value2) {
				return 1;
			} else {
				return -1;
			}
		});

		return sorted;
	}

	// sorting of project with the skillshort fall
	public List<Project> sortingProjectWithSkillGap(List<Project> shortListedProjects) {

		List<Project> sorted = new ArrayList<Project>();
		sorted = shortListedProjects;
		Collections.sort(sorted, (c1, c2) -> {

			Double value1 = c1.getSkillGap();
			Double value2 = c2.getSkillGap();

			if (value1 < value2) {
				return 1;
			} else {
				return -1;
			}
		});

		return sorted;
	}

	// check the difference of last and first competency value of two projects
	// if greater than 1 then swap students between the two projects
	// then find the SD if improved saved into the serialization otherwise shows
	// proper message
	public List<Project> autoswapCompetency(List<Project> shortListedProjects, Double previousSD)
			throws ConflictException, TeamLeaderException {

		List<Project> projectList = new ArrayList<Project>();
		projectList = shortListedProjects;
		projectList = this.sortingProjectWithCompetency(projectList);

		Double competency1 = projectList.get(0).getSkillCompetency();

		Double competency2 = projectList.get(projectList.size() - 1).getSkillCompetency();
		System.out.println("comp 1 " + competency1 + " comp 2 " + competency2);
		int countLoop = 0;

		// need to swap students between two projects
		// project 1 has higher value and project 2 has lower competency value
		Project project1 = projectList.get(0);
		Project project2 = projectList.get(projectList.size() - 1);

		boolean matched = true;
		Double minSD = previousSD;
		List<Project> updatedList = null;
		boolean isUpdated = false;

		int projectIteration = 0;

		while (projectIteration < projectList.size()) {

			for (Map.Entry<String, Integer> entry1 : project1.getSelectedStudents().entrySet()) {

				for (Map.Entry<String, Integer> entry2 : project2.getSelectedStudents().entrySet()) {

					countLoop++;
					// for project 1 to get the higher competency value of student for swapping

					String studentID1 = entry1.getKey();
					String studentID2 = entry2.getKey();

					Map<String, String> swappingStudents = new HashMap<String, String>();
					swappingStudents.put(studentID1, project1.getID());
					swappingStudents.put(studentID2, project2.getID());

					Set<String> student1 = new HashSet<String>();

					for (String key1 : project1.getSelectedStudents().keySet()) {

						if (!key1.equals(studentID1)) {
							student1.add(key1);
						}
					}
					student1.add(studentID2);

					Set<String> student2 = new HashSet<String>();

					for (String key2 : project2.getSelectedStudents().keySet()) {

						if (!key2.equals(studentID2)) {
							student2.add(key2);
						}
					}
					student2.add(studentID1);

					// check if selected students validate the team leader and conflict of students
					// in that team.

					if (handler.checkForStudentConflict(student1, "AUTO")
							|| handler.checkForTeamLeaderIncludedOnly(student1, "AUTO")) {
						continue;
					}

					if (handler.checkForStudentConflict(student2, "AUTO")
							|| handler.checkForTeamLeaderIncludedOnly(student2, "AUTO")) {
						continue;
					}

					System.out.println(swappingStudents);

					List<Project> swappedProjects = swapTwoStudents(swappingStudents, projectList, "AUTO");

					Map<String, Double> preferecesPercentage = handler.computePreferencePercentage(swappedProjects);

					Double SDPreference = handler.computeStandardDeviation(preferecesPercentage);

					System.out.println("SD for preference is " + SDPreference);

					Map<String, Double> competency = handler.computeSkillCompetency(swappedProjects);

					Double SDCompetency = handler.computeStandardDeviation(competency);

					System.out.println("SD for competency is " + SDCompetency);

					Map<String, Double> skillGap = handler.computeSkillGap(swappedProjects);

					Double SDSkillGap = handler.computeStandardDeviation(skillGap);

					System.out.println("SD for skill gap is " + SDSkillGap);

					System.out.println("Previous sd is *****************************" + previousSD);
					System.out.println("new sd is *****************************" + SDCompetency);
					if (SDCompetency < minSD) {
						previousSD = SDCompetency;
						updatedList = new ArrayList<Project>();
						isUpdated = true;
						minSD = SDCompetency;
						for(Project p : swappedProjects) {
							
							
							updatedList.add(this.createNewProjectObject(p));
						}
					}

				}

			}

			projectIteration++;

			if (projectIteration < projectList.size() - 1) {

				project1 = projectList.get(projectIteration);

			}

		}

		if (isUpdated) {
			return updatedList;
		}

		return projectList;

	}

	// check the difference of last and first preference value of two projects which
	// is on sorted order
	// if greater than 25% then swap students between the two projects
	// then find the SD if improved saved into the serialization otherwise shows
	// proper message
	public List<Project> autoPreferencePercentage(List<Project> shortListedProjects, Double originalSD)
			throws ConflictException, TeamLeaderException {
		
		
		List<Project> newProjectList = new ArrayList<Project>();
		
		List<Project>tempProjects = new ArrayList<Project>();
		tempProjects = this.sortingProjectWithPreference(shortListedProjects);

		int countLoop = 0;

		
		Project project2 = tempProjects.get(tempProjects.size() - 1);
		int projectIteration = 0;

		List<Project> minSDProjects = new ArrayList<Project>();
		boolean sdUpdated = false;

		Double minSD = originalSD;

		for(int i =0 ; i < tempProjects.size() - 1 ; i++ ) {
			Project project1 = tempProjects.get(0);
			
			for (Map.Entry<String, Integer> entry1 : project1.getSelectedStudents().entrySet()) {

				for (Map.Entry<String, Integer> entry2 : project2.getSelectedStudents().entrySet()) {

					countLoop++;

					// for project 1 to get the higher competency value of student for swapping

					String studentID1 = entry1.getKey();
					String studentID2 = entry2.getKey();

					Map<String, String> swappingStudents = new HashMap<String, String>();
					swappingStudents.put(studentID1, project1.getID());
					swappingStudents.put(studentID2, project2.getID());

					Set<String> student1 = new HashSet<String>();

					for (String key1 : project1.getSelectedStudents().keySet()) {

						if (!key1.equals(studentID1)) {
							student1.add(key1);
						}
					}
					student1.add(studentID2);

					Set<String> student2 = new HashSet<String>();

					for (String key2 : project2.getSelectedStudents().keySet()) {

						if (!key2.equals(studentID2)) {
							student2.add(key2);
						}
					}
					student2.add(studentID1);

					if (handler.checkForStudentConflict(student1, "AUTO")
							|| handler.checkForTeamLeaderIncludedOnly(student1, "AUTO")) {
						continue;
					}

					if (handler.checkForStudentConflict(student2, "AUTO")
							|| handler.checkForTeamLeaderIncludedOnly(student2, "AUTO")) {
						continue;
					}

					System.out.println(swappingStudents);
					List<Project> swappedProjects = new ArrayList<Project>();
					swappedProjects = swapTwoStudents(swappingStudents, tempProjects, "AUTO");

					Map<String, Double> preferecesPercentage = handler.computePreferencePercentage(swappedProjects);

					Double SDPreference = handler.computeStandardDeviation(preferecesPercentage);

					System.out.println("SD for preference is " + SDPreference);

					Map<String, Double> competency = handler.computeSkillCompetency(swappedProjects);

					Double SDCompetency = handler.computeStandardDeviation(competency);

					System.out.println("SD for competency is " + SDCompetency);

					Map<String, Double> skillGap = handler.computeSkillGap(swappedProjects);

					Double SDSkillGap = handler.computeStandardDeviation(skillGap);

					System.out.println("SD for skill gap is " + SDSkillGap);

					System.out.println("original sd is *****************************" + originalSD);
					System.out.println("min sd is *****************************" + minSD);
					System.out.println("current sd is *****************************" + SDPreference);
					if (SDPreference < minSD) {

						minSD = SDPreference;
						minSDProjects = new ArrayList<Project>();

						countLoop++;
						sdUpdated = true;
						
						for(Project p : swappedProjects) {
							
						
							minSDProjects.add(this.createNewProjectObject(p));
						}
						
				

					}

				}
			}


		}

		if (sdUpdated) {
			
			return minSDProjects;

		}
		
		

		System.out.println("*********************************** no new sd is computed " + minSD);
		
		return tempProjects;

	}

	// check the difference of last and first competency value of two projects
	// if greater than 1 then swap students between the two projects
	// then find the SD if improved saved into the serialization otherwise shows
	// proper message
	public List<Project> autoSkillGap(List<Project> shortListedProjects, Double previousSD)
			throws ConflictException, TeamLeaderException {

		List<Project>tempProjects = new ArrayList<Project>();
		tempProjects = this.sortingProjectWithSkillGap(shortListedProjects);

		
		tempProjects = this.sortingProjectWithSkillGap(tempProjects);

		// higher gap value
		Double gap1 = tempProjects.get(0).getSkillGap();

		// lower gap value
		Double gap2 = tempProjects.get(tempProjects.size() - 1).getSkillGap();

		System.out.println("comp 1 " + gap1 + " comp 2 " + gap2);

		int countLoop = 0;

		Project project1 = tempProjects.get(0);
		Project project2 = tempProjects.get(tempProjects.size() - 1);

		int projectIteration = 0;
		Double minSD = previousSD;
		boolean matched = true;
		List<Project> updatedList = null;
		boolean isUpdated = false;

		while (projectIteration < tempProjects.size()) {

			for (Map.Entry<String, Integer> entry1 : project1.getSelectedStudents().entrySet()) {

				for (Map.Entry<String, Integer> entry2 : project2.getSelectedStudents().entrySet()) {

					countLoop++;

					String studentID1 = entry1.getKey();
					String studentID2 = entry2.getKey();

					Map<String, String> swappingStudents = new HashMap<String, String>();
					swappingStudents.put(studentID1, project1.getID());
					swappingStudents.put(studentID2, project2.getID());

					Set<String> student1 = new HashSet<String>();

					for (String key1 : project1.getSelectedStudents().keySet()) {

						if (!key1.equals(studentID1)) {
							student1.add(key1);
						}
					}
					student1.add(studentID2);

					Set<String> student2 = new HashSet<String>();

					for (String key2 : project2.getSelectedStudents().keySet()) {

						if (!key2.equals(studentID2)) {
							student2.add(key2);
						}
					}
					student2.add(studentID1);

					// check if selected students validate the team leader and conflict of students
					// in that team.

					if (handler.checkForStudentConflict(student1, "AUTO")
							|| handler.checkForTeamLeaderIncludedOnly(student1, "AUTO")) {
						continue;
					}

					if (handler.checkForStudentConflict(student2, "AUTO")
							|| handler.checkForTeamLeaderIncludedOnly(student2, "AUTO")) {
						continue;
					}

					System.out.println(swappingStudents);

					List<Project> swappedProjects = swapTwoStudents(swappingStudents, tempProjects, "AUTO");

					Map<String, Double> preferecesPercentage = handler.computePreferencePercentage(swappedProjects);

					Double SDPreference = handler.computeStandardDeviation(preferecesPercentage);

					System.out.println("SD for preference is " + SDPreference);

					Map<String, Double> competency = handler.computeSkillCompetency(swappedProjects);

					Double SDCompetency = handler.computeStandardDeviation(competency);

					System.out.println("SD for competency is " + SDCompetency);

					Map<String, Double> skillGap = handler.computeSkillGap(swappedProjects);

					Double SDSkillGap = handler.computeStandardDeviation(skillGap);

					System.out.println("SD for skill gap is " + SDSkillGap);

					System.out.println("Previous sd is *****************************" + previousSD);
					System.out.println("Previous sd is *****************************" + skillGap);
					if (SDSkillGap < minSD) {
						System.out.println("now sd is *****************************" + skillGap);
						minSD = SDSkillGap;
						updatedList = new ArrayList<Project>();
						for(Project p : swappedProjects) {
							
							
							updatedList.add(this.createNewProjectObject(p));
						}
						// updatedList = this.sortingProjectWithCompetency(swappedProjects);
						isUpdated = true;
						

					}

				}
			}

			projectIteration++;

			if (projectIteration < tempProjects.size() - 1) {

				project1 = tempProjects.get(projectIteration);

			}

		}

		if (isUpdated) {

			tempProjects = updatedList;
		}

		return tempProjects;

	}

	// This will swap the two students from two projects
	public List<Project> swapTwoStudents(Map<String, String> swappingStudents, List<Project> shortListedProjects,
			String type) throws ConflictException, TeamLeaderException {
		List<Project>  tempProjects  = new ArrayList<Project>();
		tempProjects = shortListedProjects;
		
		if (swappingStudents.size() != 2) {

			CustomAlert alert = CustomAlert.getInstance();
			alert.showAlert("Error", "Exactly two students to select to swap. Please try again");
			return null;
		}

		// swap two students in two different project

		List<Entry<String, String>> swapList = new ArrayList<Entry<String, String>>(swappingStudents.entrySet());

		Project project1 = null;
		Project project2 = null;
		Integer prefStudent1 = null;
		Integer prefStudent2 = null;

		for (Project proj : tempProjects) {

			if (swapList.get(0).getValue().equals(proj.getID())) {
				project1 = proj;
				prefStudent1 = proj.getSelectedStudents().get(swapList.get(0).getKey());

			}
			if (swapList.get(1).getValue().equals(proj.getID())) {
				project2 = proj;
				prefStudent2 = proj.getSelectedStudents().get(swapList.get(1).getKey());
			}

		}

		List<Project> newProjectList = new ArrayList<Project>();
		

		for (Project project : tempProjects) {

			// replace project 1 student Id with the project 2 student

			if (project.getID().equals(project1.getID())) {

				Map<String, Integer> newStudentPreferences = new HashMap<String, Integer>();

				for (Map.Entry<String, Integer> entry : project.getSelectedStudents().entrySet()) {

					newStudentPreferences.put(entry.getKey(),
							handler.getPreferenceForStudentProject(project.getID(), entry.getKey()));
				}

				newStudentPreferences.remove(swapList.get(0).getKey());
				newStudentPreferences.put(swapList.get(1).getKey(), prefStudent2);

				// check if selected students validate the team leader and conflict of students
				// in that team.

				handler.checkForStudentConflict(newStudentPreferences.keySet(), type);
				handler.checkForTeamLeaderIncludedOnly(newStudentPreferences.keySet(), type);
				
				Project pr = new Project();
				pr = project;
				pr.setSelectedStudents(newStudentPreferences);
				newProjectList.add(pr);
				//project.setSelectedStudents(newStudentPreferences);

			}

			// replace project 2 student Id with the project 1 student
			else if (project.getID().equals(project2.getID())) {
				Map<String, Integer> newStudentPreferences = new HashMap<String, Integer>();

				for (Map.Entry<String, Integer> entry : project.getSelectedStudents().entrySet()) {

					newStudentPreferences.put(entry.getKey(),
							handler.getPreferenceForStudentProject(project.getID(), entry.getKey()));
				}
				newStudentPreferences.remove(swapList.get(1).getKey());
				newStudentPreferences.put(swapList.get(0).getKey(), prefStudent1);

				// check if selected students validate the team leader and conflict of students
				// in that team.

				handler.checkForStudentConflict(newStudentPreferences.keySet(), type);
				handler.checkForTeamLeaderIncludedOnly(newStudentPreferences.keySet(), type);
				
				Project pr = new Project();
				pr = project;
				pr.setSelectedStudents(newStudentPreferences);
				newProjectList.add(pr);
				//project.setSelectedStudents(newStudentPreferences);
			}
			else {
				Project pr = new Project();
				pr = project;
				newProjectList.add(pr);
			}

		}

		return newProjectList;

	}
	
	public Project createNewProjectObject(Project p) {
		
		Project newProject = new Project(p.getTitle(),p.getID(), p.getOwnerID(),p.getDescription());
		newProject.setSkillRanking(p.getSkillRanking());
		newProject.setStudentPreference(p.getStudentPreference());
		newProject.setSelectedStudents(p.getSelectedStudents());
		newProject.setSkillCompetency(p.getSkillCompetency());
		newProject.setPreferencePercentage(p.getPreferencePercentage());
		newProject.setSkillGap(p.getSkillGap());
		return newProject;
	}

}
