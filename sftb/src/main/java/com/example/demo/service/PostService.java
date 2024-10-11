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

    // 특정 Board_ID에 따른 게시물 가져오기
    public List<Post> getPostsByBoardId(int boardId) {
        logger.info("Fetching posts for Board_ID: {}", boardId);
        List<Post> posts = postMapper.findPostsByBoardId(boardId); // 특정 Board_ID에 따른 게시물 가져오기
        logger.info("Fetched {} posts for Board_ID: {}", posts.size(), boardId); // 현재 가져온 게시물의 수
        return posts;
    }

    // 하트 수 증가
    public void incrementHeartCount(Long postId) {
        logger.info("Incrementing heart count for Post_ID: {}", postId);
        postMapper.incrementHeartCount(postId); // 하트 수를 증가시키는 메서드 호출
        logger.info("Heart count incremented for Post_ID: {}", postId);
    }
    public void decrementHeartCount(Long postId) {
        postMapper.decrementHeartCount(postId); // 하트 수 감소 로직 추가
    }
    //하트 갯수 가져오기
    public int getHeartCount(Long postId) {
        return postMapper.findHeartCountByPostId(postId);
    }
}