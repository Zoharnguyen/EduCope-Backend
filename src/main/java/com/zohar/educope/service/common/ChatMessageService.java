package com.zohar.educope.service.common;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.zohar.educope.entity.ChatMessage;
import java.util.List;

public interface ChatMessageService {

  ChatMessage saveChatMessageAndSentToDestination(String body) throws JsonProcessingException;

  List<ChatMessage> getMessagesByChatId(String chatId);

  ChatMessage saveChatMessageAndSentToDestinationObject(ChatMessage chatMessage) throws JsonProcessingException;

}
