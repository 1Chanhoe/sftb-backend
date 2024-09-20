package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired; //Spring Autowired 어노테이션

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder; //Spring Security 패스워드 인코더
import org.springframework.stereotype.Service;
import com.example.demo.entity.User;
import com.example.demo.repository.UserMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class UserService {
   private static final Logger logger = LoggerFactory.getLogger(UserService.class); // Logger 객체 생성
    @Autowired //의존성 주입
    private UserMapper userMapper;

    @Autowired //의존성 주입
    private PasswordEncoder passwordEncoder;

    public void saveUser(User user) { //User객체를 데베에 저장
       
        user.setPassword(passwordEncoder.encode(user.getPassword())); //비번 암호화
        userMapper.insertUser(user); //데베에 사용자 삽입
    }
    // 로그인 메서드
    public User loginUser(String userID, String password) {
        // 아이디로 사용자 조회
       logger.info("Attempting to log in with userID: {} and password: {}", userID, password);
        User user = userMapper.findByUserId(userID);
        
        // 사용자 정보가 존재하고 비밀번호가 일치하는지 확인
        if (user != null) {
            logger.info("User found: {}", user.getUserID());
            if (passwordEncoder.matches(password, user.getPassword())) {
                logger.info("Password match for userID: {}", userID);
                return user; // 로그인 성공, 사용자 정보를 반환
            } else {
                logger.warn("Password mismatch for userID: {}", userID);
            }
        } else {
            logger.warn("No user found with userID: {}", userID);
        }

        return null; // 로그인 실패, null 반환
    }
    
    public String findUserId(String userName, String email) {
        logger.info("Attempting to find user ID for userName: {}, email: {}", userName, email);
        User user = userMapper.findByNameAndEmail(userName, email);
        if (user != null) {
            logger.info("Found user: {}", user.getUserID());
            return user.getUserID();
        } else {
            logger.warn("Failed to find user ID for name: {}, email: {}", userName, email);
            return null;
        }
    }
    
    public String findPassword(String userName, String email, String userId) {
        logger.info("Attempting to find password for userName: {}, email: {}, userId: {}", userName, email, userId);

        User user = userMapper.findByNameEmailAndId(userName, email, userId);
        if (user != null) {
            logger.info("Found password: {}", user.getPassword());
            return user.getPassword();
        } else {
            logger.warn("Failed to find password for name: {}, email: {}, userId: {}", userName, email, userId);
            return null;
        }
    }
    
 // 비밀번호 변경 메서드
    public boolean resetPassword(String userID, String newPassword) {
        User user = userMapper.findByUserId(userID);
        if (user != null) {
            user.setPassword(passwordEncoder.encode(newPassword));
            userMapper.updateUser(user); // 비밀번호 업데이트 메서드 호출
            return true;
        }
        return false;
    }
}
