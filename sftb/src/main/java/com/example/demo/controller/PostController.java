package com.example.demo.controller;

import com.example.demo.entity.Post;
import com.example.demo.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.demo.dto.PostRequest;
import com.example.demo.dto.PostDto;
import java.util.List;
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

    // 게시물 목록 가져오기
    @GetMapping
    public ResponseEntity<List<Post>> getAllPosts() {
        List<Post> posts = postService.getAllPosts(); // 게시물 목록 가져오기
        return ResponseEntity.ok(posts);
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
