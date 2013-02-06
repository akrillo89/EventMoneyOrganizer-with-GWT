package org.swp.emo.client;

import com.google.gwt.i18n.client.Messages;
import com.google.gwt.safehtml.shared.SafeHtml;


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

	   String creatorPaysAll();
		
	   String fullSplit();

	   String typ();

	   String time();
	   @DefaultMessage("Force Bill")
	   String forceBill();

	   @DefaultMessage("Description")
	   String description();
	   
	   @DefaultMessage("Location")
	   String location();
	   
	   @DefaultMessage("Add event")
	   String addEvent();

	   @DefaultMessage("Event Added and you ll be redirected in a few seconds")
	   String eventAddedAndYouWillBeRedirected();

	   @DefaultMessage("Something went wrong")
	   String eventAddedError();
	   
	   @DefaultMessage("Startpage")	   
	   String startpage();
	   
	   @DefaultMessage("You forgot something")
	   String fillAllFields();
		
	   @DefaultMessage("Something went wrong")
	   String error();

	   @DefaultMessage("Delete")
	   String delete();

	   @DefaultMessage("Event deleted")
	   String eventDeleted();

	   @DefaultMessage("Logout")
	   String logout();

	   @DefaultMessage("Account created")
	   String AccountCreated();

	   @DefaultMessage("Open payments")
	   String openPayments();

	   @DefaultMessage("Finshed payments")
	   String finishedPayments();
	   
	   @DefaultMessage("Post")
	   String post();
	   
	   @DefaultMessage("Cost")
	   String cost();

	   @DefaultMessage("Bill")
	   String bill();

	   @DefaultMessage("Add Post")
	   String addPost();
	   
	   @DefaultMessage("Edit Event")
	   String editEvent();

	   @DefaultMessage("Event edited, you ll be redirected to you event show page")
	   String editEventAndYoullBeRedirected();
	   
	}