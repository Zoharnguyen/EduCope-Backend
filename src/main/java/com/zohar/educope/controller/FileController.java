package com.zohar.educope.controller;

import com.zohar.educope.constant.ErrorConstant;
import com.zohar.educope.constant.ResultExecution;
import com.zohar.educope.dto.LocalFile;
import com.zohar.educope.service.common.FileService;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequestMapping("/file")
public class FileController {

    @Autowired
    private FileService fileService;

    @PostMapping("/upload-multi")
    public ResponseEntity uploadFiles(@RequestPart("files") MultipartFile[] files) {
        String resultExecution = fileService.uploadFiles(files);
        ResponseEntity responseEntity;
        if(resultExecution.equals(ResultExecution.SUCCESS.toString())) {
            responseEntity = new ResponseEntity("Upload files success!", HttpStatus.OK);
        } else {
            responseEntity = new ResponseEntity(ErrorConstant.Err004, HttpStatus.BAD_REQUEST);
        }
        return responseEntity;
    }

    @PostMapping("/upload-single")
    public ResponseEntity uploadProfileImage(@RequestPart("file") MultipartFile file, @RequestParam(name = "authorId") String authorId) {
        log.info("Start do uploadFile");
        String resultExecution = fileService.uploadProfileImage(file, authorId);
        ResponseEntity responseEntity;
        if (resultExecution.equals(ResultExecution.SUCCESS.toString())) {
            responseEntity = new ResponseEntity("Upload file success!", HttpStatus.OK);
            log.debug("Upload file success");
        } else {
            responseEntity = new ResponseEntity(ErrorConstant.Err004, HttpStatus.BAD_REQUEST);
        }
        log.info("End do uploadFile");
        return responseEntity;
    }

    @GetMapping("/get-file-by-id")
    public ResponseEntity getFileById(@RequestParam("fileId") String fileId) {
        LocalFile resultExecution = fileService.getFileById(fileId);
        ResponseEntity responseEntity;
        if (Objects.nonNull(resultExecution)) {
            responseEntity = new ResponseEntity(resultExecution, HttpStatus.OK);
        } else {
            responseEntity = new ResponseEntity(ErrorConstant.Err005, HttpStatus.BAD_REQUEST);
        }
        return responseEntity;
    }

}

