package com.example.chattingserver.service;

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

    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper objectMapper;
    private final SimpMessageSendingOperations messageTemplate;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            String publishMessage = redisTemplate.getStringSerializer().deserialize(message.getBody());

            ChatMessage productMessage = objectMapper.readValue(publishMessage, ChatMessage.class);

            // send message
            messageTemplate.convertAndSend("/sub/chat/product/" + productMessage.getProductId(), productMessage);
        } catch (JsonProcessingException e) {
            log.info("onMessage Error : {}", e.getMessage());
        }
    }
}
