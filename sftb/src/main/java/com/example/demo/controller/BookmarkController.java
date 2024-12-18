package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.service.BookmarkService;

@RestController
@RequestMapping("/api/posts")
public class BookmarkController {

    @Autowired
    private BookmarkService bookmarkService;
    
    @GetMapping("/{postId}/bookmarks")
    public ResponseEntity<Boolean> isBookmarked(@PathVariable("postId") Long postId, @RequestParam("userId") String userId) {
        boolean isBookmarked = bookmarkService.isBookmarked(userId, postId);
        return ResponseEntity.ok(isBookmarked);
    }
    
    @PostMapping("/{postId}/bookmarks")
    public ResponseEntity<Void> toggleBookmark(@PathVariable("postId") Long postId, @RequestParam("userId") String userId) {
        bookmarkService.toggleBookmark(userId, postId);
        return ResponseEntity.ok().build();
    }
    
    @GetMapping("/user/bookmarks")
    public List<Long> getUserBookmarks(@RequestParam("userId") String userId) {
        return bookmarkService.getUserBookmarks(userId);
    }
}