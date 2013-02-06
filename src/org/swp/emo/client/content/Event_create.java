package org.swp.emo.client.content;



import java.util.Date;
import java.util.List;

import org.swp.emo.client.EventMoneyOrganizer;
import org.swp.emo.client.EventMoneyOrganizerMessages;
import org.swp.emo.client.Mainpage;
import org.swp.emo.client.widget.InputListWidget;
import org.swp.emo.shared.DB_UsermanagementService;
import org.swp.emo.shared.DB_UsermanagementServiceAsync;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.LayoutPanel;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.NotificationMole;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.datepicker.client.DatePicker;
import com.summatech.gwt.client.HourMinutePicker;
import com.summatech.gwt.client.HourMinutePicker.PickerFormat;

public class Event_create extends FlexTable {
	
	private EventMoneyOrganizerMessages messages = GWT.create(EventMoneyOrganizerMessages.class);
	
	//rpc interface for event add registration
	private DB_UsermanagementServiceAsync userSvc = GWT.create(DB_UsermanagementService.class);
	
	public Event_create() {
		super();
		
		//center
		this.addStyleName("center");
		
		//eventname
		this.setWidget(0, 0, new Label(messages.name()));
		final TextBox eventNameInput = new TextBox();
		eventNameInput.addStyleName("input"); //See here same CSS Class is used
		this.setWidget(0, 1, eventNameInput);

		//description
		this.setWidget(1, 0, new Label(messages.description()));
		final TextArea descriptionArea = new TextArea();
		this.setWidget(1, 1, descriptionArea);
		
		//location
		this.setWidget(2, 0, new Label(messages.location()));
		final TextArea locationArea = new TextArea();
		this.setWidget(2, 1, locationArea);
		
		
		//eventdatum
		final DatePicker date = new DatePicker();
		date.setValue(new Date());
		this.setWidget(3, 0, new Label(messages.date()));
		this.setWidget(3, 1, date);
		
		//timepicker
		final HourMinutePicker hourMinutePicker = new HourMinutePicker(PickerFormat._24_HOUR);
		this.setWidget(4, 0, new Label(messages.time()));
		this.setWidget(4, 1, hourMinutePicker);
		
		//Teilnehmer
		this.setWidget(5, 0, new Label(messages.participants()));
		final InputListWidget participants = new InputListWidget();
		this.setWidget(5, 1, participants);
		
		
		//Choose what kind of event ( here u can select if result payment should be paied by the event creator or should be splitted to all participants)
		final ListBox chooseType = new ListBox(false);
		chooseType.addItem(messages.creatorPaysAll());
		chooseType.addItem(messages.fullSplit());
		this.setWidget(6, 0, new Label(messages.typ()));
		this.setWidget(6, 1, chooseType);
		
		final CheckBox forceBill = new CheckBox("");
		this.setWidget(7, 0, new Label(messages.forceBill()));
		this.setWidget(7, 1, forceBill);
		
		//submit
		Button button = new Button(messages.addEvent());
		this.setWidget(8, 0, button);
		
		
		
		//handler for event add
		button.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				
				boolean validation = false;
				if(	!eventNameInput.getText().equals("") &&
					!descriptionArea.getText().equals("") &&
					!locationArea.getText().equals("") &&
					!hourMinutePicker.getMinute().toString().equals("-15"))
					validation = true;
				
				if(validation)
				{
					// Set up the callback object.
				    AsyncCallback<Void> callback = new AsyncCallback<Void>() {
				      public void onFailure(Throwable caught) {
				    	  EventMoneyOrganizer.notice.setTextAnimatedWithAutohide(messages.eventAddedError());
				      }

				      public void onSuccess(Void result) {
				    	  
				    	  EventMoneyOrganizer.notice.setTextAnimatedWithAutohide(messages.eventAddedAndYouWillBeRedirected());
				    	  Mainpage.refreshOpenEvents();
				    	  Mainpage.contentPanel.setWidget(new Startpage());
				      }
				    };
				    
				    Date full_date = date.getValue();
				    full_date.setHours(hourMinutePicker.getHour());
				    full_date.setMinutes(hourMinutePicker.getMinute());
				    
				    // Make the call to the stock price service.
				    List<String> list_participants = participants.getItemsSelected();
				    String participantsStr = "";
				    for(int i = 0 ; i < list_participants.size(); i++)
				    {
				    	participantsStr += list_participants.get(i) + ",";
				    }
				    if(participantsStr.length() > 0)
				    	participantsStr = participantsStr.substring(0, participantsStr.length()-1);
				    System.out.println("FullDate: " + full_date.toString());
				    userSvc.registerEvent(eventNameInput.getText(), locationArea.getText(), full_date.toString(), forceBill.getValue(), chooseType.getItemText(chooseType.getSelectedIndex()), descriptionArea.getValue(),participantsStr, callback);
				}
				else
				{
					EventMoneyOrganizer.notice.setTextAnimatedWithAutohide(messages.fillAllFields());
			    	  
				}
				
			}
			
		});
		
		
	
	}
	
}
