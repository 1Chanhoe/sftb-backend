package com.example.demo.repository;

import org.apache.ibatis.annotations.*;

@Mapper
public interface HeartMapper {
    
	@Select("SELECT COUNT(*) > 0 FROM hearts WHERE UserID = #{userId} AND Post_ID = #{postId}")
	boolean isHearted(@Param("postId") Long postId, @Param("userId") String userId);
    
    @Insert("INSERT INTO hearts (UserID, Post_ID) VALUES (#{userId}, #{postId})")
    void addHeart(@Param("userId") String userId, @Param("postId") Long postId);
    
    @Delete("DELETE FROM hearts WHERE UserID = #{userId} AND Post_ID = #{postId}")
    void removeHeart(@Param("userId") String userId, @Param("postId") Long postId);
    
    // 하트 수 증가
    @Update("UPDATE post SET Heart = Heart + 1 WHERE Post_ID = #{postId}")
    void incrementHeartCount(@Param("postId") Long postId);

    // 하트 수 감소
    @Update("UPDATE post SET Heart = Heart - 1 WHERE Post_ID = #{postId}")
    void decrementHeartCount(@Param("postId") Long postId);
}
