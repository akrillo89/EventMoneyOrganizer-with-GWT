package org.swp.emo.client.widget;

import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.NotificationMole;

public class Notice extends NotificationMole {
	
	Notice self = this;
	
	public Notice() {
		
		super();
		this.setAnimationDuration(1000);
	}
	
	public void setTextAnimatedWithAutohide(String text) {
		this.setMessage(text);
		
		this.show();
		
		Timer timeoutTimer = new Timer() {
	      public void run() {
	    	  self.hide();
	      }
	    };
	    
	    timeoutTimer.schedule(3000);
	}

}
