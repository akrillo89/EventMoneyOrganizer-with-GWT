package org.swp.emo.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.datepicker.client.DatePicker;

public class Widget_CreateEvent extends FlexTable {
	
	private EventMoneyOrganizerMessages messages = GWT.create(EventMoneyOrganizerMessages.class);
	
	Widget_CreateEvent() {
		super();
		
		//center
		this.addStyleName("center");
		
		//eventname
		this.setWidget(0, 0, new Label(messages.name()));
		final TextBox eventNameInput = new TextBox();
		eventNameInput.addStyleName("input"); //See here same CSS Class is used
		this.setWidget(0, 1, eventNameInput);

		//eventdatum
		this.setWidget(1, 0, new Label(messages.date()));
		this.setWidget(1, 1, new DatePicker());
		
		//Teilnehmer
		this.setWidget(2, 0, new Label(messages.participants()));
		this.setWidget(2, 1, new TextBox());
		
		//Create new event form
	}
	
}
