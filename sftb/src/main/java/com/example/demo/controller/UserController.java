package com.example.demo.controller;

import com.example.demo.dto.ResetPasswordRequest; // DTO 클래스 임포트
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.demo.entity.User;
import com.example.demo.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



@RestController
@RequestMapping("/api/auth")
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    @Autowired
    private UserService userService;

    @PostMapping("/SignUp")
    public ResponseEntity<?> registerUser(@RequestBody User user) {
        userService.saveUser(user);
        return ResponseEntity.ok(user);
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody User loginRequest) {
        logger.info("Attempting to log in with userID: {}", loginRequest.getUserID());
        User user = userService.loginUser(loginRequest.getUserID(), loginRequest.getPassword());
        if (user != null) {
            System.out.println("Login response: success");
            logger.info("Login successful for userID: {}", user.getUserID());
            return ResponseEntity.ok(user);
        } else {
            logger.warn("Login failed for userID: {}", loginRequest.getUserID());
            return ResponseEntity.status(401).body("Invalid username or password");
        }
    }

    @PostMapping("/find-id")
    public ResponseEntity<?> findUserId(@RequestBody User findIdRequest) {
        logger.info("Attempting to find user ID for userName: {}, email: {}", findIdRequest.getUserName(), findIdRequest.getEmail());
        String userName = userService.findUserId(findIdRequest.getUserName(), findIdRequest.getEmail());
        if (userName != null) {
            logger.info("Found useName: {}", userName);
            return ResponseEntity.ok(userName);
        } else {
            logger.warn("Failed to find user ID for name: {}, email: {}", findIdRequest.getUserName(), findIdRequest.getEmail());
            return ResponseEntity.status(404).body("User ID not found");
        }
    }

    @PostMapping("/find-password")
    public ResponseEntity<?> findPassword(@RequestBody User findPasswordRequest) {
        logger.info("Attempting to find password for userName: {}, email: {}, userId: {}", findPasswordRequest.getUserName(), findPasswordRequest.getEmail(), findPasswordRequest.getUserID());

        String password = userService.findPassword(findPasswordRequest.getUserName(), findPasswordRequest.getEmail(), findPasswordRequest.getUserID());
        if (password != null) {
            logger.info("Found password: {}", password);
            return ResponseEntity.ok(password);
        } else {
            logger.warn("Failed to find password for name: {}, email: {}, userId: {}", findPasswordRequest.getUserName(), findPasswordRequest.getEmail(), findPasswordRequest.getUserID());
            return ResponseEntity.status(404).body("Password not found");
        }
    }
    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody ResetPasswordRequest request) {
        logger.info("Attempting to reset password for userID: {}", request.getUserID());

        boolean success = userService.resetPassword(request.getUserID(), request.getNewPassword());
        if (success) {
            return ResponseEntity.ok("Password successfully reset.");
        } else {
            return ResponseEntity.status(400).body("Failed to reset password.");
        }
    }
}