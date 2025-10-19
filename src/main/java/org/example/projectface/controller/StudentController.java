package org.example.projectface.controller;

import org.example.projectface.entity.Role;
import org.example.projectface.entity.Student;
import org.example.projectface.entity.User;
import org.example.projectface.service.FaceService;
import org.example.projectface.service.StudentRegister;
import org.example.projectface.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/student")
public class StudentController {
    private final StudentRegister studentRegister = new StudentRegister();

    @PostMapping("/register")
    public ResponseEntity<Student> insert(@RequestParam("name") String name, @RequestParam("role") Role role, @RequestParam("file") MultipartFile file) {
        try {
            Student student = studentRegister.insertStudent(file, name, role);
            return new ResponseEntity<>(student, HttpStatus.OK);
        } catch (Exception e) {
            throw new RuntimeException("Error in insert student", e);
        }
    }

}
