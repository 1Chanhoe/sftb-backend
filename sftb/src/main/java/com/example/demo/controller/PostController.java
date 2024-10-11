package com.example.demo.controller;

import com.example.demo.entity.Post;

import com.example.demo.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.demo.dto.PostRequest;
import com.example.demo.dto.PostDto;
import java.util.List;

import java.util.Map; // Map 클래스 import 추가
import java.util.HashMap;  // 추가된 부분

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/posts")
public class PostController {
	
	 private static final Logger logger = LoggerFactory.getLogger(PostController.class); // Logger 초기화

    @Autowired
    private PostService postService;

    // 게시물 작성
    @PostMapping
    public ResponseEntity<?> createPost(@RequestBody PostRequest postRequest) {
        Post post = new Post();
        post.setTitle(postRequest.getTitle());
        post.setUserName(postRequest.getUserName()); // Member_ID를 userName으로 사용
        post.setContent(postRequest.getContent());
        post.setBoardId(postRequest.getBoardId()); // boardId 설정
        post.setViewCount(0); // 초기 조회수 설정

        postService.createPost(post);

        return ResponseEntity.ok(post);
    }

    // 게시물 목록 가져오기 (Board_ID로 필터링)
    @GetMapping
    public ResponseEntity<List<Post>> getPostsByBoardId(@RequestParam(value = "boardId", required = false) Integer boardId) {
        List<Post> posts;

        if (boardId != null) {
            posts = postService.getPostsByBoardId(boardId); // 특정 Board_ID에 따른 게시물 가져오기
        } else {
            posts = postService.getAllPosts(); // boardId가 없으면 모든 게시물 가져오기
        }

        return ResponseEntity.ok(posts);
    }

 // 게시물 하트 수 증가/감소
    @PostMapping("/{postId}/hearts")
    public ResponseEntity<?> updateHeartCount(@PathVariable("postId") Long postId, @RequestBody Map<String, Boolean> requestBody) {
        Boolean heart = requestBody.get("heart"); // 클라이언트에서 보낸 하트 상태

        if (heart != null && heart) {
            // 하트가 눌린 상태 -> 하트 증가
            postService.incrementHeartCount(postId);
        } else {
            // 하트가 취소된 상태 -> 하트 감소
            postService.decrementHeartCount(postId);
        }

        return ResponseEntity.ok().build(); // 성공 응답 반환
    }
 // 게시물 하트 수 조회
    @GetMapping("/{postId}/hearts")
    public ResponseEntity<Map<String, Integer>> getHeartCount(@PathVariable("postId") Long postId) {
        int heartCount = postService.getHeartCount(postId); // 서비스에서 하트 수 조회
        Map<String, Integer> response = new HashMap<>();
        response.put("heartCount", heartCount);
        return ResponseEntity.ok(response);
    }

    
    // 게시글 수정 API
    @PutMapping("/{postId}")
    public ResponseEntity<String> updatePost(
        @PathVariable("postId") Long postId,
        @RequestBody PostDto postDto
    ) {
        logger.info("Received update request for postId: {} with title: {} and content: {}", postId, postDto.getTitle(), postDto.getContent());
        boolean isUpdated = postService.updatePost(postId, postDto);
        if (isUpdated) {
            return ResponseEntity.ok("Post updated successfully");
        } else {
            return ResponseEntity.status(404).body("Post not found");
        }
    }

    

   

}
