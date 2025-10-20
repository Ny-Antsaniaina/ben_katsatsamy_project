package org.example.projectface.service;

import org.example.projectface.entity.Face;
import org.example.projectface.entity.Role;
import org.example.projectface.entity.Student;
import org.example.projectface.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class StudentRegister {
    @Autowired
    private UserService userService;
    @Autowired
    private FaceService faceService ;
    @Autowired
    private StudentService studentService;

    public Student insertStudent(MultipartFile file , String name , Role role){
        try {
            User user = userService.addUser(name, role);

            Face face = faceService.addFace(file , user);

            Student student = new Student();
            student.setUser(user);
            student.setFace(face);
            studentService.addStudent(student);
            return  student;

        }catch (Exception e){
            throw new RuntimeException("Error in insert student" , e);
        }
    }
}
