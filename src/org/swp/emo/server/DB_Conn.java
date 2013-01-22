package org.swp.emo.server;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * Abstract class for DB connections. Each section like DB_Usermanagement should inherit , because of this DB_Conn is abstract
 * @author Sebastian Barthel
 *
 */
@SuppressWarnings("serial")
public class DB_Conn extends RemoteServiceServlet {


        /**
         * Constructor
         */
        public DB_Conn() {

               
        }

       
        /**
         * Just be sure that you include your jdbc connector to your included libraries
         * @return Connection
         */
        protected Connection getConn() {

                Connection conn = null;

                // figure out what server this application is being hosted on
                String url              = "usercharts.net";
                String db               = "d015e11e";
                String user     = "d015e11e";
                String pass     = "zHVV76B87PUB2sVA";
                String driver = "com.mysql.jdbc.Driver";
               
                //url = url + db;
               
                //System.out.println("connection url: " + url);
               
                try {

                        Class.forName(driver);
                        conn = DriverManager.getConnection("jdbc:mysql://"+url+"/"+db+"?"
                                        + "user="+user+"&password="+pass);

                } catch (Exception e) {

                        // error
                        System.err.println("Mysql Connection Error: ");

                        // for debugging error
                        e.printStackTrace();
                }

                if (conn == null)  {
                        System.out.println("~~~~~~~~~~ can't get a Mysql connection");
                }
               
                return conn;
        }

        /*
         * get recordset row count
         *
         * static will allow you to use it independently, persay,
         * you don't have to init the class into an object to use this method
         */
        protected static int getResultSetSize(ResultSet resultSet) {
                int size = -1;

                try {
                        resultSet.last();
                        size = resultSet.getRow();
                        resultSet.beforeFirst();
                } catch(SQLException e) {
                        return size;
                }

                return size;
        }


}

