package com.zohar.educope.controller;

import com.zohar.educope.constant.ErrorConstant;
import com.zohar.educope.dto.TokenResponse;
import com.zohar.educope.dto.UserBasic;
import com.zohar.educope.entity.User;
import com.zohar.educope.service.common.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserService userService;

    @PostMapping("/create")
    public ResponseEntity createUser(@RequestBody User user) {
        User userResponse = userService.createUser(user);
        ResponseEntity responseEntity;
        if(userResponse != null) {
            responseEntity = new ResponseEntity(userResponse, HttpStatus.OK);
        } else {
            responseEntity = new ResponseEntity(ErrorConstant.Err002, HttpStatus.BAD_REQUEST);
        }
        return responseEntity;
    }

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody UserBasic userBasic) {
        TokenResponse tokenReponse = userService.login(userBasic);
        ResponseEntity responseEntity;
        if(tokenReponse != null) {
            responseEntity = new ResponseEntity(tokenReponse, HttpStatus.OK);
        } else {
            responseEntity = new ResponseEntity(ErrorConstant.Err001, HttpStatus.BAD_REQUEST);
        }
        return responseEntity;
    }

}
