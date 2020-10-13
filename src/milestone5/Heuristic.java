package milestone5;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;

import application.CustomAlert;
import milestone1.ConflictException;
import milestone1.Preference;
import milestone1.Project;
import milestone1.RepeatedValueException;
import milestone1.ShortlistProject;
import milestone1.Student;
import milestone1.TeamLeaderException;
import milestone1.Utility;
import milestone2.Handler;

public class Heuristic {

	private final static int SWAPLIMIT = 5;

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

		for (Project proj : shortListedProjects) {
			System.out.println(proj.getSkillCompetency());
		}

		return shortListedProjects;
	}

	// sorting of project with the preference percentage
	public List<Project> sortingProjectWithPreference(List<Project> shortListedProjects) {

		Collections.sort(shortListedProjects, (c1, c2) -> {

			Double value1 = c1.getPreferencePercentage();
			Double value2 = c2.getPreferencePercentage();

			if (value1 < value2) {
				return 1;
			} else {
				return -1;
			}
		});

		for (Project proj : shortListedProjects) {
			System.out.println(proj.getPreferencePercentage());
		}

		return shortListedProjects;
	}

	// sorting of project with the skillshort fall
	public List<Project> sortingProjectWithSkillGap(List<Project> shortListedProjects) {

		Collections.sort(shortListedProjects, (c1, c2) -> {

			Double value1 = c1.getSkillGap();
			Double value2 = c2.getSkillGap();

			if (value1 < value2) {
				return 1;
			} else {
				return -1;
			}
		});

		for (Project proj : shortListedProjects) {
			System.out.println(proj.getSkillGap());
		}

		return shortListedProjects;
	}

	// check the difference of last and first competency value of two projects
	// if greater than 1 then swap students between the two projects
	// then find the SD if improved saved into the serialization otherwise shows
	// proper message
	public List<Project> autoswapCompetency(List<Project> shortListedProjects) {

		shortListedProjects = this.sortingProjectWithCompetency(shortListedProjects);

		Double competency1 = shortListedProjects.get(0).getSkillCompetency();

		Double competency2 = shortListedProjects.get(shortListedProjects.size() - 1).getSkillCompetency();
		System.out.println("comp 1 " + competency1 + " comp 2 " + competency2);
		int countLoop = 0;

		Double computedSD  = 0.0;
		
		while ((shortListedProjects.get(0).getSkillCompetency()
				- shortListedProjects.get(shortListedProjects.size() - 1).getSkillCompetency()) > 0.2
				&& countLoop <= SWAPLIMIT) {

			// need to swap students between two projects
			// project 1 has higher value and project 2 has lower competency value
			Project project1 = shortListedProjects.get(0);
			Project project2 = shortListedProjects.get(shortListedProjects.size() - 1);

			// for project 1 to get the higher competency value of student for swapping

			Map<String, Integer> selectedStudentsProject1 = project1.getSelectedStudents();

			String studentID1 = null;
			String studentID2 = null;

			int value1 = 0;
			for (Map.Entry<String, Integer> entry : selectedStudentsProject1.entrySet()) {
				Student studentInfo = handler.getStudentDetailByID(entry.getKey());

				Integer sum = studentInfo.getGrade().values().stream().mapToInt(Integer::intValue).sum();
				if (sum > value1) {
					value1 = sum;
					studentID1 = entry.getKey();
				}

			}

			// for project 2 to get the lower competency value of student for swapping

			Map<String, Integer> selectedStudentsProject2 = project2.getSelectedStudents();

			int value2 = 999;
			for (Map.Entry<String, Integer> entry : selectedStudentsProject2.entrySet()) {
				Student studentInfo = handler.getStudentDetailByID(entry.getKey());

				Integer sum = studentInfo.getGrade().values().stream().mapToInt(Integer::intValue).sum();
				if (sum < value2) {
					value2 = sum;
					studentID2 = entry.getKey();
				}

			}

			Map<String, String> swappingStudents = new HashMap<String, String>();
			swappingStudents.put(studentID1, project1.getID());
			swappingStudents.put(studentID2, project2.getID());

			Set<String> student1 = (Set<String>) project1.getSelectedStudents();
			student1.remove(studentID1);
			student1.add(studentID2);

			// check if selected students validate the team leader and conflict of students
			// in that team.
			if (checkForStudentConflict(student1, project1.getID()) || checkForTeamLeaderIncludedOnly(student1)) {
				continue;
			}

			Set<String> student2 = (Set<String>) project2.getSelectedStudents();
			student1.remove(studentID2);
			student1.add(studentID1);

			if (checkForStudentConflict(student2, project2.getID()) || checkForTeamLeaderIncludedOnly(student2)) {
				continue;
			}

			System.out.println(swappingStudents);

			List<Project> swappedProjects = swapTwoStudents(swappingStudents, shortListedProjects);

			Map<String, Double> preferecesPercentage = this.computePreferencePercentage(swappedProjects);

			Double SDPreference = this.computeStandardDeviation(preferecesPercentage);

			System.out.println("SD for preference is " + SDPreference);

			Map<String, Double> competency = this.computeSkillCompetency(swappedProjects);

			Double SDCompetency = this.computeStandardDeviation(competency);

			System.out.println("SD for competency is " + SDCompetency);

			Map<String, Double> skillGap = this.computeSkillGap(swappedProjects);

			Double SDSkillGap = this.computeStandardDeviation(skillGap);

			System.out.println("SD for skill gap is " + SDSkillGap);

			shortListedProjects = swappedProjects;
			shortListedProjects = this.sortingProjectWithCompetency(swappedProjects);

			countLoop++;
		}

		return shortListedProjects;

	}

	// check the difference of last and first preference value of two projects which
	// is on sorted order
	// if greater than 25% then swap students between the two projects
	// then find the SD if improved saved into the serialization otherwise shows
	// proper message
	public List<Project> autoPreferencePercentage(List<Project> shortListedProjects) {

		shortListedProjects = this.sortingProjectWithPreference(shortListedProjects);

		Double preference1 = shortListedProjects.get(0).getPreferencePercentage();

		Double preference2 = shortListedProjects.get(shortListedProjects.size() - 1).getPreferencePercentage();
		System.out.println("comp 1 " + preference1 + " comp 2 " + preference2);
		int countLoop = 0;
		while ((shortListedProjects.get(0).getPreferencePercentage()
				- shortListedProjects.get(shortListedProjects.size() - 1).getPreferencePercentage()) > 25
				&& countLoop <= SWAPLIMIT) {

			Project project1 = shortListedProjects.get(0);
			Project project2 = shortListedProjects.get(shortListedProjects.size() - 1);
			String studentID1 = null;
			String studentID2 = null;

			// lowest preference percentage

//			Project project1 = shortListedProjects.get(0); 
//			Project project2 = shortListedProjects.get(shortListedProjects.size() - 1);
			boolean matching = false;
			List<Preference> studentPreferences = handler.getProjectPreference(Handler.PreferenceFileNameSerialize);

			for (Project project : shortListedProjects) {

				if (!project.getID().equals(project2.getID())) {

					for (Map.Entry<String, Integer> entry : project.getSelectedStudents().entrySet()) {

						for (Preference preference : studentPreferences) {

							if (entry.getKey().equals(preference.getStudentNumber())) {

								List<Entry<String, Integer>> sortedEntries = Handler
										.SortedByValues(preference.getPreferedProjects());

								for (int i = 0; i < 2; i++) {

									String key = sortedEntries.get(i).getKey();
									// Integer value = sortedEntries.get(i).getValue();
									if (project2.getID().equals(key)) {
										matching = true;
										studentID1 = preference.getStudentNumber();
										project1 = project;
									}
								}

							}
						}

					}
				}
			}

			// second student no preference to swap

			for (Map.Entry<String, Integer> entry : project2.getSelectedStudents().entrySet()) {
				boolean found = false;
				for (Preference preference : studentPreferences) {

					if (entry.getKey().equals(preference.getStudentNumber())) {

						List<Entry<String, Integer>> sortedEntries = Handler
								.SortedByValues(preference.getPreferedProjects());

						for (int i = 0; i < 2; i++) {

							String key = sortedEntries.get(i).getKey();
							// Integer value = sortedEntries.get(i).getValue();
							if (project2.getID().equals(key)) {
								found = true;
								break;
							}
						}

						if (found) {
							studentID2 = entry.getKey();
							break;
						}

					}

				}

			}

			Map<String, String> swappingStudents = new HashMap<String, String>();

			swappingStudents.put(studentID1, project1.getID());
			swappingStudents.put(studentID2, project2.getID());

			System.out.println("%%%%%%%%% swapping student preferneces%%%%%%%%%");
			System.out.println(swappingStudents);

			List<Project> swappedProjects = swapTwoStudents(swappingStudents, shortListedProjects);

			Map<String, Double> preferecesPercentage = this.computePreferencePercentage(swappedProjects);

			Double SDPreference = this.computeStandardDeviation(preferecesPercentage);

			System.out.println("SD for preference is " + SDPreference);

			Map<String, Double> competency = this.computeSkillCompetency(swappedProjects);

			Double SDCompetency = this.computeStandardDeviation(competency);

			System.out.println("SD for competency is " + SDCompetency);

			Map<String, Double> skillGap = this.computeSkillGap(swappedProjects);

			Double SDSkillGap = this.computeStandardDeviation(skillGap);

			System.out.println("SD for skill gap is " + SDSkillGap);

			// shortListedProjects = swappedProjects;
			shortListedProjects = this.sortingProjectWithPreference(swappedProjects);

			countLoop++;
		}

		System.out.println("preference count  " + countLoop);
		return shortListedProjects;

	}

	// check the difference of last and first competency value of two projects
	// if greater than 1 then swap students between the two projects
	// then find the SD if improved saved into the serialization otherwise shows
	// proper message
	public List<Project> autoSkillGap(List<Project> shortListedProjects) {

		shortListedProjects = this.sortingProjectWithSkillGap(shortListedProjects);

		// higher gap value
		Double gap1 = shortListedProjects.get(0).getSkillGap();

		// lower gap value
		Double gap2 = shortListedProjects.get(shortListedProjects.size() - 1).getSkillGap();

		System.out.println("comp 1 " + gap1 + " comp 2 " + gap2);

		int countLoop = 0;

		while ((shortListedProjects.get(0).getSkillGap()
				- shortListedProjects.get(shortListedProjects.size() - 1).getSkillCompetency()) > 1
				&& countLoop <= SWAPLIMIT) {

			// need to swap students between two projects
			// project 1 has higher value and project 2 has lower skill fall value
			Project project1 = shortListedProjects.get(0);
			Project project2 = shortListedProjects.get(shortListedProjects.size() - 1);

			// for project 1 to get the higher skill fall value of student for swapping

			String studentID1 = null;
			String studentID2 = null;

			// get the highest skill short fall of the student according to the project 1

			Map<String, Integer> selectedStudentsProject1 = project1.getSelectedStudents();
			Double skillShort = 0.0;

			for (Map.Entry<String, Integer> entry : selectedStudentsProject1.entrySet()) {

				System.out.println(" student id is " + entry.getKey());
				Student studentInfo = handler.getStudentDetailByID(entry.getKey());
				int sum = 0;
				int count = 0;
				for (Map.Entry<String, Integer> entryGrade : studentInfo.getGrade().entrySet()) {

					int studentGrade = entryGrade.getValue();
					int diff = project1.getSkillRanking().get(entryGrade.getKey()) - studentGrade;
					if (diff > 0) {
						sum += diff;
						count++;
					}

				}

				Double updatedSkill = (double) sum / count;

				System.out.println("student id " + entry.getKey() + "updated skill " + updatedSkill);

				if (updatedSkill > skillShort) {
					skillShort = updatedSkill;
					studentID1 = entry.getKey();
				}

			}

			// get the another proper student to swap to decrease shortfall of the highest
			// skill fall student above

			Double skillShort2 = 4.0;
			for (Project project : shortListedProjects) {

				if (!project.getID().equals(project1.getID())) {

					Map<String, Integer> selectedStudentsProject2 = project.getSelectedStudents();

					for (Map.Entry<String, Integer> entry : selectedStudentsProject2.entrySet()) {

						Student studentInfo = handler.getStudentDetailByID(entry.getKey());
						int sum = 0;
						int count = 0;
						for (Map.Entry<String, Integer> entryGrade : studentInfo.getGrade().entrySet()) {

							int studentGrade = entryGrade.getValue();

							int diff = project1.getSkillRanking().get(entryGrade.getKey()) - studentGrade;
							if (diff > 0) {
								sum += diff;
								count++;
							}

						}

						Double updatedSkill = (double) sum / count;

						if (updatedSkill < skillShort2) {
							skillShort2 = updatedSkill;
							studentID2 = entry.getKey();
							project2 = project;
						}

					}

				}

			}

			Map<String, String> swappingStudents = new HashMap<String, String>();
			swappingStudents.put(studentID1, project1.getID());
			swappingStudents.put(studentID2, project2.getID());

			System.out.println(swappingStudents);

			List<Project> swappedProjects = swapTwoStudents(swappingStudents, shortListedProjects);

			Map<String, Double> preferecesPercentage = this.computePreferencePercentage(swappedProjects);

			Double SDPreference = this.computeStandardDeviation(preferecesPercentage);

			System.out.println("SD for preference is " + SDPreference);

			Map<String, Double> competency = this.computeSkillCompetency(swappedProjects);

			Double SDCompetency = this.computeStandardDeviation(competency);

			System.out.println("SD for competency is " + SDCompetency);

			Map<String, Double> skillGap = this.computeSkillGap(swappedProjects);

			Double SDSkillGap = this.computeStandardDeviation(skillGap);

			System.out.println("SD for skill gap is " + SDSkillGap);

			shortListedProjects = swappedProjects;
			shortListedProjects = this.sortingProjectWithSkillGap(swappedProjects);

			countLoop++;

		}

		return shortListedProjects;

	}

	// This will swap the two students from two projects
	public List<Project> swapTwoStudents(Map<String, String> swappingStudents, List<Project> shortListedProjects) {

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

		for (Project proj : shortListedProjects) {

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

		for (Project project : shortListedProjects) {

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
				this.checkForStudentConflict(newStudentPreferences.keySet(), project.getID());
				this.checkForTeamLeaderIncludedOnly(newStudentPreferences.keySet());

				project.setSelectedStudents(newStudentPreferences);

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
				this.checkForStudentConflict(newStudentPreferences.keySet(), project.getID());
				this.checkForTeamLeaderIncludedOnly(newStudentPreferences.keySet());

				project.setSelectedStudents(newStudentPreferences);
			}

			newProjectList.add(project);

		}

		return newProjectList;

	}

	// compute % of preference of students

	public Map<String, Double> computePreferencePercentage(List<Project> selectedProjectStudent) {

		List<Preference> studentPreference = handler.getProjectPreference(Handler.PreferenceFileNameSerialize);

		Map<String, Double> preferencePercentage = new HashMap<String, Double>();
		for (Project project : selectedProjectStudent) {
			System.out.println("Project Id is " + project.getID());
			Double percentage = 0.0;
			for (Map.Entry<String, Integer> entry : project.getSelectedStudents().entrySet()) {

				for (Preference preference : studentPreference) {

					if (entry.getKey().equals(preference.getStudentNumber())) {

						List<Entry<String, Integer>> sortedEntries = Handler
								.SortedByValues(preference.getPreferedProjects());

						for (int i = 0; i < 2; i++) {

							String key = sortedEntries.get(i).getKey();
							// Integer value = sortedEntries.get(i).getValue();
							if (project.getID().equals(key)) {
								percentage += 100;
							}
						}

					}
				}

			}

			Double percentageValue = percentage / project.getSelectedStudents().size();

			project.setPreferencePercentage(percentageValue);
			System.out.println("Preference % is " + percentageValue);
			System.out.println("**********************");
			preferencePercentage.put(project.getID(), percentageValue);
		}

		return preferencePercentage;
	}

	// computing skill competency matrix of each projects
	public Map<String, Double> computeSkillCompetency(List<Project> selectedProjectStudent) {

		System.out.println("******************Skill competency*****************");

		System.out.println("Number of projects selected are : " + selectedProjectStudent.size());
		Map<String, Double> skillCompetency = new HashMap<String, Double>();

		if (selectedProjectStudent != null && selectedProjectStudent.size() > 0) {

			for (Project selectedProject : selectedProjectStudent) {

				int totalSkillValue = 0;
				System.out.println("\nFor Project: " + selectedProject.getID());
				System.out.println("\nSelected Students with skill sets are\n");
				for (Map.Entry<String, Integer> entry : selectedProject.getSelectedStudents().entrySet()) {
					Student detailsInfo = handler.getStudentDetailByID(entry.getKey());

					System.out.print("\n");
					String ranking = "";
					for (Map.Entry<String, Integer> detailEntry : detailsInfo.getGrade().entrySet()) {
						totalSkillValue += detailEntry.getValue();
						ranking += detailEntry.getKey() + " " + detailEntry.getValue() + " ";
					}

					System.out.print(entry.getKey() + " " + ranking);
				}

				System.out.print("total skill" + " " + totalSkillValue);
				System.out.print("\n\nTeam Fitness Score: " + totalSkillValue / 16.0);
				selectedProject.setSkillCompetency(totalSkillValue / 16.0);
				System.out.println();

				skillCompetency.put(selectedProject.getID(), totalSkillValue / 16.0);

			}

		} else {
			System.out.println(" Team is not formed yet");
		}

		return skillCompetency;

	}

	// calulate the skill gap of each projects
	public Map<String, Double> computeSkillGap(List<Project> selectedProjectStudent) {

		Map<String, Double> projectGaps = new HashMap<String, Double>();
		System.out.println("\n********Gap values *******");
		Double sumValue = 0.0;
		for (Project selectedProject : selectedProjectStudent) {

			Double gapValue = 0.0;
			Map<String, Integer> projectSkillRanking = selectedProject.getSkillRanking();

			Map<String, Integer> sumOfStudentsRanking = new HashMap<String, Integer>();

			for (Map.Entry<String, Integer> entry : selectedProject.getSelectedStudents().entrySet()) {

				Student studentDetail = handler.getStudentDetailByID(entry.getKey());
				Map<String, Integer> studentSkillRanking = studentDetail.getGrade();

				for (Map.Entry<String, Integer> entryStudentGrade : studentSkillRanking.entrySet()) {

					String key = entryStudentGrade.getKey();
					Integer value = entryStudentGrade.getValue();

					Integer previousValue = sumOfStudentsRanking.get(key) == null ? 0 : sumOfStudentsRanking.get(key);
					sumOfStudentsRanking.put(key, previousValue + value);

				}

			}

			// to find the gap

			for (Map.Entry<String, Integer> ranking : sumOfStudentsRanking.entrySet()) {

				Double gap = (double) (projectSkillRanking.get(ranking.getKey())
						- (ranking.getValue() / sumOfStudentsRanking.size()));
				Double positiveGap = (gap > 0.0) ? gap : 0.0;

				gapValue += positiveGap;
				sumValue += positiveGap;

			}
			projectGaps.put(selectedProject.getID(), gapValue);

			// System.out.println("Sum value is: " + gapValue);

		}

		return projectGaps;

	}

	// compute standard deviation dynamically for each project with their values
	public Double computeStandardDeviation(Map<String, Double> values) {

		double meanValue = values.values().stream().mapToDouble(Double::doubleValue).average().orElse(0);

		System.out.println("Average is " + meanValue);

		Double meanDifferenceSquare = 0.0;
		System.out.println("\n********Gap values *******");
		for (Map.Entry<String, Double> entry : values.entrySet()) {
			System.out.println("Project ID ");
			System.out.println("gap value is: " + entry.getValue() + "\n");

			meanDifferenceSquare += Math.pow((meanValue - entry.getValue()), 2);

		}

		Double standardDeviation = Math.sqrt(meanDifferenceSquare / values.size());

		Double roundSD = Math.round(standardDeviation * 1000D) / 1000D;
		;

		return roundSD;
	}

	// updated method check for student conflict after all the selected students are
	// finalized for this project

	public boolean checkForStudentConflict(Set<String> studentsID, String ProjectID) {

		List<String> allConflicts = new ArrayList<String>();
		boolean status = false;
		for (String studentID : studentsID) {

			Student studentDetail = handler.getStudentDetailByID(studentID);
			String[] conflict = studentDetail.getConflictStudent();
			if (conflict != null && conflict.length > 0) {
				for (String con : conflict) {
					allConflicts.add(con);
				}

			}

		}

		for (String id : studentsID) {

			if (studentsID.contains(id)) {
				status = true;
				break;
			}

		}

		return status;
	}

	// if team leader is valid then return true otherwise false
	public boolean checkForTeamLeaderIncludedOnly(Set<String> addedStudents) {

		int countLeader = 0;
		for (String studentID : addedStudents) {
			Student studentDetail = handler.getStudentInfoDetailByID(studentID);
			String personality = studentDetail.getPersonality();
			// System.out.println("personlaity" + personality);
			if (personality != null && personality.equals("A")) {
				countLeader++;
			}
		}
		System.out.println("Team leader count" + countLeader);
		if (countLeader == 0 || countLeader > 1) {

			return true;

		}

		return false;
	}

}
