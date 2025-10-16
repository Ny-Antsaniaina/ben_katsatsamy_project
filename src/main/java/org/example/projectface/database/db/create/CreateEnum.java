package org.example.projectface.database.db.create;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class CreateEnum {
    private static String dbUrl = System.getenv("DB_URL");
    private static String dbUser = System.getenv("DB_USER");
    private static String dbPassword = System.getenv("DB_PASS");

    public static void type() {
        try (Connection conn = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
             Statement statement = conn.createStatement()) {


            String motifEnum = "CREATE TYPE motif_type_enum AS ENUM ('medical', 'personal', 'academic');";
            String statusEnum = "CREATE TYPE status_enum AS ENUM ('present', 'absent', 'late');";
            String roleEnum = "CREATE TYPE role_enum AS ENUM ('student', 'teacher', 'director');";

            try {
                statement.executeUpdate(motifEnum);
                System.out.println("motif_type_enum created");
            } catch (SQLException e) {
                if (e.getMessage().contains("already exists")) {
                    System.out.println("motif_type_enum already exists");
                } else throw e;
            }

            try {
                statement.executeUpdate(statusEnum);
                System.out.println("status_enum created");
            } catch (SQLException e) {
                if (e.getMessage().contains("already exists")) {
                    System.out.println("status_enum already exists");
                } else throw e;
            }

            try {
                statement.executeUpdate(roleEnum);
                System.out.println("role_enum created");
            } catch (SQLException e) {
                if (e.getMessage().contains("already exists")) {
                    System.out.println("role_enum already exists");
                } else throw e;
            }

        } catch (SQLException e) {
            System.out.println("Error database connection: " + dbUrl + " " + dbUser + "\n" + e.getMessage());
        }
    }
}
