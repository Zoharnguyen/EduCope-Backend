package com.zohar.educope.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "chatMessage")
public class ChatMessage {

  @Id
  private String id;

  @Field(name = "chatId")
  private String chatId;

  @Field(name = "senderId")
  private String senderId;

  @Field(name = "receiverId")
  private String receiverId;

  @Field(name = "timeChat")
  private String timeChat;

  @Field(name = "messageContent")
  private String messageContent;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getChatId() {
    return chatId;
  }

  public void setChatId(String chatId) {
    this.chatId = chatId;
  }

  public String getSenderId() {
    return senderId;
  }

  public void setSenderId(String senderId) {
    this.senderId = senderId;
  }

  public String getReceiverId() {
    return receiverId;
  }

  public void setReceiverId(String receiverId) {
    this.receiverId = receiverId;
  }

  public String getTimeChat() {
    return timeChat;
  }

  public void setTimeChat(String timeChat) {
    this.timeChat = timeChat;
  }

  public String getMessageContent() {
    return messageContent;
  }

  public void setMessageContent(String messageContent) {
    this.messageContent = messageContent;
  }
}