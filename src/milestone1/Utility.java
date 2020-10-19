package milestone1;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import exceptions.InvalidInputException;
import model.company.Company;
import model.preference.Preference;
import model.project.Project;
import model.projectowner.ProjectOwner;
import model.student.Student;

public class utility {

	public static final String companyFileName = "resource/companies.txt";
	public static final String OwnerFileName = "resource/owners.txt";
	public static final String ProjectFileName = "resource/projects.txt";
	public static final String StudentFileName = "resource/students.txt";
	public static final String StudentInfoFileName = "resource/studentinfo.txt";
	public static final String PreferenceFileName = "resource/preferences.txt"; 
	public static final String ShortlistFileName = "resource/shortlistedProjects.txt";
	
	
	public static Scanner scan = new Scanner(System.in);
	
	
	//Checking input validity - REGEX
	// checking regex validity for the company ID format
	
	public static Boolean checkCompanyIDValidity(String companyID)throws InvalidInputException {
		
		
		String regex = "^([C][1-9]|[C][1][0-9])$";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(companyID);
		if (matcher.matches()) {
			
			return true;
		}
		else {
			throw new InvalidInputException(" Invalid input for Company ID format");

		}
	}
	
	
	// checking regex validity for the owner ID format
	public static Boolean checkOwnerIDValidity(String ownerID)throws InvalidInputException {
		
		
		String regex = "^(Own[1-9]|Own[1][0-9])$";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(ownerID);
		if (matcher.matches()) {
			
			return true;
		}
		else {
			throw new InvalidInputException(" Invalid input for Project Owner ID format");

		}
	}
	
	// checking regex validity for the student ID format
	public static Boolean checkProjectIDValidity(String projectID)throws InvalidInputException {
		
		
		String regex = "^(Pr[1-9]|Pr[1][0])$";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(projectID);
		if (matcher.matches()) {
			
			return true;
		}
		else {
			throw new InvalidInputException(" Invalid input for Project ID format");

		}
	}
	
	
	// checking regex validity for the student ID format
	public static Boolean checkStudentIDValidity(String studentID)throws InvalidInputException {
		
		
		String regex = "^(S[1-9]|S[1][0-9])$";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(studentID);
		if (matcher.matches()) {
			
			return true;
		}
		else {
			throw new InvalidInputException(" Invalid input for Student ID format");

		}
	}
	
	// checking regex validity for the skill point input  format
		public static Boolean checkSkillPointValidity(String skillPoint)throws InvalidInputException {
			
			
			String regex = "^[1-4]$";
			Pattern pattern = Pattern.compile(regex);
			Matcher matcher = pattern.matcher(skillPoint);
			if (matcher.matches()) {
				
				return true;
			}
			else {
				throw new InvalidInputException(" Invalid input for skill point number");

			}
		}
		
		
		// checking regex validity for the skill point input  format
		public static Boolean checkPersonalityTypeValidity(String type)throws InvalidInputException {
			
			
			String regex = "^[A-D]$";
			Pattern pattern = Pattern.compile(regex);
			Matcher matcher = pattern.matcher(type);
			if (matcher.matches()) {
				
				return true;
			}
			else {
				throw new InvalidInputException(" Invalid personality type format");

			}
		}
	
	
	// checking regex validity for the student ID format
	public static Boolean checkEmailValidity(String email)throws InvalidInputException {
		
		
		//String regex = "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\\\.[A-Z]{2,6}$";
		
		String regex = "([a-zA-Z0-9]+(?:[._+-][a-zA-Z0-9]+)*)@([a-zA-Z0-9]+(?:[.-][a-zA-Z0-9]+)*[.][a-zA-Z]{2,})";
		Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(email);
		if (matcher.matches()) {
			
			return true;
		}
		else {
			throw new InvalidInputException(" Invalid input for Email address format");

		}
	}
	
	// Company file operations
	public  static Company getCompanyInput() throws IOException, InvalidInputException {
		
		
		boolean isValid = false;
		Company newCompany = null;
		
		do {
			
				String ID, name, abnNumber, url, address;
				
				System.out.println("Enter a unique company ID ");
				
				ID = scan.next();
				scan.nextLine();
				System.out.println("Enter a company name ");
				name = scan.next();
				scan.nextLine();
				System.out.println("Enter a company ABN Number ");
				abnNumber = scan.next();
				scan.nextLine();
				System.out.println("Enter a company URL ");
				url = scan.next();
				scan.nextLine();
				System.out.println("Enter a company address ");
				address = scan.next();
				scan.nextLine();
	
				if (utility.checkCompanyIDValidity(ID)) {
					isValid = true;
					newCompany = new Company(ID, name, abnNumber, url, address);
				}
			
		}while(!isValid);
		
		
		return newCompany;
		
	}
	
	
	


	
	public static boolean addCompany( Company company  , String fileName ) throws IOException {

		boolean status = false;
		
		
		List<Company> savedCompany = utility.getCompany(companyFileName);
		Boolean isExisting =  savedCompany.size() > 0 ?  true : false;
		System.out.println("Size of the company " + savedCompany.size() );
		for (Company saved : savedCompany) {
			System.out.println(saved.getID());
			if (saved.getID().equals(company.getID())) {
				status = false;
				System.out.println("Already exist this company");
				return status;
			}

		}

		// adding new company 
		savedCompany.add(company);

		FileWriter fileWriter = new FileWriter(fileName, false);
		BufferedWriter bffWriter= new BufferedWriter(fileWriter);
		
	
		if (!isExisting) {
			
			bffWriter.write(company.getID() + ";" + company.getName() + ";" + company.getABNNumber() + ";" + company.getURL() + ";" + company.getAddress());


		}
		else {

			for (Company comp : savedCompany) {
				bffWriter.write(comp.getID() + ";" + comp.getName() + ";" + comp.getABNNumber() + ";" + comp.getURL() + ";" + comp.getAddress());
				bffWriter.newLine();
			
			}
		}
		
		
		bffWriter.close();
		
		return status;
	}
	
	

	public static List<Company> getCompany(String companyFileName) throws IndexOutOfBoundsException, FileNotFoundException, IOException{

		
		List<Company> companies = new ArrayList<>();
		
		BufferedReader bfReader;
		
			bfReader = new BufferedReader(new FileReader(utility.companyFileName));
	
			String reading = bfReader.readLine();
			while(reading != null) {
				
				if (reading != null) {
					System.out.println("before split retrieve company ");
					String[] split = reading.split(";");
					Company company = new Company(split[0] ,split[1] , split[2], split[3], split[4]);
					companies.add(company);
					System.out.println("retrieve company ");
				}
				reading = bfReader.readLine();
				
			}
			System.out.println("number of company " + companies.size());
			bfReader.close();

		
		return companies;
	}


	
	// projects owners file operations

	public  static ProjectOwner getProjectOwnerInput() throws IOException , InvalidInputException{
		
		
		boolean isValid = false;

		ProjectOwner newOwner = null;
		do {
			
			
			String firstname, surname, ownerID, role, email, companyID;
			
			System.out.println("Enter Project Owner First Name ");
			
			firstname = scan.next();
			scan.nextLine();
			System.out.println("Enter Project Owner Sur Name ");
			 surname = scan.next();
			 scan.nextLine();
			System.out.println("Enter Project Owner unique owner ID ");
			ownerID = scan.next();
			scan.nextLine();
			System.out.println("Enter Project Owner Role");
			role = scan.next();
			scan.nextLine();
			System.out.println("Enter Project Owner email  ");
			email = scan.next();
			scan.nextLine();
			System.out.println("Enter Project Owner Company ID");
			companyID = scan.next();
			scan.nextLine();

			if (utility.checkOwnerIDValidity(ownerID) && utility.checkCompanyIDValidity(companyID) && utility.checkEmailValidity(email) ) {
				isValid = true;
				newOwner = new ProjectOwner(firstname, surname, ownerID, role, email, companyID);
			}
			
		}while(!isValid);
		
		return newOwner;
		
	}
	
	

	

	public static List<ProjectOwner> getProjectOwner(String companyFileName) {

		
		List<ProjectOwner> projectOWners = new ArrayList<>();
		
		BufferedReader bfReader;
		
		try {
			bfReader = new BufferedReader(new FileReader(utility.OwnerFileName));
	
			String reading = bfReader.readLine();
			while(reading != null) {
				
				
				if (reading != null) {
					String[] split = reading.split(";");
					ProjectOwner owner = new ProjectOwner(split[0] , split[1], split[2], split[3], split[4], split[5] );
					projectOWners.add(owner);
					System.out.println("retrieve company ");
				}
				reading = bfReader.readLine();
				
			}
			System.out.println("number of company " + projectOWners.size());
			bfReader.close();
			
		} catch(IndexOutOfBoundsException e) {
			System.out.println("index out of bound exception");
			e.printStackTrace();
		}
		catch (FileNotFoundException  e) {
			// TODO Auto-generated catch block
			System.out.println("file not found exception");
			e.printStackTrace();
		}
		catch( IOException io) {
			// TODO Auto-generated catch block
			System.out.println("input output  exception");
			io.printStackTrace();
		}
		

		
		
		return projectOWners;
	}
	
	
	
	public static boolean addProjectOwner( ProjectOwner owner , String fileName ) throws IOException {

		boolean status = false;
		
		
		List<ProjectOwner> savedProjectOwner = utility.getProjectOwner(companyFileName);
		Boolean isExisting =  savedProjectOwner.size() > 0 ?  true : false;
		System.out.println("Size of the company " + savedProjectOwner.size() );
		for (ProjectOwner saved : savedProjectOwner) {
			
			if (saved.getOwnerID().equals(owner.getOwnerID())) {
				status = false;
				System.out.println("Already exist this Owner");
				return status;
			}

		}

		// adding new company 
		savedProjectOwner.add(owner);

		FileWriter fileWriter = new FileWriter(fileName, false);
		BufferedWriter bffWriter= new BufferedWriter(fileWriter);
		
	
		if (!isExisting) {
			
			bffWriter.write(owner.getFirstname() + ";" + owner.getSurname() + ";" + owner.getOwnerID() + ";" + owner.getRole() + ";" + owner.getEmail() + ";" + owner.getCompanyID());


		}
		else {

			for (ProjectOwner own : savedProjectOwner) {
				bffWriter.write(own.getFirstname() + ";" + own.getSurname() + ";" + own.getOwnerID() + ";" + own.getRole() + ";" + own.getEmail() + ";" + own.getCompanyID());
				bffWriter.newLine();
			
			}
		}
		
		
		bffWriter.close();
		
		return status;
	}
	
	
	
	
	
	// Project Details
	
	/*
	private String title;
	private String ID;
	private String description;
	private String ownerID;
	private Map<String, Integer> skillRanking 
	 * */
	// Company file operations
	public  static Project getProjectDetailsInput() throws IOException, InvalidInputException {
		
		
		boolean isValid = false;
		Project newProject = null;
		
		do {
			
			String title, ID, desc,  ownerID;
			Map<String, Integer> skillRanking  = new HashMap<String, Integer>();
			
			
			System.out.println("Enter Project title ");
			title = scan.next();
			scan.nextLine();
			
			System.out.println("Enter Project unique ID ");
			 ID = scan.next();
			 scan.nextLine();
			 
			System.out.println("Enter Project descriptions ");
			desc = scan.next();
			scan.nextLine();
			
			System.out.println("Enter Project owner ID");
			ownerID = scan.next();
			scan.nextLine();
			
			System.out.println("Enter the ranking on the following skills (4 being the highest and 1 the lowest))");
			
			/*
			 p = programming and software engineering
			 n = networking and security
			 a = analytics and big data
			 w = web and mobile applications  
			 */
			
			int P, N, A, W; 
			
			System.out.println("(P) Programming & Software Engineering");
			 P = scan.nextInt();
			 scan.nextLine();
			
			System.out.println("(N) Networking and Security");
			N = scan.nextInt();
			scan.nextLine();
			
			System.out.println("(A) Analytics and Big Data");
			A = scan.nextInt();
			scan.nextLine();
			
			System.out.println("(W) Web & Mobile applications");
			W = scan.nextInt();
			scan.nextLine();
			
			
			
			if (utility.checkProjectIDValidity(ID) && utility.checkOwnerIDValidity(ownerID) ) {
				isValid = true;
				newProject = new Project(title, ID, desc, ownerID);
				
			}
			
			if (utility.checkSkillPointValidity(Integer.toString(P)) && utility.checkSkillPointValidity(Integer.toString(N)) && utility.checkSkillPointValidity(Integer.toString(A)) && utility.checkSkillPointValidity(Integer.toString(W))) {
				isValid = true;
				skillRanking.put("P", P);
				skillRanking.put("N", N);
				skillRanking.put("A", A);
				skillRanking.put("W", W);
				newProject.setSkillRanking(skillRanking);
			}
			
			return newProject;
			
		}while(!isValid);
		
	}
	

	public static List<Project> getProjectDetails(String projectileName) {

		
		List<Project> projectList = new ArrayList<>();
		
		BufferedReader bfReader;
		
		try {
			bfReader = new BufferedReader(new FileReader(projectileName));

			String reading = bfReader.readLine();

			
			while(reading != null) {

				if (reading != null) {
					String title, ID, desc,  ownerID;
					Map<String, Integer> skillRanking  = new HashMap<String, Integer>();
					
					title = reading;
					
					reading = bfReader.readLine();
					ID = reading;
					
					reading = bfReader.readLine();
					desc = reading;
					
					reading = bfReader.readLine();
					ownerID = reading;
			
					reading = bfReader.readLine();
					String[] split = reading.split(" ");
					skillRanking.put(split[0], Integer.parseInt(split[1]) );
					skillRanking.put(split[2], Integer.parseInt(split[3]) );
					skillRanking.put(split[4], Integer.parseInt(split[5]) );
					skillRanking.put(split[6], Integer.parseInt(split[7]) );
					
						Project project = new Project(title, ID, desc, ownerID);
						project.setSkillRanking(skillRanking);
						
						projectList.add(project);
						reading = bfReader.readLine();
					}
				
				}
				

			
			bfReader.close();
			
		} catch(IndexOutOfBoundsException e) {
			System.out.println("index out of bound exception");
			e.printStackTrace();
		}
		catch (FileNotFoundException  e) {
			// TODO Auto-generated catch block
			System.out.println("file not found exception");
			e.printStackTrace();
		}
		catch( IOException io) {
			// TODO Auto-generated catch block
			System.out.println("input output  exception");
			io.printStackTrace();
		}

		
		return projectList;
	}
	
	
	
	public static boolean addProjectDetails( Project project , String fileName ) throws IOException {

		boolean status = false;
		
		
		List<Project> savedProject = utility.getProjectDetails(fileName);
		Boolean isExisting =  savedProject.size() > 0 ?  true : false;
		System.out.println("Size of the project " + savedProject.size() );
		for (Project saved : savedProject) {
			
			if (saved.getID().equals(project.getID())) {
				status = false;
				System.out.println("Already exist this project detail");
				return status;
			}

		}

		// adding new company 
		savedProject.add(project);

		FileWriter fileWriter = new FileWriter(fileName, false);
		BufferedWriter bffWriter= new BufferedWriter(fileWriter);
		
	
		if (!isExisting) {
			
			String pRanking = project.getSkillRanking().get("W").toString();
			String nRanking = project.getSkillRanking().get("N").toString();
			String aRanking = project.getSkillRanking().get("A").toString();
			String wRanking = project.getSkillRanking().get("W").toString();
			System.out.println("skill ranking of W is: " + pRanking);
			
			String skillRanking = "P " + pRanking + " " + "N " + nRanking + " "+ "A " + aRanking + " " + "W " + wRanking ;
			
			bffWriter.write(project.getTitle());
			bffWriter.newLine();
			bffWriter.write(project.getID());
			bffWriter.newLine();
			bffWriter.write(project.getDescription());
			bffWriter.newLine();
			bffWriter.write(project.getOwnerID());
			bffWriter.newLine();
			bffWriter.write(skillRanking);


		}
		else {


			for (Project proj : savedProject) {
				String pRanking = proj.getSkillRanking().get("W").toString();
				String nRanking = proj.getSkillRanking().get("N").toString();
				String aRanking = proj.getSkillRanking().get("A").toString();
				String wRanking = proj.getSkillRanking().get("W").toString();
				System.out.println("skill ranking of W is: " + pRanking);
				
				String skillRanking = "P " + pRanking + " " + "N " + nRanking + " "+ "A " + aRanking + " " + "W " + wRanking ;
				
				bffWriter.write(proj.getTitle());
				bffWriter.newLine();
				bffWriter.write(proj.getID());
				bffWriter.newLine();
				bffWriter.write(proj.getDescription());
				bffWriter.newLine();
				bffWriter.write(proj.getOwnerID());
				bffWriter.newLine();
				bffWriter.write(skillRanking);
				bffWriter.newLine();
			
			}
		}

		
		bffWriter.close();
		
		return status;
	}
	
	
	
	
	/// Student details 
	
public  static Student getStudentDetailsInput() throws IOException {
		
		
		boolean isValid = false;

		
		do {
			
			String ID;
			Map<String, Integer> grades  = new HashMap<String, Integer>();
			
			
			System.out.println("Enter Student number ");
			ID = scan.next();
			scan.nextLine();
			
			System.out.println("Enter you grade point on the following courses ((HD=4, DI=3, CR=2, PA=1, NN=0)))");
			
			/*
			 p = programming and software engineering
			 n = networking and security
			 a = analytics and big data
			 w = web and mobile applications  
			 */
			
			int P, N, A, W; 
			
			System.out.println("(P) Programming & Software Engineering");
			 P = scan.nextInt();
			 scan.nextLine();
			
			System.out.println("(N) Networking and Security");
			N = scan.nextInt();
			scan.nextLine();
			
			System.out.println("(A) Analytics and Big Data");
			A = scan.nextInt();
			scan.nextLine();
			
			System.out.println("(W) Web & Mobile applications");
			W = scan.nextInt();
			scan.nextLine();
			
			
			grades.put("P", P);
			grades.put("N", N);
			grades.put("A", A);
			grades.put("W", W);
			

			Student newStudent = new Student( ID, grades);
			
			return newStudent;
			
		}while(!false);
		
	}
	

	public static List<Student> getStudentDetails(String studentFileName) {

		
		List<Student> studentList = new ArrayList<>();
		
		BufferedReader bfReader;
		
		try {
			bfReader = new BufferedReader(new FileReader(studentFileName));

			String reading = bfReader.readLine();

			
			while(reading != null) {

				
				String ID;
				Map<String, Integer> grades  = new HashMap<String, Integer>();
		
				String[] split = reading.split(" ");
				ID = split[0];
				grades.put(split[1], Integer.parseInt(split[2]) );
				grades.put(split[3], Integer.parseInt(split[4]) );
				grades.put(split[5], Integer.parseInt(split[6]) );
				grades.put(split[7], Integer.parseInt(split[8]) );
				
					Student student = new Student( ID, grades);
					
					studentList.add(student);
					
					reading = bfReader.readLine();
				}
				

			
			bfReader.close();
			
		} catch(IndexOutOfBoundsException e) {
			System.out.println("index out of bound exception");
			e.printStackTrace();
			
		}
		catch (FileNotFoundException  e) {
			// TODO Auto-generated catch block
			System.out.println("file not found exception");
			e.printStackTrace();
		}
		catch( IOException io) {
			// TODO Auto-generated catch block
			System.out.println("input output  exception");
			io.printStackTrace();
		}
		

		
		
		return studentList;
	}
	
	
	
	public static boolean addStudentDetails( Student student , String fileName ) throws IOException {

		boolean status = false;
		
		
		List<Student> savedStudent = utility.getStudentDetails(fileName);
		Boolean isExisting =  savedStudent.size() > 0 ?  true : false;
		System.out.println("Size of the project " + savedStudent.size() );
		for (Student saved : savedStudent) {
			
			if (saved.getStudentNumber().equals(student.getStudentNumber())) {
				status = false;
				System.out.println("Already exist this project detail");
				return status;
			}

		}

		// adding new company 
		savedStudent.add(student);

		FileWriter fileWriter = new FileWriter(fileName, false);
		BufferedWriter bffWriter= new BufferedWriter(fileWriter);
		
	
		if (!isExisting) {
			
			String ID = student.getStudentNumber();
			
			String pGrade = student.getGrade().get("W").toString();
			String nGrade = student.getGrade().get("N").toString();
			String aGrade = student.getGrade().get("A").toString();
			String wGrade = student.getGrade().get("W").toString();
			System.out.println("grade of W is: " + pGrade);
			
			String studentRecord = ID + " "+ "P " + pGrade + " " + "N " + nGrade + " "+ "A " + aGrade + " " + "W " + wGrade ;

			bffWriter.write(studentRecord);


		}
		else {


			for (Student stud : savedStudent) {
				
				String ID = stud.getStudentNumber();
				
				String pGrade = stud.getGrade().get("W").toString();
				String nGrade = stud.getGrade().get("N").toString();
				String aGrade = stud.getGrade().get("A").toString();
				String wGrade = stud.getGrade().get("W").toString();
				System.out.println("grade of W is: " + pGrade);
				
				String studentRecord = ID + " "+ "P " + pGrade + " " + "N " + nGrade + " "+ "A " + aGrade + " " + "W " + wGrade ;

				bffWriter.write(studentRecord);
				bffWriter.newLine();
			
			}
		}

		
		bffWriter.close();
		
		return status;
	}

	
	/// student personalities 
	
	
	
	/// Student details 
	
	/*
	Characteristics													Personality Type
	Likes to be a Leader (Director)									A			
	Outgoing and maintains good relationships (Socializer)			B
	Detail oriented (Thinker)										C
	Less assertive (Supporter)										D

	 */
	
public  static Student getStudentPersonalitiesInput() throws IOException, InvalidInputException {
		
		
		boolean isValid = false;
		Student newStudent = null;
		
		do {
			
			String ID, personalityType;
			Map<String, Integer> grades  = new HashMap<String, Integer>();
			String conflict[] = new String[2];
			
			System.out.println("Enter Student number ");
			ID = scan.next();
			scan.nextLine();
			
			System.out.println("Provide the personality type of that student on the following matrix from(A, B, C, D))");
			
			System.out.println("Likes to be a Leader (Director):                        A ");
			System.out.println("Outgoing and maintains good relationships (Socializer): B");
			System.out.println("Detail oriented (Thinker):                              C");
			System.out.println("Less assertive (Supporter):                             D ");
			
			personalityType = scan.next();
			scan.nextLine();
			System.out.println("Enter first conflict Student for current student");
			conflict[0] = scan.next();
			scan.nextLine();
			System.out.println("Enter second conflict Student for current student");
			conflict[1] = scan.next();
			scan.nextLine();
			grades = null;
			
			
			
			if (utility.checkStudentIDValidity(ID) && utility.checkPersonalityTypeValidity(personalityType) && utility.checkStudentIDValidity(conflict[0]) && utility.checkStudentIDValidity(conflict[1])) {
				
				isValid = true;
				newStudent = new Student( ID, grades, personalityType, conflict);
			}
			
			
			
			if (ID.equals(conflict[0]) || ID.equals(conflict[1])) {
				isValid = false;
				System.out.println("Can not make your own ID as a conflict");
			}

	
		}while(!isValid);
		return newStudent;
	}
	

	public static List<Student> getStudentPersonalityDetails(String studentFileName) {

		
		List<Student> studentList = new ArrayList<>();
		
		BufferedReader bfReader;
		
		try {
			bfReader = new BufferedReader(new FileReader(studentFileName));

			String reading = bfReader.readLine();

			
			while(reading != null) {
				
				String ID, personalityType;
				Map<String, Integer> grades  = new HashMap<String, Integer>();
				String conflict[] = new String[2];
				
				
				String[] split = reading.split(" ");


				ID = split[0];
				grades.put(split[1], Integer.parseInt(split[2]) );
				grades.put(split[3], Integer.parseInt(split[4]) );
				grades.put(split[5], Integer.parseInt(split[6]) );
				grades.put(split[7], Integer.parseInt(split[8]) );
			
				personalityType = split[9];
				
				conflict[0] = split[10];
				conflict[1] = split[11];
				
				Student student = new Student( ID, grades, personalityType, conflict);
					
					studentList.add(student);
					
					reading = bfReader.readLine();
				}
				
			
			bfReader.close();
			
		} catch(IndexOutOfBoundsException e) {
			System.out.println("index out of bound exception");
			e.printStackTrace();
		}
		catch (FileNotFoundException  e) {
			// TODO Auto-generated catch block
			System.out.println("file not found exception");
			e.printStackTrace();
		}
		catch( IOException io) {
			// TODO Auto-generated catch block
			System.out.println("input output  exception");
			io.printStackTrace();
		}
		

		
		return studentList;
	}
	
	

	
	public static boolean addStudentPersonalityDetails( Student student , String fileName ) throws IOException {

		boolean status = false;
		
		int savedPersonlaityCount = 0;
		
		List<Student> savedStudentPersonality = utility.getStudentPersonalityDetails(fileName);
		Boolean isExisting =  savedStudentPersonality.size() > 0 ?  true : false;
		System.out.println("Size of the student personality record " + savedStudentPersonality.size() );
		for (Student saved : savedStudentPersonality) {
			
			if (saved.getPersonality().equals(student.getPersonality())) {
				savedPersonlaityCount++;
			}
			
			if (saved.getStudentNumber().equals(student.getStudentNumber())) {
				status = false;
				System.out.println("Already entered this student personality");
				return status;
			}

		}
		
		if (savedPersonlaityCount >= 5) {
			
			System.out.println("You can not give this personality to this student as already limit reached for the number of this personlaity type student");
			return false;
		}
		
		
		// getting student information from the students.txt
		
		List<Student> savedStudent = utility.getStudentDetails(utility.StudentFileName);
		Student savedStudentDetails = null;
		Boolean isExistingStudent =  savedStudent.size() > 0 ?  true : false;
		System.out.println("Size of the project " + savedStudent.size() );
		for (Student saved : savedStudent) {
			
			if (saved.getStudentNumber().equals(student.getStudentNumber())) {
				
				savedStudentDetails = saved;
				break;
				
			}

		}
		
		
		
		
		
		
		//new Student( ID, grades, personalityType, conflict);
		
		System.out.println("New student personal type updating");
		System.out.println("student number" + student.getStudentNumber());
		System.out.println("student personality" + student.getPersonality());
		System.out.println("student grade" +savedStudentDetails.getGrade());
		System.out.println("student conflict" + student.getConflictStudent());
	
		Student newStudentPersonality = new Student(student.getStudentNumber(), savedStudentDetails.getGrade(), student.getPersonality(), student.getConflictStudent());
		

		// adding new company 
		savedStudentPersonality.add(newStudentPersonality);

		FileWriter fileWriter = new FileWriter(fileName, false);
		BufferedWriter bffWriter= new BufferedWriter(fileWriter);
		
	
		if (!isExisting) {
			
			String ID = newStudentPersonality.getStudentNumber();
			
			String pGrade = newStudentPersonality.getGrade().get("W").toString();
			String nGrade = newStudentPersonality.getGrade().get("N").toString();
			String aGrade = newStudentPersonality.getGrade().get("A").toString();
			String wGrade = newStudentPersonality.getGrade().get("W").toString();
			System.out.println("grade of W is: " + pGrade);
			
			String personality = newStudentPersonality.getPersonality();
			
			String firstConflict = newStudentPersonality.getConflictStudent()[0];
			String secondConflict = newStudentPersonality.getConflictStudent()[1];
			
			String studentRecord = ID + " "+ "P " + pGrade + " " + "N " + nGrade + " "+ "A " + aGrade + " " + "W " + wGrade + " " 
									+  personality + " " + firstConflict +  " " + secondConflict ;

			bffWriter.write(studentRecord);


		}
		else {


			for (Student stud : savedStudentPersonality) {
				
				String ID = stud.getStudentNumber();
				
				String pGrade = stud.getGrade().get("W").toString();
				String nGrade = stud.getGrade().get("N").toString();
				String aGrade = stud.getGrade().get("A").toString();
				String wGrade = stud.getGrade().get("W").toString();
				System.out.println("grade of W is: " + pGrade);
				
				
				String personality = stud.getPersonality();
				
				String firstConflict = stud.getConflictStudent()[0];
				String secondConflict = stud.getConflictStudent()[1];
				
				String studentRecord = ID + " "+ "P " + pGrade + " " + "N " + nGrade + " "+ "A " + aGrade + " " + "W " + wGrade + " " 
						+  personality + " " + firstConflict +  " " + secondConflict ;

				bffWriter.write(studentRecord);
				bffWriter.newLine();
			
			}
		}

		
		bffWriter.close();
		
		return status;
	}

	
	
	
	
	
	
	
	
	///// Student preferences 
	
	
	
	
	/// Student details 
	
public  static Preference getStudentPreferencesInput() throws IOException, InvalidInputException {
		
		
		boolean isValid = false;

		
		do {
			
			String ID;
			Map<String, Integer> preferences  = new HashMap<String, Integer>();
			
			
			System.out.println("Enter Student number ");
			ID = scan.next();
			scan.nextLine();
			System.out.println("Enter your four most prefered projects with the point( 4 for most preferred and 1 for the least");
			
			for (int i = 0 ; i < 10 ; i++) {
				System.out.println("Enter your "+ i + " preference project ID");
				String prefered = scan.next();
				scan.nextLine();
				System.out.println("Enter your preference point for the project "+ prefered);
				
				 int preferPoint = scan.nextInt();
				 scan.nextLine();
				if (utility.checkProjectIDValidity(prefered) && utility.checkSkillPointValidity(Integer.toString(preferPoint))) {
						isValid = true;
						preferences.put(prefered, preferPoint);
				}
				 
				 preferences.put(prefered, preferPoint);
			}
			
			

			Preference newPreference = new Preference( ID, preferences);
			
			return newPreference;
			
		}while(!false);
		
	}
	

	public static List<Preference> getStudentPreferencesDetails(String studentFileName) {

		
		List<Preference> preferenceList = new ArrayList<>();
		
		BufferedReader bfReader;
		
		try {
			bfReader = new BufferedReader(new FileReader(studentFileName));

			String reading = bfReader.readLine();

			
			while(reading != null) {

				
				String ID;
				Map<String, Integer> preferences  = new HashMap<String, Integer>();
				
				
				ID = reading;
				reading = bfReader.readLine();
				
				String[] split = reading.split(" ");
				for (int i = 0; i < split.length ; i+= 2) {
					preferences.put(split[i], Integer.parseInt(split[i+1]) );	
					
				}

					Preference preference = new Preference( ID, preferences);
					
					preferenceList.add(preference);
					
					reading = bfReader.readLine();
				}
				

			
			bfReader.close();
			
		} catch(IndexOutOfBoundsException e) {
			System.out.println("index out of bound exception");
			e.printStackTrace();
		}
		catch (FileNotFoundException  e) {
			// TODO Auto-generated catch block
			System.out.println("file not found exception");
			e.printStackTrace();
		}
		catch( IOException io) {
			// TODO Auto-generated catch block
			System.out.println("input output  exception");
			io.printStackTrace();
		}
		

		
		
		return preferenceList;
	}
	
	
	
	public static boolean addStudentPreferencesDetails( Preference student , String fileName ) throws IOException {

		boolean status = false;
		
		
		List<Preference> savedStudentPreferences = utility.getStudentPreferencesDetails(fileName);
		Boolean isExisting =  savedStudentPreferences.size() > 0 ?  true : false;
		System.out.println("Size of the project " + savedStudentPreferences.size() );
		
		
		/*for (Preference saved : savedStudentPreferences) {
			
			if (saved.getStudentNumber().equals(student.getStudentNumber())) {
				status = false;
				System.out.println("Already exist this preference detail");
				return status;
			}

		}
		
		*/

		// adding new company 
		savedStudentPreferences.add(student);

		FileWriter fileWriter = new FileWriter(fileName, false);
		BufferedWriter bffWriter= new BufferedWriter(fileWriter);
		
	
		if (!isExisting) {
			
			String ID = student.getStudentNumber();
			
			
			String PreferenceString = "";
			int count = 0;
			
			for (Map.Entry<String, Integer> entry : student.getPreferedProjects().entrySet()) {
				
				String preferProj = entry.getKey();
				Integer preferPoint = entry.getValue();
				
				count ++;
				PreferenceString +=  preferProj + " " + preferPoint + (count == 4 ? "" : " ");
			}
			
			
	

			bffWriter.write(ID);
			bffWriter.newLine();
			bffWriter.write(PreferenceString);


		}
		else {


			for (Preference stud : savedStudentPreferences) {
				
				String ID = stud.getStudentNumber();
				
				String PreferenceString = "";
				int count = 0;
				
				for (Map.Entry<String, Integer> entry : stud.getPreferedProjects().entrySet()) {
					
					String preferProj = entry.getKey();
					Integer preferPoint = entry.getValue();
					
					count ++;
					PreferenceString +=  preferProj + " " + preferPoint + (count == 4 ? "" : " ");
				}
				
				
		

				bffWriter.write(ID);
				bffWriter.newLine();
				bffWriter.write(PreferenceString);

				bffWriter.newLine();
			
			}
		}

		
		bffWriter.close();
		
		return status;
	}
	
	

	public static List<ShortlistProject> getShortListedProjects(String studentFileName) {

		
		List<ShortlistProject> shortLists = new ArrayList<>();
		
		BufferedReader bfReader;
		
		try {
			bfReader = new BufferedReader(new FileReader(studentFileName));

			String reading = bfReader.readLine();

			
			while(reading != null) {
				
				String ID, value;
	
				String[] split = reading.split(" ");
				ID = split[0];
				value = split[1];
				ShortlistProject shortListed = new ShortlistProject(ID, value);
				
			
					shortLists.add(shortListed);
					
					reading = bfReader.readLine();
				}
				
			
			bfReader.close();
			
		} catch(IndexOutOfBoundsException e) {
			System.out.println("index out of bound exception");
			e.printStackTrace();
		}
		catch (FileNotFoundException  e) {
			// TODO Auto-generated catch block
			System.out.println("file not found exception");
			e.printStackTrace();
		}
		catch( IOException io) {
			// TODO Auto-generated catch block
			System.out.println("input output  exception");
			io.printStackTrace();
		}
		

		
		return shortLists;
	}
	

	public static boolean showShortListedProjects(String fileName , String sortlistFileName) throws IOException {

		boolean status = false;
		
		
		List<Preference> savedStudentPreferences = utility.getStudentPreferencesDetails(fileName);
		Boolean isExisting =  savedStudentPreferences.size() > 0 ?  true : false;
		System.out.println("Size of the project " + savedStudentPreferences.size() );
		
		Map<String, Integer> sumPreferences  = new HashMap<String, Integer>();
		
		for (Preference saved: savedStudentPreferences) {
			
			for (Map.Entry<String, Integer> entry : saved.getPreferedProjects().entrySet()) {
				
				// saved data
				String preferProj = entry.getKey();
				Integer preferPoint = entry.getValue();
				
				
				// new summing data
				Integer sumPreferPoint = sumPreferences.get(preferProj) == null ? 0 : sumPreferences.get(preferProj);
	
				sumPreferences.put(preferProj, (preferPoint +sumPreferPoint));
			}
			
		}
		
		// extracting top 5 project with high preference
		
		ValueComparator comparator = new ValueComparator(sumPreferences);
        TreeMap<String, Integer> sortedPreference = new TreeMap<String, Integer>(comparator);
		
        sortedPreference.putAll(sumPreferences);
        System.out.println("results: " + sortedPreference);
		
		

		FileWriter fileWriter = new FileWriter(sortlistFileName, false);
		BufferedWriter bffWriter= new BufferedWriter(fileWriter);
		
		int count = 0 , max = 5;
		
		for (Map.Entry<String, Integer> entry : sortedPreference.entrySet()) {
			
			if (count >= max) break;
			
			String preferProj = entry.getKey();
			Integer preferPoint = entry.getValue();
			
			count ++;
			String PreferenceString =  preferProj + " " + preferPoint + (count == 10 ? "" : " ");
			
			bffWriter.write(PreferenceString);
			bffWriter.newLine();
		}
		
		
		bffWriter.close();
		
		
		
		return status;
	}
	
	
	

}



class ValueComparator implements Comparator<String> {
    Map<String, Integer> base;

    public ValueComparator(Map<String, Integer> sumPreferences) {
        this.base = sumPreferences;
    }

    // Note: this comparator imposes orderings that are inconsistent with
    // equals.
    public int compare(String a, String b) {
        if (base.get(a) >= base.get(b)) {
            return -1;
        } else {
            return 1;
        } // returning 0 would merge keys
    }
}


/*  References
 * Regex check 
 * https://regex101.com/r/cvCKTs/1
 * For tree map uses 
 https://stackoverflow.com/questions/109383/sort-a-mapkey-value-by-values
 
 
 
 */


