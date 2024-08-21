package com.example.demo.service;
import org.springframework.beans.factory.annotation.Autowired; //Spring Autowired 어노테이션
//import org.springframework.security.crypto.password.PasswordEncoder; //Spring Security 패스워드 인코더
import org.springframework.stereotype.Service;
import com.example.demo.entity.User;
import com.example.demo.repository.UserMapper;

@Service
public class UserService {
    @Autowired //의존성 주입
    private UserMapper userMapper;

//    @Autowired //의존성 주입
//    private PasswordEncoder passwordEncoder;

    public void saveUser(User user) { //User객체를 데베에 저장
    	
//        user.setPassword(passwordEncoder.encode(user.getPassword())); //비번 암호화
        userMapper.insertUser(user); //데베에 사용자 삽입
    }
}