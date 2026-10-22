package com.chatwithredis.service;

import com.chatwithredis.dto.*;
import com.chatwithredis.entity.ChatMessage;
import com.chatwithredis.repository.ChatMessageRepository;
import com.chatwithredis.repository.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import java.util.Locale;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class ChatService {
 private final ChatMessageRepository messages;
 private final UserRepository users;
 private final StringRedisTemplate redis;
 private final ChannelTopic topic;
 private final ObjectMapper mapper;

 public ChatService(ChatMessageRepository messages, UserRepository users, StringRedisTemplate redis, ChannelTopic chatTopic, ObjectMapper mapper) {
  this.messages=messages; this.users=users; this.redis=redis; this.topic=chatTopic; this.mapper=mapper;
 }

 public String directConversation(String currentUser, String otherUser) {
  String other=otherUser == null ? "" : otherUser.trim();
  if(other.isBlank() || other.equalsIgnoreCase(currentUser)) throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Choose another user to start a private chat");
  String actual=users.findByUsernameIgnoreCase(other).map(u->u.getUsername()).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,"User not found"));
  return directId(currentUser,actual);
 }

 public boolean canAccessConversation(String username, String conversationId) {
  if(username == null || conversationId == null) return false;
  if("general".equals(conversationId)) return true;
  String[] parts=conversationId.split(":",-1);
  if(parts.length!=3 || !"dm".equals(parts[0])) return false;
  String first=parts[1], second=parts[2], user=username.toLowerCase(Locale.ROOT);
  if(first.isBlank() || second.isBlank() || first.equals(second)) return false;
  if(!first.equals(user) && !second.equals(user)) return false;
  String other=first.equals(user)?second:first;
  return users.findByUsernameIgnoreCase(other).isPresent() && conversationId.equals(directId(first,second));
 }

 private String directId(String first, String second) {
  String a=first.toLowerCase(Locale.ROOT), b=second.toLowerCase(Locale.ROOT);
  return a.compareTo(b)<=0 ? "dm:"+a+":"+b : "dm:"+b+":"+a;
 }

 public ChatMessageResponse send(String username, MessageRequest request) {
  String conversationId=request.conversationId().trim();
  if(!canAccessConversation(username,conversationId)) throw new ResponseStatusException(HttpStatus.FORBIDDEN,"You are not a participant in this conversation");
  ChatMessage saved=messages.save(new ChatMessage(conversationId,username,request.content().trim()));
  ChatMessageResponse response=ChatMessageResponse.from(saved);
  try { redis.convertAndSend(topic.getTopic(),mapper.writeValueAsString(response)); }
  catch(JsonProcessingException ex) { throw new IllegalStateException("Could not publish chat message",ex); }
  return response;
 }

 public List<ChatMessageResponse> history(String username, String conversationId) {
  if(!canAccessConversation(username,conversationId)) throw new ResponseStatusException(HttpStatus.FORBIDDEN,"You are not a participant in this conversation");
  return messages.findTop100ByConversationIdOrderByCreatedAtDesc(conversationId).stream().map(ChatMessageResponse::from).sorted(java.util.Comparator.comparing(ChatMessageResponse::createdAt)).toList();
 }
}