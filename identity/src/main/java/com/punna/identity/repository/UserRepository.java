package com.punna.identity.repository;

import com.punna.identity.model.User;
import jakarta.transaction.Transactional;
import java.time.Instant;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

  Optional<User> findByUsernameOrEmail(String username, String email);

  Boolean existsByUsername(String username);

  Boolean existsByEmail(String email);

  Optional<User> findByProviderId(String providerId);

  @Modifying
  @Transactional
  @Async
  @Query("update User u set u.lastLoginAt = :loginTime  where u.username = :username")
  CompletableFuture<Void> updateLastLogin(@Param("username") String username,
      @Param("loginTime") Instant loginTime);
}
