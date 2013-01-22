package org.swp.emo.client;

import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Label;

public class Widget_FlexExampleTable extends FlexTable{

	Widget_FlexExampleTable(int number) {
		super();
		
		this.addStyleName("myCssClass");
		this.setWidth("200px");
		
		String content = Integer.toString(number);
		
		this.setWidget(0,0, new Label(content));
		this.setWidget(0,1, new Label(content));
		this.setWidget(1,0, new Label(content));
		this.setWidget(1,1, new Label(content));
		
		
		
	}
	
}
