package com.example.demo.repository;

import org.apache.ibatis.annotations.*;
import com.example.demo.entity.Post;
import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface PostMapper {


    // 게시물 작성
    @Insert("INSERT INTO post (Title, Member_ID, Content, ViewCount, Create_At, Board_ID, UserID) " +
            "VALUES (#{title}, #{userName}, #{content}, #{viewCount}, NOW(), #{boardId}, #{userId})")
    @Options(useGeneratedKeys = true, keyProperty = "postId") // AUTO_INCREMENT 컬럼이 있는 경우

    void insertPost(Post post);

 // 게시물 목록 조회
    @Select("SELECT Post_ID AS postId, Title AS title, Member_ID AS userName, Content AS content, " +
            "ViewCount AS viewCount, Create_At AS createAt, Update_At AS updateAt, " +
            "Board_ID AS boardId, Heart AS heart, file_path AS filePath " +
            "FROM post ORDER BY Create_At DESC")
    List<Post> findAllPosts();
  
   

    
 // 게시물 수정
    @Update("UPDATE post SET Title = #{title}, Content = #{content}, Update_At = NOW() WHERE Post_ID = #{postId}")
    void updatePost(@Param("postId") Long postId, @Param("title") String title, @Param("content") String content);


 // 게시글 ID로 게시글을 조회하는 메서드
    @Select("SELECT Post_ID AS postId, Title AS title, Content AS content, " +
            "Member_ID AS memberId, Create_At AS createdAt, Update_At AS updateAt " +
            "FROM post WHERE Post_ID = #{postId}")
    Post findPostById(@Param("postId") Long postId);

    
       
 // 게시물 삭제
    @Delete("DELETE FROM post WHERE Post_ID = #{postId}")
    void deletePost(@Param("postId") Long postId);

    
 

 // 특정 Board_ID에 따른 게시물 조회
    @Select("SELECT Post_ID AS postId, Title AS title, Member_ID AS userName, Content AS content, " +
            "ViewCount AS viewCount, Create_At AS createAt, Update_At AS updateAt, " +
            "Board_ID AS boardId, Heart AS heart " +
            "FROM post WHERE Board_ID = #{boardId} ORDER BY Create_At DESC")
    List<Post> findPostsByBoardId(int boardId);
    
    // 하트 수 증가
    @Update("UPDATE post SET Heart = Heart + 1 WHERE Post_ID = #{postId}")
    void incrementHeartCount(Long postId);

    // 하트 수 감소
    @Update("UPDATE post SET Heart = Heart - 1 WHERE Post_ID = #{postId}")
    void decrementHeartCount(Long postId); // 하트 수 감소 메서드 추가

    // 하트 갯수 가져오기
    @Select("SELECT Heart FROM post WHERE Post_ID = #{postId}")
    int findHeartCountByPostId(Long postId);
}
