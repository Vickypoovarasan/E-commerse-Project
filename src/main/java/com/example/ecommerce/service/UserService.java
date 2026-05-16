package com.example.ecommerce.service;

import com.example.ecommerce.exception.BadRequestException;
import com.example.ecommerce.model.User;
import com.example.ecommerce.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    // Register User
    public User registerUser(User user) {
        return userRepository.save(user);
    }

    // Login User
    public User loginUser(String login, String password) {

        Optional<User> userOptional = userRepository.findByUsername(login);

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            if (!user.getUsername().equals(login)) {
                throw new BadRequestException("invalid username or email");
            }
            if (user.getPassword().equals(password)) {
                return user;
            }
            throw new BadRequestException("username and password mismatch");
        }

        userOptional = userRepository.findByEmail(login);

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            if (!user.getEmail().equals(login)) {
                throw new BadRequestException("invalid username or email");
            }
            if (user.getPassword().equals(password)) {
                return user;
            }
            throw new BadRequestException("email and password mismatch");
        }

        throw new BadRequestException("invalid username or email");
    }

    // Get User by ID
    public Optional<User> getUserById(Long userId) {
        return userRepository.findById(userId);
    }
}
