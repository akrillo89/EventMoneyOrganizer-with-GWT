package org.swp.emo.server;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class DB_TestConnection {

        
        public static void main(String[] args) {

                //This will test the query with out to many classes in the way

                //setup the object
                DB_UsermanagementServiceImpl db_users = new DB_UsermanagementServiceImpl();
                
                System.out.println(db_users.getAllUser().toString());
                    




        }

}
