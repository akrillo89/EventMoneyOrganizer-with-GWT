package org.swp.emo.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;

public interface Resources extends ClientBundle {
	public static final Resources INSTANCE =  GWT.create(Resources.class);  
	
	@Source("/images/Eventsicon.png")
	public ImageResource eventIcon();
	
	@Source("/images/add-event.png")
	public ImageResource createNewEventIcon();
	
	@Source("/images/payment.png")
	public ImageResource payment();
	
	@Source("/images/rechnungen.png")
	public ImageResource bills();
	
	
	
}
