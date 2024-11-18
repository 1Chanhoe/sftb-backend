package com.example.demo.service;

import com.example.demo.entity.User;
import com.example.demo.repository.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StoreService {

    @Autowired
    private UserMapper userMapper;  // UserMapper를 주입받아 사용

    // 상품 구매 처리
    public boolean processPurchase(String userId, int price) {
        // 사용자의 현재 토큰 수 조회
        int currentToken = userMapper.getUserTokenCount(userId);
        
        // 충분한 토큰이 있는지 확인
        if (currentToken >= price) {
            // 토큰 차감
            userMapper.updateToken(userId, -price);
            return true;  // 구매 성공
        } else {
            return false;  // 토큰 부족
        }
    }
}
