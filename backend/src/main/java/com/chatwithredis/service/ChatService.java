package com.chatwithredis.service;
import com.chatwithredis.dto.*;
import com.chatwithredis.entity.ChatMessage;
import com.chatwithredis.repository.ChatMessageRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;
@Service
public class ChatService {
 private final ChatMessageRepository messages; private final StringRedisTemplate redis; private final ChannelTopic topic; private final ObjectMapper mapper;
 public ChatService(ChatMessageRepository messages,StringRedisTemplate redis,ChannelTopic chatTopic,ObjectMapper mapper){this.messages=messages;this.redis=redis;this.topic=chatTopic;this.mapper=mapper;}
 public ChatMessageResponse send(String username,MessageRequest request){
  ChatMessage saved=messages.save(new ChatMessage(request.conversationId().trim(),username,request.content().trim()));
  ChatMessageResponse response=ChatMessageResponse.from(saved);
  try{redis.convertAndSend(topic.getTopic(),mapper.writeValueAsString(response));}
  catch(JsonProcessingException ex){throw new IllegalStateException("Could not publish chat message",ex);}
  return response;
 }
 public List<ChatMessageResponse> history(String conversationId){
  return messages.findTop100ByConversationIdOrderByCreatedAtDesc(conversationId).stream().map(ChatMessageResponse::from).sorted(java.util.Comparator.comparing(ChatMessageResponse::createdAt)).toList();
 }
}