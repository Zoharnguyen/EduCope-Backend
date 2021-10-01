package com.zohar.educope.repository;

import com.zohar.educope.entity.ChatMessage;
import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatMessageRepos extends MongoRepository<ChatMessage, String> {

  List<ChatMessage> findChatMessagesByChatIdOrderByTimeChatAsc(String chatId);

}
