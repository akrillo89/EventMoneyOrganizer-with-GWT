package org.swp.emo.server;

import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.swp.emo.shared.DB_UsermanagementService;
import org.swp.emo.shared.Event;
import org.swp.emo.shared.Post;

import com.google.gwt.user.client.Window;

/**
 * I extend the DB_Conn abstract class, then I don't have to rewrite code
 * 
 * @author Sebastian
 * 
 */
public class DB_UsermanagementServiceImpl extends DB_Conn implements
		DB_UsermanagementService {

	final String QueryUser = "Select * from user";
	final String QueryCheckUser = "Select count(*) from user where username = ?";
	final String QueryAddUser = "INSERT INTO user (username, password, email) VALUES (?,md5(?),?);";
	final String QueryCheckLogin = "SELECT id FROM user WHERE password = md5(?) AND username = ?;";
	final String QueryAddEvent = "INSERT INTO event(name,place,event_time,proof_compulsory,payment,user,editable,comment) VALUES (?,?,?,?,?,?,?,?);";
	final String QueryAddUserToEvent = "INSERT INTO member(event,user) VALUES(? , ?);";
	final String QueryAddPost = "INSERT INTO post(event,user,name,cost,bill,accept,comment) VALUES (?,?,?,?,?,?,?);";
	final String QueryGetPost = "SELECT id,user,name,cost,bill,accept,comment FROM post WHERE event = ?;";
	final String QueryGetLastEventId = "SELECT id FROM event ORDER BY id DESC LIMIT 1;";
	final String QueryGetUserIdByMail = "SELECT id FROM user WHERE email = ?;";
	final String QueryGetOpenEventsByUserid = "SELECT event,name FROM member as m LEFT JOIN event as e ON e.id = m.event WHERE e.editable = 1 and m.user = ? ;";
	final String QueryGetEventData = "SELECT name,place,event_time,proof_compulsory,payment,user,editable,comment FROM event WHERE id = ?;";
	final String QueryGetEventParticipants = "SELECT u.username FROM member as m LEFT JOIN user as u ON u.id = m.user WHERE m.event = ? ;";
	final String QueryDeleteEventById = "DELETE FROM event WHERE id = ? and user = ?;";
	Connection connection;

	// create session and store userId
	/*
	 * Session vars: userId
	 */
	HttpServletRequest request;
	// true will create a new session if it not yet exists
	HttpSession session;

	public DB_UsermanagementServiceImpl() {

	}
	
	public void deleteEventById(int event_id) {
		String userIdStr = session.getAttribute("userId").toString();
		if (!userIdStr.equals("")) {
			int userId = Integer.parseInt(userIdStr);
			connection = this.getConn();
		
			try {
				PreparedStatement qry = connection
						.prepareStatement(this.QueryDeleteEventById);
				qry.setInt(1, event_id);
				qry.setInt(2, userId);
				qry.execute();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public Event getEventData(int event_id) {
		Event event = new Event();
		// make sure userId is set
		String userIdStr = session.getAttribute("userId").toString();
		if (!userIdStr.equals("")) {
			int userId = Integer.parseInt(userIdStr);
			connection = this.getConn();

			try {

				PreparedStatement qry = connection
						.prepareStatement(this.QueryGetEventData);
				qry.setInt(1, event_id);
				
				ResultSet resultSet = qry.executeQuery();
				
				while (resultSet.next()) {
					
					event.id = event_id;
					event.name = resultSet.getString(1);
					event.place = resultSet.getString(2);
					event.event_time = resultSet.getString(3);
					event.proof_compulsory = resultSet.getInt(4);
					event.payment = resultSet.getString(5);
					event.owner	= (resultSet.getInt(6)==userId);
					event.editable = resultSet.getInt(7);
					event.comment = resultSet.getString(8);
				}
				
				qry = connection
						.prepareStatement(this.QueryGetEventParticipants);
				qry.setInt(1, event_id);
				
				resultSet = qry.executeQuery();
				
				
				List<String> participants= new ArrayList<String>();
				
				while (resultSet.next()) {
					
					participants.add(resultSet.getString(1));
				}
				
				String[] participants_arr = new String[participants.size()];
				
				for(int i = 0 ; i < participants.size(); i++) {
					participants_arr[i] = participants.get(i);
				}
				
				event.participants = participants_arr;
				
				connection.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return event;
	}
	
	/**
	 * This is a private addUserToEvent function This ll not close a connection
	 * or check for login or anything else
	 * 
	 * @param event_id
	 * @param user_id
	 */
	private void addUserToEvent(int event_id, int user_id) {
		try {
			PreparedStatement qry = connection
					.prepareStatement(this.QueryAddUserToEvent);
			qry.setInt(1, event_id);
			qry.setInt(2, user_id);
			qry.execute();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public Event[] getOpenEvents() {
		List<Event> events = new ArrayList<Event>();
		// make sure userId is set
		String userIdStr = session.getAttribute("userId").toString();
		if (!userIdStr.equals("")) {
			int userId = Integer.parseInt(userIdStr);
			connection = this.getConn();

			try {

				PreparedStatement qry = connection
						.prepareStatement(this.QueryGetOpenEventsByUserid);
				qry.setInt(1, userId);

				ResultSet resultSet = qry.executeQuery();

				while (resultSet.next()) {
					Event addEvent = new Event();
					addEvent.id = resultSet.getInt(1);
					addEvent.name = resultSet.getString(2);

					events.add(addEvent);
				}

				connection.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		Event[] result = new Event[events.size()];

		for (int i = 0; i < result.length; i++) {
			result[i] = events.get(i);
		}

		return result;
	}

	public void registerEvent(String name, String place, String event_time,
			boolean proof_compulsory, String payment, String comment,
			String userMailsStr) {
		// make sure userId is set
		String userIdStr = session.getAttribute("userId").toString();
		if (!userIdStr.equals("")) {
			int userId = Integer.parseInt(userIdStr);
			connection = this.getConn();

			try {
				PreparedStatement qry = connection
						.prepareStatement(this.QueryAddEvent);
				qry.setString(1, name);
				qry.setString(2, place);

				SimpleDateFormat sdf = new SimpleDateFormat(
						"EEE MMM dd HH:mm:ss Z yyyy", new Locale("us"));
				SimpleDateFormat mysqlSdf = new SimpleDateFormat(
						"YYYY-MM-DD HH:MM:SS", new Locale("us"));
				Date date = null;
				;
				try {
					date = sdf.parse(event_time);

				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				qry.setString(3, mysqlSdf.format(date));
				qry.setInt(4, (proof_compulsory) ? 1 : 0); // cast bool to int
				qry.setString(5, payment);
				qry.setInt(6, userId);
				qry.setInt(7, 1); // editable
				qry.setString(8, comment);

				qry.execute();

				// get event id
				qry = connection.prepareStatement(this.QueryGetLastEventId);

				ResultSet resultSet = qry.executeQuery();
				int event_id = -1;
				while (resultSet.next()) {
					event_id = resultSet.getInt(1);
				}

				// add user to event
				int mail_user_id;

				String[] userMails = userMailsStr.split(",");

				for (String mail : userMails) {
					// get userid
					qry = connection
							.prepareStatement(this.QueryGetUserIdByMail);
					qry.setString(1, mail);
					resultSet = qry.executeQuery();
					while (resultSet.next()) {
						mail_user_id = resultSet.getInt(1);

						addUserToEvent(event_id, mail_user_id);
					}
				}

				// add owner

				addUserToEvent(event_id, userId);

				connection.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	/**
	 * Insert user to DB without validation
	 * 
	 */
	public int registerUser(String username, String password, String email) {
		connection = this.getConn();
		int res = 1 ;

		try {
			PreparedStatement qry2 = connection
					.prepareStatement(this.QueryCheckUser);
			qry2.setString(1, username);
			ResultSet resultSet = qry2.executeQuery();
			resultSet.next();
			if (resultSet.getInt(1) == 0) {
				
				PreparedStatement qry = connection
						.prepareStatement(this.QueryAddUser);
				qry.setString(1, username);
				qry.setString(2, password);
				qry.setString(3, email);
				qry.execute();
				connection.close();
				
			} else {
				connection.close();
				res = 0;

			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		}
		return res;
	}

	public int checkLogin(String username, String password) {
		session = getThreadLocalRequest().getSession();

		int id = -1;
		connection = this.getConn();
		boolean found = false;
		try {
			PreparedStatement qry = connection
					.prepareStatement(this.QueryCheckLogin);
			qry.setString(1, password);
			qry.setString(2, username);

			ResultSet resultSet = qry.executeQuery();

			while (resultSet.next()) {
				id = resultSet.getInt(1);
				found = true;
			}

			session.setAttribute("userId", id);

			connection.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return id;
	}

	/**
	 * Example function
	 * 
	 * @return List<String> allusers
	 */
	public List<String> getAllUser() {

		connection = this.getConn();

		List<String> users = new ArrayList<String>();

		try {

			Statement stmt = connection.createStatement();
			ResultSet resultSet = stmt.executeQuery(this.QueryUser);
			while (resultSet.next()) {
				users.add(resultSet.getString("name"));
			}
			connection.close();

		} catch (SQLException e) {
			// debug out output this way
			System.err.println("Mysql Statement Error: " + e.getMessage());
			e.printStackTrace();
		}

		return users;
	}

	@Override
	public void registerPost(int event, String name, double cost, byte[] bill, String comment) {
		String userIdStr = session.getAttribute("userId").toString();
		if (!userIdStr.equals("")) {
			int userId = Integer.parseInt(userIdStr);
			connection = this.getConn();

			try {
				PreparedStatement qry = connection
						.prepareStatement(this.QueryAddPost);
				qry.setInt(1, event);	//event
				qry.setLong(2, userId);		//user
				qry.setString(3, name);		//name
				qry.setDouble(4, cost); 	//cost
				qry.setBytes(5, bill);		//bill
				qry.setInt(6, 0);			//accept
				qry.setString(7, comment);	//comment

				qry.execute();

				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public Post[] getPost(int event_id) {
		Post post = new Post();
		Post[] result = null;
		// make sure userId is set
		String userIdStr = session.getAttribute("userId").toString();
		if (!userIdStr.equals("")) {
			int userId = Integer.parseInt(userIdStr);
			connection = this.getConn();
			List<Post> postList= new ArrayList<Post>();
			try {

				PreparedStatement qry = connection
						.prepareStatement(this.QueryGetPost);
				qry.setInt(1, event_id);
				
				ResultSet resultSet = qry.executeQuery();
				
				while (resultSet.next()) {
					post.id = resultSet.getInt(1);
					post.event_id = event_id;
					post.user_id = resultSet.getInt(2);
					post.name = resultSet.getString(3);
					post.cost = resultSet.getDouble(4);
					post.bill = resultSet.getBytes(5);
					post.accept	= resultSet.getInt(6);
					post.comment = resultSet.getString(7);
					postList.add(post);
				}
				
				connection.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			result = new Post[postList.size()];
			
			for(int i = 0 ; i < postList.size(); i++) {
				result[i] = postList.get(i);
			}
		}
		return result;
	}
}
