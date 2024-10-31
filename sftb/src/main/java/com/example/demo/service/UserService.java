package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.demo.entity.User;
import com.example.demo.repository.UserMapper;
import org.apache.coyote.BadRequestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;
    
    public UserService(UserMapper userMapper) {
        this.userMapper = userMapper;
    }
    
    // 회원가입 메서드 (ID, 학번, 이메일 중복 확인 로직 MyBatis 사용)
    @Transactional
    public void signUp(User user) throws BadRequestException {
        StringBuilder errorMessage = new StringBuilder();
        boolean hasError = false;

        // 학번 중복 확인
        if (userMapper.existsByStudentID(user.getStudentID())) {
            errorMessage.append("Student ID already exists. ");
            hasError = true;
        }

        // ID 중복 확인
        if (userMapper.existsByUserID(user.getUserID())) {
            errorMessage.append("User ID already exists. ");
            hasError = true;
        }

        // 이메일 중복 확인
        if (userMapper.existsByEmail(user.getEmail())) {
            errorMessage.append("Email already exists. ");
            hasError = true;
        }

        // 오류가 있는 경우 예외 발생
        if (hasError) {
            logger.warn("Failed to register user: {}", errorMessage.toString().trim());
            throw new BadRequestException(errorMessage.toString().trim());
        }

        // 비밀번호 암호화
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // 신규 회원 여부 설정
        user.setNewMember(true); // 신규 회원이면 true로 설정

        // MyBatis를 사용하여 사용자 저장
        userMapper.insertUser(user);
        logger.info("User signed up successfully with userID: {}", user.getUserID());
    }

    // 로그인 메서드 (MyBatis 사용)
    public User loginUser(String userID, String password) {
        logger.info("Attempting to log in with userID: {}", userID);
        User user = userMapper.findByUserId(userID);

        if (user != null) {
            logger.info("User found: {}", user.getUserID());
            if (passwordEncoder.matches(password, user.getPassword())) {
                logger.info("Password match for userID: {}", userID);
                return user;
            } else {
                logger.warn("Password mismatch for userID: {}", userID);
            }
        } else {
            logger.warn("No user found with userID: {}", userID);
        }

        return null;
    }

    // 사용자 ID 찾기 메서드 (MyBatis 사용)
    public String findUserId(String userName, String email) {
        logger.info("Attempting to find user ID for userName: {}, email: {}", userName, email);
        User user = userMapper.findByNameAndEmail(userName, email);
        if (user != null) {
            logger.info("Found user: {}", user.getUserID());
            return user.getUserID();
        } else {
            logger.warn("Failed to find user ID for userName: {}, email: {}", userName, email);
            return null;
        }
    }

    // 비밀번호 찾기 메서드 (MyBatis 사용)
    public String findPassword(String userName, String email, String userId) {
        logger.info("Attempting to find password for userName: {}, email: {}, userId: {}", userName, email, userId);

        User user = userMapper.findByNameEmailAndId(userName, email, userId);
        if (user != null) {
            logger.info("Found password: {}", user.getPassword());
            return user.getPassword();
        } else {
            logger.warn("Failed to find password for userName: {}, email: {}, userId: {}", userName, email, userId);
            return null;
        }
    }

    // 비밀번호 변경 메서드 (MyBatis 사용)
    public boolean resetPassword(String userID, String newPassword) {
        User user = userMapper.findByUserId(userID);
        if (user != null) {
            user.setPassword(passwordEncoder.encode(newPassword));
            userMapper.updateUser(user);
            logger.info("Password reset successful for userID: {}", userID);
            return true;
        }
        logger.warn("Failed to reset password for userID: {}", userID);
        return false;
    }

    
    // 유저 티어 경험치 추가 메서드
    @Transactional
    public void addTierExperience(String userID, int experience) {
        logger.info("Adding {} tier experience to userID: {}", experience, userID);
        userMapper.updateUserExperience(userID, experience);
        logger.info("Tier experience added successfully for userID: {}", userID);
    }
    

    // 유저 레벨 경험치 추가 메서드
    @Transactional
    public void updateUserLevelExperience(String userId, int experience) {
        // 현재 유저 레벨 경험치 가져오기
        int currentLevelExperience = userMapper.getUserLevelExperience(userId);
        
        // 새로운 유저 레벨 경험치 계산
        int newLevelExperience = experience;


        // 유저 레벨 경험치가 100 이상일 경우 처리
        if (currentLevelExperience + newLevelExperience >= 100) {
            newLevelExperience -= 100; // 100을 초과하는 부분만 남기고 초기화
            userMapper.updateUserLevel(userId); // UserLevel 필드 +1 증가
        }
        userMapper.updateUserLevelExperience(userId, newLevelExperience);
    }


    // 신규 회원 상태 업데이트 메서드
    public void updateNewMemberStatus(String userID, boolean newMember) {
        int newMemberValue = newMember ? 1 : 0; // boolean 값을 int로 변환
        logger.info("Attempting to update new member status to {} for userID: {}", newMemberValue, userID);
        userMapper.updateNewMember(userID, newMemberValue); // newMember 상태 업데이트
        logger.info("New member status updated for userID: {}", userID);
    }
    
    // 사용자 경험치 가져오기 메서드
    public int getUserLevelExperience(String userID) {

        User user = userMapper.findByUserId(userID);
        if (user != null) {
            return user.getUserLevelExperience();
        } else {
            logger.warn("User not found for userID: {}", userID);
            throw new RuntimeException("User not found");
        }
    }
    
    public int getTierExperience(String userID) {
        Integer tierExperience = userMapper.getTierExperience(userID);
        if (tierExperience != null) {
            return tierExperience;
        } else {
            logger.warn("Tier experience not found for userID: {}", userID);
            throw new RuntimeException("Tier experience not found for user");
        }
    }
    
    

}