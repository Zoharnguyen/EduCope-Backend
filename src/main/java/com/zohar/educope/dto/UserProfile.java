package com.zohar.educope.dto;

import com.zohar.educope.constant.UserType;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Field;

@Getter
@Setter
public class UserProfile {

    @Field(name = "id")
    private String id;

    private String fullName;

    private String rate;

    private String introduction;

    private String urlImageProfile;

    private String phoneNumber;

    private UserType userType;

    private List<AdjustUserProfile> adjustUserProfileList;

    private String adjustStatus = "false";

    private String topicNotification;

    private List<NotificationElement> notifications;

}
