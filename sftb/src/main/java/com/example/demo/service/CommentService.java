package com.example.demo.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.demo.repository.CommentMapper;
import com.example.demo.entity.Comment;
import java.util.List;
import java.time.LocalDateTime;

@Service
public class CommentService {

    @Autowired
    private CommentMapper commentMapper;

    public List<Comment> getCommentsByPostId(Long postId) {
        // 루트 댓글 가져오기
        List<Comment> rootComments = commentMapper.findRootCommentsByPostId(postId);
        
        // 각 루트 댓글에 대한 대댓글 가져오기
        for (Comment comment : rootComments) {
            List<Comment> replies = commentMapper.findRepliesByParentId(postId, comment.getCommentId());
            comment.setReplies(replies);
        }
        return rootComments;
    }

    public void addComment(Comment comment) {
        comment.setCreatedAt(LocalDateTime.now());
        comment.setUpdatedAt(LocalDateTime.now());
        commentMapper.insertComment(comment);
    }
}