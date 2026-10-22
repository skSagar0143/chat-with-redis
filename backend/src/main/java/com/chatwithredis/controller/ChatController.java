package com.chatwithredis.controller;
import com.chatwithredis.dto.*;
import com.chatwithredis.service.ChatService;
import jakarta.validation.Valid;
import java.security.Principal;
import java.util.List;
import org.springframework.messaging.handler.annotation.*;
import org.springframework.web.bind.annotation.*;
@RestController @RequestMapping("/api/chat")
public class ChatController {
 private final ChatService chat; public ChatController(ChatService chat){this.chat=chat;}
 @GetMapping("/{conversationId}") public List<ChatMessageResponse> history(@PathVariable String conversationId){return chat.history(conversationId);}
 @MessageMapping("/chat.send") public void send(@Valid @Payload MessageRequest request,Principal principal){
  if(principal==null)throw new IllegalArgumentException("Authenticated WebSocket session required");
  chat.send(principal.getName(),request);
 }
}