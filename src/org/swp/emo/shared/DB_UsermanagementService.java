package org.swp.emo.shared;

import com.google.gwt.user.client.rpc.RemoteService;

import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
@RemoteServiceRelativePath("Usermanagement")
public interface DB_UsermanagementService extends RemoteService{
	public int checkLogin(String username, String password);
	public void registerUser(String username, String password, String email);
}
