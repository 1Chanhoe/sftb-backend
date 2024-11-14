package com.example.demo.repository;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.example.demo.entity.Bookmark;

@Mapper
public interface BookmarkMapper {
    // 사용자 ID와 게시물 ID로 북마크 조회
    @Select("SELECT * FROM bookmarks WHERE UserID = #{userId} AND Post_ID = #{postId}")
    Bookmark findByUserIdAndPostId(@Param("userId") String userId, @Param("postId") Long postId);

    // 북마크 추가
    @Insert("INSERT INTO bookmarks (UserID, Post_ID) VALUES (#{userId}, #{postId})")
    void insertBookmark(Bookmark bookmark);

    // 북마크 삭제
    @Delete("DELETE FROM bookmarks WHERE UserID = #{userId} AND Post_ID = #{postId}")
    void deleteBookmark(@Param("userId") String userId, @Param("postId") Long postId);
}
