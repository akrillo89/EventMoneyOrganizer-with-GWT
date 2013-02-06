package org.swp.emo.client.content;

import org.swp.emo.client.EventMoneyOrganizer;
import org.swp.emo.client.EventMoneyOrganizerMessages;
import org.swp.emo.client.Mainpage;
import org.swp.emo.shared.DB_UsermanagementService;
import org.swp.emo.shared.DB_UsermanagementServiceAsync;
import org.swp.emo.shared.Event;
import org.swp.emo.shared.Post;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.Label;

public class Event_show extends FlexTable {

	private EventMoneyOrganizerMessages messages = GWT
			.create(EventMoneyOrganizerMessages.class);

	// rpc interface for event add registration
	private DB_UsermanagementServiceAsync userSvc = GWT
			.create(DB_UsermanagementService.class);

	private int event_id;

	public FlexTable self = this;

	public Event_show(final int event_id) {
		super();
		this.event_id = event_id;
		// center
		this.addStyleName("center");

		// get event data and add field if callback returns
		// Set up the callback object.
		AsyncCallback<Event> callback = new AsyncCallback<Event>() {
			public void onFailure(Throwable caught) {
				EventMoneyOrganizer.notice.setTextAnimatedWithAutohide(messages
						.error());
			}

			public void onSuccess(Event result) {
				// eventname
				self.setWidget(0, 0, new Label(messages.name()));
				self.setWidget(0, 1, new Label(result.name));

				// description
				self.setWidget(1, 0, new Label(messages.description()));
				self.setWidget(1, 1, new Label(result.comment));

				// location
				self.setWidget(2, 0, new Label(messages.location()));
				self.setWidget(2, 1, new Label(result.place));

				// eventdatum
				self.setWidget(3, 0, new Label(messages.date()));
				self.setWidget(3, 1, new Label(result.event_time));

				// Teilnehmer
				self.setWidget(4, 0, new Label(messages.participants()));
				String participants = "";
				if(result.participants != null)
				{
					for (String p : result.participants)
						participants += p + ",";
					if (participants.length() > 0)
						participants = participants.substring(0,
								participants.length() - 1);
				}
				self.setWidget(4, 1, new Label(participants));

				// Choose what kind of event ( here u can select if result
				// payment should be paied by the event creator or should be
				// splitted to all participants)
				self.setWidget(5, 0, new Label(messages.typ()));
				self.setWidget(5, 1, new Label(result.payment));

				self.setWidget(6, 0, new Label(messages.forceBill()));
				self.setWidget(6, 1,
						new Label(Integer.toString(result.proof_compulsory)));

				if (result.owner == true) {
					Button editEvent = new Button(messages.editEvent());
					editEvent.addClickHandler(new ClickHandler() {

						public void onClick(ClickEvent event) {
							Mainpage.contentPanel.setWidget(new Event_edit(event_id));
						}

					});
					self.setWidget(6, 0, editEvent);
					Button deleteEvent = new Button(messages.delete());
					deleteEvent.addClickHandler(new ClickHandler() {

						AsyncCallback<Void> callback = new AsyncCallback<Void>() {
							public void onFailure(Throwable caught) {
								EventMoneyOrganizer.notice
										.setTextAnimatedWithAutohide(messages
												.error());
							}

							public void onSuccess(Void result) {

								EventMoneyOrganizer.notice
										.setTextAnimatedWithAutohide(messages
												.eventDeleted());
								Mainpage.refreshOpenEvents();
								Mainpage.contentPanel
										.setWidget(new Startpage());
							}
						};

						public void onClick(ClickEvent event) {
							userSvc.deleteEventById(event_id, callback);
						}

					});
					self.setWidget(6, 1, deleteEvent);

				}

				Grid grid = new Grid(4, 4);
				int numRows = grid.getRowCount();
				int numColumns = grid.getColumnCount();
				for (int row = 0; row < numRows; row++) {
					for (int col = 0; col < numColumns; col++) {
						grid.setText(row, col, "bla");
					}
				}
				self.setWidget(7, 0, grid);

				self.setWidget(8, 0, new Label());
				Button addPost = new Button(messages.post());
				addPost.addClickHandler(new ClickHandler() {
					AsyncCallback<Void> callback = new AsyncCallback<Void>() {

						public void onFailure(Throwable caught) {
							EventMoneyOrganizer.notice
									.setTextAnimatedWithAutohide(messages
											.error());
						}

						public void onSuccess(Void result) {
							Mainpage.contentPanel
									.setWidget(new Widget_CreatePost(event_id));
						}
					};

					public void onClick(ClickEvent event) {
						Mainpage.contentPanel.setWidget(new Widget_CreatePost(
								event_id));
					}

				});
				self.setWidget(8, 1, addPost);
				
				
			}
		};

		 final AsyncCallback<Post[]> callback2 = new AsyncCallback<Post[]>() {
		 public void onFailure(Throwable caught) {
		 EventMoneyOrganizer.notice.setTextAnimatedWithAutohide(messages.error());
		 }
		
		 public void onSuccess(Post[] result) {
		 // Posten hinzufügen
		
				Grid grid = new Grid(4, 4);
				int numRows = grid.getRowCount();
				int numColumns = grid.getColumnCount();
				for (int row = 0; row < numRows; row++) {
					for (int col = 0; col < numColumns; col++) {
						grid.setText(row, col, "bla");
					}
				}
				self.setWidget(7, 0, grid);
		
				self.setWidget(8, 0, new Label());
				Button addPost = new Button(messages.post());
				addPost.addClickHandler(new ClickHandler() {
					AsyncCallback<Void> callback = new AsyncCallback<Void>() {
		
						public void onFailure(Throwable caught) {
							EventMoneyOrganizer.notice
									.setTextAnimatedWithAutohide(messages
											.error());
						}
		
						public void onSuccess(Void result) {
							Mainpage.contentPanel
									.setWidget(new Widget_CreatePost(event_id));
						}
					};
		
					public void onClick(ClickEvent event) {
						Mainpage.contentPanel.setWidget(new Widget_CreatePost(
								event_id));
					}
		
				});
				self.setWidget(8, 1, addPost);
			}
		 };

		// Make the call to the stock price service.
		userSvc.getEventData(event_id, callback);
		 userSvc.getPost(event_id, callback2);
	}
}
