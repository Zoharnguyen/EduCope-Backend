package com.zohar.educope.controller;

import com.zohar.educope.constant.ErrorConstant;
import com.zohar.educope.dto.NotificationElement;
import com.zohar.educope.dto.NotificationRequestDto;
import com.zohar.educope.dto.SubscriptionRequestDto;
import com.zohar.educope.dto.UserInformation;
import com.zohar.educope.dto.UserProfile;
import com.zohar.educope.service.common.NotificationService;
import java.util.Collections;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/notification")
public class NotificationController {

  @Autowired
  private NotificationService notificationService;

  // Subscribe topic
  @PostMapping("/subscribe")
  public ResponseEntity subscribeToTopic(@RequestBody SubscriptionRequestDto subscriptionRequestDto) {
    boolean response = notificationService.subscribeToTopic(subscriptionRequestDto);;
    ResponseEntity responseEntity;
    if(response) {
      responseEntity = new ResponseEntity(HttpStatus.OK);
    } else {
      responseEntity = new ResponseEntity(ErrorConstant.Err003, HttpStatus.BAD_REQUEST);
    }
    return responseEntity;
  }

  // Unsubscribe topic
  @PostMapping("/unsubscribe")
  public ResponseEntity unsubscribeFromTopic(@RequestBody  SubscriptionRequestDto subscriptionRequestDto) {
    boolean response = notificationService.unsubscribeFromTopic(subscriptionRequestDto);
    ResponseEntity responseEntity;
    if(response) {
      responseEntity = new ResponseEntity(HttpStatus.OK);
    } else {
      responseEntity = new ResponseEntity(ErrorConstant.Err003, HttpStatus.BAD_REQUEST);
    }
    return responseEntity;
  }

  // Send message to specific device
  @PostMapping("/token")
  public String sendPnsToDevice(@RequestBody NotificationRequestDto notificationRequestDto) {
    return notificationService.sendPnsToDevice(notificationRequestDto);
  }

  // Send message to topic
  @PostMapping("/topic")
  public ResponseEntity sendPnsToTopic(@RequestBody NotificationRequestDto notificationRequestDto, @RequestParam String receiverId) {
    NotificationElement notificationElement = notificationService.sendPnsToTopic(notificationRequestDto, receiverId);
    ResponseEntity responseEntity;
    if(notificationElement != null) {
      responseEntity = new ResponseEntity(notificationElement, HttpStatus.OK);
    } else {
      responseEntity = new ResponseEntity(ErrorConstant.Err003, HttpStatus.BAD_REQUEST);
    }
    return responseEntity;
  }

  @GetMapping("/get-list")
  public ResponseEntity getNotificationsByUserId(@RequestParam String userId) {
    List<NotificationElement> notifications = notificationService.getNotificationsByUserId(userId);
    ResponseEntity responseEntity;
    if(!CollectionUtils.isEmpty(notifications)) {
      responseEntity = new ResponseEntity(notifications, HttpStatus.OK);
    } else {
      responseEntity = new ResponseEntity(ErrorConstant.Err003, HttpStatus.BAD_REQUEST);
    }
    return responseEntity;
  }

}
