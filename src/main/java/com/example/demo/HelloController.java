package com.example.demo;

import java.sql.*;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {
	
	@GetMapping("/")
	public String index() {
		return "Greetings from Spring Boot!";
	}

	@GetMapping("/fromdb")
	public String fromdb() {
		
		String url = System.getenv("DB_URL");
		String user = System.getenv("DB_USER");
		String password = System.getenv("DB_PASSWORD");
		String sql = System.getenv("DB_SQL");

		Connection con;
		Statement stmt;
		ResultSet rs;
		
		String returnValue = "Something went wrong";
		
		try {
			Class.forName("com.ibm.db2.jcc.DB2Driver");
			con = DriverManager.getConnection (url, user, password);
			con.setAutoCommit(false);
			stmt = con.createStatement();
			rs = stmt.executeQuery(sql);
			
			if (rs.next()) {
				returnValue = rs.getString(1);
			}
			
			rs.close();
			stmt.close();
			con.commit();
			con.close();
		} 
		catch (ClassNotFoundException e)
	    {
			System.err.println("Could not load JDBC driver");
			System.out.println("Exception: " + e);
			e.printStackTrace();
			
			returnValue += "; Could not load JDBC driver; " + "Exception: " + e;
	    }
		catch (SQLException e) {
			returnValue += "; SQLException information; " + "Exception: " + e;
		}


		return returnValue;
	}

}
