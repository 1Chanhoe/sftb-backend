package com.example.demo.controller;

import com.example.demo.service.FileService;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
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
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"")
                    .body(resource);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
