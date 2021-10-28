package com.zohar.educope.service.common.impl;

import com.zohar.educope.dto.AdjustUserProfile;
import com.zohar.educope.dto.ChatOverview;
import com.zohar.educope.dto.ChatOverviewDto;
import com.zohar.educope.dto.LocalFile;
import com.zohar.educope.dto.TokenResponse;
import com.zohar.educope.dto.UserBasic;
import com.zohar.educope.dto.UserInformation;
import com.zohar.educope.dto.UserProfile;
import com.zohar.educope.entity.Course;
import com.zohar.educope.entity.User;
import com.zohar.educope.repository.OfferRepos;
import com.zohar.educope.repository.UserRepos;
import com.zohar.educope.service.common.FileService;
import com.zohar.educope.service.common.UserService;
import com.zohar.educope.service.security.JWTService;
import com.zohar.educope.service.security.JWTUserDetailsService;
import com.zohar.educope.service.security.PasswordSecurity;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

  private final Logger logger = LoggerFactory.getLogger(this.getClass());

  @Autowired
  private UserRepos userRepos;

  @Autowired
  private JWTService jwtService;

  @Autowired
  private JWTUserDetailsService jwtUserDetailsService;

  @Autowired
  private OfferRepos offerRepos;

  @Autowired
  private FileService fileService;

  @Override
  public User createUser(User user) {
    PasswordSecurity passwordSecurity = new PasswordSecurity();
    user.setPassword(passwordSecurity.hashPasword(user.getPassword()));
    User userResponse = null;
    try {
      // Set fullName = sign in name
      user.setFullName(user.getGmail());
      userResponse = userRepos.insert(user);
    } catch (Exception e) {
      logger.error("Error when inserting data into mongodb " + e.getMessage());
    }
    return userResponse;
  }

  @Override
  public TokenResponse login(UserBasic userBasic) {
    TokenResponse tokenResponse = null;
    BCryptPasswordEncoder bCrypt = new BCryptPasswordEncoder();
    // Get UserDetails by username
    UserDetails userDetails = jwtUserDetailsService.loadUserByUsername(userBasic.getGmail());
    if (bCrypt.matches(userBasic.getPassword(), userDetails.getPassword())) {
      tokenResponse = new TokenResponse(jwtService.generateToken(userDetails));
      User user = userRepos.findByGmail(userBasic.getGmail());
      tokenResponse.setUserId(user.getId());
    }
    return tokenResponse;
  }

  @Override
  public UserProfile getUserProfile(String userId, boolean firstLoop) {
    Optional<User> userOptional = userRepos.findById(userId);
    if(!userOptional.isPresent()) return null;
    User user = userOptional.get();
    UserProfile userProfile = convertUserToUserProfile(user);
    // Convert imageId to base64 for response
    if (Objects.nonNull(userProfile)) {
      userProfile.setUrlImageProfile(convertImageIdToBase64(userProfile.getUrlImageProfile()));
    }
    // Get latest information for list adjusted user of this userProfile
    if (firstLoop) {
      loadLatestInforOfListAdjustedUsersForUserProfile(userProfile);
    }
    return userProfile;
  }

  @Override
  public User addUserInformation(UserInformation userInformation) {
    User user = userRepos.findById(userInformation.getId()).get();
    User userResponse = null;
    if (user == null) {
      return userResponse;
    }
    updateUserInformation(user, userInformation);
    try {
      userResponse = userRepos.save(user);
    } catch (Exception e) {
      logger.error("Error when updating data into mongodb " + e.getMessage());
    }
    return userResponse;
  }

  @Override
  public UserInformation getUserInformation(String userId) {
    User user = userRepos.findById(userId).get();
    UserInformation userInformation = convertUserToUserInformation(user);
    if (Objects.nonNull(userInformation)) {
      userInformation.setUrlImageProfile(
          convertImageIdToBase64(userInformation.getUrlImageProfile()));
    }
    return userInformation;
  }

  @Override
  public User findById(String id) {
    return userRepos.findById(id).get();
  }

  @Override
  public UserProfile adjustUser(AdjustUserProfile adjustUserProfile) {
    try {
      if (adjustUserProfile.getUserBeAdjustedId() == null) {
        return null;
      }
      Optional<User> userOptional = userRepos.findById(adjustUserProfile.getUserBeAdjustedId());
      if (userOptional.isPresent()) {
        adjustUserProfile
            .setDateAdjust(new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date()));
        adjustUserProfile.setAdjustId(UUID.randomUUID().toString());

        //Update image to adjustUserProfile.
        if (adjustUserProfile.getUserAdjust() != null
            && adjustUserProfile.getUserAdjust().getId() != null) {
          // Get imageId of userAdjust
          Optional<User> userAdjustOptional = userRepos.findById(
              adjustUserProfile.getUserBeAdjustedId());
          // Get image from imageId
          LocalFile imageFile = fileService.getFileById(
              userAdjustOptional.get().getUrlImageProfile());
          // Convert file image to base64
          String contentImageFile = Base64.getEncoder().encodeToString(imageFile.getFile());
          adjustUserProfile.getUserAdjust().setUrlImageProfile(contentImageFile);
        }

        List<AdjustUserProfile> adjustUserProfileList = userOptional.get()
            .getAdjustUserProfileList();
        if (adjustUserProfileList != null) {
          adjustUserProfileList.add(adjustUserProfile);
        } else {
          userOptional.get().setAdjustUserProfileList(Arrays.asList(adjustUserProfile));
        }
      }
      calculateRatingForUser(userOptional.get());
      UserProfile userProfile = convertUserToUserProfile(userRepos.save(userOptional.get()));
      updateStatusAdjustForMemInClass(adjustUserProfile);
      return userProfile;
    } catch (Exception e) {
      logger.error("Error when inserting data into mongodb " + e.getMessage());
    }
    return null;
  }

  @Override
  public List<ChatOverviewDto> getListChat(String userId) {
    try {
      Optional<User> userOptional = userRepos.findById(userId);
      if (userOptional.isPresent()) {
        return covertChatOverviewToDto(userOptional.get().getChatOverviews());
      }
    } catch (Exception e) {
      logger.error("Error when get list chatOverview from mongodb " + e.getMessage());
    }
    return null;
  }

  public ChatOverview createNewChatOverview(ChatOverview chatOverview, String userId) {
    try {
      Optional<User> userOptional = userRepos.findById(userId);
      if (userOptional.isPresent()) {
        if (userOptional.get().getChatOverviews() == null) {
          userOptional.get().setChatOverviews(new ArrayList<>());
        }
        fulfillValueForChatOverview(chatOverview);
        userOptional.get().getChatOverviews().add(chatOverview);
        log.info("Save new chatOverview into db.");
        userRepos.save(userOptional.get());
        return chatOverview;
      }
    } catch (Exception e) {
      logger.error("Error when save chatOverview from mongodb " + e.getMessage());
    }
    return null;
  }

  public ChatOverview updateChatOverview(ChatOverview chatOverview, String userId) {
    try {
      Optional<User> userOptional = userRepos.findById(userId);
      if (userOptional.isPresent()) {
        if (userOptional.get().getChatOverviews() == null) {
          log.debug("User with id {} don't have any chatOverview", userId);
          return null;
        }
        ;
        updateLastMessageForChatOverview(userOptional.get().getChatOverviews(), chatOverview);
        userRepos.save(userOptional.get());
        return chatOverview;
      }
    } catch (Exception e) {
      logger.error("Error when update chatOverview from mongodb " + e.getMessage());
    }
    return null;
  }

  private void updateLastMessageForChatOverview(List<ChatOverview> chatOverviews,
      ChatOverview chatOverviewUpdate) {
    for (int i = 0; i < chatOverviews.size(); i++) {
      if (chatOverviews.get(i).getChatId().equals(chatOverviewUpdate.getChatId())) {
        chatOverviews.get(i).setMessageContent(chatOverviewUpdate.getMessageContent());
        chatOverviews.get(i).setMessageStatus("unread");
        chatOverviews.get(i).setTimeSend(chatOverviewUpdate.getTimeSend());
        break;
      }
    }
  }

  private void fulfillValueForChatOverview(ChatOverview chatOverview) {
    if (StringUtils.isEmpty(chatOverview.getChatId())) {
      chatOverview.setChatId(UUID.randomUUID().toString());
    }
    chatOverview.setTimeSend(new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date()));
    chatOverview.setMessageStatus("unread");
  }

  private List<ChatOverviewDto> covertChatOverviewToDto(List<ChatOverview> chatOverviews) {
    List<ChatOverviewDto> chatOverviewDtos = new ArrayList<>();
    for (ChatOverview chatOverview : chatOverviews) {
      ChatOverviewDto chatOverviewDto = new ChatOverviewDto();
      chatOverviewDto.setChatId(chatOverview.getChatId());
      chatOverviewDto.setMessageContent(chatOverview.getMessageContent());
      chatOverviewDto.setMessageStatus(chatOverview.getMessageStatus());
      chatOverviewDto.setPartnerId(chatOverview.getPartnerId());
      chatOverviewDto.setTimeSend(chatOverview.getTimeSend());
      setImageAndNamePartner(chatOverviewDto, chatOverview.getPartnerId());
      chatOverviewDtos.add(chatOverviewDto);
    }
    return chatOverviewDtos;
  }

  private void setImageAndNamePartner(ChatOverviewDto chatOverviewDto, String partner) {
    if (StringUtils.isEmpty(partner)) {
      return;
    }
    UserProfile userProfilePartner = getUserProfile(partner, false);
    if (Objects.nonNull(userProfilePartner)) {
      chatOverviewDto.setPartnerName(userProfilePartner.getFullName());
      chatOverviewDto.setPartnerImage(userProfilePartner.getUrlImageProfile());
    }
  }

  private void updateStatusAdjustForMemInClass(AdjustUserProfile adjustUserProfile) {
    Optional<Course> courseOptional = offerRepos.findById(adjustUserProfile.getCourseId());
    if (courseOptional.isPresent()) {
      List<UserProfile> userProfiles = courseOptional.get().getMemberClassList();
      for (int i = 0; i < userProfiles.size(); i++) {
        if (userProfiles.get(i).getId().equals(adjustUserProfile.getUserBeAdjustedId())) {
          userProfiles.get(i).setAdjustStatus("true");
        }
      }
      courseOptional.get().setMemberClassList(userProfiles);
      offerRepos.save(courseOptional.get());
    }
  }

  private void calculateRatingForUser(User user) {
    if (user.getAdjustUserProfileList() != null) {
      int rateTotal = 0;
      int count = 0;
      for (AdjustUserProfile adjustUserProfile : user.getAdjustUserProfileList()) {
        if (adjustUserProfile.getRate() != null) {
          try {
            int rateAdjust = Integer.parseInt(adjustUserProfile.getRate());
            rateTotal = rateTotal + rateAdjust;
            count++;
          } catch (Exception e) {
            log.error("Error when parse rate {}", adjustUserProfile.getRate());
          }
        }
      }
      try {
        String rateUser = String.valueOf(Math.round(rateTotal / count));
        user.setRate(rateUser);
      } catch (Exception e) {
        log.error("Error when parse with rateTotal = {} and count = {}", rateTotal, count);
      }
    }
  }

  private UserProfile convertUserToUserProfile(User user) {
    if (user == null) {
      return null;
    }
    UserProfile userProfile = new UserProfile();
    userProfile.setId(user.getId());
    userProfile.setUrlImageProfile(user.getUrlImageProfile());
    userProfile.setFullName(user.getFullName());
    userProfile.setIntroduction(user.getIntroduction());
    userProfile.setRate(user.getRate());
    userProfile.setUserType(user.getUserType());
    userProfile.setAdjustUserProfileList(user.getAdjustUserProfileList());
    userProfile.setTopicNotification(user.getTopicNotification());
    userProfile.setNotifications(user.getNotifications());
    return userProfile;
  }

  private void updateUserInformation(User user, UserInformation userInformation) {
    user.setAddress(userInformation.getAddress());
    user.setCertificate(userInformation.getCertificate());
    user.setDoB(userInformation.getDoB());
    user.setExperience(userInformation.getExperience());
    user.setGender(userInformation.getGender());
    user.setPhone(userInformation.getPhoneNumber());
    user.setFullName(userInformation.getFullName());
    user.setIntroduction(userInformation.getIntroduction());
    user.setRate(userInformation.getRate());
    if (!StringUtils.isEmpty(userInformation.getUrlImageProfile())) {
      user.setUrlImageProfile(userInformation.getUrlImageProfile());
    }
  }

  private UserInformation convertUserToUserInformation(User user) {
    if (user == null) {
      return null;
    }
    UserInformation userInformation = new UserInformation();
    userInformation.setId(user.getId());
    userInformation.setAddress(user.getAddress());
    userInformation.setCertificate(user.getCertificate());
    userInformation.setDoB(user.getDoB());
    userInformation.setExperience(user.getExperience());
    userInformation.setGender(user.getGender());
    userInformation.setPhoneNumber(user.getPhone());
    userInformation.setFullName(user.getFullName());
    userInformation.setIntroduction(user.getIntroduction());
    userInformation.setRate(user.getRate());
    userInformation.setUrlImageProfile(user.getUrlImageProfile());
    return userInformation;
  }

  private String convertImageIdToBase64(String imageId) {
    String base64Image = imageId;
    if (!StringUtils.isEmpty(imageId)) {
      try {
        LocalFile imageFile = fileService.getFileById(imageId);
        base64Image = Base64.getEncoder().encodeToString(imageFile.getFile());
      } catch (Exception e) {
        logger.error("Can not convert imageId to base64 ", e);
      }
    }
    return base64Image;
  }

  private void loadLatestInforOfListAdjustedUsersForUserProfile(UserProfile userProfile) {
    if (!CollectionUtils.isEmpty(userProfile.getAdjustUserProfileList())) {
      for (int i = 0; i < userProfile.getAdjustUserProfileList().size(); i++) {
        userProfile.getAdjustUserProfileList().get(i).setUserAdjust(
            getUserProfileForAdjustedUser(userProfile.getAdjustUserProfileList().get(i)));
      }
    }
  }

  private UserProfile getUserProfileForAdjustedUser(AdjustUserProfile adjustUserProfile) {
    if (Objects.nonNull(adjustUserProfile)) {
      if (Objects.nonNull(adjustUserProfile.getUserAdjust()) && !StringUtils.isEmpty(
          adjustUserProfile.getUserAdjust().getId())) {
        return getUserProfile(adjustUserProfile.getUserAdjust().getId(), false);
      }
      return adjustUserProfile.getUserAdjust();
    }
    return null;
  }

}
