package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.service.UserService;

@RestController
@RequestMapping("/api/experience")
public class ExperiencePointsController {

    @Autowired
    private UserService userService;

    @PostMapping("/assign")
    public ResponseEntity<Integer> assignExperiencePoints(@RequestBody UserRegistrationDto userDto) {
        int experiencePoints = 0;
        // 신규 회원 가입 시 경험치 부여
        if (userService.isNewUser(userDto)) {
            experiencePoints = (int) (userService.getTotalExperiencePoints() * 0.4);
        }
        
        userService.addExperiencePoints(userDto.getUserId(), experiencePoints);
        return ResponseEntity.ok(experiencePoints);
    }
}
