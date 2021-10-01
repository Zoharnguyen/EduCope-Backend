package com.zohar.educope.service.common.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zohar.educope.dto.ChatOverview;
import com.zohar.educope.dto.ChatOverviewDto;
import com.zohar.educope.entity.ChatMessage;
import com.zohar.educope.repository.ChatMessageRepos;
import com.zohar.educope.repository.UserRepos;
import com.zohar.educope.service.common.ChatMessageService;
import com.zohar.educope.service.common.UserService;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

@Service
@Slf4j
public class ChatMessageServiceImpl implements ChatMessageService {

  @Autowired
  SimpMessagingTemplate template;

  @Autowired
  ChatMessageRepos chatMessageRepos;

  @Autowired
  UserRepos userRepos;

  @Autowired
  UserService userService;

  @Override
  public ChatMessage saveChatMessageAndSentToDestination(String body)
      throws JsonProcessingException {
    ChatMessage chatMessage = covertJsonToChatMessage(body);
    // Fulfill some information for chatMessage and create chatOverview if this is the first chat between 2 users
    fulfillChatMessage(chatMessage);
    String messageDestination = "/chat/" + chatMessage.getChatId();
    log.info("Send message to destination: {}", messageDestination);
    template.convertAndSend(messageDestination, chatMessage);
    log.info("Save message into db: {}", new ObjectMapper().writeValueAsString(chatMessage));
    chatMessageRepos.save(chatMessage);
    return chatMessage;
  }

  @Override
  public List<ChatMessage> getMessagesByChatId(String chatId) {
    if (StringUtils.isEmpty(chatId)) {
      return null;
    }
    try {
      return chatMessageRepos.findChatMessagesByChatIdOrderByTimeChatAsc(chatId);
    } catch (Exception e) {
      log.error("Error when get list chatMessage from db: {}", e);
    }
    return null;
  }

  @Override
  public ChatMessage saveChatMessageAndSentToDestinationObject(ChatMessage chatMessage)
      throws JsonProcessingException {
    fulfillChatMessage(chatMessage);
    String messageDestination = "/chat/" + chatMessage.getChatId();
    log.info("Send message to destination: {}", messageDestination);
    template.convertAndSend(messageDestination, chatMessage);
    log.info("Save message into db: {}", new ObjectMapper().writeValueAsString(chatMessage));
    chatMessageRepos.save(chatMessage);
    return chatMessage;
  }

  private void fulfillChatMessage(ChatMessage chatMessage) {
    chatMessage.setTimeChat(new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date()));
    // Check if chatId is empty or this is the first chat message between 2 users
    if (StringUtils.isEmpty(chatMessage.getChatId()) || checkTheFirstTimeChatOf2Users(chatMessage,
        chatMessage.getSenderId(),
        chatMessage.getReceiverId())) {
      checkAndCreateChatOverviewForUser(chatMessage, chatMessage.getSenderId(),
          chatMessage.getReceiverId());
      checkAndCreateChatOverviewForUser(chatMessage, chatMessage.getReceiverId(),
          chatMessage.getSenderId());
    }
  }

  private void checkAndCreateChatOverviewForUser(ChatMessage chatMessage, String ownerId,
      String partnerId) {
    //Create new ChatOverview and add chatId to ChatMessage
    ChatOverview chatOverview = setValueChatMessageForChatOverview(chatMessage, ownerId,
        partnerId);
    ChatOverview result = userService.createNewChatOverview(chatOverview, ownerId);
    chatMessage.setChatId(result.getChatId());
  }

  private boolean checkTheFirstTimeChatOf2Users(ChatMessage chatMessage, String ownerId,
      String partnerId) {
    List<ChatOverviewDto> chatOverviewDtos = userService.getListChat(ownerId);
    if (!CollectionUtils.isEmpty(chatOverviewDtos)) {
      // Set chatId for existed chat
      for (ChatOverviewDto chatOverviewDto : chatOverviewDtos) {
        if (chatOverviewDto.getPartnerId().equals(partnerId)) {
          chatMessage.setChatId(chatOverviewDto.getChatId());
          return false;
        }
      }
    }
    return true;
  }

  private ChatOverview setValueChatMessageForChatOverview(ChatMessage chatMessage, String senderId,
      String partnerId) {
    ChatOverview chatOverview = new ChatOverview();
    if (!StringUtils.isEmpty(chatMessage.getChatId())) {
      chatOverview.setChatId(chatMessage.getChatId());
    }
    chatOverview.setPartnerId(partnerId);
    chatOverview.setMessageContent(chatMessage.getMessageContent());
    return chatOverview;
  }

  private ChatMessage covertJsonToChatMessage(String body) throws JsonProcessingException {
    if (StringUtils.isEmpty(body)) {
      return new ChatMessage();
    }
    ObjectMapper objectMapper = new ObjectMapper();
    return objectMapper.readValue(body, ChatMessage.class);
  }

}
