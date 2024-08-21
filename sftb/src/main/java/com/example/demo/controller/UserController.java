package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired; // Spring Autowired 어노테이션
import org.springframework.http.ResponseEntity; // Spring ResponseEntity 클래스
import org.springframework.web.bind.annotation.*; // Spring Web 어노테이션
import com.example.demo.entity.User; // User 엔티티 클래스 임포트
import com.example.demo.service.UserService; // UserService 클래스 임포트

@RestController // Spring RestController로 선언
@RequestMapping("/api/auth") // 요청 매핑
public class UserController { // UserController 클래스 선언
    @Autowired // 의존성 주입
    private UserService userService;

    @PostMapping("/SignUp") // POST 요청 매핑
    public ResponseEntity<?> registerUser(@RequestBody User user) { // registerUser 메서드 선언
    	
    	userService.saveUser(user); // 사용자 저장
        return ResponseEntity.ok(user); // 응답 반환
    }
    
    @GetMapping("/SignUp")
    public ResponseEntity<String> getSignUpPage() {
        return ResponseEntity.ok("SignUp page");
    }
}