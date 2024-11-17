package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.entity.Heart;
import com.example.demo.repository.HeartMapper;
import com.example.demo.repository.PostMapper;

@Service
public class HeartService {

    @Autowired
    private HeartMapper heartMapper;
    
    public boolean isHearted(Long postId, String userId) {
    	return heartMapper.isHearted(postId, userId);
    }

    public void toggleHeart(Heart heart) {
        Long postId = heart.getPostId();
        String userId = heart.getUserId();

        if (isHearted(postId, userId)) {
            // 하트를 취소할 경우
            heartMapper.removeHeart(userId, postId);
            heartMapper.decrementHeartCount(postId); // 하트 수 감소
        } else {
            // 하트를 추가할 경우
            heartMapper.addHeart(userId, postId);
            heartMapper.incrementHeartCount(postId); // 하트 수 증가
        }
    }
}