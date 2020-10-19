package controller;



import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import commandManager.CAction;
import commandManager.CommandManager;
import customAlert.CustomAlert;
import exceptions.ConflictException;
import exceptions.InvalidSelectionException;
import exceptions.RepeatedValueException;
import exceptions.TeamLeaderException;

import java.util.Map;

import javafx.scene.Node;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import model.database.DatabaseConnect;
import model.database.DatabseHandler;
import model.fileHandler.FileHandler;
import model.handler.Handler;
import model.heuristic.Heuristic;
import model.project.Project;
import model.student.Student;

enum RadioSelected {
	PREFERENCE, COMPETENCY, SHORTFALL
}

public class ProjectManagementController {

	@FXML
	private TextField s1TextField;
	@FXML
	private TextField s2TextField;
	@FXML
	private TextField s3TextField;
	@FXML
	private TextField s4TextField;
	@FXML
	private TextField s5TextField;
	@FXML
	private TextField s6TextField;
	@FXML
	private TextField s7TextField;
	@FXML
	private TextField s8TextField;
	@FXML
	private TextField s9TextField;
	@FXML
	private TextField s10TextField;
	@FXML
	private TextField s11TextField;
	@FXML
	private TextField s12TextField;
	@FXML
	private TextField s13TextField;
	@FXML
	private TextField s14TextField;
	@FXML
	private TextField s15TextField;
	@FXML
	private TextField s16TextField;
	@FXML
	private TextField s17TextField;
	@FXML
	private TextField s18TextField;
	@FXML
	private TextField s19TextField;
	@FXML
	private TextField s20TextField;

	// Checkboxes

	@FXML
	private CheckBox s1Checkbox;
	@FXML
	private CheckBox s2Checkbox;
	@FXML
	private CheckBox s3Checkbox;
	@FXML
	private CheckBox s4Checkbox;
	@FXML
	private CheckBox s5Checkbox;
	@FXML
	private CheckBox s6Checkbox;
	@FXML
	private CheckBox s7Checkbox;
	@FXML
	private CheckBox s8Checkbox;
	@FXML
	private CheckBox s9Checkbox;
	@FXML
	private CheckBox s10Checkbox;
	@FXML
	private CheckBox s11Checkbox;
	@FXML
	private CheckBox s12Checkbox;
	@FXML
	private CheckBox s13Checkbox;
	@FXML
	private CheckBox s14Checkbox;
	@FXML
	private CheckBox s15Checkbox;
	@FXML
	private CheckBox s16Checkbox;
	@FXML
	private CheckBox s17Checkbox;
	@FXML
	private CheckBox s18Checkbox;
	@FXML
	private CheckBox s19Checkbox;
	@FXML
	private CheckBox s20Checkbox;

	// projects ID/ title

	@FXML
	private Label project1;
	@FXML
	private Label project2;
	@FXML
	private Label project3;
	@FXML
	private Label project4;
	@FXML
	private Label projectLabel5;

	// Pane
	@FXML
	private Pane pane1;
	@FXML
	private Pane pane2;
	@FXML
	private Pane pane3;
	@FXML
	private Pane pane4;
	@FXML
	private Pane pane5;

	// grid pane

	@FXML
	private GridPane grid1;
	@FXML
	private GridPane grid2;
	@FXML
	private GridPane grid3;
	@FXML
	private GridPane grid4;
	@FXML
	private GridPane grid5;

	// bar charts

	@FXML
	private BarChart<?, ?> preferencePercentageBarChart;

	@FXML
	private BarChart<?, ?> competencyBarChart;

	@FXML
	private BarChart<?, ?> skillGapBarChart;

	// add new student on project

	@FXML
	private TextField projectIDTextField;

	@FXML
	private TextField studentIDTextField;

	// standard deviation labels

	@FXML
	private Label preferenceSDLabel;
	@FXML
	private Label competencySDLabel;
	@FXML
	private Label skillGapSDLabel;

	@FXML
	private RadioButton radioPreference;

	@FXML
	private RadioButton radioCompetency;

	@FXML
	private RadioButton radioSkillGap;

	// array of project
	Label[] projectLabels;

	// array of textfields of student
	TextField[] studentTextFields;

	// array of checkboxes
	CheckBox[] studentCheckbox;

	Project projectList;

	private Map<String, String> swappingStudents = new HashMap<String, String>();

	Handler handler = new Handler();
	Heuristic heuristic = new Heuristic();

	List<Project> shortListedProjects = new ArrayList<Project>();

	RadioSelected radioSelected = null;

	private ToggleGroup radioGroup;

	List<Project> updatedProjectList = new ArrayList<Project>();

	// create array of label and textfields
	CommandManager manager;
	
	Double preferenceSD, competencySD, skillGapSD;
	@FXML
	public void initialize() {

		projectLabels = new Label[] { project1, project2, project3, project4, projectLabel5 };

		studentTextFields = new TextField[] { s1TextField, s2TextField, s3TextField, s4TextField, s5TextField,
				s6TextField, s7TextField, s8TextField, s9TextField, s10TextField, s11TextField, s12TextField,
				s13TextField, s14TextField, s15TextField, s16TextField, s17TextField, s18TextField, s19TextField,
				s20TextField };

		studentCheckbox = new CheckBox[] { s1Checkbox, s2Checkbox, s3Checkbox, s4Checkbox, s5Checkbox, s6Checkbox,
				s7Checkbox, s8Checkbox, s9Checkbox, s10Checkbox, s11Checkbox, s12Checkbox, s13Checkbox, s14Checkbox,
				s15Checkbox, s16Checkbox, s17Checkbox, s18Checkbox, s19Checkbox, s20Checkbox };

		// set all radio buttons are on a same toggle group so only one can select

		manager = CommandManager.getInstance();
		radioGroup = new ToggleGroup();
		radioPreference.setToggleGroup(radioGroup);
		radioCompetency.setToggleGroup(radioGroup);
		radioSkillGap.setToggleGroup(radioGroup);

		shortListedProjects = handler.getShortlistedProjectStudent("");
		updatedProjectList  = shortListedProjects;
		if (shortListedProjects.size() > 0) {

			saveDataFortheUnDoOperation(shortListedProjects);
			this.updateUI(shortListedProjects);
			this.saveData(shortListedProjects);

		} else {
			System.out.println("No project is shortlisted");
		}

	}

	// this method populate details of projects

	public void populateProjectsDetail(List<Project> projects) {
		int i = 0; // project number
		int j = 0; // student number

		for (Project project : projects) {

			projectLabels[i].setText("Project " + project.getID());

			for (Entry<String, Integer> entry : project.getSelectedStudents().entrySet()) {

				studentTextFields[j].setText(entry.getKey());

				j++;

			}

			i++;
		}

	}

	// // when check box is clicked and count only two check box is selected once
	// and nore more than 2
	// otherwise display error dialogue box

	@FXML
	public void clickedCheckBox(ActionEvent event) {
		CheckBox clickedBox = (CheckBox) event.getSource();

		String checkBoxID = clickedBox.getId();

		// find the corresponding student ID and project ID
		int checkBoxNumber = this.getNumberOnly(checkBoxID);
		System.out.println(" id " + checkBoxNumber);

		String studentID = studentTextFields[checkBoxNumber - 1].getText();

		Pane clickedPane = (Pane) clickedBox.getParent().getParent();
		String projectID = "";

		for (Node node : clickedPane.getChildren()) {

			if (node instanceof Label) {
				String labelText = ((Label) node).getText();
				projectID = "Pr" + this.getNumberOnly(labelText);
			}
		}

		if (clickedBox.isSelected()) {

			if (swappingStudents.size() > 2) {
				CustomAlert alert = CustomAlert.getInstance();
				alert.showAlert("Error", "Exactly two students to select to swap. Please try again");
			} else {
				// swappingStudents.add(e)

				swappingStudents.put(studentID, projectID);

			}
		} else {

			if (swappingStudents.size() > 0) {

				swappingStudents.remove(studentID);
			}

		}

		System.out.println("swapping students " + swappingStudents);

	}

	// // when check box is clicked and count only two check box is selected once
	// and nore more than 2
	// otherwise display error dialogue box
	// store two different project with student ID in different variable and once
	// find the two student to swap
	// then swap two student in different projects
	@FXML
	public void swapClicked(ActionEvent event) throws ConflictException {
		System.out.println("swap id");

		if (swappingStudents.size() != 2) {

			CustomAlert alert = CustomAlert.getInstance();
			alert.showAlert("Error", "Exactly two students to select to swap. Please try again");
			return;
		}

		Heuristic heu = new Heuristic();
		try {
			List<Project> projectAfterSwap = heu.swapTwoStudents(swappingStudents, shortListedProjects, "NAUTO");
			
			clearAllSelected();

			this.updateUI(projectAfterSwap);
			
			saveDataFortheUnDoOperation(projectAfterSwap);
		}
		catch (ConflictException e) {
			clearAllSelected();
			System.out.println(e.getMessage());
			
		} catch (TeamLeaderException e) {
			System.out.println(e.getMessage());
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		
		finally {
			clearAllSelected();
		}
		

	}

	// // clear all selected checkbox once swap finished
	public void clearAllSelected() {

		for (CheckBox checkbox : studentCheckbox) {

			if (checkbox.isSelected()) {
				checkbox.setSelected(false);

			}

		}

		swappingStudents.clear();

	}

	public void updateUI(List<Project> projects) {

		// handler.updateShortListedProjects(shortListedProjects);

		Map<String, Double> preferences = handler.computePreferencePercentage(projects);

		 preferenceSD = handler.computeStandardDeviation(preferences);

		Map<String, Double> competency = handler.computeSkillCompetency(projects);

		 competencySD = handler.computeStandardDeviation(competency);

		Map<String, Double> skillGap = handler.computeSkillGap(projects);

		 skillGapSD = handler.computeStandardDeviation(skillGap);

		//System.out.println("Size of the shortlisted projects are " + shortListedProjects.size());
		populateProjectsDetail(projects);
		populateGraph(projects, preferenceSD, competencySD, skillGapSD);
		
		

	}

	public void saveData(List<Project> updatedProjects) {

		handler.updateShortListedProjects(updatedProjects);

		shortListedProjects = updatedProjects;

	}

	// // update all the UI elements of project student as well as graph with SD
	// deviation update
	public void updateAll() {

		handler.updateShortListedProjects(shortListedProjects);

		Map<String, Double> preference = handler.computePreferencePercentage(shortListedProjects);

		preferenceSD = handler.computeStandardDeviation(preference);

		Map<String, Double> competency = handler.computeSkillCompetency(shortListedProjects);

		competencySD = handler.computeStandardDeviation(competency);

		Map<String, Double> skillGap = handler.computeSkillGap(shortListedProjects);

		skillGapSD = handler.computeStandardDeviation(skillGap);

		shortListedProjects = handler.getShortlistedProjectStudent("");

		System.out.println("Size of the shortlisted project is " + shortListedProjects.size());
		populateProjectsDetail(shortListedProjects);
		populateGraph(shortListedProjects, preferenceSD, competencySD, skillGapSD);

	}

	public int getStudentNumberFromCheckBox(String text, String type) {

		int totalLength = text.length();
		int numberLength = totalLength - 1 - type.length();

		String number = text.substring(1, (totalLength - 1 - numberLength));

		return Integer.parseInt(number);

	}

	// // populate the 3 different graphs

	public void populateGraph(List<Project> projects, Double preferenceSD, Double competencySD, Double skillGapSD) {

		XYChart.Series dataSeriesPreference = new XYChart.Series();
		XYChart.Series dataSeriesCompetency = new XYChart.Series();
		XYChart.Series dataSeriesSkillGap = new XYChart.Series();

		for (Project project : projects) {

			dataSeriesPreference.getData().add(new XYChart.Data(project.getID(), project.getPreferencePercentage()));
			dataSeriesCompetency.getData().add(new XYChart.Data(project.getID(), project.getSkillCompetency()));
			dataSeriesSkillGap.getData().add(new XYChart.Data(project.getID(), project.getSkillGap()));
		}

		preferencePercentageBarChart.getData().clear();
		preferencePercentageBarChart.getData().add(dataSeriesPreference);
		preferencePercentageBarChart.getXAxis().setLabel("Project ID");
		preferencePercentageBarChart.getYAxis().setLabel("% getting Preferences");

		competencyBarChart.getData().clear();
		competencyBarChart.getData().add(dataSeriesCompetency);
		competencyBarChart.getXAxis().setLabel("Project ID");
		competencyBarChart.getYAxis().setLabel("Average Competency value");

		skillGapBarChart.getData().clear();
		skillGapBarChart.getData().add(dataSeriesSkillGap);
		skillGapBarChart.getXAxis().setLabel("Project ID");
		skillGapBarChart.getYAxis().setLabel("Skill Gap Value");

		// SD shows
		preferenceSDLabel.setText("Standard Deviation : " + preferenceSD);
		competencySDLabel.setText("Standard Deviation : " + competencySD);
		skillGapSDLabel.setText("Standard Deviation : " + skillGapSD);

	}

	public int getNumberOnly(String numberString) {

		String nums = numberString.replaceAll("[^0-9 ]", "").replaceAll(" +", " ").trim();
		int result = Integer.parseInt(nums);

		return result;
	}

	// DATABASE classes

	@FXML
	public void addStudentClicked(ActionEvent event) {

		String projectID = projectIDTextField.getText();
		String studentID = studentIDTextField.getText();

		try {

			if (!projectID.isEmpty() && !studentID.isEmpty()) {
				Project projectDetail = handler.getSelectedProjectDetailByID(projectID);
				Student studentDetail = handler.getStudentDetailByID(studentID);

				if (projectDetail != null && studentDetail != null) {

					Map<String, Integer> alreadyAddedStudent = projectDetail.getSelectedStudents();

					
					handler.checkForDuplicateStudentAssign(studentID, alreadyAddedStudent);


					Integer preferences = handler.getPreferenceForStudentForProject(projectDetail, studentID);
					alreadyAddedStudent.put(studentID, preferences);

					Integer preference = handler.getPreferenceForStudentProject(projectID, studentID);
					alreadyAddedStudent.put(studentID, preference);

					handler.checkForStudentConflict(alreadyAddedStudent.keySet(), "NAUTO");

					if (alreadyAddedStudent.size() == 4) {
						handler.checkForTeamLeaderIncludedOnly(alreadyAddedStudent.keySet(), "NAUTO");

						// handler.displayPreferedStudentListForProjectID(projects, projectID);
						// updateAll();

					}

					System.out.println("Added student " + studentID + "on the project " + projectID);

				} else {
					CustomAlert.getInstance().showAlert("Error", "Please provide valid project ID and student ID");

				}

			} else {

				CustomAlert.getInstance().showAlert("Error", "Please provide valid project ID and student ID");
			}
		} catch (ConflictException ce) {
			System.out.println("conflict Exception occur " + ce.getMessage());
		} catch (RepeatedValueException re) {
			System.out.println("Repetition Exception occur " + re.getMessage());
		}
		catch (TeamLeaderException e) {
			System.out.println(e.getMessage());
		}

		catch (Exception e) {
			System.out.println("Exception occur " + e.getMessage());
		}

	}

	// database operations goes here
	public void dataBaseOperation() {
		System.out.println("All students");
		getAllStudent();

		System.out.println(" student by ID");
		getStudentByID("S2");

		System.out.println("All projects");
		getAllProject();

		System.out.println(" project by ID");
		getProjectByID("Pr10");
	}

	// this will call the add student method of Database handler
	public void addStudent() throws SQLException {

		Connection connect = DatabaseConnect.getInstance();

		List<Student> students = handler.getStudentsWithInfo(Handler.StudentInfoFileNameSerialize);
		System.out.println(students.size());
		DatabseHandler dbHandler = new DatabseHandler();

		dbHandler.createStudent(connect);
		dbHandler.insertStudent(students, connect);

	}

	// this will call the add project method of Database handler
	public void addProject() {

		Connection connect = DatabaseConnect.getInstance();

		DatabseHandler dbHandler = new DatabseHandler();

		List<Project> shortListedProjects = handler
				.getShortlistedProjectStudent(Handler.ShortlistProjectStudentSerialize);

		dbHandler.createProject(connect);

		dbHandler.insertProject(shortListedProjects, connect);

	}

	// this will call the get all projects method of Database handler
	public void getAllStudent() {

		Connection connect = DatabaseConnect.getInstance();

		DatabseHandler dbHandler = new DatabseHandler();

		try {
			List<Student> students = dbHandler.getAllStudents(connect);

			for (Student student : students) {
				System.out.println("Student id " + student.getStudentNumber() + " Student personality "
						+ student.getPersonality());

			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

	}

	// this will call the get project by Id method of Database handler
	public void getStudentByID(String ID) {

		Connection connect = DatabaseConnect.getInstance();

		DatabseHandler dbHandler = new DatabseHandler();

		try {
			Student student = dbHandler.getStudentByID(ID, connect);
			System.out.println("Student id by id " + student.getStudentNumber());

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

	}

	// get all project from database handler method called and print the data
	public void getAllProject() {

		Connection connect = DatabaseConnect.getInstance();

		DatabseHandler dbHandler = new DatabseHandler();

		try {
			List<Project> projects = dbHandler.getAllProjects(connect);

			System.out.println("number of projects " + projects.size());

			for (Project project : projects) {
				System.out.println("project id " + project.getID());
				System.out.println("project id " + project.getTitle());
				System.out.println("project id " + project.getDescription());
			}

			// projects.toString();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

	}

	public void getProjectByID(String ID) {

		Connection connect = DatabaseConnect.getInstance();

		DatabseHandler dbHandler = new DatabseHandler();

		try {
			Project project = dbHandler.getProjectByID(ID, connect);
			System.out.println("project id " + project.getID());
			System.out.println("project id " + project.getTitle());
			System.out.println("project id " + project.getDescription());

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

	}

	@FXML
	public void showStudentsButtonClicked(ActionEvent event) {

		System.out.println("All students");
		getAllStudent();

		System.out.println(" student by ID");
		getStudentByID("S2");

//			
//			System.out.println("All projects");
//			getAllProject();
//			
//			
//			System.out.println(" project by ID");
//			getProjectByID("Pr10");

	}

	@FXML
	public void showProjectsButtonClicked(ActionEvent event) {

		System.out.println("All projects");
		getAllProject();

		System.out.println(" project by ID");
		getProjectByID("Pr10");

	}

	@FXML
	public void showSelectedProjectButtonClicked(ActionEvent event) {

		System.out.println("All projects");
		getAllProject();

		System.out.println(" project by ID");
		getProjectByID("Pr10");

	}

	@FXML
	void radioToggleClicked(ActionEvent event) {

		System.out.println("radio button clicked");

		RadioButton radio = (RadioButton) event.getSource();

		if (radio.getId().equals("radioPreference")) {
			radioSelected = RadioSelected.PREFERENCE;
		} else if (radio.getId().equals("radioCompetency")) {
			radioSelected = RadioSelected.COMPETENCY;
		} else if (radio.getId().equals("radioSkillGap")) {
			radioSelected = RadioSelected.SHORTFALL;
		}

	}

	// This class will check if one objective function is selected or not
	// if selected swapped accordingly
	//else return Error dialogue
	@FXML
	void autoSwapClicked(ActionEvent event) {

		List<Project> shortListedProjects = updatedProjectList;
				//handler.getShortlistedProjectStudent(Handler.ShortlistProjectStudentSerialize);

		System.out.println("Auto swap clicked");

			try {
				if (radioSelected != null) {
					List<Project> newProjectList = new ArrayList<Project>();
					
					if (radioSelected == RadioSelected.PREFERENCE) {

						newProjectList = heuristic.autoPreferencePercentage(shortListedProjects, preferenceSD);
					} else if (radioSelected == RadioSelected.COMPETENCY) {

						newProjectList = heuristic.autoswapCompetency(shortListedProjects,competencySD );
					} else if (radioSelected == RadioSelected.SHORTFALL) {

						newProjectList = heuristic.autoSkillGap(shortListedProjects, skillGapSD);
					}
					
					List<Project> projectsToSave = new ArrayList<Project>();
					projectsToSave = newProjectList;
					this.updateUI(projectsToSave);
					saveDataFortheUnDoOperation(projectsToSave);
					updatedProjectList = projectsToSave;
				
				}
				else {
					throw new InvalidSelectionException("Please select one objective function to do auto swap");
					
				}

			}
			catch(InvalidSelectionException e) {
				System.out.println(e.getMessage());
			}
			catch (ConflictException e) {
				System.out.println(e.getMessage());
				
			} catch (TeamLeaderException e) {
				System.out.println(e.getMessage());
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}

	}

	@FXML
	void saveButtonClicked(ActionEvent event) {

		 this.saveData(updatedProjectList);
	}

	// pop data from the stack after undo clicked.
	@FXML
	void undoButtonClicked(ActionEvent event) {

		System.out.println("Undo button clicked");
		
		
		Object obj = manager.undo();
		
		if (obj == null) {
			CustomAlert.getInstance().showAlert("Undo", "No data for undo.");
		}
		else {
			System.out.println(" data is on Undo stack");
			List<Project>  undoProjects = (List<Project>) obj;
			
			for(Project project : undoProjects) {
				System.out.println(project.getSelectedStudents());
				System.out.println("Sd for preference " + project.getPreferencePercentage());
			}
			
			
			updatedProjectList = undoProjects;
			this.updateUI(undoProjects);
		}
		
	}
	
	// push into the stack
   void saveDataFortheUnDoOperation(List<Project> projects) {
		
		manager.execute(new CAction("undo", projects));
		
	}

}
