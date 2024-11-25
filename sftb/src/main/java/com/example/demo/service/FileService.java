package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.repository.PostMapper;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;


@Service
public class FileService {
   
   

    @Value("${file.upload-dir}") // 파일 저장 디렉토리 경로를 설정 파일에서 가져옴
    private String uploadDir;
    
    @Autowired
    private PostMapper postMapper;

    public String saveFile(MultipartFile file, Long postId) throws IOException {
        // 원본 파일명 가져오기
        String originalFilename = file.getOriginalFilename();
        
        // UUID를 사용하여 고유한 파일 이름 생성
        String uniqueFilename = UUID.randomUUID().toString() + "_" + originalFilename;
        
        // 저장 경로 설정
        Path filePath = Paths.get(uploadDir).resolve(uniqueFilename).normalize();

        // 디렉토리 생성
        Files.createDirectories(filePath.getParent());

        // 파일 저장
        Files.copy(file.getInputStream(), filePath);

        // 데이터베이스에 파일 경로 업데이트
        postMapper.updateFilePath(postId, uniqueFilename); // 파일 이름만 저장

        // 저장된 파일 경로 반환
        return filePath.toString().replace("\\", "/");
    }



    // 파일 경로 가져오기 메서드 - Resource로 반환하도록 수정(외부 사용)
    public Resource loadFileAsResource(String filePath) {
        try {
            // 직접 filePath를 절대 경로로 설정
            Path path = Paths.get(filePath).normalize();
            System.out.println("Loading file from path: " + path.toString()); // 디버깅용 로그 추가
            
            Resource resource = new UrlResource(path.toUri());

            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                System.out.println("File not found or not readable at path: " + path.toString());
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // 파일 ID로 파일 경로를 조회하는 메서드 (데베 조회)
    public String getFilePathById(Long postId) {
        return postMapper.findFilePathByPostId(postId); // 데이터베이스에서 fileId로 경로 조회
    }
    
    // 파일 삭제 메서드 추가
    public void deleteFile(String filePath) {
        try {
            Path path = Paths.get(filePath).normalize(); // 파일 경로 설정
            if (Files.exists(path)) {
                Files.delete(path); // 파일이 존재하면 삭제
                System.out.println("Deleted file: " + path.toString());
            } else {
                System.out.println("File not found at path: " + path.toString());
            }
        } catch (IOException e) {
            System.err.println("Failed to delete file: " + filePath);
            e.printStackTrace();
        }
    }

    public void updateFilePath(Long postId, String filePath) {
        postMapper.updateFilePath(postId, filePath); // 파일 경로를 null로 업데이트
    }
    

}