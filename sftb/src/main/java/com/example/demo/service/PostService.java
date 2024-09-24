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
        logger.info("Post created successfully with CreSeq: {}", post.getCreSeq());
    }

    // 게시물 목록 가져오기
    public List<Post> getAllPosts() {
        logger.info("axios all posts");
        List<Post> posts = postMapper.findAllPosts();     //데이터베이스에서 모든 게시물을 가져와 posts 리스트에 저장
        logger.info("axios {} posts", posts.size());      //현재 가져온 게시물의 수
        return posts;
    }
}
