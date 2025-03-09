package com.punna.identity.security;

import com.punna.identity.service.impl.JwtService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomOauth2LoginSuccessHandler implements AuthenticationSuccessHandler {

  private final JwtService jwtService;

  @Value("${FRONTEND_ANGULAR_URL:http://localhost:4200}")
  private String frontendAngularUrl;

  @Override
  public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
      Authentication authentication) throws IOException, ServletException {
    OAuth2User oauth2User = (OAuth2User) authentication.getPrincipal();
    String email = oauth2User.getAttribute("email");

    String jwt = jwtService.generateToken(email);

    Cookie authCookie = new Cookie("auth_jwt", jwt);
    authCookie.setPath("/");
    authCookie.setDomain("localhost");
    response.addCookie(authCookie);

    response.sendRedirect(frontendAngularUrl + "/oauth2/success");
  }
}
