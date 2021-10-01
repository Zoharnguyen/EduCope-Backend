package com.zohar.educope.service.common;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zohar.educope.dto.ChatOverview;
import com.zohar.educope.dto.ChatOverviewDto;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class RabbitService {

  @Autowired
  private AmqpTemplate amqpTemplate;

  @Value("${educope.rabbitmq.exchange}")
  private String exchange;

  @Value("${educope.rabbitmq.routingkey}")
  private String routingkey;

  @Autowired
  private UserService userService;

  public void sendMessage(ChatOverviewDto chatOverviewDto) {
    ObjectMapper mapper = new ObjectMapper();
    try {
      amqpTemplate.convertAndSend(exchange, routingkey, mapper.writeValueAsString(chatOverviewDto));
    } catch (JsonProcessingException e) {
      e.printStackTrace();
    }
    System.out.println("Send msg = " + chatOverviewDto);
  }

  @RabbitListener(queues = "${educope.rabbitmq.queue}")
  public void recievedMessage(String messageBody) {
    System.out.println("Recieved Message From RabbitMQ: " + messageBody);
    ObjectMapper mapper = new ObjectMapper();
    ChatOverviewDto chatOverviewDto = null;
    try {
      chatOverviewDto = mapper.readValue(messageBody, ChatOverviewDto.class);
      if (Objects.isNull(chatOverviewDto)) {
        log.error("Message is wrong format {}", chatOverviewDto);
      } else if (chatOverviewDto.getOwnerId() != null) {
        ChatOverview chatOverview = convertChatOverviewDtoToChatOverview(chatOverviewDto);
        if (Objects.isNull(
            userService.updateChatOverview(chatOverview, chatOverviewDto.getOwnerId()))) {
          log.error("Can not update last message to list chat of userId {}",
              chatOverviewDto.getOwnerId());
        } else {
          log.info("Update last message to list chat success!!!");
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private ChatOverview convertChatOverviewDtoToChatOverview(ChatOverviewDto chatOverviewDto) {
    ChatOverview chatOverview = new ChatOverview();
    chatOverview.setChatId(chatOverviewDto.getChatId());
    chatOverview.setTimeSend(new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date()));
    chatOverview.setMessageContent(chatOverviewDto.getMessageContent());
    return chatOverview;
  }

}
