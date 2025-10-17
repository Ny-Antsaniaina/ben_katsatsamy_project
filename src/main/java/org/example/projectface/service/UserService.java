package org.example.projectface.service;

import org.example.projectface.entity.User;
import org.example.projectface.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepository = new UserRepository();

    public User addUser(User user) {
        return userRepository.saveAll(user);
    }


}
