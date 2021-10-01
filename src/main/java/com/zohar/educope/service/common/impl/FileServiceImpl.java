package com.zohar.educope.service.common.impl;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.client.gridfs.model.GridFSFile;
import com.zohar.educope.constant.ResultExecution;
import com.zohar.educope.dto.LocalFile;
import com.zohar.educope.entity.User;
import com.zohar.educope.repository.UserRepos;
import com.zohar.educope.service.common.FileService;
import java.io.IOException;
import java.util.Objects;
import java.util.Optional;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsOperations;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileServiceImpl implements FileService {

  private final Logger logger = LoggerFactory.getLogger(this.getClass());

  @Autowired
  GridFsTemplate gridFsTemplate;

  @Autowired
  UserRepos userRepos;

  @Autowired
  private GridFsOperations operations;

  @Override
  public String uploadFiles(MultipartFile[] files) {
    for (MultipartFile file : files) {
      DBObject metaData = new BasicDBObject();
      // To Do - Set username of current user
//            metaData.put("author","Zohar");
      try {
        gridFsTemplate.store(file.getInputStream(), file.getOriginalFilename(),
            file.getContentType(), metaData);
      } catch (IOException e) {
        logger.error("Error when reading data from file " + e.getMessage());
        return ResultExecution.FAIL.toString();
      }
    }
    return ResultExecution.SUCCESS.toString();
  }

  @Override
  public String uploadProfileImage(MultipartFile file, String authorId) {
    if (!file.isEmpty()) {
      DBObject metaData = new BasicDBObject();
      // To Do - Set username of current user
      metaData.put("authorId", authorId);
      try {
        ObjectId fileId = gridFsTemplate.store(file.getInputStream(), file.getOriginalFilename(),
            file.getContentType(), metaData);
        // Update fileId to user's information
        if (Objects.nonNull(fileId)) {
          Optional<User> userOptional = userRepos.findById(authorId);
          if (userOptional.isPresent()) {
            userOptional.get().setUrlImageProfile(fileId.toString());
            userRepos.save(userOptional.get());
          }
        }
      } catch (IOException e) {
        logger.error("Error when reading data from file " + e.getMessage());
        return ResultExecution.FAIL.toString();
      }
    }
    return ResultExecution.SUCCESS.toString();
  }

  @Override
  public LocalFile getFileById(String fileId) {
    if (!StringUtils.isEmpty(fileId)) {
      try {
        GridFSFile gridFsFile = gridFsTemplate.findOne(new Query(Criteria.where("_id").is(fileId)));
        LocalFile localFile = convertGridFsFIleToLocalFile(gridFsFile);
        return localFile;
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
    return null;
  }

  private LocalFile convertGridFsFIleToLocalFile(GridFSFile gridFsFile) throws IOException {
    LocalFile localFile = new LocalFile();
    if (gridFsFile != null && gridFsFile.getMetadata() != null) {
      localFile.setFilename(gridFsFile.getFilename());

//      Get information from metadata of file
//      localFile.setFileType(gridFsFile.getMetadata().get("_contentType").toString());
//      localFile.setFileSize(gridFsFile.getMetadata().get("fileSize").toString());

      localFile.setFile(
          IOUtils.toByteArray(operations.getResource(gridFsFile).getInputStream()));
    }
    return localFile;
  }

}
