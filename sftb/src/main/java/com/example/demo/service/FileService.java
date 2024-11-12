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


@Service
public class FileService {
	
	

    @Value("${file.upload-dir}") // 파일 저장 디렉토리 경로를 설정 파일에서 가져옴
    private String uploadDir;
    
    @Autowired
    private PostMapper postMapper;

    // 파일 저장 메서드
    public String saveFile(MultipartFile file) throws IOException {
    	// 파일 이름 생성 (현재 시간 + 원본 파일명)
        String originalFilename = file.getOriginalFilename();
        String encodedFileName = URLEncoder.encode(originalFilename, StandardCharsets.UTF_8.toString()); // URL 인코딩
        
        // 인코딩된 파일 이름을 사용하여 경로 설정
        String fileName = System.currentTimeMillis() + "_" + encodedFileName; 
        Path filePath = Paths.get(uploadDir).resolve(fileName).normalize(); // 경로 설정

        Files.createDirectories(filePath.getParent()); // 디렉토리 생성
        Files.copy(file.getInputStream(), filePath); // 파일 저장

     // 파일 경로를 슬래시로 변환하여 반환
        return filePath.toString().replace("\\", "/");
    }

    // 파일 경로 가져오기 메서드 - 외부 사용을 위해 Resource로 반환하도록 수정
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

 // 파일 ID로 파일 경로를 조회하는 메서드
    public String getFilePathById(Long postId) {
        return postMapper.findFilePathByPostId(postId); // 데이터베이스에서 fileId로 경로 조회
    }
}
