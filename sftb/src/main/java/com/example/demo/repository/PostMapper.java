package com.example.demo.repository;

import org.apache.ibatis.annotations.*;
import com.example.demo.entity.Post;

import java.util.List;

@Mapper
public interface PostMapper {

    // 게시물 작성
    @Insert("INSERT INTO CerInfo_Post (Title, Member_ID, Content, ViewCount, Create_At) " +
            "VALUES (#{title}, #{userName}, #{content}, #{viewCount}, NOW())")
    @Options(useGeneratedKeys = true, keyProperty = "creSeq") // AUTO_INCREMENT 컬럼이 있는 경우
    void insertPost(Post post);

    // 게시물 목록 조회
    @Select("SELECT * FROM CerInfo_Post ORDER BY Create_At DESC")
    @Results({
        @Result(property = "creSeq", column = "CreSeq"),
        @Result(property = "title", column = "Title"),
        @Result(property = "userName", column = "Member_ID"),
        @Result(property = "content", column = "Content"),
        @Result(property = "viewCount", column = "ViewCount"),
        @Result(property = "createAt", column = "Create_At")
    })
    List<Post> findAllPosts();
}
