package com.chatwithredis.entity;
import jakarta.persistence.*;
import java.time.Instant;
@Entity @Table(name="app_users", uniqueConstraints=@UniqueConstraint(name="uk_app_users_username", columnNames="username"))
public class AppUser {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
 @Column(nullable=false,length=30) private String username;
 @Column(nullable=false,length=100) private String passwordHash;
 @Column(nullable=false,updatable=false) private Instant createdAt;
 protected AppUser() {}
 public AppUser(String username,String passwordHash){this.username=username;this.passwordHash=passwordHash;this.createdAt=Instant.now();}
 @PrePersist void onCreate(){if(createdAt==null)createdAt=Instant.now();}
 public Long getId(){return id;} public String getUsername(){return username;} public String getPasswordHash(){return passwordHash;} public Instant getCreatedAt(){return createdAt;}
}