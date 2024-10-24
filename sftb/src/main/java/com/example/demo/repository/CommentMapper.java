package com.example.demo.repository;
import com.example.demo.entity.Comment;
import java.util.List;
import org.apache.ibatis.annotations.*;
@Mapper
public interface CommentMapper {

	@Select("SELECT Comment_ID AS commentId, Parent_Seq AS parentSeq, Content AS content, " +
	        "Member_ID AS memberId, UserID AS userId, Created_At AS createdAt, Updated_At AS updatedAt, " +
	        "Adopt AS adopt, Heart AS heart, Post_ID AS postId " +
	        "FROM comments WHERE Post_ID = #{postId} AND Parent_Seq IS NULL")
	List<Comment> findRootCommentsByPostId(Long postId);

	@Select("SELECT Comment_ID AS commentId, Parent_Seq AS parentSeq, Content AS content, " +
	        "Member_ID AS memberId, UserID AS userId, Created_At AS createdAt, Updated_At AS updatedAt, " +
	        "Adopt AS adopt, Heart AS heart, Post_ID AS postId " +
	        "FROM comments WHERE Post_ID = #{postId} AND Parent_Seq = #{parentSeq}")
	List<Comment> findRepliesByParentId(@Param("postId") Long postId, @Param("parentSeq") Long parentSeq);
	
	//댓글 추가 쿼리
	@Insert("INSERT INTO comments (Content, Member_ID, UserID, Post_ID, Parent_Seq, Created_At, Heart, Adopt) " +
	        "VALUES (#{content}, #{memberId}, #{userId}, #{postId}, #{parentSeq}, NOW(), #{heart}, #{adopt})")
	@Options(useGeneratedKeys = true, keyProperty = "commentId")
	void insertComment(Comment comment);

    // 댓글 수정 쿼리
    @Update("UPDATE comments SET Content = #{content}, Updated_At = NOW() WHERE Comment_ID = #{commentId}")
    void updateComment(@Param("commentId") Long commentId, @Param("content") String content);
    
    //댓글 ID로 댓글을 조회하는 메서드
    @Select("SELECT Comment_ID AS commentId, Parent_Seq AS parentSeq, Content AS content, " +
            "Member_ID AS memberId, UserID AS userId, Created_At AS createdAt, Updated_At AS updatedAt, " +
            "Adopt AS adopt, Heart AS heart, Post_ID AS postId " +
            "FROM comments WHERE Comment_ID = #{commentId}")
    Comment findCommentById(Long commentId);
    
    // 댓글 삭제 쿼리
    @Delete("DELETE FROM comments WHERE Comment_ID = #{commentId}")
    void deleteComment(@Param("commentId") Long commentId);
    
    @Delete("DELETE FROM comments WHERE Parent_Seq = #{commentId}")
    void deleteRepliesByParentId(@Param("commentId") Long commentId);
    
    //채택시 Adopt True로 변환
    @Update("UPDATE comments SET Adopt = true WHERE Comment_ID = #{commentId}")
    void updateAdoptStatus(@Param("commentId") Long commentId);
}