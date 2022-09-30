package com.example.chattingserver.dto;

import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ChatMessage implements Serializable {

    private Long productId;
    private String authorId; // same with author
    private String author;
    private String message;
    private MessageType messageType;
    @Setter
    private LocalDateTime sendAt;

    @Builder
    public ChatMessage(Long productId, String author, String message, MessageType messageType) {
        this.productId = productId;
        this.authorId = author;
        this.author = author;
        this.message = message;
        this.messageType = messageType;
    }
}
