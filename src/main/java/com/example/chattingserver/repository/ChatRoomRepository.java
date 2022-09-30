package com.example.chattingserver.repository;

import com.example.chattingserver.dto.ChatMessage;
import com.example.chattingserver.redis.RedisSubscriber;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Repository
public class ChatRoomRepository {

    private static final String CHAT_MESSAGE = "CHAT_MESSAGE";
    private static final Long CHAT_MESSAGE_MAX_SIZE = 20L;

    // 채팅방(topic)에 발행을 listen
    private final RedisMessageListenerContainer redisMessageListenerContainer;
    // subscribe service
    private final RedisSubscriber redisSubscriber;

    private final RedisTemplate<String, ChatMessage> redisTemplate;
    private ListOperations<String, ChatMessage> chatMessageListOperations;
    private Map<Long, ChannelTopic> topics;

    @PostConstruct
    private void init() {
        chatMessageListOperations = redisTemplate.opsForList();
        topics = new HashMap<>();
    }


    public void insertChatMessage(ChatMessage chatMessage) {
        chatMessageListOperations.leftPush(CHAT_MESSAGE + chatMessage.getProductId(), chatMessage);
        chatMessageListOperations.trim(CHAT_MESSAGE + chatMessage.getMessage(), 0, CHAT_MESSAGE_MAX_SIZE - 1);
    }

    public List<ChatMessage> findALLByProduct(Long productId) {
        return chatMessageListOperations.range(CHAT_MESSAGE + productId, 0, 20);
    }

    public void setTopic(Long productId) {
        if (!topics.containsKey(productId)) {
            ChannelTopic topic = new ChannelTopic(productId.toString());
            redisMessageListenerContainer.addMessageListener(redisSubscriber, topic);
            topics.put(productId, topic);
        }
    }

    public ChannelTopic getTopic(Long productId) {
        return topics.get(productId);
    }
}
