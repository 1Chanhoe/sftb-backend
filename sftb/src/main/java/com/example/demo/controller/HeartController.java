package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.example.demo.service.HeartService;
import com.example.demo.entity.Heart;

@RestController
@RequestMapping("/api/posts")
public class HeartController {
	
	 @Autowired
	    private HeartService heartService;
	 
	@GetMapping("{postId}/hearts")
	public ResponseEntity<Boolean> checkHeart(@PathVariable("postId") Long postId, @RequestParam("userId") String userId) {
	    boolean isHearted = heartService.isHearted(postId, userId);
	    return ResponseEntity.ok(isHearted);
	}
	
	@PostMapping("/{postId}/hearts/toggle")
    public ResponseEntity<Void> toggleHeart(@PathVariable("postId") Long postId, @RequestBody Heart heart) {
        heart.setPostId(postId); // 게시글 ID 설정
        heartService.toggleHeart(heart);
        return ResponseEntity.ok().build();
    }

}
