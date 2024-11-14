package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.entity.Bookmark;
import com.example.demo.repository.BookmarkMapper;

@Service
public class BookmarkService {

    @Autowired
    private BookmarkMapper bookmarkMapper;

    public void toggleBookmark(String userId, Long postId) {
        Bookmark bookmark = bookmarkMapper.findByUserIdAndPostId(userId, postId);
        
        if (bookmark != null) {
            // 북마크가 존재하면 제거
            bookmarkMapper.deleteBookmark(userId, postId);
        } else {
            // 북마크가 없으면 추가
            Bookmark newBookmark = new Bookmark();
            newBookmark.setUserId(userId);
            newBookmark.setPostId(postId);
            bookmarkMapper.insertBookmark(newBookmark);
        }
    }
    
    public boolean isBookmarked(String userId, Long postId) {
        Bookmark bookmark = bookmarkMapper.findByUserIdAndPostId(userId, postId);
        return bookmark != null;
    }
}
