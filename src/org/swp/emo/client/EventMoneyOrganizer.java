package org.swp.emo.client;

import org.swp.emo.client.widget.Notice;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.NotificationMole;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class EventMoneyOrganizer implements EntryPoint {

	public static Notice notice = new Notice();
	
	public void onModuleLoad() {
		RootPanel.get().add(notice); 
		this.chooseWidget();
		
		
	}
	
	public static void chooseWidget() {
		String cookie = Cookies.getCookie("EMO");
		System.out.println(Cookies.getCookieNames());
		//check ob der nutzer schonmal eingeloggt war, bisher total unsicher aber soll für den zweck genügen
		// -1 ist nicht eingeloggt, alles andere sind die userids
		if(cookie != null && !cookie.equals("-1"))
		{
			notice.setTextAnimatedWithAutohide("Du bist eingeloggt, userid: " + cookie);
			
			Mainpage mainpage = new Mainpage();
			
		}
		else
		{
			Usermanagement usermanagement = new Usermanagement();
			//Window.alert("Du bist nicht eingeloggt, userid: " + cookie);
		}
	}

}
