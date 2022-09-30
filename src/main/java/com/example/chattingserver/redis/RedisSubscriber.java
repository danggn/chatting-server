package com.example.chattingserver.redis;

import com.example.chattingserver.dto.ChatMessage;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Slf4j
@Service
public class RedisSubscriber implements MessageListener {

    private final RedisTemplate<String, ChatMessage> redisTemplate;
    private final ObjectMapper objectMapper;
    private final SimpMessageSendingOperations messageTemplate;

    /**
     * Redis 메시지가 발행(publish)되면 대기하고 있던 MessageListener 구현한 RedisSubscriber
     * Listen -> onMessage 메시지를 받아 처리
     */
    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            // redis 발행된 message -> deserialize
            String publishMessage = redisTemplate.getStringSerializer().deserialize(message.getBody());

            // mapping
            ChatMessage productMessage = objectMapper.readValue(publishMessage, ChatMessage.class);

            // send message to subscriber
            messageTemplate.convertAndSend("/sub/chat/product/" + productMessage.getProductId(), productMessage);
        } catch (JsonProcessingException e) {
            log.info("onMessage Error : {}", e.getMessage());
        }
    }
}
