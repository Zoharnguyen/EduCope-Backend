package com.zohar.educope.service.common;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import com.zohar.educope.dto.NotificationElement;
import com.zohar.educope.dto.NotificationRequestDto;
import com.zohar.educope.dto.SubscriptionRequestDto;
import com.zohar.educope.entity.User;
import com.zohar.educope.repository.UserRepos;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import javax.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestParam;

@Slf4j
@Service
public class NotificationService {

  @Value("${app.firebase-config}")
  private String firebaseConfig;

  @Autowired
  private UserRepos userRepos;

  private FirebaseApp firebaseApp;

  @PostConstruct
  private void initialize() {
    try {
      FirebaseOptions options = new FirebaseOptions.Builder()
          .setCredentials(
              GoogleCredentials.fromStream(new ClassPathResource(firebaseConfig).getInputStream()))
          .build();

      if (FirebaseApp.getApps().isEmpty()) {
        this.firebaseApp = FirebaseApp.initializeApp(options);
      } else {
        this.firebaseApp = FirebaseApp.getInstance();
      }
    } catch (IOException e) {
      log.error("Create FirebaseApp Error", e);
    }
  }

  public boolean subscribeToTopic(SubscriptionRequestDto subscriptionRequestDto) {
    try {
      FirebaseMessaging.getInstance(firebaseApp)
          .subscribeToTopic(subscriptionRequestDto.getTokens(),
              subscriptionRequestDto.getTopicName());
    } catch (FirebaseMessagingException e) {
      log.error("Firebase subscribe to topic fail", e);
      return false;
    }
    return true;
  }

  public boolean unsubscribeFromTopic(SubscriptionRequestDto subscriptionRequestDto) {
    try {
      FirebaseMessaging.getInstance(firebaseApp)
          .unsubscribeFromTopic(subscriptionRequestDto.getTokens(),
              subscriptionRequestDto.getTopicName());
    } catch (FirebaseMessagingException e) {
      log.error("Firebase unsubscribe from topic fail", e);
      return false;
    }
    return true;
  }

  public String sendPnsToDevice(NotificationRequestDto notificationRequestDto) {
    Message message = Message.builder()
        .setToken(notificationRequestDto.getTarget())
        .setNotification(
            new Notification(notificationRequestDto.getTitle(),
                getContentNotification(notificationRequestDto.getNotificationElement())))
        .putData("content", notificationRequestDto.getTitle())
        .putData("body", getContentNotification(notificationRequestDto.getNotificationElement()))
        .build();

    String response = null;
    try {
      response = FirebaseMessaging.getInstance().send(message);
    } catch (FirebaseMessagingException e) {
      log.error("Fail to send firebase notification", e);
    }

    return response;
  }

  public NotificationElement sendPnsToTopic(NotificationRequestDto notificationRequestDto,
      String receiverId) {
    Optional<User> userOptional = userRepos.findById(receiverId);
    if (!userOptional.isPresent()) {
      return null;
    }
    if (notificationRequestDto.getNotificationElement() == null) {
      return null;
    }
    if (userOptional.get().getNotifications() == null) {
      userOptional.get().setNotifications(new ArrayList<>());
    }
    Message message = Message.builder()
        .setTopic(notificationRequestDto.getTarget())
        .setNotification(
            new Notification(notificationRequestDto.getTitle(),
                getContentNotification(notificationRequestDto.getNotificationElement())))
        .putData("content", notificationRequestDto.getTitle())
        .putData("body", getContentNotification(notificationRequestDto.getNotificationElement()))
        .build();
    try {
      FirebaseMessaging.getInstance().send(message);
      notificationRequestDto.getNotificationElement()
          .setNotificationId(UUID.randomUUID().toString());
      notificationRequestDto.getNotificationElement().setSeeStatus("false");
      notificationRequestDto.getNotificationElement()
          .setTimeCreated(new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date()));
      userOptional.get().getNotifications().add(notificationRequestDto.getNotificationElement());
      userRepos.save(userOptional.get());
      return notificationRequestDto.getNotificationElement();
    } catch (FirebaseMessagingException e) {
      log.error("Fail to send firebase notification", e);
    }
    return null;
  }

  public List<NotificationElement> getNotificationsByUserId(@RequestParam String userId) {
    List<NotificationElement> notificationElements = new ArrayList<>();
    try {
      Optional<User> userOptional = userRepos.findById(userId);
      if (!userOptional.isPresent()) {
        return null;
      }
      List<NotificationElement> notificationElementsResponse = userOptional.get()
          .getNotifications();
      if (!CollectionUtils.isEmpty(notificationElementsResponse)) {
        Collections.reverse(notificationElementsResponse);
      }
      return notificationElementsResponse;
    } catch (Exception e) {
      log.error("Fail to get notifications", e);
    }
    return notificationElements;
  }

  private String getContentNotification(NotificationElement notificationElement) {
    if (notificationElement != null && notificationElement.getContent() != null) {
      return notificationElement.getContent();
    }
    return "";
  }

}
