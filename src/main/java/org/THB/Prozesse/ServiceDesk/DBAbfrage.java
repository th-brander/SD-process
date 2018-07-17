package org.THB.Prozesse.ServiceDesk;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.logging.Logger;
import java.sql.Timestamp;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;

//INSERT into SDDATA (Title,dat,firstn,lastn,phonen,email,describtion) values ('Dieter','Herr','2018-06-03','Patric','Bruns','01622061212','Patric_Bruns@web.de','Das ist ein Test')
//CREATE TABLE SDDATA (TICKETID int(11)  AUTO_INCREMENT(10000,1), Tickettyp varchar(20),user varchar(15),prio varchar(10), title varchar(8), dat Timestamp, firstN varchar(15), lastN varchar(15), phoneN varchar(15),email varchar(30), description clob,PRIMARY KEY (TicketID))
public class DBAbfrage implements JavaDelegate {
	// JDBC driver name and database URL
	static final String JDBC_DRIVER = "org.h2.Driver";
	static final String DB_URL = "jdbc:h2:~/test";

	private final static Logger LOGGER = Logger.getLogger(DBAbfrage.class.getName());
	
	// Database credentials
	static final String USER = "sa";
	static final String PASS = "";

	public void execute(DelegateExecution execution) throws Exception {
		Connection conn = null;
		Statement stmt = null;
		
		try {
			// STEP 1: Register JDBC driver
			Class.forName(JDBC_DRIVER);

			// STEP 2: Open a connection
			//System.out.println("Connecting to database...");
			conn = DriverManager.getConnection(DB_URL, USER, PASS);

			// STEP 3: Execute a query
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM SDDATA WHERE TICKETID=10000");
			while (rs.next()) {
				System.out.printf( "%s, %s, %s, %s, %s, %s, %s, %s, %s, %s%n", rs.getString(1),rs.getString(2),
						rs.getString(3),rs.getString(4),rs.getString(5),rs.getString(6),rs.getString(7),rs.getString(8),rs.getString(9),rs.getString(10));
			}
			String reqTicketID = rs.getString(1);
			String reqTicketTyp = rs.getString(2);
			String reqUser = rs.getString(3);
			String reqPrio = rs.getString(4);
			String reqTitle = rs.getString(5);
			String reqDat = rs.getString(6);
			String reqFirstN = rs.getString(7);
			String reqLastN = rs.getString(8);
			
			execution.setVariable("reqTicketID", reqTicketID);
			execution.setVariable("reqTicketTyp", reqTicketTyp);
			execution.setVariable("reqUser", reqUser);
			execution.setVariable("reqPrio", reqPrio);
			execution.setVariable("reqTitle", reqTitle);
			execution.setVariable("reqDat", reqDat);
			execution.setVariable("reqFirstN", reqFirstN);
			execution.setVariable("reqLastN", reqLastN);
			 LOGGER.info("Stored Data in Database"); 
			 
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
		LOGGER.info("Store Data in Database");
	}
}

