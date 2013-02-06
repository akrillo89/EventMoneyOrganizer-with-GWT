package org.swp.emo.shared;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface DB_UsermanagementServiceAsync {
	public void deleteEventById(int event_id, AsyncCallback<Void> callback);
	public void getEventData(int event_id, AsyncCallback<Event> callback);
	public void getOpenEvents(AsyncCallback<Event[]> callback);
	public void getPost(int event_id, AsyncCallback<Post[]> callback);
	public void checkLogin(String username, String password, AsyncCallback<Integer> callback);
	public void registerEvent(String name, String place, String event_time, boolean proof_compulsory, String payment, String comment, String userEmails,AsyncCallback<Void> callback );
	public void registerUser(String username, String password, String email, AsyncCallback<Integer> callbackreg);
	public void registerPost(int eventId, String name, double cost, byte[] bill, String comment, AsyncCallback<Void> callback);
	public void updateEvent(String name, String place, String event_time,boolean proof_compulsory, String payment, String comment,String userMailsStr, int event_id, AsyncCallback<Void> callback);
}
