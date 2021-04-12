package com.zohar.educope.controller;

import com.zohar.educope.constant.ErrorConstant;
import com.zohar.educope.constant.ResultExecution;
import com.zohar.educope.service.common.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/file")
public class FileController {

    @Autowired
    private FileService fileService;

    @PostMapping("/upload")
    public ResponseEntity uploadFile(@RequestPart("files") MultipartFile[] files) {
        String resultExecution = fileService.uploadFiles(files);
        ResponseEntity responseEntity;
        if(resultExecution.equals(ResultExecution.SUCCESS.toString())) {
            responseEntity = new ResponseEntity("Upload files success!", HttpStatus.OK);
        } else {
            responseEntity = new ResponseEntity(ErrorConstant.Err004, HttpStatus.BAD_REQUEST);
        }
        return responseEntity;
    }

}
