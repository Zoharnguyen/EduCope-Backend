package com.zohar.educope.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.zohar.educope.constant.ErrorConstant;
import com.zohar.educope.dto.ChatOverviewDto;
import com.zohar.educope.entity.ChatMessage;
import com.zohar.educope.service.common.ChatMessageService;
import com.zohar.educope.service.common.RabbitService;
import io.jsonwebtoken.lang.Collections;
import java.util.List;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class ChatController {

  @Autowired
  ChatMessageService chatMessageService;

  @Autowired
  RabbitService rabbitService;

  // Test send message to socket
  @PostMapping("/send")
  public ResponseEntity<Void> sendMessage(@RequestBody ChatMessage chatMessage) {
    try {
      chatMessageService.saveChatMessageAndSentToDestinationObject(chatMessage);
    } catch (JsonProcessingException e) {
      e.printStackTrace();
    }
    return new ResponseEntity<>(HttpStatus.OK);
  }

  @MessageMapping("/sendMessage")
  public void receiveMessage(@Payload String body) {
    log.info("Receive message from app: {}", body);
    // receive message from client
    try {
      ChatMessage chatMessage = chatMessageService.saveChatMessageAndSentToDestination(body);
      if (Objects.nonNull(chatMessage)) {
        System.out.println("Message received: " + chatMessage.getMessageContent());
      }
      log.info("Saved message into db and send message to destination success!");
    } catch (JsonProcessingException e) {
      e.printStackTrace();
    }
  }

  @GetMapping("/chat")
  public ResponseEntity getMessagesByChatId(@RequestParam(name = "chatId") String chatId) {
    log.info("Start get list chatMessage from db.");
    List<ChatMessage> chatMessageList = chatMessageService.getMessagesByChatId(chatId);
    ResponseEntity responseEntity;
    if (!Collections.isEmpty(chatMessageList)) {
      responseEntity = new ResponseEntity(chatMessageList, HttpStatus.OK);
    } else {
      responseEntity = new ResponseEntity(ErrorConstant.Err003, HttpStatus.BAD_REQUEST);
    }
    return responseEntity;
  }

  // Test send message to RabbitMQ
  @PostMapping(value = "/producer")
  public String producer(@RequestBody ChatOverviewDto chatOverviewDto) {

    rabbitService.sendMessage(chatOverviewDto);

    return "Message sent to the RabbitMQ Successfully";
  }

}
