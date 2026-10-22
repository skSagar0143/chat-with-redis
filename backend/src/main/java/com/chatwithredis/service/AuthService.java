package com.chatwithredis.service;
import com.chatwithredis.dto.*;
import com.chatwithredis.entity.AppUser;
import com.chatwithredis.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
@Service
public class AuthService {
 private final UserRepository users; private final PasswordEncoder encoder; private final JwtService jwt;
 public AuthService(UserRepository users,PasswordEncoder encoder,JwtService jwt){this.users=users;this.encoder=encoder;this.jwt=jwt;}
 public AuthResponse register(RegisterRequest request){
  if(users.existsByUsername(request.username()))throw new ResponseStatusException(HttpStatus.CONFLICT,"Username is already taken");
  AppUser user=users.save(new AppUser(request.username(),encoder.encode(request.password())));
  return new AuthResponse(jwt.generate(user.getUsername()),user.getUsername());
 }
 public AuthResponse login(LoginRequest request){
  AppUser user=users.findByUsername(request.username()).orElseThrow(()->new ResponseStatusException(HttpStatus.UNAUTHORIZED,"Invalid username or password"));
  if(!encoder.matches(request.password(),user.getPasswordHash()))throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,"Invalid username or password");
  return new AuthResponse(jwt.generate(user.getUsername()),user.getUsername());
 }
}