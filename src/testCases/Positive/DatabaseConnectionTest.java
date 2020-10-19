package testCases.Positive;

import static org.junit.Assert.*;

import java.sql.Connection;

import org.junit.Before;
import org.junit.Test;

import model.database.DatabaseConnect;
import model.handler.Handler;

public class DatabaseConnectionTest {

	DatabaseConnect dbConnect;
	Connection connect;
	
	@Before
	public void setup() {
		connect = dbConnect.getInstance();
	}
	
	
	@Test
	public void testGetInstance() {
		
		assertEquals(connect, DatabaseConnect.getInstance());
	}

}
