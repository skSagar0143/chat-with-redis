package com.chatwithredis.config;
import com.chatwithredis.service.JwtService;
import org.springframework.messaging.*;
import org.springframework.messaging.simp.stomp.*;
import org.springframework.messaging.support.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.stereotype.Component;
@Component
public class StompAuthChannelInterceptor implements ChannelInterceptor {
 private final JwtService jwt; public StompAuthChannelInterceptor(JwtService jwt){this.jwt=jwt;}
 @Override public Message<?> preSend(Message<?> message,MessageChannel channel){
  StompHeaderAccessor a=MessageHeaderAccessor.getAccessor(message,StompHeaderAccessor.class);
  if(a!=null&&StompCommand.CONNECT.equals(a.getCommand())){
   String h=a.getFirstNativeHeader("Authorization");
   if(h==null||!h.startsWith("Bearer ")||!jwt.isValid(h.substring(7)))throw new IllegalArgumentException("A valid bearer token is required");
   a.setUser(new UsernamePasswordAuthenticationToken(jwt.extractUsername(h.substring(7)),null,AuthorityUtils.createAuthorityList("ROLE_USER")));
  }
  return message;
 }
}