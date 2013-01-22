package org.swp.emo.server;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.swp.emo.shared.DB_UsermanagementService;

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
		
		Connection connection;
		
        public DB_UsermanagementServiceImpl() {
        	 //setup the object
            
            
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
