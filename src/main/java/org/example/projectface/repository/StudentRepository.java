package org.example.projectface.repository;

import org.example.projectface.database.ConnectionToDataBase;
import org.example.projectface.entity.Student;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@Repository
public class StudentRepository implements CrudOperator<Student> {
    private ConnectionToDataBase datasource =  new ConnectionToDataBase();

    @Override
    public Student saveAll(Student student) {
        String sql = "Insert into student (face_id , user_id) values (?, ?)";

        try(Connection conn = datasource.getConnection();
            PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setInt(1, student.getFace().getId());
            statement.setInt(2, student.getUser().getId());
            statement.execute();

        } catch (SQLException e) {
            throw new  RuntimeException("Error while inserting student" , e);
        }
        return student;
    }
}
