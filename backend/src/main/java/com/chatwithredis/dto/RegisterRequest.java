package com.chatwithredis.dto;
import jakarta.validation.constraints.*;
public record RegisterRequest(@NotBlank @Size(min=3,max=30) @Pattern(regexp="^[a-zA-Z0-9_.-]+$") String username,@NotBlank @Size(min=8,max=72) String password){}