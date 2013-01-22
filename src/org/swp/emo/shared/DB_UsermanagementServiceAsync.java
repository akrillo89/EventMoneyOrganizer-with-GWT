package org.swp.emo.shared;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface DB_UsermanagementServiceAsync {
	public void checkLogin(String username, String password, AsyncCallback<Integer> callback);
	public void registerUser(String username, String password, String email, AsyncCallback<Void> callback);
}
