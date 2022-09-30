package com.example.chattingserver.redis;

import com.example.chattingserver.dto.ChatMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class RedisPublisher {

    private final RedisTemplate<String, ChatMessage> redisTemplate;

    public void publish(ChannelTopic channelTopic, ChatMessage message) {
        redisTemplate.convertAndSend(channelTopic.getTopic(), message);
    }
}
