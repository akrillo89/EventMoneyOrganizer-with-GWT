package org.swp.emo.client;

import java.util.Date;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;

import org.swp.emo.shared.DB_UsermanagementService;
import org.swp.emo.shared.DB_UsermanagementServiceAsync;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import java.util.Iterator;

public class Usermanagement {

	// DB_UsermanagementServiceImpl db_usermanagement = new
	// DB_UsermanagementServiceImpl();

	private DB_UsermanagementServiceAsync userSvc = GWT
			.create(DB_UsermanagementService.class);

	private EventMoneyOrganizerMessages messages = GWT
			.create(EventMoneyOrganizerMessages.class);
	Validator validator = Validation.buildDefaultValidatorFactory()
			.getValidator();
	// register widget
	final DialogBox registerDialog = createRegisterDialogBox();

	// login widget
	HTMLPanel loginpanel = null;
	HTMLPanel shortLinks = null;

	Usermanagement() {

		setupLoginform();
	}

	/**
	 * Hier wird das Login Form initialisiert
	 * 
	 * @return
	 */
	private void setupLoginform() {

		String html = "<div id=\"loginform\" name=\"loginform\">"
				+ "<p id=\"username\" >" + "<label>" + messages.username()
				+ "<br/>" + "</p>" + "<p id=\"password\">" + "<label>"
				+ messages.password() + "<br/>" + "</p>"
				+ "<p id=\"submit\" class=\"submit\">" + "</p>" + "</div>";

		shortLinks = new HTMLPanel("<div id=\"loginShortlinks\">" + "</div>");

		loginpanel = new HTMLPanel(html); // Create the htmlpanel

		final TextBox user = new TextBox();
		user.addStyleName("input"); // See here same CSS Class is used
		loginpanel.add(user, "username");

		final TextBox password = new PasswordTextBox();
		password.addStyleName("input");
		loginpanel.add(password, "password");

		Button submit = new Button("Login");

		// Login Button , handler onclick
		submit.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				checkLogin(user.getValue(), password.getValue());
			}
		});
		submit.getElement().setId("loginsubmit"); // Give a new ID
		loginpanel.add(submit, "submit");

		Anchor register = new Anchor("Register");
		register.addStyleName("a");
		register.setText("Register");
		register.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {

				registerDialog.show();
			}
		});
		shortLinks.add(register);

		// RootPanel.get("registerDialogBox").add(registerDialog);
		RootPanel.get("loginform").add(loginpanel);
		RootPanel.get("shortlinks").add(shortLinks);
	}

	private void checkLogin(String username, String password) {

		// Set up the callback object.
		AsyncCallback<Integer> callback = new AsyncCallback<Integer>() {
			public void onFailure(Throwable caught) {
				// TODO: Do something with errors.
			}

			public void onSuccess(Integer result) {
				Window.alert(Integer.toString(result));

				// set cookie for logged in, duration just for current browser
				// session
				Date expireDate = new Date();
				Cookies.setCookie("EMO", Integer.toString(result));

				if (result != -1) {
					exit();
					EventMoneyOrganizer.chooseWidget();
				}
			}
		};

		// Make the call to the stock price service.
		userSvc.checkLogin(username, password, callback);
	}

	/**
	 * Close Userlogin Widget
	 */
	public void exit() {
		registerDialog.removeFromParent();
		loginpanel.removeFromParent();
		shortLinks.removeFromParent();
	}

	private DialogBox createRegisterDialogBox() {
		final Registration reg = new Registration();
		final Label label = new Label();

		// Create a dialog box and set the caption text
		final DialogBox dialogBox = new DialogBox();
		dialogBox.ensureDebugId("cwDialogBox");
		dialogBox.setText("Register");

		// Create a table to layout the content
		VerticalPanel dialogContents = new VerticalPanel();
		dialogContents.setWidth("100%");
		dialogContents.setSpacing(4);
		dialogBox.setWidget(dialogContents);

		// Add some text to the top of the dialog
		HTML username_text = new HTML(messages.username());
		dialogContents.add(username_text);

		final TextBox username_input = new TextBox();
		username_input.addStyleName("input"); // See here same CSS Class is used
		dialogContents.add(username_input);
		

		// Add some text to the top of the dialog
		HTML email_text = new HTML(messages.email());
		dialogContents.add(email_text);

		final TextBox email_input = new TextBox();
		email_input.addStyleName("input"); // See here same CSS Class is used
		dialogContents.add(email_input);

		// Add some text to the top of the dialog
		HTML pass1_text = new HTML(messages.password());
		dialogContents.add(pass1_text);

		final TextBox pass1_input = new PasswordTextBox();
		pass1_input.addStyleName("input"); // See here same CSS Class is used
		dialogContents.add(pass1_input);

		// Add some text to the top of the dialog
		HTML pass2_text = new HTML("Repeat Password");
		dialogContents.add(pass2_text);

		final TextBox pass2_input = new PasswordTextBox();
		pass1_input.addStyleName("input"); // See here same CSS Class is used
		dialogContents.add(pass2_input);

		// Add a close button at the bottom of the dialog
		Button register_button = new Button("register");
		register_button.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {

				reg.setUsername(username_input.getText());
				reg.setEmail(email_input.getText());
				reg.setPassword1(pass1_input.getText());
				// Set<ConstraintViolation<Registration>> violationsMail =
				// validator.validate(reg);
				label.setText("");
				Set<ConstraintViolation<Registration>> constraintViolations = validator
						.validate(reg);
				if (constraintViolations.size() > 0) {
					Iterator<ConstraintViolation<Registration>> iterator = constraintViolations
							.iterator();
					String str = new String();
					Integer count = 0;
					while (iterator.hasNext()) {
						ConstraintViolation<Registration> cv = iterator.next();

						str = str + cv.getPropertyPath().toString() + " "
								+ cv.getMessage()
								+ System.getProperty("line.separator", "\n");

						count = count + 30;

					}

					label.setStyleName("error", true);
					label.setHeight(count.toString());
					label.setText(str);

				} else if (constraintViolations.size() == 0) {
					label.setText("");
				
					Integer res=registerUser(reg.getUsername(), reg.getEmail(),
							reg.getPassword1());
				Window.alert(res.toString());
			//	if (res == 0){
				//	label.setText("User Exist");
				//}
				//else if (res==1){
					//label.setText("User hinzufügt");
					
			//	}

				}
			}

		});

		dialogContents.add(label);

		// label.addStyleName("error");
		dialogContents.add(register_button);
		dialogContents.setCellHorizontalAlignment(register_button,
				HasHorizontalAlignment.ALIGN_LEFT);

		// Add a close button at the bottom of the dialog
		Button closeButton = new Button("close");
		closeButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {

				dialogBox.hide();
			}
		});
		dialogContents.add(closeButton);
		dialogContents.setCellHorizontalAlignment(closeButton,
				HasHorizontalAlignment.ALIGN_RIGHT);

		// Return the dialog box

		return dialogBox;

	}

	private int registerUser(String username, String email, String password) {
   int res = 1;
		// Set up the callback object.
		AsyncCallback<Integer> callbackreg = new AsyncCallback<Integer>() {
			public void onFailure(Throwable caught) {
				// TODO: Do something with errors.
			}

			
			public void onSuccess(Integer result) {
				// TODO Auto-generated method stub
				
				
			}
			
		};

		// Make the call to the stock price service.
		 userSvc.registerUser(username, password, email, callbackreg);
		return Integer.valueOf(callbackreg.toString());
		
	}
}