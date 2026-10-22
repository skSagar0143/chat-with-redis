package com.chatwithredis.entity;
import jakarta.persistence.*;
import java.time.Instant;
@Entity @Table(name="chat_messages", indexes=@Index(name="idx_chat_conversation_created",columnList="conversationId,createdAt"))
public class ChatMessage {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
 @Column(nullable=false,length=80) private String conversationId;
 @Column(nullable=false,length=30) private String sender;
 @Column(nullable=false,length=2000) private String content;
 @Column(nullable=false,updatable=false) private Instant createdAt;
 protected ChatMessage() {}
 public ChatMessage(String conversationId,String sender,String content){this.conversationId=conversationId;this.sender=sender;this.content=content;this.createdAt=Instant.now();}
 @PrePersist void onCreate(){if(createdAt==null)createdAt=Instant.now();}
 public Long getId(){return id;} public String getConversationId(){return conversationId;} public String getSender(){return sender;} public String getContent(){return content;} public Instant getCreatedAt(){return createdAt;}
}