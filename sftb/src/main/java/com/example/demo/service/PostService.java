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

    // 게시물 작성 (파일 업로드 추가)
    public void createPost(Post post, MultipartFile file) throws Exception {
        logger.info("Creating a new post with title: {} by userName: {} and boardId: {}", post.getTitle(), post.getUserName(), post.getBoardId());
        
        // 파일이 있는 경우 처리
        if (file != null && !file.isEmpty()) {
            try {
                String filePath = fileService.saveFile(file); // 변환된 파일 경로 반환
                post.setFilePath(filePath);
                logger.info("File saved at: {}", filePath);
            } catch (IOException e) {
                logger.error("File saving failed", e);
                throw new Exception("File saving failed", e);
            	}
        	}
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

    // 게시물 수정
    public Post updatePost(Long postId, PostDto postDto, MultipartFile file) {
    	logger.info("Updating post with ID: {}, Title: {}, Content: {}", postId, postDto.getTitle(), postDto.getContent());

        // 해당 ID의 게시글을 찾음
        Post existingPost = postMapper.findPostById(postId);
        if (existingPost == null) {
            throw new IllegalArgumentException("게시물이 존재하지 않습니다.");
        }

        // PostDto에서 받은 데이터를 Post 엔티티에 직접 업데이트
        existingPost.setTitle(postDto.getTitle());
        existingPost.setContent(postDto.getContent());
        existingPost.setUpdateAt(LocalDateTime.now());
        
        // 파일 삭제 요청 플래그가 있는 경우 처리
        if (postDto.isDeleteFile() && existingPost.getFilePath() != null) { // 삭제 요청 플래그 체크
            fileService.deleteFile(existingPost.getFilePath()); // 기존 파일 삭제
            existingPost.setFilePath(null); // 파일 경로 제거
            logger.info("Deleted file at path: {}", existingPost.getFilePath());
        }
        
        // 파일이 새로 업로드된 경우 처리
        if (file != null && !file.isEmpty()) {
            // 기존 파일이 있는 경우 삭제
            if (existingPost.getFilePath() != null && !existingPost.getFilePath().isEmpty()) {
                fileService.deleteFile(existingPost.getFilePath()); // 기존 파일 삭제
                logger.info("Deleted old file at path: {}", existingPost.getFilePath());
            }

            // 새 파일 저장
            try {
                String newFilePath = fileService.saveFile(file); // 파일을 저장하고 경로를 얻음
                existingPost.setFilePath(newFilePath); // 새 파일 경로 설정
            } catch (IOException e) {
                logger.error("파일 저장 중 오류 발생:", e);
                throw new RuntimeException("파일 저장에 실패했습니다.", e);
            }
        } else if (postDto.getFilePath() != null) {
            // 새 파일이 없으면 기존 파일 경로 유지
            existingPost.setFilePath(postDto.getFilePath());
        }
        // 수정된 게시글을 DB에 저장
        postMapper.updatePost(postId, existingPost.getTitle(), existingPost.getContent(), existingPost.getFilePath());
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
    
    // 게시글 삭제
    public Post deletePost(Long postId) {
        // 해당 ID의 게시글을 찾음
        Post existingPost = postMapper.findPostById(postId);
        if (existingPost == null) {
            throw new IllegalArgumentException("게시물이 존재하지 않습니다."); // 게시물이 없으면 예외 발생
        }
        
        // 첨부된 파일이 있는 경우 파일 삭제
        String filePath = existingPost.getFilePath(); // 기존 게시물에서 파일 경로를 가져옴
        if (filePath != null && !filePath.isEmpty()) {
            fileService.deleteFile(filePath); // 파일 서비스에서 파일 삭제 메서드 호출하여 파일 삭제
            logger.info("File at path: {} deleted successfully", filePath);
        }
       
        postMapper.deletePost(postId); // PostMapper에서 postId에 해당하는 게시물 삭제
        logger.info("Post with ID: {} deleted successfully", postId);

        // 삭제된 게시물 객체 반환
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

}
