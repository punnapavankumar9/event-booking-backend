package com.punna.identity.service.impl;

import com.punna.identity.dto.OAuthProvider;
import com.punna.identity.model.User;
import com.punna.identity.repository.UserRepository;
import com.punna.identity.service.UserService;
import java.util.Objects;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService implements
    OAuth2UserService<OAuth2UserRequest, OAuth2User> {

  private final UserRepository userRepository;

  private final UserService userService;


  @Override
  public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
    OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
    OAuth2User oAuth2User = delegate.loadUser(userRequest);
    String provider = userRequest.getClientRegistration().getRegistrationId();

    OAuthProvider oAuthProvider = OAuthProvider.GOOGLE;
    String providerId = null;
    if (provider.equals("google")) {
      providerId = oAuth2User.getAttribute("sub");
    } else if (provider.equals("github")) {
      oAuthProvider = OAuthProvider.GITHUB;
      providerId = Objects.requireNonNull(oAuth2User.getAttribute("id")).toString();
    }
    String email = oAuth2User.getAttribute("email");

    Optional<User> user = userRepository.findByUsernameOrEmail(email, email);
    if (user.isPresent()) {
      return oAuth2User;
    } else {
      userRepository.save(
          User.builder().email(email).username(email).enabled(true).password(null)
              .provider(oAuthProvider)
              .providerId(providerId).build());
    }

    log.info("OAuth2User: {}", oAuth2User);
    return oAuth2User;
  }
}
