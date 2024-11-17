package com.example.demo.controller;

import com.example.demo.service.FileService;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/files")
public class FileController {

    private final FileService fileService;

    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    // 이미지 파일을 제공하는 API
    @GetMapping("/{postId}")
    public ResponseEntity<Resource> getFileById(@PathVariable("postId") Long postId) {
    	
        // 데이터베이스에서 파일 경로 조회
        String filePath = fileService.getFilePathById(postId);
        System.out.println("File path for postId " + postId + ": " + filePath); // 파일 경로 조회 로그
        
        if (filePath == null) {
            return ResponseEntity.notFound().build();
        }

        // 조회한 경로로 Resource 로드
        Resource resource = fileService.loadFileAsResource(filePath);

        if (resource != null && resource.exists()) {
            try {
                // 파일 이름을 UTF-8로 인코딩하여 Base64로 변환
                String encodedFileName = Base64.getEncoder().encodeToString(resource.getFilename().getBytes(StandardCharsets.UTF_8));
                String contentDisposition = "inline; filename*=UTF-8''" + encodedFileName;

                return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition)
                    .body(resource);
            } catch (Exception e) {
                e.printStackTrace();
                return ResponseEntity.internalServerError().build();
            }
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    @DeleteMapping("/{postId}")
    public ResponseEntity<Void> deleteFileByPostId(@PathVariable("postId") Long postId) {
        // 데이터베이스에서 postId로 파일 경로 조회
        String filePath = fileService.getFilePathById(postId);
        System.out.println("Deleting file for postId " + postId + ": " + filePath); // 파일 경로 로그

        if (filePath == null) {
            return ResponseEntity.notFound().build(); // 파일 경로가 없으면 404 반환
        }

        try {
            // 파일 삭제
            fileService.deleteFile(filePath);

            // 데이터베이스에서 파일 경로 제거
            fileService.updateFilePath(postId, null); // FileService를 통해 파일 경로를 null로 업데이트

            return ResponseEntity.ok().build(); // 삭제 성공 응답
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build(); // 오류 시 500 반환
        }
    }

}
