package com.kedaya.springboot3mongodb.service;

import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

/**
 * GridFS文件存储服务
 */
public interface GridFsService {


    /**
     * 上传文件到GridFS
     *
     * @param file     上传的文件
     * @param filename 文件名
     * @param metadata 元数据
     * @return 存储的文件ID
     */
    String uploadFile(MultipartFile file, String filename, Map<String, String> metadata);

    /**
     * 根据ID获取文件
     *
     * @param fileId 文件ID
     * @return GridFsResource文件资源
     */
    GridFsResource getFileById(String fileId);

    /**
     * 根据文件名获取文件
     *
     * @param filename 文件名
     * @return GridFsResource文件资源
     */
    GridFsResource getFileByName(String filename);

    /**
     * 删除文件
     *
     * @param fileId 文件ID
     */
    void deleteFile(String fileId);

    /**
     * 通过文件名删除文件
     *
     * @param filename 文件名
     */
    void deleteFileByName(String filename);
} 