package com.zohar.educope.service.common;

import org.springframework.web.multipart.MultipartFile;

public interface FileService {

    public String uploadFiles(MultipartFile[] files);

}
