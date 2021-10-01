package com.zohar.educope.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChatOverviewDto {

  private String chatId;
  private String partnerName;
  private String partnerImage;
  private String ownerId;
  private String messageContent;
  private String messageStatus;
  private String timeSend;
  private String partnerStatus;
  private String partnerId;

}
