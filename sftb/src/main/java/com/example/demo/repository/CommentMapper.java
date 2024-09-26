package com.example.demo.repository;
import com.example.demo.entity.Comment;
import java.util.List;
import org.apache.ibatis.annotations.*;
@Mapper
public interface CommentMapper {

    @Select("SELECT * FROM comments WHERE post_id = #{postId} AND parent_seq IS NULL")
    List<Comment> findRootCommentsByPostId(Long postId);

    @Select("SELECT * FROM comments WHERE post_id = #{postId} AND parent_seq = #{parentSeq}")
    List<Comment> findRepliesByParentId(@Param("postId") Long postId, @Param("parentSeq") Long parentSeq);

    @Insert("INSERT INTO comments (content, member_id, post_id, parent_seq, created_at, updated_at, heart, adopt) " +
            "VALUES (#{content}, #{memberId}, #{postId}, #{parentSeq}, #{createdAt}, #{updatedAt}, #{heart}, #{adopt})")
    @Options(useGeneratedKeys = true, keyProperty = "commentId")
    void insertComment(Comment comment);
}