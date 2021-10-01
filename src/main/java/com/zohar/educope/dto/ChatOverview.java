package com.zohar.educope.dto;

import org.springframework.data.mongodb.core.mapping.Field;

public class ChatOverview {

  @Field(name = "chatId")
  private String chatId;
  @Field(name = "senderId")
  private String senderId;
  @Field(name = "messageContent")
  private String messageContent;
  @Field(name = "timeSend")
  private String timeSend;
  @Field(name = "messageStatus")
  private String messageStatus;
  @Field(name = "partnerStatus")
  private String partnerStatus;
  @Field(name = "partnerId")
  private String partnerId;

  public String getPartnerId() {
    return partnerId;
  }

  public void setPartnerId(String partnerId) {
    this.partnerId = partnerId;
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

  public String getMessageContent() {
    return messageContent;
  }

  public void setMessageContent(String messageContent) {
    this.messageContent = messageContent;
  }

  public String getTimeSend() {
    return timeSend;
  }

  public void setTimeSend(String timeSend) {
    this.timeSend = timeSend;
  }

  public String getMessageStatus() {
    return messageStatus;
  }

  public void setMessageStatus(String messageStatus) {
    this.messageStatus = messageStatus;
  }

  public String getPartnerStatus() {
    return partnerStatus;
  }

  public void setPartnerStatus(String partnerStatus) {
    this.partnerStatus = partnerStatus;
  }
}
