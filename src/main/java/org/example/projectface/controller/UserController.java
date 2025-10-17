package org.example.projectface.controller;

import org.example.projectface.entity.Face;
import org.example.projectface.entity.User;
import org.example.projectface.repository.FaceRepository;
import org.example.projectface.service.FaceService;
import org.example.projectface.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.function.EntityResponse;

@RestController
@RequestMapping("/user")
public class UserController {
    private final UserService userService = new UserService();
    private final FaceService faceService = new FaceService();

    @PostMapping(value = "/add" , consumes = {"application/json" ,"multipart/form-data"})
    public ResponseEntity<User> insert(@RequestBody(required = false) @ModelAttribute User user){
        return ResponseEntity.ok(userService.addUser(user));
    }
    @PostMapping(value = "/face" , consumes = {"multipart/form-data" , "application/json"})
    public Face insertFace(@RequestBody(required = false) @ModelAttribute Face face){
        return faceService.addFace(face);
    }
}
