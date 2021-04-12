package com.zohar.educope.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdjustUserProfile {

  private String dateAdjust;
  private UserProfile userAdjust;
  private String rate;
  private String content;
  private String adjustId;
  private String userBeAdjustedId;
  private String courseId;

}
