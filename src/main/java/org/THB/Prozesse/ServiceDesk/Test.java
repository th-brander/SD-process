package org.THB.Prozesse.ServiceDesk;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.mail.Email;
import org.apache.commons.mail.SimpleEmail;
import org.camunda.bpm.engine.RuntimeService;

public class Test {
		// JDBC driver name and database URL
		static final String JDBC_DRIVER = "org.h2.Driver";
		static final String DB_URL = "jdbc:h2:~/test";
		
		private final static Logger LOGGER = Logger.getLogger(DBConnect.class.getName());
		
		// Database credentials
		static final String DBUSER = "sa";
		static final String PASS = "";
		
		// TODO: Set Mail Server Properties
		private static final String HOST = "smtp.gmail.com";
		private static final String USER = "camundauser@gmail.com";
		private static final String PWD = "pyZ-Bk3-u2u-WY9";
	
	public static void main(String[] args) {

		Connection conn = null;
		Statement stmt = null;
		Integer ticketID = 0;
		
		try {
			// STEP 1: Register JDBC driver
			Class.forName(JDBC_DRIVER);
			
			// STEP 2: Open a connection
			//System.out.println("Connecting to database...");
			LOGGER.info("Connecting to DB");
			conn = DriverManager.getConnection(DB_URL, DBUSER, PASS);

			// STEP 3: Execute a query
			//System.out.println("Creating table in given database...");
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT MAX(TICKETID) FROM SDDATA");
			rs.next();
			ticketID = rs.getInt(1);
			
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

		String var = ("Teppich");
		String varName = ("Name");
		String recipient = "bruns@th-brandenburg.de";
		String etext = "Sehr geehrter" + varName + ", \n\n Ich wuerde gerne folgenden Artikel bestellen: " + var + ".\n\n Mit freundlichen Gruessen, \n\n Demo Demo";

		Email email = new SimpleEmail();
		email.setCharset("utf-8");
		email.setHostName(HOST);
		email.setSmtpPort(465);
		email.setAuthentication(USER, PWD);
		email.setSSL(true);
		LOGGER.info("----------------/n");
		try {
			email.setFrom("noreplykunden@Camunda.org");
			email.setSubject("Ticket " + ticketID + "wurde für Sie eröffnet");
			email.setMsg(etext);

			email.addTo(recipient);

			email.send();
			LOGGER.info("Task Assignment Email successfully sent to address: " + recipient);

		} catch (Exception e) {
			LOGGER.log(Level.WARNING, "Could not send email to assignee", e);
		}

//	RuntimeService runtimeService = execution.getProcessEngineServices().getRuntimeService();
//		runtimeService.correlateMessage("startMessage");
//		runtimeService.startProcessInstanceByMessage("startMessage");
		
		
		
		
		
	}
}
