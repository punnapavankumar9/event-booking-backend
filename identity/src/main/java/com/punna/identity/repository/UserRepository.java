package com.punna.identity.repository;

import com.punna.identity.model.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

    Optional<User> findByUsernameOrEmail(String username, String email);

    @Modifying
    @Transactional
    @Async
    @Query("update User u set u.lastLoginAt = :loginTime  where u.username = :username")
    CompletableFuture<Void> updateLastLogin(@Param("username") String username,
                                            @Param("loginTime") LocalDateTime loginTime);
}
