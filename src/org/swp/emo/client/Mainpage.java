package org.swp.emo.client;

import java.util.Date;

import org.swp.emo.client.content.Event_show;
import org.swp.emo.client.content.Startpage;
import org.swp.emo.client.content.Event_create;
import org.swp.emo.client.widget.Widget_FlexExampleTable;
import org.swp.emo.client.widget.Widget_ListBoxChangeLanguage;
import org.swp.emo.shared.DB_UsermanagementService;
import org.swp.emo.shared.DB_UsermanagementServiceAsync;
import org.swp.emo.shared.Event;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.client.Cookies;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.LayoutPanel;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.StackLayoutPanel;
import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.TreeItem;
import com.google.gwt.user.client.ui.Widget;

/**
 * This page ll be shown if the user is logged in
 * @author akrillo
 *
 */
public class Mainpage {
	
	HorizontalPanel header;
	
	public StackLayoutPanel leftMenu;
	
	//Main structure panel
	public LayoutPanel mainPanel;
	
	private EventMoneyOrganizerMessages messages = GWT.create(EventMoneyOrganizerMessages.class);
	
	private static DB_UsermanagementServiceAsync userSvc = GWT.create(DB_UsermanagementService.class);
	//content
	public static ScrollPanel contentPanel = new ScrollPanel(new Label("Hallo"));
	
	static Event[] openEvents;
	
	static TreeItem runningEvents = null;
	/**
	 * Constuctor: create the main menu panel and initialize the content panel
	 */
	Mainpage() {
		this.setupMainMenu();
		this.addContentPanel();
		contentPanel.setWidget(new Startpage());
	}



	private void setupMainMenu() {
		// Create a new stack layout panel.
	    leftMenu = new StackLayoutPanel(Unit.EM);
	    leftMenu.addStyleName("mainMenu");
	    leftMenu.setPixelSize(200, 600);
		
	 // Add the Event folders.
	    Widget eventHeader = createHeaderWidget(messages.events(),Resources.INSTANCE.eventIcon());
	 // Add the Event folders.
	    Widget paymentHeader = createHeaderWidget(messages.payment(),Resources.INSTANCE.eventIcon());
	    
	    leftMenu.add(createEventItem(), eventHeader, 4);
	    leftMenu.add(createPaymentItem(), paymentHeader, 5);
	    
	    // Create a Split Panel
	    
	    mainPanel = new LayoutPanel();
	    mainPanel.setStyleName("mainPanel");
	    mainPanel.setPixelSize(800, 700);
	    
	    mainPanel.ensureDebugId("cwSplitLayoutPanel");
	    
	    header = new HorizontalPanel();
	    header.setWidth("800px");
	    header.setStyleName("header");
	    
	    //logout
	    Button logoutBtn = new Button();
	    logoutBtn.setText(messages.logout());
	    logoutBtn.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				Cookies.removeCookie("EMO");
				exit();
				EventMoneyOrganizer.chooseWidget();
			}
	    	
	    });
	    
	    header.add(logoutBtn);
	    HTML headline = new HTML("");
	    headline.addStyleName("center");
	    header.add(headline); 
	    Widget_ListBoxChangeLanguage lang_changer = new Widget_ListBoxChangeLanguage();
	    lang_changer.setStyleName("right");
	    header.add(lang_changer);
	    header.setCellHorizontalAlignment(lang_changer, HasHorizontalAlignment.ALIGN_RIGHT);

	    Widget footer = new Label("Footer");
	    
	    //set content id
	    contentPanel.setStyleName("content");
	    
	    //add all items to our layout
	    mainPanel.add(contentPanel);
	    mainPanel.add(leftMenu);
	    mainPanel.add(footer);
	    
	    //set position of this items
	    mainPanel.setWidgetLeftWidth(leftMenu, 0, Unit.PX, 200, Unit.PX);
	    mainPanel.setWidgetRightWidth(contentPanel, 0, Unit.PX, 600, Unit.PX);
	    mainPanel.setWidgetBottomHeight(footer, 0, Unit.PX, 100, Unit.PX);
	    //define margines
	    
	    
	    RootPanel.get().add(header);
	    RootPanel.get().add(mainPanel);
	}



	private void addContentPanel() {
		// TODO Auto-generated method stub
		
	}
	
	/**
	 * Create a widget to display in the header that includes an image and some
	 * text.
	 * 
	 * @param text the header text
	 * @param image the {@link ImageResource} to add next to the header
	 * @return the header widget
	 */
	private Widget createHeaderWidget(String text, ImageResource image) {
		// Add the image and text to a horizontal panel
	    HorizontalPanel hPanel = new HorizontalPanel();
	    hPanel.setHeight("100%");
	    hPanel.setSpacing(0);
	    hPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
	    hPanel.add(new Image(image));
	    HTML headerText = new HTML(text);
	    headerText.setStyleName("cw-StackPanelHeader");
	    hPanel.add(headerText);
	    return new SimplePanel(hPanel);
	}

	
	 /**
	   * Create the {@link Tree} of Mail options.
	 * @param Images 
	   * 
	   * @param images the {@link Images} used in the Mail options
	   * @return the {@link Tree} of mail options
	   */
	private Widget createEventItem() {
		Tree eventPanel = new Tree();
		
		final TreeItem createNewEvent = eventPanel.addItem(createEasyItem(messages.createNewEvent(), Resources.INSTANCE.createNewEventIcon()));
		
		
		runningEvents = eventPanel.addTextItem(messages.running() + " " + messages.events());
	

		
		
		
//		final TreeItem runningEvents_1 = addItem(runningEvents, Resources.INSTANCE.eventIcon(), "Test");
//		final TreeItem runningEvents_2 = addItem(runningEvents, Resources.INSTANCE.eventIcon(), "Test 2");
		
		refreshOpenEvents();
		
		
		final TreeItem closedEvents = eventPanel.addTextItem(messages.closed() + " " + messages.events());
		final TreeItem closedEvents_1 = addItem(closedEvents, Resources.INSTANCE.eventIcon(), "Closed Test",0);
		final TreeItem closedEvents_2 = addItem(closedEvents, Resources.INSTANCE.eventIcon(), "Closed Test 2",0);
		
		//Onclick handler for eventPanel
		eventPanel.addSelectionHandler(new SelectionHandler<TreeItem>()
		{
		    public void onSelection(SelectionEvent<TreeItem> event)
		    {
		        if(event.getSelectedItem() == runningEvents)
		        {
		            Window.alert("runningEvents");
		        }
		        else if(event.getSelectedItem() == closedEvents) 
		        {
		        	Window.alert("Closed");	
		        }
		        else if(event.getSelectedItem() == closedEvents_1)
		        {
		        	contentPanel.setWidget(new Widget_FlexExampleTable(3));
		        }
		        else if(event.getSelectedItem() == closedEvents_2)
		        {
		        	contentPanel.setWidget(new Widget_FlexExampleTable(4));
		        }
		        else if(event.getSelectedItem() == createNewEvent)
		        {
		        	contentPanel.setWidget(new Event_create());
		        }
		        else
		        {
		        	contentPanel.setWidget(new Event_show(openEvents[event.getSelectedItem().getParentItem().getChildIndex(event.getSelectedItem())].id));
		        }
		    }
		});
		
		runningEvents.setState(true);
		return eventPanel;
	}
	
	public static void refreshOpenEvents() {
		addOpenEvents(runningEvents);
	}
	
	 private static void addOpenEvents(final TreeItem runningEvents) {
		 runningEvents.removeItems();
		// Set up the callback object.
		    AsyncCallback<Event[]> callback = new AsyncCallback<Event[]>() {
		      public void onFailure(Throwable caught) {
		        // TODO: Do something with errors.
		      }

		      public void onSuccess(Event[] result) {
		    	  openEvents = new Event[result.length];
		    	  for(int i = 0 ; i < result.length; i++)
		    	  {
		    		  Event toAddEvent = new Event();
		    		  toAddEvent.id = result[i].id;
		    		  toAddEvent.name = result[i].name;
		    		  openEvents[i] = toAddEvent;
		    		  addItem(runningEvents, Resources.INSTANCE.eventIcon(), result[i].name, result[i].id); 
		    		  
		    		  
		    	  }
		    	  
		      }
		    };

		    // Make the call to the stock price service.
		    userSvc.getOpenEvents(callback);
		
	}



	/**
	   * Create the {@link Tree} of Mail options.
	 * @param Images 
	   * 
	   * @param images the {@link Images} used in the Mail options
	   * @return the {@link Tree} of mail options
	   */
	private Widget createPaymentItem() {
//		TreeItem paymentPanelRoot = paymentPanel.addTextItem("Amounts outstanding");
//		addItem(paymentPanelRoot, Resources.INSTANCE.eventIcon(), "Test",0);
//		addItem(paymentPanelRoot, Resources.INSTANCE.eventIcon(), "Test 2",0);
//		
//		TreeItem paymentPanelRootClosed = paymentPanel.addTextItem("Amounts paid");
//		addItem(paymentPanelRootClosed, Resources.INSTANCE.eventIcon(), "Closed Test",0);
//		addItem(paymentPanelRootClosed, Resources.INSTANCE.eventIcon(), "Closed Test 2",0);
//		
//		paymentPanelRoot.setState(true);
		
		Tree paymentPanel = new Tree();
		final TreeItem openPayments = paymentPanel.addItem(createEasyItem(messages.openPayments(), Resources.INSTANCE.payment()));
		final TreeItem finishedPayments = paymentPanel.addItem(createEasyItem(messages.finishedPayments(), Resources.INSTANCE.bills()));
		
		//Onclick handler for eventPanel
		paymentPanel.addSelectionHandler(new SelectionHandler<TreeItem>()
		{
		    public void onSelection(SelectionEvent<TreeItem> event)
		    {
		        if(event.getSelectedItem() == openPayments)
		        {
		        	//TODO Mohamed, set your widget
		        	//contentPanel.setWidget();
		        	Window.alert("openPayments");
		        }
		        else if(event.getSelectedItem() == finishedPayments)
		        {
		        	//TODO Mohamed, set your widget
		        	//contentPanel.setWidget();
		        	Window.alert("finshedPayments");
		        }
		    }
		});
		return paymentPanel;
	}

	/**
	 * Add an easy menu item with a picture
	 */
	private Widget createEasyItem(String text, ImageResource image) {
		SafeHtmlBuilder sb = new SafeHtmlBuilder();
		sb.append(SafeHtmlUtils.fromTrustedString(AbstractImagePrototype
				.create(image).getHTML()));
		sb.appendEscaped(" ").appendEscaped(text);
		HTML res = new HTML(sb.toSafeHtml());
		res.setStyleName("item");
		return res;
		
		
	}
	
	private static TreeItem addItem(TreeItem root, ImageResource image, String label, int id) {
		SafeHtmlBuilder sb = new SafeHtmlBuilder();
		sb.append(SafeHtmlUtils.fromTrustedString(AbstractImagePrototype
				.create(image).getHTML()));
		sb.appendEscaped(" ").appendEscaped(label);
		
		HTML res = new HTML(sb.toSafeHtml());
		res.setStyleName("item");
		res.setTitle(Integer.toString(id));
		return root.addItem(res);
	}
	
	private void exit() {
		mainPanel.removeFromParent();
		header.removeFromParent();
	}
}
