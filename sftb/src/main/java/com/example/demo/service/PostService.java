package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.demo.entity.Post;
import com.example.demo.repository.PostMapper;
import com.example.demo.dto.PostDto;
import java.time.LocalDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@Service
public class PostService {
    private static final Logger logger = LoggerFactory.getLogger(PostService.class);

    @Autowired
    private PostMapper postMapper;

    // 게시물 작성
    public void createPost(Post post) {
        logger.info("Creating a new post with title: {} by userName: {} and boardId: {}", post.getTitle(), post.getUserName(), post.getBoardId());
        postMapper.insertPost(post);
        logger.info("Post created successfully with Post_ID: {}", post.getPostId());
    }

    // 게시물 목록 가져오기
    public List<Post> getAllPosts() {
        logger.info("Fetching all posts");
        List<Post> posts = postMapper.findAllPosts();  // 데이터베이스에서 모든 게시물을 가져와 posts 리스트에 저장
        logger.info("Fetched {} posts", posts.size()); // 현재 가져온 게시물의 수
        return posts;
    }

    // 게시물 수정
    public boolean updatePost(Long postId, PostDto postDto) {
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

        // 수정된 게시글을 DB에 저장
        postMapper.updatePost(postId, postDto.getTitle(), postDto.getContent(), existingPost.getUpdateAt());

        return true; // 성공 시 true 반환
    }
    
    // 게시물 ID로 조회
    public Post getPostById(Long postId) {
        logger.info("Fetching post with ID: {}", postId);
        Post post = postMapper.findPostById(postId);
        if (post == null) {
            throw new IllegalArgumentException("게시물이 존재하지 않습니다.");
        }
        return post; // 게시글 반환
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
        
        return true; // 성공 시 true 반환
    }


}
