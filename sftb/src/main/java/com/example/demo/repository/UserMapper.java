package com.example.demo.repository;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import com.example.demo.entity.User;

@Mapper
public interface UserMapper {
    // 사용자 삽입
    @Insert("INSERT INTO customers (UserID, StudentID, Password, UserName, Email, NewMember, ExperiencePoints) VALUES (#{userID}, #{studentID}, #{password}, #{userName}, #{email}, #{newMember}, #{experiencePoints})")
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

    //티어경험치 추가 COALESCE()는 NULL인경우 0을반환, 아닐경우 그값을 그대로 반영함 나중에 레벨,티어 업데이트할때도 사용해야할거같음
    @Update("UPDATE customers SET Tier_Experience = COALESCE(Tier_Experience, 0) + #{experience} WHERE UserID = #{userID}")
    void updateUserExperience(@Param("userID") String userID, @Param("experience") int experience);
    
    // 경험치 업데이트
    @Update("UPDATE customers SET UserLevel_Experience = COALESCE(UserLevel_Experience, 0) + #{experience} WHERE UserID = #{userId}")
    void updateUserLevelExperience(@Param("userId") String userId, @Param("experience") int experience);



    // 경험치 업데이트
    @Update("UPDATE customers SET ExperiencePoints = #{experiencePoints} WHERE UserID = #{userID}")
    void updateExperiencePoints(@Param("userID") String userID, @Param("experiencePoints") int experiencePoints);
    
    @Select("SELECT ExperiencePoints FROM customers WHERE UserID = #{userID}")
    int findExperiencePointsByUserId(@Param("userID") String userID);


}