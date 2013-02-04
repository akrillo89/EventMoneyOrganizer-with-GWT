package org.swp.emo.shared;


import com.google.gwt.user.client.rpc.IsSerializable;

public class Post implements IsSerializable  {
	public int id;
	public int event_id;
	public int user_id;
	public String name;
	public double cost;
	public byte[] bill;
	public int accept;
	public String comment; 
}
