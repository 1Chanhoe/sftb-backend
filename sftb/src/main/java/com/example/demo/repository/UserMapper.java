package com.example.demo.repository;
//Mybatis를 사용해 데베와 상호작용하는 인터페이스 부분
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Insert;
import com.example.demo.entity.User;

@Mapper //Mybatis가 이 인터페이스를 매퍼로 인식하게 함
public interface UserMapper {
    @Insert("INSERT INTO customers (UserID, StudentID, Password, UserName, Email) VALUES (#{userID}, #{studentID}, #{password}, #{userName}, #{email})") //SQL INSERT문 정의
    void insertUser(User user); //User객체를 데베에 삽입
}