package com.example.demo.controller;

import com.example.demo.dto.CommentRequest;
import com.example.demo.dto.ResetPasswordRequest;

import com.example.demo.entity.User;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.apache.coyote.BadRequestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.List;
import com.example.demo.dto.UserExperienceUpdateRequest;
import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping("/api/auth")
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;
    
    @PostMapping("/logout")
    public ResponseEntity<?> logoutUser(HttpServletRequest request, HttpServletResponse response) {
        // 세션 무효화
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        // JSESSIONID 쿠키 삭제
        Cookie cookie = new Cookie("JSESSIONID", null);
        cookie.setPath("/"); // 쿠키 경로 설정
        cookie.setHttpOnly(true); // 보안 설정
        cookie.setMaxAge(0); // 쿠키 만료 시간 0으로 설정 (즉시 삭제)
        response.addCookie(cookie);
        return ResponseEntity.ok("Logged out successfully");

    }

  
    @PostMapping("/SignUp")
    public ResponseEntity<?> registerUser(@RequestBody User user) {

        try {
            userService.signUp(user);
            return ResponseEntity.ok("User registered successfully");
        } catch (BadRequestException e) {
            logger.warn("Failed to register user: {}", e.getMessage());
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("message", e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }

    }
    

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody User loginRequest) {
        logger.info("Attempting to log in with userID: {}", loginRequest.getUserID());
        User user = userService.loginUser(loginRequest.getUserID(), loginRequest.getPassword()); // 서비스에서 로그인 처리

        if (user != null) {
            logger.info("Login successful for userID: {}", user.getUserID());
            return ResponseEntity.ok(user); // 성공 시 사용자 정보 반환
        } else {
            logger.warn("Login failed for userID: {}", loginRequest.getUserID());
            return ResponseEntity.status(401).body("Invalid username or password"); // 실패 시 401 상태 코드 반환
        }
    }

    // 사용자 ID 찾기 요청을 처리하는 메서드
    @PostMapping("/find-id")
    public ResponseEntity<?> findUserId(@RequestBody User findIdRequest) {
        logger.info("Attempting to find user ID for userName: {}, email: {}", findIdRequest.getUserName(), findIdRequest.getEmail());
        String userId = userService.findUserId(findIdRequest.getUserName(), findIdRequest.getEmail()); // 서비스에서 ID 찾기 처리

        if (userId != null) {
            logger.info("Found user ID: {}", userId);
            return ResponseEntity.ok(userId); // 성공 시 사용자 ID 반환
        } else {
            logger.warn("Failed to find user ID for userName: {}, email: {}", findIdRequest.getUserName(), findIdRequest.getEmail());
            return ResponseEntity.status(404).body("User ID not found"); // 실패 시 404 상태 코드 반환
        }
    }

    // 비밀번호 찾기 요청을 처리하는 메서드
    @PostMapping("/find-password")
    public ResponseEntity<?> findPassword(@RequestBody User findPasswordRequest) {
        logger.info("Attempting to find password for userName: {}, email: {}, userId: {}", 
                    findPasswordRequest.getUserName(), findPasswordRequest.getEmail(), findPasswordRequest.getUserID());

        String password = userService.findPassword(findPasswordRequest.getUserName(), 
                                                   findPasswordRequest.getEmail(), 
                                                   findPasswordRequest.getUserID()); // 서비스에서 비밀번호 찾기 처리

        if (password != null) {
            logger.info("Found password for userId: {}", findPasswordRequest.getUserID());
            return ResponseEntity.ok(password); // 성공 시 비밀번호 반환
        } else {
            logger.warn("Failed to find password for userName: {}, email: {}, userId: {}", 
                        findPasswordRequest.getUserName(), findPasswordRequest.getEmail(), findPasswordRequest.getUserID());
            return ResponseEntity.status(404).body("Password not found"); // 실패 시 404 상태 코드 반환
        }
    }
    

    // 비밀번호 재설정 요청을 처리하는 메서드
    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody ResetPasswordRequest request) {
        logger.info("Attempting to reset password for userID: {}", request.getUserID());

        boolean success = userService.resetPassword(request.getUserID(), request.getNewPassword()); // 서비스에서 비밀번호 재설정 처리
        if (success) {
            return ResponseEntity.ok("Password successfully reset."); // 성공 시 메시지 반환
        } else {
            logger.warn("Failed to reset password for userID: {}", request.getUserID());
            return ResponseEntity.status(400).body("Failed to reset password."); // 실패 시 400 상태 코드 반환
        }
    }
    
    // 신규 회원 상태 업데이트 메서드
    @PutMapping("/users/{userID}/newmember") 
    public ResponseEntity<?> updateNewMemberStatus(@PathVariable("userID") String userID) {
        logger.info("Updating new member status for userID: {}", userID);
        
        try {
            userService.updateNewMemberStatus(userID, false); // 사용자 상태를 false로 업데이트
            return ResponseEntity.ok("New member status updated successfully."); // 성공 메시지 반환
        } catch (Exception e) {
            logger.error("Failed to update new member status for userID: {}", userID, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to update new member status."); // 실패 메시지 반환
        }
    }
    
    
    @PutMapping("/users/{userID}/experience")
    public ResponseEntity<?> updateUserLevelExperience(
        @PathVariable("userID") String userID,
        @RequestBody Map<String, Integer> request) {

        logger.info("Updating experience points for userID: {}", userID);
        
        try {
            int userLevelExperience = request.get("userLevelExperience");
            userService.updateUserLevelExperience(userID, userLevelExperience);
            return ResponseEntity.ok("Experience points updated successfully."); // 성공 메시지 반환
        } catch (Exception e) {
            logger.error("Failed to update experience points for userID: {}", userID, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to update experience points."); // 실패 메시지 반환
        }
    }
    
    @GetMapping("/users/{userID}/experience")
    public ResponseEntity<?> getUserExperience(@PathVariable("userID") String userID) {
        try {
            int userLevelExperience = userService.getUserLevelExperience(userID);
            int tierExperience = userService.getTierExperience(userID); // 티어 경험치 가져오기
            int userLevel = userService.getUserLevel(userID);
            Map<String, Integer> experienceData = new HashMap<>();
            experienceData.put("userLevelExperience", userLevelExperience);
            experienceData.put("tierExperience", tierExperience);
            experienceData.put("userLevel", userLevel);
            return ResponseEntity.ok(experienceData);
        } catch (Exception e) {
            logger.error("Failed to fetch experience points for userID: {}", userID, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Failed to fetch experience points.");
        }
    }


    
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<Map<String, String>> handleBadRequest(BadRequestException ex) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("message", ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    
    @PutMapping("/experience")
    public ResponseEntity<User> updateExperience(@RequestBody UserExperienceUpdateRequest userLevelExperience) {
        userService.updateUserLevelExperience(userLevelExperience.getUserId(), userLevelExperience.getUserLevelExperience());
        return ResponseEntity.ok().build();
    }
    
    @PutMapping("/tier-experience")
    public ResponseEntity<String> updateTierExperience(@RequestBody CommentRequest commentRequest) {
        // CommentRequest에서 사용자 ID와 경험치 업데이트 값을 가져옴
        userService.addTierExperience(commentRequest.getUserId(), commentRequest.getTierExperience());
        return ResponseEntity.ok("티어 경험치가 성공적으로 업데이트되었습니다.");
    }
    
    @GetMapping("/token")
    public ResponseEntity<?> getUserToken(@RequestParam("userId") String userId) {
        logger.info("Fetching token count for userID: {}", userId);

        try {
            int tokenCount = userService.getUserTokenCount(userId); // 서비스 메서드 호출
            return ResponseEntity.ok(tokenCount);
        } catch (Exception e) {
            logger.error("Failed to fetch token count for userID: {}", userId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to fetch token count.");
        }
    }
    
    @GetMapping("/users")
    public ResponseEntity<List<User>> getUsers() {
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }
}


