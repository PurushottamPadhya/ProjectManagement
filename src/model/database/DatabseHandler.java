package model.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import customAlert.CustomAlert;
import model.project.Project;
import model.student.Student;

public class DatabseHandler {
	
	// create student table 
	public void createStudent(Connection con) {
		
		
		String sqlCreate = "CREATE TABLE IF NOT EXISTS STUDENT (\n"
                + "	ID text PRIMARY KEY,\n"
                + "	personality text ,\n"
                + "	firstConflict text ,\n"
                + "	secondConflict text ,\n"
                + "	P integer ,\n"
                + "	N integer ,\n"
                + "	A integer ,\n"
                + "	W integer \n"
                + ");";
		
		String sqlDrop = "DROP TABLE IF EXISTS STUDENT";
		
		try {
			
			Statement stmt = con.createStatement();
			
			stmt.executeUpdate(sqlDrop);
			System.out.println("Student table dropped successfully");
			stmt.executeUpdate(sqlCreate);
			System.out.println("Student table created successfully");
		}
		catch(Exception e) {
			System.out.println("Error on creating a table");
			CustomAlert.getInstance().showAlert("Error", e.getMessage());
		}
		
		
	}
	
	
	
	public void insertStudent(List<Student> students, Connection conn) throws SQLException {
		
		String sql = "INSERT INTO STUDENT(ID, personality, firstConflict, secondConflict, P, N, A, W) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
	try {
		
		
		PreparedStatement ps = conn.prepareStatement(sql);
		for(Student student : students) {
				
			
				ps.setString(1, student.getStudentNumber());
				ps.setString(2, student.getPersonality());
				ps.setString(3, student.getConflictStudent()[0]);
				ps.setString(4, student.getConflictStudent()[1]);
		
				for(Map.Entry<String, Integer>entry: student.getGrade().entrySet()) {
					System.out.println("KEy is "+ entry.getKey() + "  value is "+ entry.getValue());
					if (entry.getKey().equals("P")) {
						ps.setInt(5, entry.getValue());
					}
					else if (entry.getKey().equals("N")) {
						ps.setInt(6, entry.getValue());
					}
					else if (entry.getKey().equals("A")) {
						ps.setInt(7, entry.getValue());
					}
					else if (entry.getKey().equals("W")) {
						ps.setInt(8, entry.getValue());
					}
					

				}
				
				ps.executeUpdate();
				System.out.println("student added successfully");

			}
		conn.close();
		}
	catch(Exception e) {
		System.out.println("error on inserting student.");
		System.out.println(e.getLocalizedMessage());
		System.out.println(e.getMessage());
	}

	}
	
	
	// create project table 
	
	public void createProject(Connection con) {
		
		String sql = "CREATE TABLE IF NOT EXISTS PROJECT (\n"
                + "	ID TEXT PRIMARY KEY,\n"
                + "	title TEXT ,\n"
                + "	description TEXT ,\n"
                + "	ownerID TEXT ,\n"
                + "	skillGap REAL ,\n"
                + "	skillCompetency REAL ,\n"
                + "	preferencePercentage REAL ,\n"
                + "	P integer ,\n"
                + "	N integer ,\n"
                + "	A integer ,\n"
                + "	W integer \n"
                + ");";
		
		try {
			
			Statement stmt = con.createStatement();
			stmt.executeUpdate(sql);
			System.out.println("Project table created successfully");
			
			createSelectedStudentProject(con);
		}
		catch(Exception e) {
			System.out.println("Error on creating a table");
			CustomAlert.getInstance().showAlert("Error", e.getMessage());
		}
		
		
	}
	
	
	// insert data into project table 
	public void insertProject(List<Project> projects, Connection conn) {
		
		String sql = "INSERT INTO PROJECT(ID, title, description, ownerID, skillGap, skillCompetency, preferencePercentage, P, N, A, W) VALUES (?, ?, ?, ?, ?, ?, ?, ?,?,?,?)";
		
		
		try {
			
			PreparedStatement ps = conn.prepareStatement(sql);
			
			for(Project project: projects) {
				System.out.println(project.getID());
				System.out.println(project.getTitle());
				System.out.println(project.getDescription());
				System.out.println(project.getOwnerID());
				
				
				ps.setString(1, project.getID());
				ps.setString(2, project.getTitle());
				ps.setString(3, project.getDescription());
				ps.setString(4, project.getOwnerID());
				ps.setDouble(5, project.getSkillGap());
				ps.setDouble(6, project.getSkillCompetency());
				ps.setDouble(7, project.getPreferencePercentage());

				for(Map.Entry<String, Integer>entry: project.getSkillRanking().entrySet()) {
					System.out.println("Key is "+ entry.getKey() + "  value is "+ entry.getValue());
					if (entry.getKey().equals("P")) {
						ps.setInt(8, entry.getValue());
					}
					else if (entry.getKey().equals("N")) {
						ps.setInt(9, entry.getValue());
					}
					else if (entry.getKey().equals("A")) {
						ps.setInt(10, entry.getValue());
					}
					else if (entry.getKey().equals("W")) {
						ps.setInt(11, entry.getValue());
					}
					
				}
				
				ps.executeUpdate();
				System.out.println("project added successfully");
				
				// create selected student project
				
				this.insertSelectedStudentProject(project.getID(), project.getSelectedStudents(), conn);
				
			}

			conn.close();

		}
		catch(Exception e) {
			System.out.println("error on inserting project.");
			System.out.println(e.getLocalizedMessage());
			System.out.println(e.getMessage());
		}
		
	}
	
	
	
	// create student project table
	public void createSelectedStudentProject(Connection con) {
		
		String sql = "CREATE TABLE IF NOT EXISTS SELECTEDSTUDENTPROJECT (\n"
                + "	projectID TEXT ,\n"
                + "	studentID TEXT ,\n"
                + "	preference TEXT ,\n"
                + " PRIMARY KEY(projectID, studentID) ,\n"
                + "FOREIGN KEY(projectID) REFERENCES PROJECT(ID) ,\n"
                + "FOREIGN KEY(studentID) REFERENCES STUDENT(ID) \n"
                + ");";
		
		try {
			Statement stmt = con.createStatement();
			stmt.executeUpdate(sql);
			System.out.println("Student project table created successfully");
		}
		catch(Exception e) {
			System.out.println("Error on creating a table");
			CustomAlert.getInstance().showAlert("Error", e.getMessage());
		}
		
		
	}
	
	
	// insert into student project table
	public void insertSelectedStudentProject(String  projectID, Map<String, Integer> studentPreference, Connection conn) {
		
		String sql = "INSERT INTO SELECTEDSTUDENTPROJECT(projectID, studentID, preference) VALUES (?, ?, ?)";
		
		try {
			
			PreparedStatement ps = conn.prepareStatement(sql);
			
			for(Map.Entry<String, Integer>entry: studentPreference.entrySet()) {
				System.out.println("selected project id" + projectID);
				System.out.println(entry.getKey());
				System.out.println(entry.getValue());
				
				ps.setString(1, projectID);
				ps.setString(2, entry.getKey());
				ps.setInt(3, entry.getValue());

				ps.executeUpdate();
				System.out.println("selected student added successfully");
			}


		}
		catch(Exception e) {
			System.out.println("error on inserting project student.");
			System.out.println(e.getLocalizedMessage());
			System.out.println(e.getMessage());
		}
		
	}
	
	public List<Student> getAllStudents(Connection conn) throws SQLException {
		
		String sql = "SELECT * FROM STUDENT";
		
		Statement stmt = conn.createStatement();
		
		ResultSet rSet = stmt.executeQuery(sql);
		
		List<Student> students = new ArrayList<Student>();
		
		
		while(rSet.next()) {
			
			String ID = rSet.getString(1);
			String personality = rSet.getString(2);
			String conflict1 = rSet.getString(3);
			String conflict2 = rSet.getString(4);
			
			String [] conflicts = {conflict1, conflict2};
			int P = rSet.getInt(5);
			int N = rSet.getInt(6);
			int A = rSet.getInt(7);
			int W = rSet.getInt(8);
			
			
			Map<String, Integer> skills = new HashMap<String, Integer>() {{
			    put("P",P);
			    put("N",N);
			    put("A",A);
			    put("W",W);
			}};
			
			Student student = new Student(ID, skills, personality, conflicts);
			
			students.add(student);
		}
		System.out.println(" size " + students.size());
		
		return students;
		
	}
	
	public Student getStudentByID(String studentID, Connection conn) throws SQLException {
		
		String sql = "SELECT * FROM STUDENT WHERE ID = " + "'" + studentID + "'" ;
		
		
		Statement stmt = conn.createStatement();
		ResultSet rSet = stmt.executeQuery(sql);

		while(rSet.next()) {
			
			
			String ID = rSet.getString(1);
			String personality = rSet.getString(2);
			String conflict1 = rSet.getString(3);
			String conflict2 = rSet.getString(4);
			
			String [] conflicts = {conflict1, conflict2};
			int P = rSet.getInt(5);
			int N = rSet.getInt(6);
			int A = rSet.getInt(7);
			int W = rSet.getInt(8);
			
			Map<String, Integer> skills = new HashMap<String, Integer>() {{
			    put("P",P);
			    put("N",N);
			    put("A",A);
			    put("W",W);
			}};
			
			Student student = new Student(ID, skills, personality, conflicts);

			conn.close();
			return student;
		}
		return null;
		
	}
	
	public List<Project> getAllProjects(Connection conn) throws SQLException{
		
		String sql = "SELECT * FROM PROJECT";
		
		Statement stmt = conn.createStatement();
		
		ResultSet rSet = stmt.executeQuery(sql);
		
		List<Project> projects = new ArrayList<Project>();
		
		while(rSet.next()) {
			
			String ID = rSet.getString(1);
			String title = rSet.getString(2);
			String description = rSet.getString(3);
			String ownerID = rSet.getString(4);
			
			double skillGap = rSet.getDouble(5);
			double skillCompetency = rSet.getDouble(6);
			double preferencePercentage = rSet.getDouble(7);
			
			
			int P = rSet.getInt(8);
			int N = rSet.getInt(9);
			int A = rSet.getInt(10);
			int W = rSet.getInt(11);
			
			Map<String, Integer> skills = new HashMap<String, Integer>() {{
			    put("P",P);
			    put("N",N);
			    put("A",A);
			    put("W",W);
			}};
			
	
			Project project = new Project(title, ID, description, ownerID);
			project.setSkillRanking(skills);
			project.setSkillGap(skillGap);
			project.setSkillCompetency(skillCompetency);
			project.setPreferencePercentage(preferencePercentage);
			
			Map<String, Integer> selectedStudents = this.getSelectedStudentForProject(ID, conn) ; 
			
			project.setSelectedStudents(selectedStudents);
			
			
			projects.add(project);
		}
		
		
		return projects;
	}
	
	public Project getProjectByID(String projectID, Connection conn) throws SQLException {
		
		
		String sql = "SELECT * FROM PROJECT WHERE ID = " + "'" + projectID + "'" ;
		
		
		Statement stmt = conn.createStatement();
		ResultSet rSet = stmt.executeQuery(sql);

		while(rSet != null) {
			
			String ID = rSet.getString(1);
			String title = rSet.getString(2);
			String description = rSet.getString(3);
			String ownerID = rSet.getString(4);
			
			double skillGap = rSet.getDouble(5);
			double skillCompetency = rSet.getDouble(6);
			double preferencePercentage = rSet.getDouble(7);
			
			
			int P = rSet.getInt(8);
			int N = rSet.getInt(9);
			int A = rSet.getInt(10);
			int W = rSet.getInt(11);
			
			Map<String, Integer> skills = new HashMap<String, Integer>() {{
			    put("P",P);
			    put("N",N);
			    put("A",A);
			    put("W",W);
			}};
			
			
			Project project = new Project(title, ID, description, ownerID);
			project.setSkillRanking(skills);
			project.setSkillGap(skillGap);
			project.setSkillCompetency(skillCompetency);
			project.setPreferencePercentage(preferencePercentage);
			
			Map<String, Integer> selectedStudents = this.getSelectedStudentForProject(ID, conn) ; 
			
			project.setSelectedStudents(selectedStudents);
			return project;
		}
		
		return null;
	}
	
	public Map<String, Integer> getSelectedStudentForProject(String projectID, Connection conn) {
		String sql = "SELECT studentID, preference FROM SELECTEDSTUDENTPROJECT WHERE projectID = " + "'" + projectID + "'" ;
		
		
		Map<String, Integer> preferences = new HashMap<String, Integer>();
		
		try {
			Statement stmt = conn.createStatement();
			ResultSet rSet = stmt.executeQuery(sql);
			
			while(rSet.next()) {
				
				String studentID = rSet.getString(1);
				Integer preference = rSet.getInt(2);
				preferences.put(studentID, preference);
				
			}
		}
		catch(Exception e) {
			System.out.println(e.getMessage());
		}

		
		return preferences;
	
	}
	
	
	
}
