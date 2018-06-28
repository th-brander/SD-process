package org.camunda.bpm.getstarted.itbeschaffung;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.mail.Email;
import org.apache.commons.mail.SimpleEmail;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.camunda.bpm.engine.runtime.Execution;


public class ITLiefern implements JavaDelegate {

  // TODO: Set Mail Server Properties
	  private static final String HOST = "smtp.gmail.com";
	  private static final String USER = "camundauser@gmail.com";
	  private static final String PWD = "pyZ-Bk3-u2u-WY9";

  private final static Logger LOGGER = Logger.getLogger(ITLiefern.class.getName());

  public void execute(DelegateExecution execution) throws Exception {
      
	  
      String recipient = "camundauser@gmail.com";
      
      Email email = new SimpleEmail();
      email.setCharset("utf-8");
      email.setHostName(HOST);
      email.setSmtpPort(465);
      email.setAuthentication(USER, PWD);
      email.setSSL(true);
      
      try {
          email.setFrom("noreplylieferant@camunda.org");
          email.setSubject("Artikel Liefern");
          email.setMsg("Sehr geehrte Damen/Herren, \n\n Hiermit sende ich Ihnen den Artikel.\n\n Mit freundlichen Grüßen, \n\n Lieferant");

          email.addTo(recipient);

          email.send();
          LOGGER.info("Task Assignment Email successfully sent to address: " + recipient); 

        } catch (Exception e) {
          LOGGER.log(Level.WARNING, "Could not send email to assignee", e);
        }
      

      	
      RuntimeService runtimeService = execution.getProcessEngineServices().getRuntimeService();
      
      	runtimeService.createMessageCorrelation("receiveMessage");
      	Execution aa = runtimeService.createExecutionQuery().messageEventSubscriptionName("receiveMessage").singleResult();
      	
        runtimeService.messageEventReceived("receiveMessage", aa.getId());
      	

    }

}