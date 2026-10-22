package com.chatwithredis.service;
import com.chatwithredis.dto.ChatMessageResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.redis.connection.*;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
@Component
public class RedisChatSubscriber implements MessageListener {
 private final ObjectMapper mapper; private final SimpMessagingTemplate messaging;
 public RedisChatSubscriber(ObjectMapper mapper,SimpMessagingTemplate messaging){this.mapper=mapper;this.messaging=messaging;}
 @Override public void onMessage(Message message,byte[] pattern){
  try{ChatMessageResponse event=mapper.readValue(message.getBody(),ChatMessageResponse.class);messaging.convertAndSend("/topic/conversations/"+event.conversationId(),event);}
  catch(Exception ex){ /* Ignore malformed events; listener container remains alive. */ }
 }
}