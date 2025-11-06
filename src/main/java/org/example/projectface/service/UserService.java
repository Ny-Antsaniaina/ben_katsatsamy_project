package org.example.projectface.service;

import org.example.projectface.entity.Role;
import org.example.projectface.entity.User;
import org.example.projectface.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepository = new UserRepository();

    public User addUser(String name , Role role) {
        User user = new User();
        user.setName(name);
        user.setRole(role);
        int id = userRepository.saveAll(user);
        user.setId(id);
        return user;
    }
}
