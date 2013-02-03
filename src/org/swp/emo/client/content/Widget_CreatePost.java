package org.swp.emo.client.content;

import java.sql.Blob;

import org.swp.emo.client.EventMoneyOrganizer;
import org.swp.emo.client.EventMoneyOrganizerMessages;
import org.swp.emo.client.Mainpage;
import org.swp.emo.shared.DB_UsermanagementService;
import org.swp.emo.shared.DB_UsermanagementServiceAsync;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;

public class Widget_CreatePost extends FlexTable {
	
	private EventMoneyOrganizerMessages messages = GWT.create(EventMoneyOrganizerMessages.class);
	
	//rpc interface for event add registration
	private DB_UsermanagementServiceAsync userSvc = GWT.create(DB_UsermanagementService.class);
	
	public Widget_CreatePost(final int eventId) {
		super();
		
		//center
		this.addStyleName("center");
		
		//postname
		this.setWidget(0, 0, new Label(messages.name()));
		final TextBox postNameInput = new TextBox();
		postNameInput.addStyleName("input"); 
		this.setWidget(0, 1, postNameInput);

		//cost
		this.setWidget(1, 0, new Label(messages.cost()));
		final TextBox postCostInput = new TextBox();
		postCostInput.addStyleName("input"); 
		this.setWidget(1, 1, postCostInput);
		
		//comment
		this.setWidget(2, 0, new Label(messages.description()));
		final TextArea descArea = new TextArea();
		this.setWidget(2, 1, descArea);
		
		//bill a FileUpload widget
		this.setWidget(3, 0, new Label(messages.bill()));
		final FileUpload billUpload = new FileUpload();
		billUpload.setName("uploadFormElement");
		this.setWidget(3, 1, billUpload);
		
		//submit
		Button button = new Button(messages.addPost());
		this.setWidget(4, 0, button);
		
		//handler for post add
		button.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				double zahl = 0;
				boolean validation = false;
				if(	!postNameInput.getText().equals("") &&
					!postCostInput.getText().equals(""));
				
				try {
    				zahl  = Double.parseDouble( postCostInput.getText() ) ;
			    	validation = true;
			    } catch (Exception e) {
			        System.out.println("Kein gültiger Wert");
			        validation = false;
			    }
			    	
					
				
				if(validation)
				{
					// Set up the callback object.
				    AsyncCallback<Void> callback = new AsyncCallback<Void>() {
				      public void onFailure(Throwable caught) {
				    	  EventMoneyOrganizer.notice.setTextAnimatedWithAutohide("Hier klappt etwas nicht :(");
				      }

				      public void onSuccess(Void result) {
				    	  
				    	  EventMoneyOrganizer.notice.setTextAnimatedWithAutohide(messages.eventAddedAndYouWillBeRedirected());
				    	  Mainpage.refreshOpenEvents();
				    	  Mainpage.contentPanel.setWidget(new Startpage());
				      }
				    };
				    
				    // Make the call to the stock price service.
//				    List<String> list_participants = participants.getItemsSelected();
//				    String participantsStr = "";
//				    for(int i = 0 ; i < list_participants.size(); i++)
//				    {
//				    	participantsStr += list_participants.get(i) + ",";
//				    }
//				    if(participantsStr.length() > 0)
//				    	participantsStr = participantsStr.substring(0, participantsStr.length()-1);
				    
				    System.out.println("Event-ID: " + eventId + "; Type: " + String.valueOf(eventId));
				    System.out.println("Name: " + postNameInput.getText() + "; Type: " + String.valueOf(postNameInput.getText()));
				    System.out.println("Kosten: " + zahl + "; Type: " + String.valueOf(zahl));
				    System.out.println("Rechnung: " + billUpload.toString().getBytes() + "; Type: " + String.valueOf(billUpload.toString().getBytes()));
				    System.out.println("Kommentar: " + descArea.getText() + "; Type: " + String.valueOf(descArea.getText()));
				    userSvc.registerPost(eventId, postNameInput.getText(), zahl, billUpload.toString().getBytes(), descArea.getText(), callback);
				}
				else
				{
					EventMoneyOrganizer.notice.setTextAnimatedWithAutohide(messages.fillAllFields());
			    	  
				}
				
			}
			
		});
		
		
	
	}
	
}
