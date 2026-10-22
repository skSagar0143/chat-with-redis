package com.chatwithredis.repository;
import com.chatwithredis.entity.ChatMessage;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
public interface ChatMessageRepository extends JpaRepository<ChatMessage,Long>{List<ChatMessage> findTop100ByConversationIdOrderByCreatedAtDesc(String conversationId);}