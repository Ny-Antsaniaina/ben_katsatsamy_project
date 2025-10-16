package org.example.projectface.database.db.create;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class CreateTable {
    private static String dbUrl = System.getenv("DB_URL");
    private static String dbUser = System.getenv("DB_USER");
    private static String dbPassword = System.getenv("DB_PASS");

    public static void tables() {
        try (Connection conn = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
             Statement statement = conn.createStatement()) {


            String face = """
                CREATE TABLE face (
                    face_id SERIAL PRIMARY KEY,
                    name VARCHAR(100) NOT NULL
                );
            """;


            String users = """
                CREATE TABLE users (
                    id SERIAL PRIMARY KEY,
                    name VARCHAR(100) NOT NULL,
                    role role_enum NOT NULL
                );
            """;

            String director = """
                CREATE TABLE director (
                    id SERIAL PRIMARY KEY,
                    user_id INT UNIQUE NOT NULL,
                    FOREIGN KEY (user_id)
                        REFERENCES users(id)
                        ON DELETE CASCADE
                        ON UPDATE CASCADE
                );
            """;


            String teacher = """
                CREATE TABLE teacher (
                    teacher_id SERIAL PRIMARY KEY,
                    face_id INT UNIQUE,
                    user_id INT UNIQUE,
                    FOREIGN KEY (face_id)
                        REFERENCES face(face_id)
                        ON DELETE SET NULL
                        ON UPDATE CASCADE,
                    FOREIGN KEY (user_id)
                        REFERENCES users(id)
                        ON DELETE CASCADE
                        ON UPDATE CASCADE
                );
            """;


            String student = """
                CREATE TABLE student (
                    std_id SERIAL PRIMARY KEY,
                    face_id INT UNIQUE,
                    user_id INT UNIQUE,
                    FOREIGN KEY (face_id)
                        REFERENCES face(face_id)
                        ON DELETE SET NULL
                        ON UPDATE CASCADE,
                    FOREIGN KEY (user_id)
                        REFERENCES users(id)
                        ON DELETE CASCADE
                        ON UPDATE CASCADE
                );
            """;


            String course = """
                CREATE TABLE course (
                    course_id SERIAL PRIMARY KEY,
                    name VARCHAR(100) NOT NULL,
                    date TIMESTAMP NOT NULL,
                    teacher_id INT,
                    FOREIGN KEY (teacher_id)
                        REFERENCES teacher(teacher_id)
                        ON DELETE SET NULL
                        ON UPDATE CASCADE
                );
            """;


            String attendance = """
                CREATE TABLE attendance (
                    attendance_id SERIAL PRIMARY KEY,
                    student_id INT NOT NULL,
                    course_id INT NOT NULL,
                    date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                    status status_enum DEFAULT 'absent',
                    verify_by VARCHAR(100),
                    FOREIGN KEY (student_id)
                        REFERENCES student(std_id)
                        ON DELETE CASCADE
                        ON UPDATE CASCADE,
                    FOREIGN KEY (course_id)
                        REFERENCES course(course_id)
                        ON DELETE CASCADE
                        ON UPDATE CASCADE
                );
            """;


            String motif = """
                CREATE TABLE motif (
                    motif_id SERIAL PRIMARY KEY,
                    type motif_type_enum NOT NULL,
                    description VARCHAR(255),
                    certified BOOLEAN DEFAULT FALSE,
                    student_id INT,
                    FOREIGN KEY (student_id)
                        REFERENCES student(std_id)
                        ON DELETE CASCADE
                        ON UPDATE CASCADE
                );
            """;


            executeSafe(statement, face, "face");
            executeSafe(statement, users, "users");
            executeSafe(statement, director, "director");
            executeSafe(statement, teacher, "teacher");
            executeSafe(statement, student, "student");
            executeSafe(statement, course, "course");
            executeSafe(statement, attendance, "attendance");
            executeSafe(statement, motif, "motif");

        } catch (SQLException e) {
            System.out.println("Database connection error: " + e.getMessage());
        }
    }

    private static void executeSafe(Statement statement, String sql, String name) {
        try {
            statement.executeUpdate(sql);
            System.out.println("Table '" + name + "' created successfully.");
        } catch (SQLException e) {
            if (e.getMessage().contains("already exists")) {
                System.out.println("Table '" + name + "' already exists.");
            } else {
                System.out.println("❌ Error creating table '" + name + "': " + e.getMessage());
            }
        }
    }
}
