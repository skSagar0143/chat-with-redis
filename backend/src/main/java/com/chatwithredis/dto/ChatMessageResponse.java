package com.chatwithredis.dto;
import com.chatwithredis.entity.ChatMessage;
import java.time.Instant;
public record ChatMessageResponse(Long id,String conversationId,String sender,String content,Instant createdAt){
 public static ChatMessageResponse from(ChatMessage m){return new ChatMessageResponse(m.getId(),m.getConversationId(),m.getSender(),m.getContent(),m.getCreatedAt());}
}