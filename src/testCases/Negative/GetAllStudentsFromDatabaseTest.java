package testCases.Negative;

import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import model.database.DatabaseConnect;
import model.database.DatabseHandler;
import model.handler.Handler;

public class GetAllStudentsFromDatabaseTest {

	
	Connection con;
	DatabseHandler handler;
	
	@Before
	public void setup() {
		handler = new DatabseHandler();
		//con = DatabaseConnect.getConnection();
	}

	
	@Test(expected = SQLException.class)
	public void testGetAllStudents() throws SQLException{
			
			System.out.println("size is " + handler.getAllStudents(con).size());
			handler.getAllStudents(con);
			//assertEquals(20, handler.getAllStudents(con).size(),0.1);
	
	}
	
	@After
	public void tearDown() {
		handler = null;
	}

}
