package org.camunda.bpm.getstarted.itbeschaffung;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.logging.Logger;
import java.sql.Timestamp;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;

//INSERT into SDDATA (Title,dat,firstn,lastn,phonen,email,describtion) values ('Dieter','Herr','2018-06-03','Patric','Bruns','01622061212','Patric_Bruns@web.de','Das ist ein Test')
//CREATE TABLE SDDATA (TICKETID int(11)  AUTO_INCREMENT(10000,1), Tickettyp varchar(20),user varchar(15),prio varchar(10), title varchar(8), dat Timestamp, firstN varchar(15), lastN varchar(15), phoneN varchar(15),email varchar(30), description clob,PRIMARY KEY (TicketID))
public class DBConnect implements JavaDelegate {
	// JDBC driver name and database URL
	static final String JDBC_DRIVER = "org.h2.Driver";
	static final String DB_URL = "jdbc:h2:~/test";

	private final static Logger LOGGER = Logger.getLogger(DBConnect.class.getName());
	
	// Database credentials
	static final String USER = "sa";
	static final String PASS = "";

	public void execute(DelegateExecution execution) throws Exception {
		Connection conn = null;
		Statement stmt = null;
		String vartickettyp=(String) execution.getVariable("tickettyp");
		String varuser=(String) execution.getVariable("user");
		String varprio=(String) execution.getVariable("prio");
		String vartitle=(String) execution.getVariable("title");
		Date vardat=(Date) execution.getVariable("dat");
		String varfirstN=(String) execution.getVariable("firstN");
		String varlastN=(String) execution.getVariable("lastN");
		String varphoneN=(String) execution.getVariable("phoneN");
		String varemail=(String) execution.getVariable("email");
		String vardescription=(String) execution.getVariable("description");
		
		try {
			// STEP 1: Register JDBC driver
			Class.forName(JDBC_DRIVER);
			
			// STEP 2: Open a connection
			//System.out.println("Connecting to database...");
			LOGGER.info("Connecting to DB");
			Timestamp timestamp = new Timestamp(vardat.getTime());
			conn = DriverManager.getConnection(DB_URL, USER, PASS);

			// STEP 3: Execute a query
			//System.out.println("Creating table in given database...");
			stmt = conn.createStatement();
			String sql = "INSERT INTO SDDATA (TICKETTYP,USER,PRIO,TITLE,DAT,FIRSTN,LASTN,PHONEN,EMAIL,DESCRIPTION) VALUES ('" + vartickettyp + "','" + varuser + "','" + varprio + "','" + vartitle + "','" + timestamp +"','"+ varfirstN + "','"+ varlastN + "','"+ varphoneN + "','" + varemail +"','"+ vardescription + "')";
			LOGGER.info("Try to Insert in DB: "+sql);
			stmt.executeUpdate(sql);
			LOGGER.info("complete");
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
		LOGGER.info("Store Data in Database");
	}
}

