package com.chatwithredis.repository;
import com.chatwithredis.entity.AppUser;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
public interface UserRepository extends JpaRepository<AppUser,Long>{Optional<AppUser> findByUsername(String username); Optional<AppUser> findByUsernameIgnoreCase(String username); boolean existsByUsername(String username);}