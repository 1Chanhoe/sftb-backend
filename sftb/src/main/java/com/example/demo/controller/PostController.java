package com.example.demo.controller;

import com.example.demo.entity.Post;
import com.example.demo.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.example.demo.dto.PostRequest;
import com.example.demo.dto.PostDto;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.springframework.http.HttpStatus;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/posts")
public class PostController {
    
    private static final Logger logger = LoggerFactory.getLogger(PostController.class);

    @Autowired
    private PostService postService;

 // 게시물 작성 (사진 파일 첨부 가능)
    @PostMapping

    public ResponseEntity<?> createPost(@RequestBody PostRequest postRequest) 
    {
        Post post = new Post();
        post.setTitle(postRequest.getTitle());
        post.setUserName(postRequest.getUserName()); // Member_ID를 userName으로 사용
        post.setContent(postRequest.getContent());
        post.setBoardId(postRequest.getBoardId()); // boardId 설정
        post.setViewCount(0); // 초기 조회수 설정
        post.setUserId(postRequest.getUserId());
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
    public ResponseEntity<Post> updatePost(
        @PathVariable("postId") Long postId,
        @RequestBody PostDto postDto
    ) {
        // 게시글 수정 서비스 호출
        Post updatedPost = postService.updatePost(postId, postDto);
        
        if (updatedPost != null) {
            return ResponseEntity.ok(updatedPost); // 수정된 게시글 객체 반환
        } else {    
            return ResponseEntity.status(404).body(null); // 게시글이 존재하지 않음
        }
    }
    
 // 게시물 삭제
    @DeleteMapping("/{postId}")
    public ResponseEntity<?> deletePost(@PathVariable("postId") Long postId) {
        logger.info("Received delete request for postId: {}", postId);
        
        // 삭제된 게시물 객체를 반환하도록 수정
        Post deletedPost = postService.deletePost(postId);
        
        if (deletedPost != null) {
            return ResponseEntity.ok(deletedPost); // 삭제된 게시물 반환
        } else {
            return ResponseEntity.status(404).body("게시물을 찾을 수 없습니다."); // 삭제 실패 메시지
        }
    }
    

  //특정 게시물의 세부사항 가져오기
    @GetMapping("/{postId}")
    public ResponseEntity<Post> getPostById(@PathVariable("postId") Long postId) {
        Post post = postService.getPostById(postId);
        return ResponseEntity.ok(post);
    }
    
 // 게시물 채택
    @PutMapping("/{postId}/adopt")
    public ResponseEntity<Post> adoptPost(@PathVariable("postId") Long postId, @RequestBody PostRequest postRequest) { 
        try {
            // 게시물 채택 처리 및 업데이트된 게시물 정보 가져오기
            Post updatedPost = postService.adoptPost(postId, postRequest.getUserId(), postRequest.getTierExperience());
            return ResponseEntity.ok(updatedPost); // 업데이트된 게시물 정보를 반환
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null); // 오류 발생 시 null 반환
        }
    }



}
