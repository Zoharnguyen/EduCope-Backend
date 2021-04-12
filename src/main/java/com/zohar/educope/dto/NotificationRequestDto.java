package com.zohar.educope.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NotificationRequestDto {

  private String target;
  private String title;
  private NotificationElement notificationElement;

}
