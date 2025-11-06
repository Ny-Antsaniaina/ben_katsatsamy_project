package org.example.projectface.repository;

import org.example.projectface.database.ConnectionToDataBase;
import org.example.projectface.entity.Face;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
@Repository
public class FaceRepository {
    private final ConnectionToDataBase datasource = new ConnectionToDataBase();

    public int saveAll(Face face) {
     String sql = "insert into face (name) values (?) RETURNING face_id";

     try(Connection conn = datasource.getConnection();
         PreparedStatement statement = conn.prepareStatement(sql)){
         statement.setString(1,face.getName());
         ResultSet rs = statement.executeQuery();
         if(rs.next()){
             return rs.getInt("face_id");
         }
     } catch (Exception e) {
         throw new RuntimeException(e);
     }
     return 0;
    }
}
