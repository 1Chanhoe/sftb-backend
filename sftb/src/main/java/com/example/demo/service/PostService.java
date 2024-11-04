package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.demo.entity.Post;
import com.example.demo.repository.PostMapper;
import com.example.demo.dto.PostDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

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

    // 게시물 작성 (파일 업로드 추가)
    public void createPost(Post post, MultipartFile file) throws Exception {
        logger.info("Creating a new post with title: {} by userName: {} and boardId: {}", post.getTitle(), post.getUserName(), post.getBoardId());

        // 파일이 있는 경우 처리
        if (file != null && !file.isEmpty()) {
            String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            Path filePath = Paths.get("uploads/" + fileName);
            Files.createDirectories(filePath.getParent()); // 디렉토리 생성
            Files.write(filePath, file.getBytes()); // 파일 저장
            post.setFilePath(filePath.toString()); // 파일 경로 설정
            logger.info("File saved at: {}", filePath.toString());
        }

        postMapper.insertPost(post);
        logger.info("Post created successfully with Post_ID: {}", post.getPostId());
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

    // 하트 수 증가
    public void incrementHeartCount(Long postId) {
        logger.info("Incrementing heart count for Post_ID: {}", postId);
        postMapper.incrementHeartCount(postId);
        logger.info("Heart count incremented for Post_ID: {}", postId);
    }

    // 하트 수 감소
    public void decrementHeartCount(Long postId) {
        postMapper.decrementHeartCount(postId);
    }

    // 하트 갯수 가져오기
    public int getHeartCount(Long postId) {
        return postMapper.findHeartCountByPostId(postId);
    }

    // 게시물 수정
    public Post updatePost(Long postId, PostDto postDto) {
        logger.info("Updating post with ID: {}", postId);

        // 해당 ID의 게시글을 찾음
        Post existingPost = postMapper.findPostById(postId);
        if (existingPost == null) {
            throw new IllegalArgumentException("게시물이 존재하지 않습니다.");
        }

        // PostDto에서 받은 데이터를 Post 엔티티에 직접 업데이트
        existingPost.setTitle(postDto.getTitle());
        existingPost.setContent(postDto.getContent());
        existingPost.setUpdateAt(LocalDateTime.now());
        existingPost.setFilePath(postDto.getFilePath()); // 파일 경로 설정

        // 수정된 게시글을 DB에 저장
        postMapper.updatePost(postId, postDto.getTitle(), postDto.getContent(), postDto.getFilePath());

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
    public boolean deletePost(Long postId) {
        logger.info("Deleting post with ID: {}", postId);

        // 해당 ID의 게시글을 찾음
        Post existingPost = postMapper.findPostById(postId);
        if (existingPost == null) {
            throw new IllegalArgumentException("게시물이 존재하지 않습니다.");
        }

        // 게시물 삭제
        postMapper.deletePost(postId);
        logger.info("Post with ID: {} deleted successfully", postId);

        return true;
    }
}
