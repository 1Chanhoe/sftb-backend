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

    // 게시물 ID로 게시물 조회
    @Select("SELECT * FROM CerInfo_Post WHERE Cre_seq = #{creSeq}")
    Post findById(@Param("creSeq") Long creSeq);

    // 회원 ID로 작성된 모든 게시물 조회
    @Select("SELECT * FROM CerInfo_Post WHERE Member_ID = #{userName}")
    List<Post> findByUserName(@Param("userName") String userName);

    // 게시물 수정
    @Update("UPDATE CerInfo_Post SET Title = #{title}, Content = #{content}, Update_At = NOW() WHERE Cre_seq = #{creSeq}")
    void updatePost(Post post);

    // 게시물 삭제
    @Delete("DELETE FROM CerInfo_Post WHERE Cre_seq = #{creSeq}")
    void deletePost(@Param("creSeq") Long creSeq);
}
