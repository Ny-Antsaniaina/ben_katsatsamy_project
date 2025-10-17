package org.example.projectface.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionToDataBase implements AutoCloseable {
   private static final String dbUrl = System.getenv("DB_URL");
   private static final String dbUser = System.getenv("DB_USER");
   private static final String dbPassword = System.getenv("DB_PASS");
   public Connection connection;

   public ConnectionToDataBase() {
       if (dbUrl == null || dbUser == null || dbPassword == null) {
           throw new IllegalArgumentException("dbUrl or dbUser and dbPassword are null");
       }
   }

   public Connection getConnection() {
       try{
           connection = DriverManager.getConnection(dbUrl,dbUser,dbPassword);
           return connection;
       } catch (SQLException e) {
           throw new RuntimeException("Error connecting to database.", e);
       }
   }


    @Override
    public void close() {
        try{
            if(connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error closing database.", e);
        }
    }
}
