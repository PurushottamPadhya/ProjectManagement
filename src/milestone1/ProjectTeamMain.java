package milestone1;

import java.io.IOException;
import java.util.InputMismatchException;
import java.util.Scanner;

import exceptions.InvalidInputException;
import model.company.Company;
import model.fileHandler.FileHandler;
import model.handler.Handler;
import model.orojectowner.ProjectOwner;
import model.preference.Preference;
import model.project.Project;
import model.student.Student;

public class ProjectTeamMain {

	public static void main(String[] args)  {
		// TODO Auto-generated method stub

		System.out.println("Old main called");
		
		
		boolean isValid = false;
		char input;
		

			do {
				
				
				try {
					
					FileHandler fHandler = new FileHandler();
					fHandler.serializeTxFile();
					Handler handler = new Handler();
					
					System.out.println("\nChoose your option from MENU \n");

					System.out.println("A Add Company");
					System.out.println("B Add Project Owner");
					System.out.println("C Add Project");
					System.out.println("D Capture Student Personalities");
					System.out.println("E Add Student Preferences");
					System.out.println("F Shortlist Projects");
					System.out.println("G Form Teams");
					System.out.println("H Display Team Fitness Metrics");
					System.out.println("Q Quit");

					String inputText = utility.scan.next();
					if (inputText.length() ==  1 ) {
						
						input = inputText.charAt(0);

						switch (input) {
						case 'A': {
							

							Company newCompany = utility.getCompanyInput();
							utility.addCompany(newCompany, utility.companyFileName);

							System.out.println("new company created");

							break;
						}
						case 'B': {
							
							ProjectOwner newOwner = utility.getProjectOwnerInput();
							utility.addProjectOwner(newOwner, utility.OwnerFileName);

							System.out.println("new owner added");
							

							break;
						}
						case 'C': {
							
							Project newProject = utility.getProjectDetailsInput();
							utility.addProjectDetails(newProject, utility.ProjectFileName);
							
							System.out.println("new project created");

							break;
						}
						case 'D': {
							
							
							Student newStudentPersonalityType = utility.getStudentPersonalitiesInput();
							utility.addStudentPersonalityDetails(newStudentPersonalityType, utility.StudentInfoFileName);
							
							System.out.println("new student personality added");
							
							
							/*
							Student newStudent = Utility.getStudentDetailsInput();
							Utility.addStudentDetails(newStudent, Utility.StudentFileName);
							
							System.out.println("new student added");
							
							*/
							

							break;
						}
						case 'E': {

							Preference newStudentPreference = utility.getStudentPreferencesInput();
							utility.addStudentPreferencesDetails(newStudentPreference, utility.PreferenceFileName);
							
							System.out.println("new student preferences added added");
							
							
							break;
						}
						case 'F': {

							utility.showShortListedProjects(utility.PreferenceFileName , utility.ShortlistFileName);
							break;
						}
						case 'G': {

							
							handler.FormTeam();
	
							
							break;
						}
						case 'H': {

							// show team fitness matrix
							handler.displayTeamFitnessMetrics();
							
							break;
						}
						case 'Q':{
							
							isValid = true;
							break;
						}
						
						default:
							break;

						}

						
						
					}
					
					
					
					
				}
				catch (InvalidInputException e) {
					// TODO Auto-generated catch block
				 
				 	System.out.println("\n*********Exception Caught*********");
				 	System.out.println(e.getMessage() + "\n");
					;
				}
			
			catch (InputMismatchException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.out.println("\n*********Exception Caught*********");
			 	System.out.println(e.getMessage() + "\n");
			
			}
			catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.out.println("\n*********Exception Caught*********");
			 	System.out.println(e.getMessage() + "\n");
			
			}


			} while (!isValid);


	}

}
