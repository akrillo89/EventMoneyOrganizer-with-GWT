package org.swp.emo.client.content;



import java.util.Date;
import java.util.List;

import org.swp.emo.client.EventMoneyOrganizer;
import org.swp.emo.client.EventMoneyOrganizerMessages;
import org.swp.emo.client.Mainpage;
import org.swp.emo.client.widget.InputListWidget;
import org.swp.emo.shared.DB_UsermanagementService;
import org.swp.emo.shared.DB_UsermanagementServiceAsync;
import org.swp.emo.shared.Event;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
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

public class Event_edit extends FlexTable {
	
	private EventMoneyOrganizerMessages messages = GWT.create(EventMoneyOrganizerMessages.class);
	
	//rpc interface for event add registration
	private DB_UsermanagementServiceAsync userSvc = GWT.create(DB_UsermanagementService.class);
	
	private Event_edit self = this;
	
	private int event_id;
	
	public Event_edit( int event_id) {
		super();
		
		this.event_id = event_id;
		
		//center
		this.addStyleName("center");
		
		// Set up the callback object.
		AsyncCallback<Event> callback = new AsyncCallback<Event>() {
			public void onFailure(Throwable caught) {
				EventMoneyOrganizer.notice.setTextAnimatedWithAutohide(messages
						.error());
			}

			public void onSuccess(Event result) {
				//eventname
				self.setWidget(0, 0, new Label(messages.name()));
				final TextBox eventNameInput = new TextBox();
				eventNameInput.addStyleName("input"); //See here same CSS Class is used
				eventNameInput.setText(result.name);
				self.setWidget(0, 1, eventNameInput);
		
				//description
				self.setWidget(1, 0, new Label(messages.description()));
				final TextArea descriptionArea = new TextArea();
				descriptionArea.setText(result.comment);
				self.setWidget(1, 1, descriptionArea);
				
				//location
				self.setWidget(2, 0, new Label(messages.location()));
				final TextArea locationArea = new TextArea();
				locationArea.setText(result.place);
				self.setWidget(2, 1, locationArea);
				
				
				//eventdatum
				final DatePicker date = new DatePicker();
				System.out.println("Data: " + result.event_time);
				Date localDate = DateTimeFormat.getFormat("yyyy-MM-dd HH:mm:ss.S").parse(result.event_time);
				date.setValue(localDate);
				self.setWidget(3, 0, new Label(messages.date()));
				self.setWidget(3, 1, date);
				
				//timepicker
				final HourMinutePicker hourMinutePicker = new HourMinutePicker(PickerFormat._24_HOUR);
				hourMinutePicker.setTime(null, localDate.getHours(), localDate.getMinutes());
				self.setWidget(4, 0, new Label(messages.time()));
				self.setWidget(4, 1, hourMinutePicker);
				
				//Teilnehmer
				self.setWidget(5, 0, new Label(messages.participants()));
				final InputListWidget participants = new InputListWidget();
				int i = 0;
				for(String p : result.participantsMail ) {
					if(i++ != 0)
						participants.addItemsSelected(p);
				}
				self.setWidget(5, 1, participants);
				
				
				//Choose what kind of event ( here u can select if result payment should be paied by the event creator or should be splitted to all participants)
				final ListBox chooseType = new ListBox(false);
				chooseType.addItem(messages.creatorPaysAll());
				if(result.payment.equals(messages.creatorPaysAll()))
					chooseType.setItemSelected(0, true);
				chooseType.addItem(messages.fullSplit());
				if(result.payment.equals(messages.fullSplit()))
					chooseType.setItemSelected(1, true);
				
				self.setWidget(6, 0, new Label(messages.typ()));
				self.setWidget(6, 1, chooseType);
				
				final CheckBox forceBill = new CheckBox("");
				self.setWidget(7, 0, new Label(messages.forceBill()));
				System.out.println("Force bill: " + result.proof_compulsory);
				if(result.proof_compulsory == 1)
					forceBill.setChecked(true);
				self.setWidget(7, 1, forceBill);
				
				//submit
				Button button = new Button(messages.editEvent());
				self.setWidget(8, 0, button);
				
				
				
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
						    	  
						    	  EventMoneyOrganizer.notice.setTextAnimatedWithAutohide(messages.editEventAndYoullBeRedirected());
						    	  Mainpage.refreshOpenEvents();
						    	  Mainpage.contentPanel.setWidget(new Event_show(self.event_id));
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
						    userSvc.updateEvent(eventNameInput.getText(), locationArea.getText(), full_date.toString(), forceBill.getValue(), chooseType.getItemText(chooseType.getSelectedIndex()), descriptionArea.getValue(),participantsStr, self.event_id, callback);
						}
						else
						{
							EventMoneyOrganizer.notice.setTextAnimatedWithAutohide(messages.fillAllFields());
					    	  
						}
						
					}
					
				});
			}
		};
		
		userSvc.getEventData(event_id, callback);
	
	}
	
}
