package com.zohar.educope.service.common;

import com.zohar.educope.dto.AdjustUserProfile;
import com.zohar.educope.dto.NotificationElement;
import com.zohar.educope.dto.NotificationRequestDto;
import com.zohar.educope.dto.TokenResponse;
import com.zohar.educope.dto.UserBasic;
import com.zohar.educope.dto.UserInformation;
import com.zohar.educope.dto.UserProfile;
import com.zohar.educope.entity.User;

public interface UserService {

    public User createUser(User user);

    public TokenResponse login(UserBasic userBasic);

    public UserProfile getUserProfile(String userId);

    public User addUserInformation(UserInformation userInformation);

    public UserInformation getUserInformation(String userId);

    public User findById(String id);

    public UserProfile adjustUser(AdjustUserProfile adjustUserProfile);

}
