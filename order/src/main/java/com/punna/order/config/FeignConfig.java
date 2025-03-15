package com.punna.order.config;

import feign.RequestInterceptor;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Configuration
public class FeignConfig {

  private static final String HEADER_NAME = "X-USER-DETAILS";

  @Bean
  public RequestInterceptor requestInterceptor() {
    return requestTemplate -> {
      ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
      if (attributes != null) {
        HttpServletRequest request = attributes.getRequest();
 
        String userDetailsHeader = request.getHeader(HEADER_NAME);
        if (userDetailsHeader != null) {
          requestTemplate.header(HEADER_NAME, userDetailsHeader);
        }
      }
    };
  }

}
