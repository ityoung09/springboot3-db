package com.kedaya.springboot3mongodb.controller;

import com.kedaya.springboot3mongodb.service.impl.GridFsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * 文件上传下载控制器
 */
@RestController
@RequestMapping("/files")
public class FileController {

    @Autowired
    private GridFsServiceImpl gridFsService;

    /**
     * 上传文件
     * @param file 文件
     * @param filename 可选的文件名
     * @return 响应
     */
    @PostMapping("/upload")
    public ResponseEntity<Map<String, Object>> uploadFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "filename", required = false) String filename) {
        
        try {
            // 如果没有提供文件名，使用原始文件名
            if (filename == null || filename.isEmpty()) {
                filename = file.getOriginalFilename();
            }
            
            // 准备元数据
            Map<String, String> metadata = new HashMap<>();
            metadata.put("uploadTime", String.valueOf(System.currentTimeMillis()));
            
            // 存储文件
            String fileId = gridFsService.uploadFile(file, filename, metadata);
            
            // 构建响应
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("fileId", fileId);
            response.put("filename", filename);
            response.put("size", file.getSize());
            response.put("contentType", file.getContentType());
            
            return ResponseEntity.ok(response);
        } catch (IOException e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("error", e.getMessage());
            
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * 根据ID下载文件
     * @param fileId 文件ID
     * @return 文件
     */
    @GetMapping("/download/{fileId}")
    public ResponseEntity<InputStreamResource> downloadFile(@PathVariable String fileId) {
        try {
            GridFsResource resource = gridFsService.getFileById(fileId);

            if (resource == null || !resource.exists()) {
                return ResponseEntity.notFound().build();
            }
            // 获取文件大小，用于设置Content-Length
            long contentLength = resource.contentLength();
            // 设置响应头
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType(resource.getContentType()));
            headers.setContentDispositionFormData("attachment", URLEncoder.encode(resource.getFilename(), StandardCharsets.UTF_8));
            headers.setContentLength(contentLength);
            // 返回文件内容
            return ResponseEntity.ok()
                    .headers(headers)
                    .body(new InputStreamResource(resource.getInputStream()));
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * 根据文件名下载文件
     * @param filename 文件名
     * @return 文件
     */
    @GetMapping("/download/byName/{filename}")
    public ResponseEntity<InputStreamResource> downloadFileByName(@PathVariable String filename) {
        try {
            GridFsResource resource = gridFsService.getFileByName(filename);
            
            if (resource == null || !resource.exists()) {
                return ResponseEntity.notFound().build();
            }
            
            // 设置响应头
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType(resource.getContentType()));
            headers.setContentDispositionFormData("attachment", URLEncoder.encode(resource.getFilename(), StandardCharsets.UTF_8));
            
            // 返回文件内容
            return ResponseEntity.ok()
                    .headers(headers)
                    .body(new InputStreamResource(resource.getInputStream()));
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * 删除文件
     * @param fileId 文件ID
     * @return 响应
     */
    @DeleteMapping("/{fileId}")
    public ResponseEntity<Map<String, Object>> deleteFile(@PathVariable String fileId) {
        try {
            gridFsService.deleteFile(fileId);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "文件已成功删除");
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("error", e.getMessage());
            
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
} 