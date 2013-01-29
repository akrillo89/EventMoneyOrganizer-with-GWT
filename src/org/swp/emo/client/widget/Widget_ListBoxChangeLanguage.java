package org.swp.emo.client.widget;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.ListBox;

public class Widget_ListBoxChangeLanguage extends ListBox {
	
	String rootUrl = Window.Location.getHref();
	
	public Widget_ListBoxChangeLanguage() {
		super();
		
		this.addItem("English");
		this.addItem("German");
		
		//check if its german or english, if its german just replace root url and remove locale variable and preselect german
		if(rootUrl.indexOf("&locale=de") != -1)
		{
			this.setItemSelected(1, true);
			
			rootUrl = rootUrl.replaceAll("&locale=de", "");
		}
		
		
		
		
		this.addChangeHandler(new ChangeHandler()
		{
			  public void onChange(ChangeEvent event)
			  {
				  int selected = getCurrentIndex();
				  if(selected == 0)
				  {
					  Window.Location.replace( rootUrl );
				  }
				  else
				  {
					  Window.Location.replace( rootUrl + "&locale=de");
				  }
			  }
		});
		
		
		
	}
	
	protected int getCurrentIndex()  {
		return this.getSelectedIndex();
	}

}
