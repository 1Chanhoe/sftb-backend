package com.example.demo.repository;

import org.apache.ibatis.annotations.*;
import com.example.demo.entity.Post;
import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface PostMapper {

    // 게시물 작성
    @Insert("INSERT INTO post (Title, Member_ID, Content, ViewCount, Create_At, Board_ID) " +
            "VALUES (#{title}, #{userName}, #{content}, #{viewCount}, NOW(), #{boardId})")
    @Options(useGeneratedKeys = true, keyProperty = "postId") // AUTO_INCREMENT 컬럼이 있는 경우
    void insertPost(Post post);

    // 게시물 목록 조회
    @Select("SELECT * FROM post ORDER BY Create_At DESC")
    @Results({
        @Result(property = "postId", column = "Post_ID"),
        @Result(property = "title", column = "Title"),
        @Result(property = "userName", column = "Member_ID"),
        @Result(property = "content", column = "Content"),
        @Result(property = "viewCount", column = "ViewCount"),
        @Result(property = "createAt", column = "Create_At"),
        @Result(property = "updateAt", column = "Update_At"),
        @Result(property = "boardId", column = "Board_ID"),
        @Result(property = "heart", column = "Heart") // 추가: 하트 수 매핑
    })
    List<Post> findAllPosts();
   
 // 게시물 수정
    @Update("UPDATE post SET Title = #{title}, Content = #{content}, Update_At = #{updateAt} WHERE Post_ID = #{postId}")
    void updatePost(@Param("postId") Long postId, @Param("title") String title, @Param("content") String content, @Param("updateAt") LocalDateTime updateAt);


    // 게시물 조회
    @Select("SELECT * FROM post WHERE Post_ID = #{postId}")
    Post findPostById(@Param("postId") Long postId);

    
       
 // 게시물 삭제
    @Delete("DELETE FROM post WHERE Post_ID = #{postId}")
    void deletePost(@Param("postId") Long postId);

    
 

    // 특정 Board_ID에 따른 게시물 조회
    @Select("SELECT * FROM post WHERE Board_ID = #{boardId} ORDER BY Create_At DESC")
    @Results({
        @Result(property = "postId", column = "Post_ID"),
        @Result(property = "title", column = "Title"),
        @Result(property = "userName", column = "Member_ID"),
        @Result(property = "content", column = "Content"),
        @Result(property = "viewCount", column = "ViewCount"),
        @Result(property = "createAt", column = "Create_At"),
        @Result(property = "updateAt", column = "Update_At"),
        @Result(property = "boardId", column = "Board_ID"),
        @Result(property = "heart", column = "Heart") // 추가: 하트 수 매핑
    })
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
