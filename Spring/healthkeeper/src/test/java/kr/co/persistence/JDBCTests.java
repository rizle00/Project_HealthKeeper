package kr.co.persistence;

import static org.junit.Assert.fail;

import java.sql.Connection;
import java.sql.DriverManager;

import org.junit.Test;

public class JDBCTests {
	
	static {
		try {
			Class.forName("net.sf.log4jdbc.sql.jdbcapi.DriverSpy");
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	@Test
	public void testConnection() {
		
		try(Connection con = 
				DriverManager.getConnection(
						System.getenv("DB_URL"),
						System.getenv("DB_USER"),
						System.getenv("DB_PASSWORD"))){
			System.out.println(con);
		} catch (Exception e) {
			fail(e.getMessage());
		}
		
	}

}
