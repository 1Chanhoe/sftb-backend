package com.example.demo.controller;

import com.example.demo.entity.Post;
import com.example.demo.service.FileService;
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

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/posts")
public class PostController {
    
    private static final Logger logger = LoggerFactory.getLogger(PostController.class);

    @Autowired
    private PostService postService;
    
    @Autowired
    private FileService fileService;

 // 게시물 작성 (사진 파일 첨부 가능)
    @PostMapping

    public ResponseEntity<?> createPost( //매개변수들
        @RequestParam("title") String title,
        @RequestParam("content") String content,
        @RequestParam("userName") String userName,
        @RequestParam("boardId") Integer boardId,
        @RequestParam("userId") String userId,
        @RequestParam(value = "file", required = false) MultipartFile file) { // 첨부 파일을 받는 매개변수
        
    	 // Post 객체 생성 및 설정
    	Post post = new Post();
        post.setTitle(title);
        post.setContent(content);
        post.setUserName(userName);
        post.setBoardId(boardId);
        post.setUserId(userId);

        try {
        	// 파일이 존재하는 경우에만 파일 저장
        	if (file != null && !file.isEmpty()) {
        		
        		String filePath = fileService.saveFile(file); // 파일 저장
                post.setFilePath(filePath); // 저장된 파일 경로를 설정
                }
            
            
        	// PostService의 createPost 메서드를 호출하여 게시물 생성
            postService.createPost(post, file);
            return ResponseEntity.ok(post); // 성공 시 생성된 게시물을 반환
        } catch (Exception e) {
            return ResponseEntity.status(500).body("게시물 작성 중 오류가 발생했습니다.");
        }

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
        
        boolean isDeleted = postService.deletePost(postId);
        
        if (isDeleted) {
            return ResponseEntity.ok("게시물이 삭제되었습니다."); // 삭제 성공 메시지 반환
        } else {
            return ResponseEntity.status(404).body("게시물을 찾을 수 없습니다."); // 삭제 실패 메시지
        }
    }

}
