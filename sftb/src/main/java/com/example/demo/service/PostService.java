package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.example.demo.service.UserService;
import com.example.demo.entity.Post;
import com.example.demo.repository.PostMapper;
import com.example.demo.dto.PostDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class PostService {
    private static final Logger logger = LoggerFactory.getLogger(PostService.class);

    @Autowired
    private PostMapper postMapper;
    
    @Autowired

    private FileService fileService;
    
    @Autowired
    private UserService userService;
    
    @Value("${file.upload-dir}")
    private String uploadDir; // 업로드 디렉토리 설정 값

 // 게시물 작성 (다중 파일 업로드 지원)
    public void createPost(Post post, List<MultipartFile> files) throws Exception {
        logger.info("Creating a new post with title: {} by userName: {} and boardId: {}", post.getTitle(), post.getUserName(), post.getBoardId());

        // 1. 게시물 저장 (postId 생성)
        postMapper.insertPost(post);

        // 2. 파일 저장 및 파일 경로 삽입
        if (files != null && !files.isEmpty()) {
            for (MultipartFile file : files) {
                String filePath = fileService.saveFile(file, post.getPostId()); // 파일 저장
                postMapper.updateFilePath(post.getPostId(), filePath); // DB에 파일 경로 업데이트
            }
        }

        logger.info("Post created with ID: {}", post.getPostId());
    }

    // 게시물 정보 저장
    public void createPost(Post post) {
        postMapper.insertPost(post);
    }

    // 게시물 목록 가져오기
    public List<Post> getAllPosts() {
        logger.info("Fetching all posts");
        List<Post> posts = postMapper.findAllPosts();
        logger.info("Fetched {} posts", posts.size());
        return posts;
    }

    // 특정 Board_ID에 따른 게시물 가져오기
    public List<Post> getPostsByBoardId(int boardId) {
        logger.info("Fetching posts for Board_ID: {}", boardId);
        List<Post> posts = postMapper.findPostsByBoardId(boardId);
        logger.info("Fetched {} posts for Board_ID: {}", posts.size(), boardId);
        return posts;
    }

 // 게시물 수정 (단일 파일 처리)
    public Post updatePost(Long postId, PostDto postDto, MultipartFile file) throws IOException {
        logger.info("Updating post with ID: {}, Title: {}, Content: {}", postId, postDto.getTitle(), postDto.getContent());

        // 1. 게시물 조회
        Post existingPost = postMapper.findPostById(postId);
        if (existingPost == null) {
            throw new IllegalArgumentException("게시물이 존재하지 않습니다.");
        }

        // 2. 게시물 내용 업데이트
        existingPost.setTitle(postDto.getTitle());
        existingPost.setContent(postDto.getContent());
        existingPost.setUpdateAt(LocalDateTime.now());

        // 3. 파일 경로 처리
        String updatedFilePath = existingPost.getFilePath(); // 기존 파일 경로 유지

        if (file != null && !file.isEmpty()) {
            // 기존 파일 삭제
            if (updatedFilePath != null) {
                fileService.deleteFile(updatedFilePath); // 기존 파일 삭제
            }

            // 새 파일 저장
            updatedFilePath = fileService.saveFile(file, postId); // 새 파일 저장
        }

        // 4. 게시물 업데이트
        existingPost.setFilePath(updatedFilePath); // 파일 경로 반영
        postMapper.updatePost(postId, existingPost.getTitle(), existingPost.getContent(), updatedFilePath);

        logger.info("Updated Post filePath: {}", updatedFilePath);
        
     // 5. 최종 게시물 반환
        return existingPost;
    }


    // 게시물 ID로 조회
    public Post getPostById(Long postId) {
        logger.info("Fetching post with ID: {}", postId);
        Post post = postMapper.findPostById(postId);
        if (post == null) {
            throw new IllegalArgumentException("게시물이 존재하지 않습니다.");
        }
        return post;
    }
    
 // 게시물 삭제
    public Post deletePost(Long postId) {
        logger.info("Deleting post with ID: {}", postId);

        // 1. 게시물 조회
        Post existingPost = postMapper.findPostById(postId);
        if (existingPost == null) {
            throw new IllegalArgumentException("게시물이 존재하지 않습니다.");
        }

        // 2. 파일 삭제
        String filePath = fileService.getFilePathById(postId);
        if (filePath != null) {
            fileService.deleteFile(filePath); // 파일 삭제
        }

        // 3. 게시물 삭제
        postMapper.deletePost(postId);
        logger.info("Post with ID: {} deleted successfully", postId);

        return existingPost;
    }

    //게시글 작성자 조회하기
    public String getPostAuthorId(Long postId) {
        // 게시글 조회
        Post post = getPostById(postId); // 기존에 작성된 메서드 사용
        return post.getUserId(); // 게시글 작성자 ID 반환
    }
        
 // 관리자 채택 관련
    public Post adoptPost(Long postId, String userId, int tierExperience) {
       logger.info("adoptPost 서비스 메서드 호출 - postId: {}, userId: {}, tierExperience: {}", postId, userId, tierExperience);
        // 게시물 조회
        Post post = postMapper.findPostById(postId);

        // 게시글이 존재하지 않으면 예외 처리
        if (post == null) {
            throw new IllegalArgumentException("Post not found.");
        }

        // 티어 경험치 부여
        userService.addTierExperience(userId, tierExperience); // 받은 티어 경험치로 업데이트

        // 게시물 채택 상태 업데이트
        post.setAdopt(true); // Adopt 상태 변경
        postMapper.updateAdoptPostStatus(postId); // Adopt 상태 업데이트
        return post; // 업데이트된 게시물 정보를 반환
    }

    // 조회수 증가
    public void incrementViewCount(Long postId) {
        postMapper.incrementViewCount(postId);
    }
    
    public List<Post> getMyPosts(String userId) {
        return postMapper.getMyPosts(userId);
    }
    
}