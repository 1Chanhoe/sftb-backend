package com.example.demo.controller;

import com.example.demo.dto.PostRequest;
import com.example.demo.entity.Post;
import com.example.demo.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/posts")
public class PostController {

    @Autowired
    private PostService postService;

    // 게시물 작성
    @PostMapping
    public ResponseEntity<?> createPost(@RequestBody PostRequest postRequest) {
        Post post = new Post();
        post.setTitle(postRequest.getTitle());
        post.setUserName(postRequest.getUserName()); // memberId를 userName으로 변경
        post.setContent(postRequest.getContent());

        postService.createPost(post);

        return ResponseEntity.ok(post);
    }

    // 게시물 조회 (ID로)
    @GetMapping("/{creSeq}")
    public ResponseEntity<?> getPostById(@PathVariable Long creSeq) {
        Post post = postService.getPostById(creSeq);
        if (post != null) {
            return ResponseEntity.ok(post);
        } else {
            return ResponseEntity.status(404).body("Post not found");
        }
    }

    // 회원 ID로 작성된 모든 게시물 조회
    @GetMapping("/member/{userName}")
    public ResponseEntity<?> getPostsByUserName(@PathVariable String userName) {
        List<Post> posts = postService.getPostsByUserName(userName); // 메서드 이름도 userName으로 변경
        if (posts != null && !posts.isEmpty()) {
            return ResponseEntity.ok(posts);
        } else {
            return ResponseEntity.status(404).body("No posts found for this user");
        }
    }

    // 게시물 수정
    @PutMapping("/{creSeq}")
    public ResponseEntity<?> updatePost(@PathVariable Long creSeq, @RequestBody PostRequest postRequest) {
        Post post = new Post();
        post.setCreSeq(creSeq);
        post.setTitle(postRequest.getTitle());
        post.setContent(postRequest.getContent());

        boolean updated = postService.updatePost(post);
        if (updated) {
            return ResponseEntity.ok(post);
        } else {
            return ResponseEntity.status(404).body("Post not found");
        }
    }

    // 게시물 삭제
    @DeleteMapping("/{creSeq}")
    public ResponseEntity<?> deletePost(@PathVariable Long creSeq) {
        boolean deleted = postService.deletePost(creSeq);
        if (deleted) {
            return ResponseEntity.ok("Post successfully deleted");
        } else {
            return ResponseEntity.status(404).body("Post not found");
        }
    }
}
