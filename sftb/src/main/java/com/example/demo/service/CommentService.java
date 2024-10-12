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
        if (rootComments.isEmpty()) {
            System.out.println("루트 댓글이 없습니다.");
        }
        // 각 루트 댓글에 대한 대댓글 가져오기
        for (Comment comment : rootComments) {
            List<Comment> replies = commentMapper.findRepliesByParentId(postId, comment.getCommentId());
            comment.setReplies(replies);
        }
        return rootComments;
    }

    public void addComment(Comment comment) {
        comment.setCreatedAt(LocalDateTime.now());
        if (comment.getParentSeq() != null) {
            comment.setParentSeq(comment.getParentSeq());
        } else {
            comment.setParentSeq(null); // 루트 댓글인 경우
        }
        commentMapper.insertComment(comment);
    }
    
    // 댓글 수정 메소드
    public Comment updateComment(Long commentId, String content) {
        commentMapper.updateComment(commentId, content);
        // 수정된 댓글을 가져오기 위해 댓글 ID를 사용하여 다시 조회
        return commentMapper.findCommentById(commentId);
    }

    // 댓글 삭제 메소드
    public void deleteComment(Long commentId) {
        commentMapper.deleteComment(commentId);
    }
    
    // 대댓글 수정 메소드
    public Comment updateReply(Long replyId, String content) {
        commentMapper.updateComment(replyId, content);
        return commentMapper.findCommentById(replyId); // 수정된 대댓글을 반환
    }

    // 대댓글 삭제 메소드
    public void deleteReply(Long replyId) {
        commentMapper.deleteComment(replyId);
    }
}