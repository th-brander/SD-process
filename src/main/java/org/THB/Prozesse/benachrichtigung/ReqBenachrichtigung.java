package org.THB.Prozesse.benachrichtigung;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.SimpleEmail;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;

public class ReqBenachrichtigung implements JavaDelegate {

	static final String JDBC_DRIVER = "org.h2.Driver";
	static final String DB_URL = "jdbc:h2:~/test";

	// Database credentials
	static final String DBUSER = "sa";
	static final String PASS = "";

	// TODO: Set Mail Server Properties
    private static final String HOST = "smtp.gmail.com";
    private static final int PORT = 465;
    private static final boolean SSL_FLAG = true; 

	private final static Logger LOGGER = Logger.getLogger(ReqBenachrichtigung.class.getName());

	public void execute(DelegateExecution execution) throws Exception {

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

		String var = (String) execution.getVariable("bezeichnung");
		String varName = (String) execution.getVariable("Name");
		String vartitle = (String) execution.getVariable("title");
		String varlastN = (String) execution.getVariable("lastN");
		String etext = "Thank you for contacting the THB Support team. This message is to confirm that we have received your request and have opened a case for your issue. \r\n" + 
				"\r\n" + 
				"The new case number is: "+ ticketID + ".In order to check on the status of your ticket, please contact the THB Support team at 03381 350";
		execution.setVariable("ticketID", ticketID);

		   
        String userName = "THBSD.mail@gmail.com";
        String password = "J63c5Q1Xc1wF";
        
        String fromAddress="THBSupportTeam@TH-Brandenburg.de";
        String toAddress =  "bruns@th-brandenburg.de";
        String subject = "Service-Ticket: "+ ticketID;
        String message = etext;
        
        try {
            Email email = new SimpleEmail();
            email.setHostName(HOST);
            email.setSmtpPort(PORT);
            email.setAuthenticator(new DefaultAuthenticator(userName, password));
            email.setSSL(true);
            email.setFrom(fromAddress);
            email.setSubject(subject);
            email.setMsg(message);
            email.addTo(toAddress);
            email.send();
        }catch(Exception ex){
            System.out.println("Unable to send email");
            System.out.println(ex);
        }

		RuntimeService runtimeService = execution.getProcessEngineServices().getRuntimeService();
		runtimeService.correlateMessage("startMessage");
		runtimeService.startProcessInstanceByMessage("startMessage");

	}

}
