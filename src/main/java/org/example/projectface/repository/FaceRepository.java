package org.example.projectface.repository;

import org.example.projectface.database.ConnectionToDataBase;
import org.example.projectface.entity.Face;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class FaceRepository implements CrudOperator<Face> {
    private ConnectionToDataBase datasource = new ConnectionToDataBase();

    @Override
    public Face saveAll(Face face) {
     String sql = "insert into face (name) values (?)";

     try(Connection conn = datasource.getConnection();
         PreparedStatement statement = conn.prepareStatement(sql)){
         statement.setString(1,face.getName());
         statement.execute();
     } catch (Exception e) {
         throw new RuntimeException(e);
     }
     return face;
    }
}
