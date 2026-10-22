package com.chatwithredis.service;
import java.time.Duration;
import java.util.*;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
@Service
public class PresenceService {
 private static final String INDEX="presence:users"; private final StringRedisTemplate redis;
 public PresenceService(StringRedisTemplate redis){this.redis=redis;}
 public void heartbeat(String username){redis.opsForValue().set("presence:user:"+username,"online",Duration.ofSeconds(60));redis.opsForSet().add(INDEX,username);}
 public List<String> onlineUsers(){
  Set<String> users=redis.opsForSet().members(INDEX);if(users==null)return List.of();List<String> online=new ArrayList<>();
  for(String user:users){if(Boolean.TRUE.equals(redis.hasKey("presence:user:"+user)))online.add(user);else redis.opsForSet().remove(INDEX,user);}
  return online.stream().sorted().toList();
 }
 public void offline(String username){redis.delete("presence:user:"+username);redis.opsForSet().remove(INDEX,username);}
}