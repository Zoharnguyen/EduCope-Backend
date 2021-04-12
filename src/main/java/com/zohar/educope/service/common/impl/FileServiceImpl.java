package com.zohar.educope.service.common.impl;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.zohar.educope.constant.ResultExecution;
import com.zohar.educope.service.common.FileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class FileServiceImpl implements FileService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    GridFsTemplate gridFsTemplate;

    @Override
    public String uploadFiles(MultipartFile[] files) {
        for(MultipartFile file : files) {
            DBObject metaData = new BasicDBObject();
            // To Do - Set username of current user
            metaData.put("author","Zohar");
            try {
                gridFsTemplate.store(file.getInputStream(), file.getOriginalFilename(), file.getContentType(), metaData);
            } catch (IOException e) {
                logger.error("Error when reading data from file " + e.getMessage());
                return ResultExecution.FAIL.toString();
            }
        }
        return ResultExecution.SUCCESS.toString();
    }

}
