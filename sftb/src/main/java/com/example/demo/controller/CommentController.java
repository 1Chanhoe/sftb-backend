package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.demo.service.CommentService;
import com.example.demo.service.PostService;

import java.util.List;
import com.example.demo.entity.Comment;
import com.example.demo.dto.CommentRequest; // DTO 클래스 임포트
import com.example.demo.dto.CommentResponseDto;


@RestController
@RequestMapping("/api/comments")
public class CommentController {

    @Autowired
    private CommentService commentService;
    
    @Autowired
    private PostService postService; // PostService 인스턴스 주입
    
    @GetMapping("/{postId}")
    public ResponseEntity<List<Comment>> getComments(@PathVariable("postId") Long postId) {
        List<Comment> comments = commentService.getCommentsByPostId(postId);
        return ResponseEntity.ok(comments);
    }
    
    //댓글 추가 API
    @PostMapping
    public ResponseEntity<CommentResponseDto> createComment(@RequestBody CommentRequest commentRequest) {
    	Comment comment = commentRequest.toComment();
    	commentService.addComment(comment);
    	// 게시글의 작성자 ID 가져오기
        String postAuthorId = postService.getPostAuthorId(commentRequest.getPostId());
        CommentResponseDto response = new CommentResponseDto(comment, postAuthorId);
        return ResponseEntity.ok(response);
    }
    
    //댓글 수정 API
    @PutMapping("/{commentId}")
    public ResponseEntity<Comment> updateComment(@PathVariable("commentId") Long commentId, @RequestBody CommentRequest commentRequest) {
        Comment updatedComment = commentService.updateComment(commentId, commentRequest.getContent());
        return ResponseEntity.ok(updatedComment); // 수정된 댓글 객체 반환
    }
    
    // 댓글 삭제 API String으로 반환하는이유는 간단한 게시글작성자ID만 필요하고 추가적인 정보는 필요하지않으므로 String이 적절하다
    @DeleteMapping("/{commentId}")
    public ResponseEntity<String> deleteComment(@PathVariable("commentId") Long commentId) {
    	Comment comment = commentService.getCommentById(commentId);
    	String postAuthorId = postService.getPostAuthorId(comment.getPostId()); // 댓글에 대한 게시글 ID가 필요합니다.
        
        commentService.deleteComment(commentId);
        return ResponseEntity.ok(postAuthorId);
    }
    
    // 대댓글 수정 API
    @PutMapping("/replies/{replyId}")
    public ResponseEntity<Comment> updateReply(@PathVariable("replyId") Long replyId, @RequestBody CommentRequest commentRequest) {
        Comment updatedReply = commentService.updateReply(replyId, commentRequest.getContent());
        return ResponseEntity.ok(updatedReply); // 수정된 대댓글을 반환
    }

    // 대댓글 삭제 API
    @DeleteMapping("/replies/{replyId}")
    public ResponseEntity<String> deleteReply(@PathVariable("replyId") Long replyId) {
    	Comment replyComment = commentService.getCommentById(replyId);
    	String postAuthorId = postService.getPostAuthorId(replyComment.getPostId());
        commentService.deleteComment(replyId);
        return ResponseEntity.ok(postAuthorId);
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
    
    //특정 유저의 댓글에서 게시글ID 가져오기
    @GetMapping("/mine")
    public List<Comment> getMyComments(@RequestParam("userId") String userId) {
        return commentService.getMyComments(userId);
    }
    
}