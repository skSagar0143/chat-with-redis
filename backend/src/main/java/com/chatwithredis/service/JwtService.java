package com.chatwithredis.service;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
@Service
public class JwtService {
 private final SecretKey key; private final long expirationMs;
 public JwtService(@Value("${app.jwt.secret}") String secret,@Value("${app.jwt.expiration-ms:86400000}") long expirationMs){
  if(secret.getBytes(StandardCharsets.UTF_8).length<32) throw new IllegalArgumentException("JWT secret must be at least 32 bytes");
  this.key=Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));this.expirationMs=expirationMs;
 }
 public String generate(String username){Date now=new Date();return Jwts.builder().subject(username).issuedAt(now).expiration(new Date(now.getTime()+expirationMs)).signWith(key).compact();}
 public String extractUsername(String token){return claims(token).getSubject();}
 public boolean isValid(String token){try{claims(token);return true;}catch(RuntimeException ex){return false;}}
 private Claims claims(String token){return Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload();}
}