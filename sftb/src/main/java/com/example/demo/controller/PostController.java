package com.example.demo.controller;

import com.example.demo.entity.Post;
import com.example.demo.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.example.demo.dto.PostRequest;
import com.example.demo.dto.PostDto;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/posts")
public class PostController {
    
    private static final Logger logger = LoggerFactory.getLogger(PostController.class);

    @Autowired
    private PostService postService;

    // 게시물 작성
    @PostMapping
    public ResponseEntity<?> createPost(
        @RequestParam("title") String title,
        @RequestParam("content") String content,
        @RequestParam("userName") String userName,
        @RequestParam("boardId") Integer boardId,
        @RequestParam(value = "file", required = false) MultipartFile file // 파일 받기
    ) {
        Post post = new Post();
        post.setTitle(title);
        post.setUserName(userName);
        post.setContent(content);
        post.setBoardId(boardId);
        post.setViewCount(0);

        // 파일이 있는 경우 저장
        if (file != null && !file.isEmpty()) {
            try {
                String fileName = file.getOriginalFilename();
                Path filePath = Paths.get("uploads/" + fileName);
                Files.copy(file.getInputStream(), filePath);

                // 파일 경로 설정
                post.setFilePath(filePath.toString());
            } catch (Exception e) {
                e.printStackTrace();
                return ResponseEntity.status(500).body("파일 업로드에 실패했습니다.");
            }
        }

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
    public ResponseEntity<PostDto> updatePost(
        @PathVariable("postId") Long postId,
        @RequestBody PostDto postDto
    ) {
        logger.info("Received update request for postId: {} with title: {} and content: {}", postId, postDto.getTitle(), postDto.getContent());
        
        boolean isUpdated = postService.updatePost(postId, postDto);
        
        if (isUpdated) {
            // 수정된 게시글을 다시 조회하여 최신 데이터를 클라이언트에 반환
            Post updatedPost = postService.getPostById(postId);
            PostDto updatedPostDto = new PostDto();
            updatedPostDto.setTitle(updatedPost.getTitle());
            updatedPostDto.setContent(updatedPost.getContent());
            updatedPostDto.setPostId(updatedPost.getPostId());
            updatedPostDto.setUpdateAt(updatedPost.getUpdateAt());

            return ResponseEntity.ok(updatedPostDto); // 수정된 게시글 정보 반환
        } else {	
            return ResponseEntity.status(404).body(null); // Post not found
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
