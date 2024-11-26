package com.example.demo.controller;

import com.example.demo.entity.Post;
import com.example.demo.service.FileService;
import com.example.demo.service.PostService;
import com.example.demo.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
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

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/posts")
public class PostController {
    
    private static final Logger logger = LoggerFactory.getLogger(PostController.class);

    @Autowired
    private PostService postService;
    
    @Autowired
    private FileService fileService;
    
 // 게시물 작성
    @PostMapping(consumes = "multipart/form-data")
    public ResponseEntity<?> createPost(
            @ModelAttribute PostRequest postRequest,
            @RequestParam(value = "file", required = false) MultipartFile file) throws IOException {

        Post post = postRequest.toPost();

        try {
            // 게시물 저장
            postService.createPost(post, file != null ? List.of(file) : null); // 단일 파일 처리
            return ResponseEntity.ok(post);
        } catch (Exception e) {
            logger.error("Error creating post", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("게시물 작성 중 오류가 발생했습니다.");
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
     
 // 게시물 수정
    @PutMapping(value = "/{postId}", consumes = "multipart/form-data")
    public ResponseEntity<Post> updatePost(
            @PathVariable("postId") Long postId,
            @ModelAttribute PostDto postDto,
            @RequestParam(value = "file", required = false) MultipartFile file) {

    	try {
            // 게시물 수정 및 파일 처리
            Post updatedPost = postService.updatePost(postId, postDto, file);

            // 성공적으로 수정된 게시물을 반환
            return ResponseEntity.ok(updatedPost);
        } catch (IllegalArgumentException e) {
            logger.error("Post not found: {}", postId, e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (Exception e) {
            logger.error("Error updating post", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
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
            logger.info("adoptPost 호출 - postId: {}, userId: {}, tierExperience: {}", 
                     postId, postRequest.getUserId(), postRequest.getTierExperience());
            // 게시물 채택 처리 및 업데이트된 게시물 정보 가져오기
            Post updatedPost = postService.adoptPost(postId, postRequest.getUserId(), postRequest.getTierExperience());
            return ResponseEntity.ok(updatedPost); // 업데이트된 게시물 정보를 반환
        } catch (Exception e) {
           logger.error("게시물 채택 중 오류 발생 - postId: {}, userId: {}, tierExperience: {}", 
                    postId, postRequest.getUserId(), postRequest.getTierExperience(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null); // 오류 발생 시 null 반환
        }
    }

    // 조회수 증가
    @PostMapping("/{postId}/incrementViewCount")
    public void incrementViewCount(@PathVariable("postId") Long postId) {
        postService.incrementViewCount(postId);
    }
    
    //특정 사용자의 게시글 정보 가져오기
    @GetMapping("/myposts")
    public List<Post> getMyPosts(@RequestParam("userId") String userId) {
        return postService.getMyPosts(userId);
    }
    @GetMapping("/{postId}/file")
    public ResponseEntity<Resource> downloadFile(@PathVariable("postId") Long postId) {
        try {
            // 게시물 ID로 파일 경로 가져오기
            String filePath = fileService.getFilePathById(postId);
            if (filePath == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }

            // 리소스 로드
            Resource resource = fileService.loadFileAsResource(filePath);

            if (resource == null || !resource.exists()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }

            // 다운로드 응답 생성
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + "\"")
                    .body(resource);
        } catch (Exception e) {
            logger.error("Error downloading file", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    
}
