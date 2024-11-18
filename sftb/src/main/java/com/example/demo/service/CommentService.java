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
    
    @Autowired
    private UserService userService; // 사용자 서비스

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
            String authorId = comment.getUserId();
            String authorTier = userService.getUserTier(authorId);
            comment.setAuthorTier(authorTier);
            
            for (Comment reply : replies) {
                String replyAuthorId = reply.getUserId(); // 대댓글의 작성자 ID
                String replyAuthorTier = userService.getUserTier(replyAuthorId); // 사용자 티어 가져오기
                reply.setAuthorTier(replyAuthorTier); // 대댓글에 티어 설정
            }
        }
        
        return rootComments;
    }
    
    // 댓글 ID로 댓글 조회
    public Comment getCommentById(Long commentId) {
        Comment comment = commentMapper.findCommentById(commentId);
        return comment; // 댓글 반환
    }
    //댓글 추가 메소드
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
    	 // 대댓글 먼저 삭제
        commentMapper.deleteRepliesByParentId(commentId);
        // 본 댓글 삭제
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
    
    //댓글 채택
    public Comment adoptComment(Long commentId, String userId, int tierExperience) {
        // 댓글 조회 (CommentMapper 사용)
        Comment comment = commentMapper.findCommentById(commentId);
        
        if (comment == null) {
            throw new RuntimeException("댓글을 찾을 수 없습니다.");
        }

        // 티어 경험치 부여
        userService.addTierExperience(userId, tierExperience); // 받은 티어 경험치로 업데이트

        // 댓글 채택 상태 업데이트
        comment.setAdopt(true); // Adopt 상태 변경
        commentMapper.updateAdoptStatus(commentId); // Adopt 상태 업데이트

        return comment; // 업데이트된 댓글 정보를 반환
    }
    
    public List<Comment> getMyComments(String userId) {
        return commentMapper.getMyComments(userId);
    }
    
}