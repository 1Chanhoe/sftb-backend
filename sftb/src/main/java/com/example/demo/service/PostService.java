package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.demo.entity.Post;
import com.example.demo.repository.PostMapper;
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
        logger.info("Creating a new post with title: {} by userName: {}", post.getTitle(), post.getUserName());
        postMapper.insertPost(post);
        logger.info("Post created successfully with ID: {}", post.getCreSeq());
    }

    // 게시물 ID로 게시물 조회
    public Post getPostById(Long creSeq) {
        logger.info("Fetching post with ID: {}", creSeq);
        Post post = postMapper.findById(creSeq);
        if (post != null) {
            logger.info("Post found with ID: {}", creSeq);
        } else {
            logger.warn("No post found with ID: {}", creSeq);
        }
        return post;
    }

    // 회원 이름으로 작성된 모든 게시물 조회
    public List<Post> getPostsByUserName(String userName) {
        logger.info("Fetching posts for userName: {}", userName);
        List<Post> posts = postMapper.findByUserName(userName);
        if (posts != null && !posts.isEmpty()) {
            logger.info("Found {} posts for userName: {}", posts.size(), userName);
        } else {
            logger.warn("No posts found for userName: {}", userName);
        }
        return posts;
    }

    // 게시물 수정
    public boolean updatePost(Post post) {
        logger.info("Updating post with ID: {}", post.getCreSeq());
        Post existingPost = postMapper.findById(post.getCreSeq());
        if (existingPost != null) {
            postMapper.updatePost(post);
            logger.info("Post updated successfully with ID: {}", post.getCreSeq());
            return true;
        } else {
            logger.warn("No post found with ID: {}", post.getCreSeq());
            return false;
        }
    }

    // 게시물 삭제
    public boolean deletePost(Long creSeq) {
        logger.info("Deleting post with ID: {}", creSeq);
        Post existingPost = postMapper.findById(creSeq);
        if (existingPost != null) {
            postMapper.deletePost(creSeq);
            logger.info("Post deleted successfully with ID: {}", creSeq);
            return true;
        } else {
            logger.warn("No post found with ID: {}", creSeq);
            return false;
        }
    }
}
