package org.swp.emo.server;

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

/**
 * I extend the DB_Conn abstract class, then I don't have to rewrite code
 * 
 * @author Sebastian
 *
 */
public class DB_UsermanagementServiceImpl extends DB_Conn  implements DB_UsermanagementService {
	
		final String QueryUser = "Select * from user";
		final String QueryAddUser = "INSERT INTO user (username, password, email) VALUES (?,md5(?),?);";
		final String QueryCheckLogin = "SELECT id FROM user WHERE password = md5(?) AND username = ?;";
		final String QueryAddEvent = "INSERT INTO event(name,place,event_time,proof_compulsory,payment,user,editable,comment) VALUES (?,?,?,?,?,?,?,?);";
		final String QueryAddUserToEvent = "INSERT INTO member(event,user) VALUES(? , ?);";
		final String QueryGetLastEventId = "SELECT id FROM event ORDER BY id DESC LIMIT 1;";
		final String QueryGetUserIdByMail = "SELECT id FROM user WHERE email = ?;";
		final String QueryGetOpenEventsByUserid = "SELECT event,name FROM member as m LEFT JOIN event as e ON e.id = m.event WHERE e.editable = 1 and m.user = ? ;";
		
		Connection connection;
		
		// create session and store userId
		/*
		 * Session vars: userId
		 */
		HttpServletRequest request;
		//true will create a new session if it not yet exists
		HttpSession session;
		
        public DB_UsermanagementServiceImpl() {
    		
            
        }
        
        /**
         * This is a private addUserToEvent function
         * This ll not close a connection or check for login or anything else
         * @param event_id
         * @param user_id
         */
        private void addUserToEvent(int event_id, int user_id) {
	        	try 
	        	{
					PreparedStatement qry = connection.prepareStatement(this.QueryAddUserToEvent);
					qry.setInt(1, event_id);
					qry.setInt(2, user_id);
					qry.execute();
	        	} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
        }
        
        public Event[] getOpenEvents() {
        	List<Event> events= new ArrayList<Event>();
        	//make sure userId is set
        	String userIdStr = session.getAttribute("userId").toString();
        	if(!userIdStr.equals(""))
        	{
        		int userId = Integer.parseInt(userIdStr);
	        	connection = this.getConn();
	        	
	        	try {
	        		
	        		PreparedStatement qry = connection.prepareStatement(this.QueryGetOpenEventsByUserid);
					qry.setInt(1, userId);
					
					ResultSet resultSet = qry.executeQuery();
					
					
					while(resultSet.next())
					{
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
        	
        	for(int i = 0; i < result.length; i++)
        	{
        		result[i] = events.get(i);
        	}
        	
        	return result;
        }
        
        public void registerEvent(String name, String place, String event_time, boolean proof_compulsory, String payment, String comment, String userMailsStr ) {
        	//make sure userId is set
        	String userIdStr = session.getAttribute("userId").toString();
        	if(!userIdStr.equals(""))
        	{
        		int userId = Integer.parseInt(userIdStr);
	        	connection = this.getConn();
	        	
	        	try {
					PreparedStatement qry = connection.prepareStatement(this.QueryAddEvent);
					qry.setString(1, name);
					qry.setString(2, place);
					
					SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss Z yyyy", new Locale("us"));
					SimpleDateFormat mysqlSdf = new SimpleDateFormat("YYYY-MM-DD HH:MM:SS", new Locale("us"));
					Date date = null;;
					try {
						date = sdf.parse(event_time);
						
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					
					qry.setString(3, mysqlSdf.format(date));
					qry.setInt(4, (proof_compulsory) ? 1 : 0); //cast bool to int
					qry.setString(5, payment);
					qry.setInt(6, userId);
					qry.setInt(7, 1); //editable
					qry.setString(8, comment);
					
					qry.execute();
					
					//get event id
					qry = connection.prepareStatement(this.QueryGetLastEventId);

					ResultSet resultSet = qry.executeQuery();
					int event_id = -1 ;
					while (resultSet.next()) {
						event_id = resultSet.getInt(1);
					}

					//add user to event
					int mail_user_id;
					
					String[] userMails = userMailsStr.split(",");
					
					for (String mail: userMails) {
						   //get userid
							qry = connection.prepareStatement(this.QueryGetUserIdByMail);
							qry.setString(1, mail);
							resultSet = qry.executeQuery();
							while(resultSet.next())
							{
								mail_user_id = resultSet.getInt(1);
								
								addUserToEvent(event_id,mail_user_id);
							}
					}
					
					
					//add owner
					
					addUserToEvent(event_id,userId);
					
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
        public void registerUser(String username, String password, String email){
        	connection = this.getConn();
        	
        	try {
				PreparedStatement qry = connection.prepareStatement(this.QueryAddUser);
				qry.setString(1, username);
				qry.setString(2, password);
				qry.setString(3, email);
				qry.execute();
				connection.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
        
        public int checkLogin(String username, String password) {
        	session = getThreadLocalRequest().getSession();
        	
        	int id = -1;
        	connection = this.getConn();
        	boolean found = false;
        	try {
				PreparedStatement qry = connection.prepareStatement(this.QueryCheckLogin);
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
				//debug out output this way
                System.err.println("Mysql Statement Error: " + e.getMessage());
				e.printStackTrace();
			}
        	
        	return users;
        }

}
