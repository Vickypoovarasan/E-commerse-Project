package com.example.ecommerce.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.ecommerce.model.ApiResponse;
import com.example.ecommerce.model.User;
import com.example.ecommerce.service.UserService;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
public class UserController {

    @Autowired
    private UserService userService;

    // ✅ REGISTER
    @PostMapping("/register")
    public ResponseEntity<ApiResponse> registerUser(@RequestBody User user) {
        User savedUser = userService.registerUser(user);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse("User registered successfully", savedUser));
    }

    // ✅ LOGIN (NO JWT ✅)
    @PostMapping("/login")
    public ResponseEntity<ApiResponse> loginUser(@RequestParam("username") String username,
                                                 @RequestParam String password) {

        User user = userService.loginUser(username, password);
        String message = user.getRole() == null || user.getRole().name().equals("CUSTOMER")
                ? "login successfully"
                : "admin login successfully";

        return ResponseEntity.ok(new ApiResponse(message, user));
    }

    // ✅ GET USER BY ID
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getUser(@PathVariable Long id) {
        Optional<User> user = userService.getUserById(id);
        return user.map(value -> ResponseEntity.ok(new ApiResponse("User found", value)))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse("User not found")));
    }
}
