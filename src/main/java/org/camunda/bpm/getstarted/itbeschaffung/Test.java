package org.camunda.bpm.getstarted.itbeschaffung;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.logging.Logger;

public class Test {
	
	// JDBC driver name and database URL
	static final String JDBC_DRIVER = "org.h2.Driver";
	static final String DB_URL = "jdbc:h2:~/test";

	private final static Logger LOGGER = Logger.getLogger(DBConnect.class.getName());
	
	// Database credentials
	static final String USER = "sa";
	static final String PASS = "";
public static void main(String[] args) {
	Connection conn = null;
	Statement stmt = null;
	String vartitle=("title");
	String vardat= ("2018-06-17T00:00:00");
	String varfirstN=("Patric");
	String varlastN=("Bruns");
	String varphoneN=("01622061212");
	String varemail=("Patric_Bruns@web.de");
	String vardescription=("lkmkjdlvkjdlfjvldkjvldfkjlvkj");
	
	vardat = vardat.substring(0,10);
	try {
		// STEP 1: Register JDBC driver
		Class.forName(JDBC_DRIVER);

		// STEP 2: Open a connection
		//System.out.println("Connecting to database...");
		conn = DriverManager.getConnection(DB_URL, USER, PASS);

		// STEP 3: Execute a query
		
		//System.out.println("Creating table in given database...");
		stmt = conn.createStatement();
		String sql = "INSERT INTO SDDATA (title,dat,firstN,lastN,phoneN,email,description) "
				+ "VALUES ('"+ vartitle + "','"+ vardat + "','"+ varfirstN + "','"+ varlastN + "','"+ varphoneN + "','"+ varemail + "','"+ vardescription + "')";
		stmt.executeUpdate(sql);
		//while (rs.next()) {
		//	System.out.println(rs.getString("FIRST") + ", " + rs.getString("LAST"));
		//}
		 LOGGER.info("Store Data in Database"); 
		// STEP 4: Clean-up environment
		stmt.close();
		conn.close();
	} catch (SQLException se) {
		// Handle errors for JDBC
		se.printStackTrace();
	} catch (Exception e) {
		// Handle errors for Class.forName
		e.printStackTrace();
	} finally {
		// finally block used to close resources
		try {
			if (stmt != null)
				stmt.close();
		} catch (SQLException se2) {
		} // nothing we can do
		try {
			if (conn != null)
				conn.close();
		} catch (SQLException se) {
			se.printStackTrace();
		} // end finally try
	} // end try
	//System.out.println("Goodbye!");
}
}

