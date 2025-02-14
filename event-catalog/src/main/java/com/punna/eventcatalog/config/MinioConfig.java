package com.punna.eventcatalog.config;

import com.punna.eventcatalog.properties.MinioProperties;
import io.minio.MinioClient;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "application", value = "storage", havingValue = "minio")
@EnableConfigurationProperties(MinioProperties.class)
public class MinioConfig {

  private final MinioProperties minioProperties;

  @Bean
  MinioClient minioClient() {
    return MinioClient.builder()
        .credentials(minioProperties.getAccessKey(), minioProperties.getSecretKey())
        .endpoint(minioProperties.getEndpoint()).build();
  }
}
