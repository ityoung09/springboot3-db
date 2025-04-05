package com.kedaya.springboot3mongodb.service.impl;

import com.mongodb.client.gridfs.model.GridFSFile;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsOperations;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * GridFS文件存储服务
 */
@Service
public class GridFsServiceImpl {

    @Autowired
    private GridFsTemplate gridFsTemplate;
    
    @Autowired
    private GridFsOperations gridFsOperations;
    
    /**
     * 上传文件到GridFS
     * @param file 上传的文件
     * @param filename 文件名
     * @param metadata 元数据
     * @return 存储的文件ID
     */
    public String uploadFile(MultipartFile file, String filename, Map<String, String> metadata) throws IOException {
        // 设置元数据
        if (metadata == null) {
            metadata = new HashMap<>();
        }
        
        // 添加一些默认元数据
        metadata.put("contentType", file.getContentType());
        metadata.put("size", String.valueOf(file.getSize()));
        
        InputStream inputStream = file.getInputStream();
        ObjectId objectId = gridFsTemplate.store(
                inputStream, 
                filename, 
                file.getContentType(), 
                metadata);
        
        inputStream.close();
        return objectId.toString();
    }
    
    /**
     * 根据ID获取文件
     * @param fileId 文件ID
     * @return GridFsResource文件资源
     */
    public GridFsResource getFileById(String fileId) {
        GridFSFile gridFSFile = gridFsTemplate.findOne(
                new Query(Criteria.where("_id").is(new ObjectId(fileId))));
        
        if (gridFSFile == null) {
            return null;
        }
        
        return gridFsOperations.getResource(gridFSFile);
    }
    
    /**
     * 根据文件名获取文件
     * @param filename 文件名
     * @return GridFsResource文件资源
     */
    public GridFsResource getFileByName(String filename) {
        GridFSFile gridFSFile = gridFsTemplate.findOne(
                new Query(Criteria.where("filename").is(filename)));
        
        if (gridFSFile == null) {
            return null;
        }
        
        return gridFsOperations.getResource(gridFSFile);
    }
    
    /**
     * 删除文件
     * @param fileId 文件ID
     */
    public void deleteFile(String fileId) {
        gridFsTemplate.delete(new Query(Criteria.where("_id").is(new ObjectId(fileId))));
    }
    
    /**
     * 通过文件名删除文件
     * @param filename 文件名
     */
    public void deleteFileByName(String filename) {
        gridFsTemplate.delete(new Query(Criteria.where("filename").is(filename)));
    }
} 