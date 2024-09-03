package com.example.demo.repository;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import com.example.demo.entity.User;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface UserMapper {
    @Insert("INSERT INTO customers (UserID, StudentID, Password, UserName, Email) VALUES (#{userID}, #{studentID}, #{password}, #{userName}, #{email})")
    void insertUser(User user);

    @Select("SELECT * FROM customers WHERE UserID = #{userID}")
    User findByUserId(@Param("userID") String userID);
    
    @Select("SELECT * FROM customers WHERE UserName = #{userName} AND Email = #{email}")
    User findByNameAndEmail(@Param("userName") String userName, @Param("email") String email);
    
    @Select("SELECT * FROM customers WHERE UserName = #{userName} AND Email = #{email} AND UserID = #{userID}")
    User findByNameEmailAndId(@Param("userName") String userName, @Param("email") String email, @Param("userID") String userID);
    
    @Update("UPDATE customers SET Password = #{password} WHERE UserID = #{userID}")
    void updateUser(User user);
    
    

}