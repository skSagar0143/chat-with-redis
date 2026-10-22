package com.chatwithredis.dto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
public record MessageRequest(@NotBlank @Size(max=80) String conversationId,@NotBlank @Size(max=2000) String content){}