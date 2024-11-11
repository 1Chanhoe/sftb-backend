package com.example.demo.repository;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import com.example.demo.entity.User;
import java.util.List;
@Mapper
public interface UserMapper {
    // 사용자 삽입
    @Insert("INSERT INTO customers (UserID, StudentID, Password, UserName, Email, NewMember,  UserLevel_Experience) VALUES (#{userID}, #{studentID}, #{password}, #{userName}, #{email}, #{newMember}, #{userLevelExperience})")
    void insertUser(User user);

    // UserID로 사용자 검색
    @Select("SELECT * FROM customers WHERE UserID = #{userID}")
    User findByUserId(@Param("userID") String userID);
    
    // UserName과 Email로 사용자 검색
    @Select("SELECT * FROM customers WHERE UserName = #{userName} AND Email = #{email}")
    User findByNameAndEmail(@Param("userName") String userName, @Param("email") String email);
    
    // UserName, Email, UserID로 사용자 검색
    @Select("SELECT * FROM customers WHERE UserName = #{userName} AND Email = #{email} AND UserID = #{userID}")
    User findByNameEmailAndId(@Param("userName") String userName, @Param("email") String email, @Param("userID") String userID);
    
    // 신규 회원 여부 업데이트
    @Update("UPDATE customers SET NewMember = #{newMember} WHERE UserID = #{userID}")
    void updateNewMember(@Param("userID") String userID, @Param("newMember") int newMember);

    // 비밀번호 업데이트
    @Update("UPDATE customers SET Password = #{password} WHERE UserID = #{userID}")
    void updateUser(User user);
    
    // 학번으로 중복 체크
    @Select("SELECT COUNT(*) > 0 FROM customers WHERE StudentID = #{studentID}")
    boolean existsByStudentID(@Param("studentID") String studentID);
    
    // 아이디로 중복 체크
    @Select("SELECT COUNT(*) > 0 FROM customers WHERE UserID = #{userID}")
    boolean existsByUserID(@Param("userID") String userID);
    
    // 이메일로 중복 체크
    @Select("SELECT COUNT(*) > 0 FROM customers WHERE Email = #{email}")
    boolean existsByEmail(@Param("email") String email);
    
    // 유저레벨경험치 업데이트
    @Update("UPDATE customers SET UserLevel_Experience = COALESCE(UserLevel_Experience, 0) + #{userLevelExperience} WHERE UserID = #{userId}")
    void updateUserLevelExperience(@Param("userId") String userId, @Param("userLevelExperience") int userLevelExperience);

    // 유저 레벨 증가
    @Update("UPDATE customers SET UserLevel = COALESCE(UserLevel, 0) + 1 WHERE UserID = #{userId}")
    void updateUserLevel(@Param("userId") String userId);

    // 현재 유저 레벨 경험치 조회
    @Select("SELECT COALESCE(UserLevel_Experience, 0) FROM customers WHERE UserID = #{userId}")
    int getUserLevelExperience(String userId);
    
    // 현재 유저 레벨 조회
    @Select("SELECT UserLevel FROM customers WHERE UserID = #{userId}")
    int getUserLevel(String userId);
    
    // 현재 유저의 티어 경험치 조회
    @Select("SELECT COALESCE(Tier_Experience, 0) FROM customers WHERE UserID = #{userId}")
    int getTierExperience(String userId);
    
    // 현재 유저의 티어 조회
    @Select("SELECT Tier FROM customers WHERE UserID = #{userId}")
    String getTier(String userId); // 유저의 현재 티어를 반환
    
    // 티어 업데이트
    @Update("UPDATE customers SET Tier = #{tier} WHERE UserID = #{userId}")
    void updateTier(@Param("userId") String userId, @Param("tier") String tier);
    
    // 티어경험치 업데이트 COALESCE()는 NULL인경우 0을반환, 아닐경우 그값을 그대로 반영함 나중에 레벨,티어 업데이트할때도 사용해야할거같음
    @Update("UPDATE customers SET Tier_Experience = COALESCE(Tier_Experience, 0) + #{experience} WHERE UserID = #{userID}")
    void updateUserExperience(@Param("userID") String userID, @Param("experience") int experience);
    
    // Token 업데이트
    @Update("UPDATE customers SET Token = COALESCE(Token, 0) + #{amount} WHERE UserID = #{userId}")
    void updateToken(@Param("userId") String userId, @Param("amount") int amount);
    
    //Token 조회
    @Select("SELECT Token FROM customers WHERE UserID = #{userId}")
    int getUserTokenCount(String userId);
    
    //모든유저 아이디,이름,티어,레벨,레벨경험치,티어경험치 내림차순으로 조회
    @Select("SELECT UserID, UserName, Tier, UserLevel, UserLevel_Experience, Tier_Experience FROM customers ORDER BY Tier DESC, UserLevel DESC, Tier_Experience DESC, UserLevel_Experience DESC")
    List<User> findAllUsers();
    
    //티어 조회
    @Select("SELECT Tier FROM customers WHERE UserID = #{userId}")
    String getUserTier(String userId);
    
    // 관리자인지 여부 확인
    @Select("SELECT Manager FROM customers WHERE UserID = #{userID}")
    int getManagerStatus(@Param("userID") String userID);


}