package org.swp.emo.shared;

import com.google.gwt.user.client.rpc.RemoteService;

import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
@RemoteServiceRelativePath("Usermanagement")
public interface DB_UsermanagementService extends RemoteService{
	public Event[] getOpenEvents();
	public int checkLogin(String username, String password);
	public void registerEvent(String name, String place, String event_time, boolean proof_compulsory, String payment, String comment, String usermails );
	public void registerUser(String username, String password, String email);
	
}
