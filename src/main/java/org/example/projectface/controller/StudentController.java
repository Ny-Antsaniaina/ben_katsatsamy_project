package org.example.projectface.controller;

import org.apache.commons.imaging.ImageReadException;
import org.example.projectface.entity.Role;
import org.example.projectface.entity.Student;
import org.example.projectface.entity.User;
import org.example.projectface.service.FaceService;
import org.example.projectface.service.StudentRegister;
import org.example.projectface.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/student")
public class StudentController {


    private final StudentRegister studentRegister;

    private final FaceService faceService ;

    public StudentController(StudentRegister studentRegister, FaceService faceService) {
        this.studentRegister = studentRegister;
        this.faceService = faceService;
    }


    @PostMapping("/register")
    public ResponseEntity<Student> insert(@RequestParam("name") String name, @RequestParam("role") Role role, @RequestParam("file") MultipartFile file) {
        try {
            Student student = studentRegister.insertStudent(file, name, role);
            return new ResponseEntity<>(student, HttpStatus.OK);
        } catch (Exception e) {
            throw new RuntimeException("Error in insert student", e);
        }
    }

    @PostMapping("/photo")
    public ResponseEntity<String> upload(@RequestParam("file") MultipartFile file) {
        try{
            boolean faceAccepted = faceService.recognizingFace(file);
            if (faceAccepted) {
                return ResponseEntity.ok("face accepted");
            }else  {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("face not accepted");
            }
        } catch (IOException | ImageReadException e) {
            throw new RuntimeException(e);
        }
    }

}
