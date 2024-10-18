package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.demo.service.CommentService;
import java.util.List;
import com.example.demo.entity.Comment;
import com.example.demo.dto.CommentRequest; // DTO 클래스 임포트


@RestController
@RequestMapping("/api/comments")
public class CommentController {

    @Autowired
    private CommentService commentService;
    
    @GetMapping("/{postId}")
    public ResponseEntity<List<Comment>> getComments(@PathVariable("postId") Long postId) {
        List<Comment> comments = commentService.getCommentsByPostId(postId);
        return ResponseEntity.ok(comments);
    }

    @PostMapping
    public ResponseEntity<Void> createComment(@RequestBody CommentRequest commentRequest) {
        commentService.addComment(commentRequest.toComment()); // DTO를 Comment로 변환
        return ResponseEntity.ok().build();
    }
    
    //댓글 수정 API
    @PutMapping("/{commentId}")
    public ResponseEntity<Comment> updateComment(@PathVariable("commentId") Long commentId, @RequestBody CommentRequest commentRequest) {
        Comment updatedComment = commentService.updateComment(commentId, commentRequest.getContent());
        return ResponseEntity.ok(updatedComment); // 수정된 댓글 객체 반환
    }
    
    // 댓글 삭제 API
    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable("commentId") Long commentId) {
        commentService.deleteComment(commentId);
        return ResponseEntity.ok().build();
    }
    
    // 대댓글 수정 API
    @PutMapping("/replies/{replyId}")
    public ResponseEntity<Comment> updateReply(@PathVariable("replyId") Long replyId, @RequestBody CommentRequest commentRequest) {
        Comment updatedReply = commentService.updateReply(replyId, commentRequest.getContent());
        return ResponseEntity.ok(updatedReply); // 수정된 대댓글을 반환
    }

    // 대댓글 삭제 API
    @DeleteMapping("/replies/{replyId}")
    public ResponseEntity<Void> deleteReply(@PathVariable("replyId") Long replyId) {
        commentService.deleteComment(replyId);
        return ResponseEntity.ok().build();
    }
    
    //댓글 채택시 경험치 추가 API
    @PostMapping("/{commentId}/adopt")
    public ResponseEntity<Comment> adoptComment(@PathVariable("commentId") Long commentId, @RequestBody CommentRequest commentRequest) {
        try {
            // 댓글 채택 처리 및 업데이트된 댓글 정보 가져오기
            Comment updatedComment = commentService.adoptComment(commentId, commentRequest.getUserId(), commentRequest.getTierExperience());
            return ResponseEntity.ok(updatedComment); // 업데이트된 댓글 정보를 반환
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null); // 오류 발생 시 null 반환
        }
    }
}