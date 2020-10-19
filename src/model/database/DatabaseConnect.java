package model.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import customAlert.CustomAlert;
import model.student.Student;

public class DatabaseConnect {
	
	
	
	public static Connection conn = null;
	private static DatabaseConnect instances = null;
	
	public DatabaseConnect() {

		try {

			
			// database storage location
			final String databaseBaseUrl = "jdbc:sqlite:/Users/purushottam/eclipse-workspace/ProjectManagement/src/model/database/ProjectManagement.db";

			
			String url =  databaseBaseUrl ;
			
			// create connection to the database
			
			conn = DriverManager.getConnection(url);

			System.out.println("Connection successfully");
			
			
		}
		catch(SQLException e) {
			System.out.println(e.getMessage());
		}
		catch(Exception e) {
			System.out.println("Error");
			System.out.println(e.getMessage());
		}
	}
	
	
	public static Connection getInstance() {
		// check if already connected 
		if (instances == null) {
			instances = new DatabaseConnect();
		}
		return conn;
	}
	
	
	// this will provide singleton of connection
	public static Connection getConnection() {
		
		if (conn != null) {
			return conn;
		}
		
		return null;
		
	}
	

}
