package com.zohar.educope.service.common;

import com.mongodb.client.gridfs.model.GridFSFile;
import com.zohar.educope.dto.LocalFile;
import org.springframework.web.multipart.MultipartFile;

public interface FileService {

    public String uploadFiles(MultipartFile[] files);

    public String uploadProfileImage(MultipartFile file, String authorId);

    public LocalFile getFileById(String fileId);

}
