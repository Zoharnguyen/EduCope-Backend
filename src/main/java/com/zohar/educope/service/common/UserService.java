package com.zohar.educope.service.common;

import com.zohar.educope.dto.AdjustUserProfile;
import com.zohar.educope.dto.ChatOverview;
import com.zohar.educope.dto.ChatOverviewDto;
import com.zohar.educope.dto.TokenResponse;
import com.zohar.educope.dto.UserBasic;
import com.zohar.educope.dto.UserInformation;
import com.zohar.educope.dto.UserProfile;
import com.zohar.educope.entity.User;
import java.util.List;

public interface UserService {

  public User createUser(User user);

  public TokenResponse login(UserBasic userBasic);

  public UserProfile getUserProfile(String userId, boolean fistLoop);

  public User addUserInformation(UserInformation userInformation);

  public UserInformation getUserInformation(String userId);

  public User findById(String id);

  public UserProfile adjustUser(AdjustUserProfile adjustUserProfile);

  public List<ChatOverviewDto> getListChat(String userId);

  public ChatOverview createNewChatOverview(ChatOverview chatOverview, String userId);

  ChatOverview updateChatOverview(ChatOverview chatOverview, String userId);

}
