package org.example.projectface.database.db.create;

import org.example.projectface.database.ConnectionToDataBase;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class CreateDataBase {
    private static String dbUrl = System.getenv("C_DB");
    private  static String dbUser = System.getenv("DB_USER");
    private  static String dbPassword = System.getenv("DB_PASS");

    public static void createDataBase() {
        String dataBaseName = "face";
        try(Connection connection = DriverManager.getConnection(dbUrl,dbUser,dbPassword);
            Statement statement = connection.createStatement()) {
            String sql = "CREATE DATABASE " + dataBaseName;
            statement.execute(sql);
            System.out.println("Database " + dataBaseName + " created successfully" );
        } catch (SQLException e) {
            if (e.getMessage().contains("already exists")) {
                System.out.println("Database " + dataBaseName + " already exists");
            }else {
                System.err.println("Error creating database: " + e.getMessage());
            }
        }
    }
}
