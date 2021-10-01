package com.zohar.educope.controller;

import com.zohar.educope.constant.ErrorConstant;
import com.zohar.educope.dto.AdjustUserProfile;
import com.zohar.educope.dto.ChatOverviewDto;
import com.zohar.educope.dto.TokenResponse;
import com.zohar.educope.dto.UserBasic;
import com.zohar.educope.dto.UserInformation;
import com.zohar.educope.dto.UserProfile;
import com.zohar.educope.entity.User;
import com.zohar.educope.service.common.UserService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/get-profile")
    public ResponseEntity getUserProfile(@RequestParam(name = "userId") String userId) {
        UserProfile userProfile = userService.getUserProfile(userId, true);
        ResponseEntity responseEntity;
        if(userProfile != null) {
            responseEntity = new ResponseEntity(userProfile, HttpStatus.OK);
        } else {
            responseEntity = new ResponseEntity(ErrorConstant.Err003, HttpStatus.BAD_REQUEST);
        }
        return responseEntity;
    }

    @PutMapping("/add-user-information")
    public ResponseEntity updateUserInformation(@RequestBody UserInformation userInformation) {
        User user = userService.addUserInformation(userInformation);
        ResponseEntity responseEntity;
        if(user != null) {
            responseEntity = new ResponseEntity(userInformation, HttpStatus.OK);
        } else {
            responseEntity = new ResponseEntity(ErrorConstant.Err002, HttpStatus.BAD_REQUEST);
        }
        return responseEntity;
    }

    @GetMapping("/get-user-information")
    public ResponseEntity getUserInformation(@RequestParam String userId) {
        UserInformation userInformation = userService.getUserInformation(userId);
        ResponseEntity responseEntity;
        if(userInformation != null) {
            responseEntity = new ResponseEntity(userInformation, HttpStatus.OK);
        } else {
            responseEntity = new ResponseEntity(ErrorConstant.Err003, HttpStatus.BAD_REQUEST);
        }
        return responseEntity;
    }

    @PostMapping("/adjust-user")
    public ResponseEntity adjustUser(@RequestBody AdjustUserProfile adjustUserProfile) {
        UserProfile response = userService.adjustUser(adjustUserProfile);
        ResponseEntity responseEntity;
        if(response != null) {
            responseEntity = new ResponseEntity(response, HttpStatus.OK);
        } else {
            responseEntity = new ResponseEntity(ErrorConstant.Err002, HttpStatus.BAD_REQUEST);
        }
        return responseEntity;
    }

    @GetMapping("/get-list-chat")
    public ResponseEntity getListChat(@RequestParam String userId) {
        List<ChatOverviewDto> chatOverviewDtos = userService.getListChat(userId);
        ResponseEntity responseEntity;
        if(chatOverviewDtos != null) {
            responseEntity = new ResponseEntity(chatOverviewDtos, HttpStatus.OK);
        } else {
            responseEntity = new ResponseEntity(ErrorConstant.Err003, HttpStatus.BAD_REQUEST);
        }
        return responseEntity;
    }

}
