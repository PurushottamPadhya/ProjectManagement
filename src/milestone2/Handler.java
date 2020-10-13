package milestone2;

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
import java.util.stream.Collectors;

import milestone1.ConflictException;
import milestone1.Preference;
import milestone1.Project;
import milestone1.RepeatedValueException;
import milestone1.ShortlistProject;
import milestone1.Student;
import milestone1.TeamLeaderException;
import milestone1.Utility;

public class Handler {

	public static final String companyFileNameSerialize = "companies.dat";
	public static final String OwnerFileNameSerialize = "owners.dat";
	public static final String ProjectFileNameSerialize = "projects.dat";
	public static final String StudentFileNameSerialize = "students.dat";
	public static final String StudentInfoFileNameSerialize = "studentinfo.dat";
	public static final String PreferenceFileNameSerialize = "preferences.dat";
	public static final String ShortlistFileNameSerialize = "shortlistedProjects.dat";
	public static final String ShortlistProjectStudentSerialize = "shortlistedProjectStudent.dat";

	public static final int numberOfPreferedStudentLimit = 5;

	/*
	 * Begin input
	 * 
	 * 
	 */

	public void FormTeam() {

		List<ShortlistProject> shortListed = this.getShortlistedProjects(ShortlistFileNameSerialize);

		this.displayShortListedProjects(shortListed);

		List<Project> projects = this.getTopMatchingStudent();

		// this will show the shortlisted Projects
		System.out.println("\nPlease Select your project");

		String projectID = Utility.scan.next();

		// this will show the preference based on the projects
		this.displayPreferedStudentListForProjectID(projects, projectID);

		Project projectDetail = this.getProjectDetailByID(projectID);

		// this will show all the projects with student preferences
		// this.displayPreferedStudentList(projects);

		Map<String, Integer> addedStudents = new HashMap<String, Integer>();

		try {

			for (int i = 0; i < 4; i++) {
				System.out.println("Which student do you want to add?");
				String studentID = Utility.scan.next();

				this.checkForDuplicateStudentAssign(studentID, addedStudents);
				this.checkForStudentConflict(studentID, addedStudents);

				Integer preferences = this.getPreferenceForStudentProject(projectID, studentID);
				
				
				addedStudents.put(studentID, preferences);

			}

			
			
			this.checkForTeamLeaderIncludedOnly(addedStudents);

			this.displayPreferedStudentListForProjectID(projects, projectID);

			
			
			projectDetail.setSelectedStudents(addedStudents);
			this.WriteShortlistedProjectStudent(projectDetail);

			System.out.println("Sucessfully created a team for the project " + projectID);
			this.showAddedStudentsInProject(projectID);

		} catch (RepeatedValueException e) {
			System.out.println(e.getMessage());
		} catch (ConflictException e) {
			System.out.println(e.getMessage());
		} catch (TeamLeaderException e) {
			System.out.println(e.getMessage());
		}

	}

	/*
	 * Display the team fitness matric and calculate the standard deviation for the
	 * skill short fall across the project
	 */

	public void displayTeamFitnessMetrics() {

		this.showDetailOfProjectFitnessMatrix();

		Double standardDeviation = this.computeStandardDeviationForSkillShortFall();

		System.out.println("\n#######Standard Deviation for Skill ShortFall across projects is: " + standardDeviation);

		this.computePreferencePercentage();
	}

	public Integer getPreferenceForStudentForProject(Project details, String studentID) {

		for (Map.Entry<String, Integer> entry : details.getStudentPreference().entrySet()) {

			if (entry.getKey().equals(studentID)) {
				return entry.getValue();
			}
		}

		return null;
	}

	public Integer getPreferenceForStudentProject(String projectID, String studentID) {

		List<Preference> preferences = this.getProjectPreference("");
		for(Preference pref: preferences) {
			
			if (pref.getStudentNumber().equals(studentID)) {
				
				return pref.getPreferedProjects().get(projectID) != null ?pref.getPreferedProjects().get(projectID) : 0;
			}
		}

		return null;
	}
	
	public int testException() {
		return 5;
		// throw new TeamLeaderException("Only one team leader should be in a team");
	}

	// if team leader is valid then return true otherwise false
	public void checkForTeamLeaderIncludedOnly(Map<String, Integer> addedStudents) throws TeamLeaderException {

		int countLeader = 0;
		for (Map.Entry<String, Integer> entry : addedStudents.entrySet()) {
			Student studentDetail = this.getStudentInfoDetailByID(entry.getKey());
			String personality = studentDetail.getPersonality();
			// System.out.println("personlaity" + personality);
			if (personality != null && personality.equals("A")) {
				countLeader++;
			}
		}
		System.out.println("Team leader count" + countLeader);
		if (countLeader == 0 || countLeader > 1) {

			throw new TeamLeaderException("Only one team leader should be in a team");

		}

		// return false;
	}

	// if repetition then return true otherwise false
	public void checkForDuplicateStudentAssign(String studentID, Map<String, Integer> addedStudents)
			throws RepeatedValueException {

		// this checks on the currently selected students

		this.checkForStudentRepetition(studentID, addedStudents);

		// this checks if students are already added in another project before

		List<Project> alreadySelectedProjects = this.getShortlistedProjectStudent(ShortlistProjectStudentSerialize);
		if (alreadySelectedProjects != null && alreadySelectedProjects.size() > 0) {
			for (Project aSelected : alreadySelectedProjects) {

				this.checkForStudentRepetition(studentID, aSelected.getSelectedStudents());
			}
		}

		// return false;
	}

	// if repetition then return true otherwise false
	public void checkForStudentRepetition(String studentID, Map<String, Integer> addedStudents)
			throws RepeatedValueException {

		// this checks on the currently selected students

		for (Map.Entry<String, Integer> entry : addedStudents.entrySet()) {

			if (entry.getKey().equals(studentID)) {

				throw new RepeatedValueException("Student ID already selected for the project");

			}

		}

		// this checks if students are already added in another project before

		List<Project> alreadySelectedProjects = this.getShortlistedProjectStudent(ShortlistProjectStudentSerialize);
		if (alreadySelectedProjects != null && alreadySelectedProjects.size() > 0) {
			for (Project aSelected : alreadySelectedProjects) {

				for (Map.Entry<String, Integer> entry : aSelected.getSelectedStudents().entrySet()) {

					if (entry.getKey().equals(studentID)) {

						throw new RepeatedValueException("Student ID already selected for the project");

					}

				}

			}
		}

		// return false;
	}


	
	
	// if conflict then return true otherwise false
	public void checkForStudentConflict(String studentID, Map<String, Integer> addedStudents) throws ConflictException {

		Student studentDetail = this.getStudentDetailByID(studentID);
		String[] conflictStudent = studentDetail.getConflictStudent();
		if (conflictStudent != null && conflictStudent.length > 0) {
			for (Map.Entry<String, Integer> entry : addedStudents.entrySet()) {
				for (String conflictID : conflictStudent) {
					if (conflictID != null) {
						if (conflictID.equals(entry.getKey())) {

							throw new ConflictException("Can not add conflict student on the same group");
						}
					}

				}

			}
		}

		// return false;
	}
	
	
	public void checkForStudentConflict(String studentID) throws ConflictException {
		
		Student studentDetail = this.getStudentDetailByID(studentID);
		String[] conflictStudent = studentDetail.getConflictStudent();
		if (conflictStudent != null && conflictStudent.length > 0) {
				for (String conflictID: conflictStudent) {
					if (conflictID != null) {
						if (conflictID.equals(studentID)) {
							throw new ConflictException("Can not add conflict student on the same group");
						}
					}

				}
		}
		

		
		//return false;
	}
	
	
	

	
	
	
	
	
	
	// check for conflict of students on swapping
	public void checkForStudentConflict(String studentID, Project project, String swapStudentID) throws ConflictException {
		
		Student studentDetail = this.getStudentDetailByID(studentID);
		String[] conflictStudent = studentDetail.getConflictStudent();
		
		
		System.out.println("Conflict students for student Id " + studentID + " is " + conflictStudent);
		
		Map<String, Integer> selectedStudents = project.getSelectedStudents();
		
		System.out.println("SelectedStudents " + selectedStudents);
		
		if (conflictStudent != null && conflictStudent.length > 0) {
			
				for (String conflictID: conflictStudent) {

					if (conflictID != null) {
						for (Map.Entry<String, Integer> entry: selectedStudents.entrySet()) {
							
							if (!entry.getKey().equals(swapStudentID)) {
								
								if (conflictID.equals(entry.getKey())) {
									throw new ConflictException("Can not add conflict student on the same group");
								}
								
							}
						}
	
					}

				}
		}
		

		
		//return false;
	}

	/*
	 * End of input
	 * 
	 * 
	 */

	/*
	 * 
	 * Starting Writing on a file
	 */

	@SuppressWarnings("unchecked")
	public void WriteShortlistedProjectStudent(Project project) {
		
		List<Project> previousProjects = new ArrayList<Project>();
		previousProjects = this.getShortlistedProjectStudent(ShortlistProjectStudentSerialize);
		previousProjects.add(project);

		System.out.println("Project ID for write " + project.getID());

		try {
			ObjectOutputStream outPutStream = new ObjectOutputStream(
					new FileOutputStream(ShortlistProjectStudentSerialize));
			
			outPutStream.writeObject(previousProjects);

			outPutStream.close();
		} catch (IOException e) {
			e.getMessage();
		}

	}

	public void updateShortListedProjects(List<Project> projects) {

		try {
			ObjectOutputStream outPutStream = new ObjectOutputStream(
					new FileOutputStream(ShortlistProjectStudentSerialize));

			outPutStream.writeObject(projects);

			outPutStream.close();
		} catch (IOException e) {
			e.getMessage();
		}

	}

	/*
	 * starting of file read using serializations
	 * 
	 * 
	 */

	// projects list
	@SuppressWarnings("unchecked")
	public List<Project> getProjects(String fileName) {

		List<Project> projects = null;

		try {
			ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(ProjectFileNameSerialize));

			Object object = inputStream.readObject();
			if (object instanceof List<?>) {
				projects = (List<Project>) object;
			}

		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.getMessage();
		} catch (IOException e) {
			e.getMessage();
		}

		return projects;

	}

	// students list
	@SuppressWarnings("unchecked")
	public List<Student> getStudents(String fileName) {

		List<Student> students = null;

		try {
			ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(StudentFileNameSerialize));

			Object object = inputStream.readObject();
			if (object instanceof List<?>) {
				students = (List<Student>) object;
			}

		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.getMessage();
		} catch (IOException e) {
			e.getMessage();
		}

		return students;

	}

	@SuppressWarnings("unchecked")
	public List<Student> getStudentsWithInfo(String fileName) {

		List<Student> students = null;

		try {
			ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(StudentInfoFileNameSerialize));

			Object object = inputStream.readObject();
			if (object instanceof List<?>) {
				students = (List<Student>) object;
			}

		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.getMessage();
		} catch (IOException e) {
			e.getMessage();
		}

		return students;

	}

	// Project preference
	@SuppressWarnings("unchecked")
	public List<Preference> getProjectPreference(String fileName) {

		List<Preference> preferences = null;

		try {
			ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(PreferenceFileNameSerialize));

			Object object = inputStream.readObject();
			if (object instanceof List<?>) {
				preferences = (List<Preference>) object;
			}

		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.getMessage();
		} catch (IOException e) {
			e.getMessage();
		}

		return preferences;

	}

	// Project preference
	@SuppressWarnings("unchecked")
	public List<ShortlistProject> getShortlistedProjects(String fileName) {

		List<ShortlistProject> preferences = null;

		try {
			ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(ShortlistFileNameSerialize));

			Object object = inputStream.readObject();
			if (object instanceof List<?>) {
				preferences = (List<ShortlistProject>) object;
			}

		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.getMessage();
		} catch (IOException e) {
			e.getMessage();
		}

		return preferences;

	}

	// Project preference
	@SuppressWarnings("unchecked")
	public List<Project> getShortlistedProjectStudent(String fileName) {

		List<Project> projects = new ArrayList<Project>();

		try {
			ObjectInputStream inputStream = new ObjectInputStream(
					new FileInputStream(ShortlistProjectStudentSerialize));

			Object object = inputStream.readObject();

			projects = (List<Project>) object;

		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.getMessage();
		} catch (IOException e) {
			e.getMessage();
		}

		return projects;

	}

	// get project detail by project ID

	public Project getProjectDetailByID(String ID) {

		List<Project> allProjects = getProjects(ProjectFileNameSerialize);

		for (Project project : allProjects) {
			if (project.getID().equals(ID)) {
				return project;
			}

		}

		return null;
	}
	
	
	public Project getSelectedProjectDetailByID(String ID) {

		List<Project> allProjects = getProjects(ShortlistProjectStudentSerialize);

		for (Project project : allProjects) {
			if (project.getID().equals(ID)) {
				return project;
			}

		}

		return null;
	}

	// get student detail by student ID

	public Student getStudentDetailByID(String ID) {

		List<Student> allStudents = getStudents(StudentFileNameSerialize);

		for (Student student : allStudents) {
			if (student.getStudentNumber().equals(ID)) {
				return student;
			}

		}

		return null;
	}

	public Student getStudentInfoDetailByID(String ID) {

		List<Student> allStudents = getStudentsWithInfo(StudentInfoFileNameSerialize);

		for (Student student : allStudents) {
			if (student.getStudentNumber().equals(ID)) {
				return student;
			}

		}

		return null;
	}

	/*
	 * end of file read using serializations
	 * 
	 * 
	 */

	// find the matching students for the project
	// add the preference of the students and preference marks on the projects
	// according to shortlisted projects
	public List<Project> getTopMatchingStudent() {

		List<ShortlistProject> shortListedProjects = this.getShortlistedProjects(ShortlistFileNameSerialize);

		for (ShortlistProject shortlist : shortListedProjects) {
			System.out.print(shortlist.getProjectID() + " " + shortlist.getPrefernceSum() + " ");
		}

		List<Project> preferedProjects = new ArrayList<>();

		for (ShortlistProject shortLis : shortListedProjects) {

			Project project = this.getProjectDetailByID(shortLis.getProjectID());

			Project projecWithPreference = this.getListOfPreferedStudents(project);
			for (Map.Entry<String, Integer> entry : projecWithPreference.getStudentPreference().entrySet()) {
				System.out.println("key is " + entry.getKey() + "value " + entry.getValue());
			}

			preferedProjects.add(projecWithPreference);
		}

		return preferedProjects;

	}

	// find the preference of each students with the projects

	public Project getListOfPreferedStudents(Project project) {

		List<Preference> studentPreference = this.getProjectPreference(PreferenceFileNameSerialize);

		Map<String, Integer> previousPreference = project.getStudentPreference();

		for (Preference pref : studentPreference) {

			if (pref.getPreferedProjects().get(project.getID()) != null) {

				previousPreference.put(pref.getStudentNumber(), pref.getPreferedProjects().get(project.getID()));

			}

		}

		Map<String, Integer> sorted = this.sortingStudentPreference(previousPreference);

		project.setStudentPreference(sorted);

		// sorting preferences

		return project;
	}

	public boolean checkConflictStudent() {

		return true;
	}

	public boolean checkRepeatationStudent() {

		return true;
	}
//Entry.comparingByValue()
	public Map<String, Integer> sortingStudentPreference(Map<String, Integer> unsortedMap) {

		Map<String, Integer> sortedMap = unsortedMap.entrySet().stream().sorted(Entry.comparingByValue())
				.collect(Collectors.toMap(Entry::getKey, Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));


		
		return sortedMap;
	}

	// Sorting by value in descending order
	public static <K, V extends Comparable<? super V>> List<Entry<K, V>> SortedByValues(Map<K, V> map) {

		List<Entry<K, V>> sortedEntries = new ArrayList<Entry<K, V>>(map.entrySet());

		Collections.sort(sortedEntries, new Comparator<Entry<K, V>>() {
			@Override
			public int compare(Entry<K, V> e1, Entry<K, V> e2) {
				return e2.getValue().compareTo(e1.getValue());
			}
		});

		return sortedEntries;
	}

	/*
	 * Displaying contents
	 */

	public void displayShortListedProjects(List<ShortlistProject> projects) {

		System.out.println("");

		String output = "";
		for (ShortlistProject currentProject : projects) {

			output += currentProject.getProjectID() + " ";

		}
		System.out.println(output);

	}

	public void displayPreferedStudentList(List<Project> projects) {

		System.out.println("");
		for (Project currentProject : projects) {

			System.out.println("******************************************************");
			System.out.println("Preference for the Project " + currentProject.getID());

			System.out.println("Required Skill ranking are ");
			String ranking = "";
			for (Map.Entry<String, Integer> entry : currentProject.getSkillRanking().entrySet()) {

				ranking += entry.getKey() + " " + entry.getValue() + " ";
			}

			System.out.println("Matching students with preferences ");
			String output = "";

			for (Map.Entry<String, Integer> entry : currentProject.getStudentPreference().entrySet()) {

				output += entry.getKey() + " " + entry.getValue() + " ";
			}

			System.out.println(output);
		}

	}

	public void displayPreferedStudentListForProjectID(List<Project> projects, String ID) {

		System.out.println("");
		for (Project currentProject : projects) {
			if (currentProject.getID().equals(ID)) {

				System.out.println("******************************************************");
				System.out.println("Preference for the Project " + currentProject.getID());

				System.out.println("Required Skill ranking are ");
				String ranking = "";
				for (Map.Entry<String, Integer> entry : currentProject.getSkillRanking().entrySet()) {

					ranking += entry.getKey() + " " + entry.getValue() + " ";
				}

				System.out.println(ranking);

				System.out.println("Matching students ");
				String output = "";

				for (Map.Entry<String, Integer> entry : currentProject.getStudentPreference().entrySet()) {

					output += entry.getKey() + " " + entry.getValue() + " ";
				}

				System.out.println(output);

			}

		}

	}

	public void showAddedStudentsInProject(String projectID) {

		double averageSkillCompetency = this.getAverageSkillCompetancy(projectID);
		System.out.print("\nScore: " + averageSkillCompetency);
		System.out.println();
	}

	public double getAverageSkillCompetancy(String projectID) {
		int totalSkillValue = 0;

		List<Project> selectedProjectStudent = this.getShortlistedProjectStudent(ShortlistProjectStudentSerialize);

		if (selectedProjectStudent != null && selectedProjectStudent.size() > 0) {

			for (Project selectedProject : selectedProjectStudent) {

				if (selectedProject.getID().equals(projectID)) {
					
					for (Map.Entry<String, Integer> entry : selectedProject.getSelectedStudents().entrySet()) {
						Student detailsInfo = this.getStudentDetailByID(entry.getKey());

						System.out.print("\n");
						String ranking = "";
						for (Map.Entry<String, Integer> detailEntry : detailsInfo.getGrade().entrySet()) {
							totalSkillValue += detailEntry.getValue();
							ranking += detailEntry.getKey() + " " + detailEntry.getValue() + " ";
						}

						System.out.print(entry.getKey() + " " + ranking);
					}
				}

			}
		} else {
			System.out.println(" Team is not formed yet");
		}

		System.out.print("\ntotal skill" + " " + totalSkillValue);
		return (totalSkillValue / 16.0);
	}

	/*
	 * Display the project fitness value for the formed each projects
	 */

	public Map<String, Double> showDetailOfProjectFitnessMatrix() {

		System.out.println("******************TEAM FITNESS MATRIX*****************");

		List<Project> selectedProjectStudent = this.getShortlistedProjectStudent(ShortlistProjectStudentSerialize);
		System.out.println("Number of projects selected are : " + selectedProjectStudent.size());
		Map<String, Double> skillCompetency = new HashMap<String, Double>();
		
		if (selectedProjectStudent != null && selectedProjectStudent.size() > 0) {

			for (Project selectedProject : selectedProjectStudent) {

				int totalSkillValue = 0;
				System.out.println("\nFor Project: " + selectedProject.getID());
				System.out.println("\nSelected Students with skill sets are\n");
				for (Map.Entry<String, Integer> entry : selectedProject.getSelectedStudents().entrySet()) {
					Student detailsInfo = this.getStudentDetailByID(entry.getKey());

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
				
				skillCompetency.put(selectedProject.getID(), totalSkillValue/16.0);

			}

			this.updateShortListedProjects(selectedProjectStudent);

		} else {
			System.out.println(" Team is not formed yet");
		}
		
		
		return skillCompetency;
		

	}

	/*
	 * 
	 * Standard deviation computation
	 */

	/*
	 * Get all the selected project
	 * 
	 * Sum all the selected projects students skill ranking and subtract with the
	 * project required ranking then store on the projectGap collection Find the
	 * mean of the positive gap only Subtract mean from the original gap value and
	 * square the result and similarly do for the each gap and compute sum finally
	 * divide the last result by the number of gaps and get the square root of the
	 * result resulting value is the standard deviation of the overall project
	 * formation matrix based on skill shortfall
	 */
	


	public Map<String, Double> computeSkillGap() {
		
		List<Project> selectedProjectStudent = this.getShortlistedProjectStudent(ShortlistProjectStudentSerialize);

		Map<String, Double> projectGaps = new HashMap<String, Double>();
		System.out.println("\n********Gap values *******");
		Double sumValue = 0.0;
		for (Project selectedProject : selectedProjectStudent) {

			Double gapValue = 0.0;
			Map<String, Integer> projectSkillRanking = selectedProject.getSkillRanking();

			int sumOfStudentsRankingP = 0;
			int sumOfStudentsRankingN = 0;
			int sumOfStudentsRankingA = 0;
			int sumOfStudentsRankingW = 0;

			Map<String, Integer> sumOfStudentsRanking = new HashMap<String, Integer>();

			for (Map.Entry<String, Integer> entry : selectedProject.getSelectedStudents().entrySet()) {

				Student studentDetail = this.getStudentDetailByID(entry.getKey());
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
			selectedProject.setSkillGap(gapValue);

			// System.out.println("Sum value is: " + gapValue);

		}

		this.updateShortListedProjects(selectedProjectStudent);
		
		// calculating standard deviation

		Double meanValue = sumValue / projectGaps.size();

		// compute difference of each value from the mean and square the result and sum
		// for each projects

		Double meanDifferenceSquare = 0.0;
		System.out.println("\n********Gap values *******");
		for (Map.Entry<String, Double> entry : projectGaps.entrySet()) {
			System.out.println("Project ID ");
			System.out.println("gap value is: " + entry.getValue() + "\n");
		
			meanDifferenceSquare += Math.pow((meanValue - entry.getValue()), 2);

		}

		Double standardDeviation = Math.sqrt(meanDifferenceSquare / projectGaps.size());

		this.updateShortListedProjects(selectedProjectStudent);

		return projectGaps;
		
	}
	
	public Double computeStandardDeviation(Map<String, Double> values) {
		
		double meanValue = values.values().stream().mapToDouble(Double:: doubleValue).average().orElse(0);
		
		System.out.println("Average is " + meanValue);
		
		Double meanDifferenceSquare = 0.0;
		System.out.println("\n********Gap values *******");
		for (Map.Entry<String, Double> entry : values.entrySet()) {
			System.out.println("Project ID ");
			System.out.println("gap value is: " + entry.getValue() + "\n");
		
			meanDifferenceSquare += Math.pow((meanValue - entry.getValue()), 2);

		}

		Double standardDeviation = Math.sqrt(meanDifferenceSquare / values.size());

		
		Double roundSD =   Math.round(standardDeviation * 1000D) / 1000D;;
		
		
		return roundSD;
	}
	
	
	public Double computeStandardDeviationForSkillShortFall() {

		List<Project> selectedProjectStudent = this.getShortlistedProjectStudent(ShortlistProjectStudentSerialize);
		
		
		for(Project project :selectedProjectStudent) {
			
			for(Map.Entry<String, Integer>entry: project.getSelectedStudents().entrySet()) {
				
				System.out.println("Key is "+ entry.getKey() + " value is "+ entry.getValue());
				
			}
		}

		Map<String, Double> projectGaps = new HashMap<String, Double>();

		Double sumValue = 0.0;
		for (Project selectedProject : selectedProjectStudent) {

			Double gapValue = 0.0;
			Map<String, Integer> projectSkillRanking = selectedProject.getSkillRanking();


			Map<String, Integer> sumOfStudentsRanking = new HashMap<String, Integer>();

			for (Map.Entry<String, Integer> entry : selectedProject.getSelectedStudents().entrySet()) {

				Student studentDetail = this.getStudentDetailByID(entry.getKey());
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
			selectedProject.setSkillGap(gapValue);

			// System.out.println("Sum value is: " + gapValue);

		}

		// calculating standard deviation

		Double meanValue = sumValue / projectGaps.size();

		// compute difference of each value from the mean and square the result and sum
		// for each projects

		Double meanDifferenceSquare = 0.0;
		System.out.println("\n********Gap values *******");
		for (Map.Entry<String, Double> entry : projectGaps.entrySet()) {
			System.out.println("Project ID ");
			System.out.println("gap value is: " + entry.getValue() + "\n");
		
			meanDifferenceSquare += Math.pow((meanValue - entry.getValue()), 2);

		}

		Double standardDeviation = Math.sqrt(meanDifferenceSquare / projectGaps.size());

		this.updateShortListedProjects(selectedProjectStudent);

		return standardDeviation;

	}

	public Map<String, Double> computePreferencePercentage() {
		List<Project> selectedProjectStudent = this.getShortlistedProjectStudent(ShortlistProjectStudentSerialize);

		List<Preference> studentPreference = this.getProjectPreference(PreferenceFileNameSerialize);

		Map<String, Double> preferencePercentage = new HashMap<String, Double>();
		for (Project project : selectedProjectStudent) {
			System.out.println("Project Id is "+ project.getID());
			Double percentage = 0.0;
			for (Map.Entry<String, Integer> entry : project.getSelectedStudents().entrySet()) {

				for (Preference preference : studentPreference) {

					if (entry.getKey().equals(preference.getStudentNumber())) {

						
						List<Entry<String, Integer>> sortedEntries = this.SortedByValues(preference.getPreferedProjects());
						
						for (int i = 0 ; i < 2 ; i ++) {
							
							String key = sortedEntries.get(i).getKey();
							//Integer value = sortedEntries.get(i).getValue();
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

		this.updateShortListedProjects(selectedProjectStudent);

		return preferencePercentage;
	}
	
	

	
	

}
