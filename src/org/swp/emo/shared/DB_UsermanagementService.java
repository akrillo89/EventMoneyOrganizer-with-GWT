package org.swp.emo.shared;


import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.RemoteService;

import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
@RemoteServiceRelativePath("Usermanagement")
public interface DB_UsermanagementService extends RemoteService{
	public void deleteEventById(int event_id);
	public Event getEventData(int event_id);
	public Event[] getOpenEvents();
	public Post[] getPost(int event_id);
	public int checkLogin(String username, String password);
	public void registerEvent(String name, String place, String event_time, boolean proof_compulsory, String payment, String comment, String usermails );
	public int registerUser(String username, String password, String email);
	public void registerPost(int event, String name, double cost, byte[] bill, String comment);
	public void updateEvent(String name, String place, String event_time,boolean proof_compulsory, String payment, String comment,String userMailsStr, int event_id);
}
