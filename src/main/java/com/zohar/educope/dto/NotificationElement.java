package com.zohar.educope.dto;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NotificationElement {

  private String notificationId;
  private String screenName;
  private List<String> screenVariables;
  private String sender;
  private String iconSenderUrl;
  private String content;
  private String timeCreated;
  private String seeStatus;

}
