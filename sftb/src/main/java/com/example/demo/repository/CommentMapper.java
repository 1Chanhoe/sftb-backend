package com.example.demo.repository;
import com.example.demo.entity.Comment;
import java.util.List;
import org.apache.ibatis.annotations.*;
@Mapper
public interface CommentMapper {

	@Select("SELECT Comment_ID AS commentId, Parent_Seq AS parentSeq, Content AS content, " +
	        "Member_ID AS memberId, Created_At AS createdAt, Updated_At AS updatedAt, " +
	        "Adopt AS adopt, Heart AS heart,Post_ID AS postId " +
	        "FROM comments WHERE Post_ID = #{postId} AND Parent_Seq IS NULL")
	List<Comment> findRootCommentsByPostId(Long postId);

	@Select("SELECT Comment_ID AS commentId, Parent_Seq AS parentSeq, Content AS content, " +
	        "Member_ID AS memberId, Created_At AS createdAt, Updated_At AS updatedAt, " +
	        "Adopt AS adopt, Heart AS heart, Post_ID AS postId " +
	        "FROM comments WHERE Post_ID = #{postId} AND Parent_Seq = #{parentSeq}")
	List<Comment> findRepliesByParentId(@Param("postId") Long postId, @Param("parentSeq") Long parentSeq);

    @Insert("INSERT INTO comments (Content, Member_ID, Post_ID, Parent_Seq, Created_At, Updated_At, Heart, Adopt) " +
            "VALUES (#{content}, #{memberId}, #{postId}, #{parentSeq}, #{createdAt}, #{updatedAt}, #{heart}, #{adopt})")
    @Options(useGeneratedKeys = true, keyProperty = "commentId")
    void insertComment(Comment comment);
}