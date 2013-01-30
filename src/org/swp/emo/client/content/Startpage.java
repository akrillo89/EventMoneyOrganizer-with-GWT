package org.swp.emo.client.content;

import org.swp.emo.client.EventMoneyOrganizerMessages;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.FlexTable;

public class Startpage  extends FlexTable  {
	
	private EventMoneyOrganizerMessages messages = GWT.create(EventMoneyOrganizerMessages.class);
	
	public Startpage() {
		super();
		this.setText(0, 0, messages.startpage());
	}

}
