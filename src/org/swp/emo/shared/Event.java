package org.swp.emo.shared;


import com.google.gwt.user.client.rpc.IsSerializable;

public class Event implements IsSerializable  {
	public int id;
	public String name;
	public String place;
	public String event_time;
	public int proof_compulsory;
	public String payment;
	public String comment;
	public String[] participants;
	public boolean owner;
	public int editable; 
}
