package com.example.demo.controller;

import com.example.demo.entity.Post;
import com.example.demo.service.FileService;
import com.example.demo.service.PostService;
import com.example.demo.service.UserService;

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

import java.io.IOException;
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
    
    @Autowired
    private FileService fileService;
    
 // 게시물 작성 (사진 파일 첨부 가능)
    @PostMapping(consumes = "multipart/form-data")
    public ResponseEntity<?> createPost(
            @ModelAttribute PostRequest postRequest,
            @RequestParam(value = "file", required = false) MultipartFile file) throws IOException
    {
    	Post post = postRequest.toPost();

            // 파일이 존재하는 경우에만 파일 저장
            if (file != null && !file.isEmpty()) {
                String filePath = fileService.saveFile(file); // 파일 저장
                post.setFilePath(filePath); // 저장된 파일 경로 설정
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
     
    // 게시글 수정 API
    @PutMapping(value = "/{postId}", consumes = "multipart/form-data")
    public ResponseEntity<Post> updatePost(
        @PathVariable("postId") Long postId,
        @ModelAttribute PostDto postDto,  // PostDto 필드가 개별 파트로 전달됨
        @RequestPart(value = "file", required = false) MultipartFile file) throws IOException
    {
        // 게시글 수정 서비스 호출
        Post updatedPost = postService.updatePost(postId, postDto, file);
        
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
    
}
