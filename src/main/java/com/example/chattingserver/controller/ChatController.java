package com.example.chattingserver.controller;

import com.example.chattingserver.dto.ChatMessage;
import com.example.chattingserver.redis.RedisPublisher;
import com.example.chattingserver.repository.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin
@RequiredArgsConstructor
@RestController
public class ChatController {

    private final ChatRoomRepository chatRoomRepository;
    private final RedisPublisher redisPublisher;

    /**
     * websocket "/pub/chat/message" 로 들어오는 메시지를 처리
     */
    @MessageMapping("/chat/message")
    public void message(ChatMessage chatMessage) {
        chatMessage.setSendAt(LocalDateTime.now());

        chatRoomRepository.setTopic(chatMessage.getProductId());

        chatRoomRepository.insertChatMessage(chatMessage);

        // Websocket 발행된 메시지를 redis publish
        ChannelTopic topic = chatRoomRepository.getTopic(chatMessage.getProductId());
        redisPublisher.publish(topic, chatMessage);
    }

    @GetMapping("/products/{productId}/history")
    public List<ChatMessage> getChatMessageHistory(@PathVariable Long productId) {
        List<ChatMessage> messages = chatRoomRepository.findALLByProduct(productId);
        return messages.stream().sorted(Comparator.comparing(ChatMessage::getSendAt)).collect(Collectors.toList());
    }
}
