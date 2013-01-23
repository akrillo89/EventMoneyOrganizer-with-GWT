package org.swp.emo.client;

import com.google.gwt.i18n.client.Messages;


	public interface EventMoneyOrganizerMessages extends Messages {
		
	   @DefaultMessage("Event Money Organizer in GWT")
	   String applicationTitle();
	 
	   @DefaultMessage("Username")
	   String username();
	 
	   @DefaultMessage("Password")
	   String password();

	   @DefaultMessage("Login")
	   String login();
	   
	   @DefaultMessage("Register")
	   String register();
	   
	   @DefaultMessage("EMail-Address")
	   String email();
	   
	   @DefaultMessage("Repeat Password")
	   String repeat();
	   
	   @DefaultMessage("close")
	   String close();
	   
	   @DefaultMessage("You are logged in")
	   String loginaccept();
	   
	   String events();
	   
	   String running();
	   
	   String closed();
	   
	   String payment();
	   
	   String createNewEvent();

	   String name();
	
	   String date();
	   
	   String participants();
	   
	   
	}