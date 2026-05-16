package com.example.ecommerce.service;

import com.example.ecommerce.exception.BadRequestException;
import com.example.ecommerce.model.User;
import com.example.ecommerce.repository.UserRepository;
import com.example.ecommerce.util.PasswordValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // Register User
    public User registerUser(User user) {
        // Validate password strength
        if (!PasswordValidator.isValid(user.getPassword())) {
            throw new BadRequestException(PasswordValidator.getValidationError());
        }

        // Check if username already exists
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new BadRequestException("username already exited, create new one");
        }

        // Check if email already exists
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new BadRequestException("email already exited, create new one");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
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
            if (passwordEncoder.matches(password, user.getPassword())) {
                return user;
            }
            if (user.getPassword().equals(password)) {
                user.setPassword(passwordEncoder.encode(password));
                userRepository.save(user);
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
            if (passwordEncoder.matches(password, user.getPassword())) {
                return user;
            }
            if (user.getPassword().equals(password)) {
                user.setPassword(passwordEncoder.encode(password));
                userRepository.save(user);
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
