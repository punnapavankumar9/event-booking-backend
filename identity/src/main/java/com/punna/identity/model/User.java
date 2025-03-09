package com.punna.identity.model;

import com.punna.identity.dto.OAuthProvider;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import java.time.Instant;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "app_users",
    indexes = {@Index(columnList = "username", unique = true),
        @Index(columnList = "email", unique = true)})
public class User implements UserDetails {

  @Id
  private String username;

  private String password;

  private String email;

  private boolean enabled = true;

  @ElementCollection(fetch = FetchType.EAGER)
  @CollectionTable(name = "user_authorities",
      joinColumns = {@JoinColumn(name = "user_username", referencedColumnName = "username")})
  private List<SimpleGrantedAuthority> authorities;

  @CreatedDate
  @Column(updatable = false, nullable = false)
  private Instant createdAt;

  @LastModifiedDate
  @Column(nullable = false)
  private Instant lastModifiedAt;

  private Instant lastLoginAt;

  // id from providers like google,apple oauth servers
  private String providerId;

  // provider name such as GOOGLE, APPLE
  private OAuthProvider provider;
}
