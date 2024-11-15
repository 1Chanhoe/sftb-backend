package com.example.demo.repository;

import org.apache.ibatis.annotations.*;
import com.example.demo.entity.Post;
import java.util.List;

@Mapper
public interface PostMapper {

    // 게시물 작성 - 파일 경로 포함
    @Insert("INSERT INTO post (Title, Member_ID, Content, ViewCount, Create_At, Board_ID, UserID, file_path) " +
            "VALUES (#{title}, #{userName}, #{content}, #{viewCount}, NOW(), #{boardId}, #{userId}, #{filePath})")
    @Options(useGeneratedKeys = true, keyProperty = "postId")
    void insertPost(Post post);

    // 게시물 목록 조회
    @Select("SELECT Post_ID AS postId, Title AS title, Member_ID AS userName, Content AS content, " +
            "ViewCount AS viewCount, Create_At AS createAt, Update_At AS updateAt, " +
            "Board_ID AS boardId, Heart AS heart, file_path AS filePath, UserID AS userId " +
            "FROM post ORDER BY Create_At DESC")
    List<Post> findAllPosts();

    // 게시물 수정
    @Update("UPDATE post SET Title = #{title}, Content = #{content}, Update_At = NOW(), file_path = #{filePath} WHERE Post_ID = #{postId}")
    void updatePost(@Param("postId") Long postId, @Param("title") String title, @Param("content") String content, @Param("filePath") String filePath);

    // 게시글 ID로 게시글을 조회하는 메서드
    @Select("SELECT Post_ID AS postId, Title AS title, Content AS content, ViewCount AS viewCount, Heart AS heart," +
            "Member_ID AS memberId, Create_At AS createdAt, Update_At AS updateAt, UserID AS userId, file_path AS filePath, Adopt AS adopt " +
            "FROM post WHERE Post_ID = #{postId}")
    Post findPostById(@Param("postId") Long postId);

    // 게시물 삭제
    @Delete("DELETE FROM post WHERE Post_ID = #{postId}")
    void deletePost(@Param("postId") Long postId);

    // 특정 Board_ID에 따른 게시물 조회
    @Select("SELECT Post_ID AS postId, Title AS title, Member_ID AS userName, Content AS content, " +
            "ViewCount AS viewCount, Create_At AS createAt, Update_At AS updateAt, " +
            "Board_ID AS boardId, Heart AS heart, UserID AS userId, file_path AS filePath " +
    		"FROM post WHERE Board_ID = #{boardId} ORDER BY Create_At DESC")
    List<Post> findPostsByBoardId(int boardId);

    //파일 경로 조회
    @Select("SELECT file_path FROM post WHERE Post_ID = #{postId}")
    String findFilePathByPostId(Long postId);

 // 게시글 채택 상태 업데이트
    @Update("UPDATE post SET Adopt = true WHERE Post_ID = #{postId}")
    void updateAdoptPostStatus(@Param("postId") Long postId);

    // 조회수 증가 SQL
    @Update("UPDATE post SET ViewCount = ViewCount + 1 WHERE Post_ID = #{postId}")
    void incrementViewCount(Long postId);


}
