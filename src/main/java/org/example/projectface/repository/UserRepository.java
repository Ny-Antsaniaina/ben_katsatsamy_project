package org.example.projectface.repository;

import org.example.projectface.database.ConnectionToDataBase;
import org.example.projectface.entity.User;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
@Repository
public class UserRepository  {
    private final ConnectionToDataBase dataSource = new ConnectionToDataBase();

    public int saveAll(User user) {
        String sql = "Insert into users (name , role) values (?, cast(?  as role_enum)) returning id on conflict do nothing";
        try(Connection conn = dataSource.getConnection();
            PreparedStatement statement = conn.prepareStatement(sql)){
            statement.setString(1,user.getName());
            statement.setString(2,user.getRole().toString());
            ResultSet rs = statement.executeQuery();
            if(rs.next()){
                return rs.getInt("id");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error inserting users into database." , e);
        }
        return 0;
    }
}
