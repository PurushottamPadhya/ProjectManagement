package milestone1;

import java.io.IOException;
import java.util.InputMismatchException;
import java.util.Scanner;

import milestone2.FileHandler;
import milestone2.Handler;

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

					String inputText = Utility.scan.next();
					if (inputText.length() ==  1 ) {
						
						input = inputText.charAt(0);

						switch (input) {
						case 'A': {
							

							Company newCompany = Utility.getCompanyInput();
							Utility.addCompany(newCompany, Utility.companyFileName);

							System.out.println("new company created");

							break;
						}
						case 'B': {
							
							ProjectOwner newOwner = Utility.getProjectOwnerInput();
							Utility.addProjectOwner(newOwner, Utility.OwnerFileName);

							System.out.println("new owner added");
							

							break;
						}
						case 'C': {
							
							Project newProject = Utility.getProjectDetailsInput();
							Utility.addProjectDetails(newProject, Utility.ProjectFileName);
							
							System.out.println("new project created");

							break;
						}
						case 'D': {
							
							
							Student newStudentPersonalityType = Utility.getStudentPersonalitiesInput();
							Utility.addStudentPersonalityDetails(newStudentPersonalityType, Utility.StudentInfoFileName);
							
							System.out.println("new student personality added");
							
							
							/*
							Student newStudent = Utility.getStudentDetailsInput();
							Utility.addStudentDetails(newStudent, Utility.StudentFileName);
							
							System.out.println("new student added");
							
							*/
							

							break;
						}
						case 'E': {

							Preference newStudentPreference = Utility.getStudentPreferencesInput();
							Utility.addStudentPreferencesDetails(newStudentPreference, Utility.PreferenceFileName);
							
							System.out.println("new student preferences added added");
							
							
							break;
						}
						case 'F': {

							Utility.showShortListedProjects(Utility.PreferenceFileName , Utility.ShortlistFileName);
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
