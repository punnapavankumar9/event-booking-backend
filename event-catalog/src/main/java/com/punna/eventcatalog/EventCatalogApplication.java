package com.punna.eventcatalog;

import jakarta.annotation.PostConstruct;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.FileSystemResource;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@SpringBootApplication
public class EventCatalogApplication {

  @Value("${application.config.assets}")
  private String assetsPath;
  @Value("${application.storage}")
  private String storageType;

  public static void main(String[] args) {
    SpringApplication.run(EventCatalogApplication.class, args);
  }

  @PostConstruct
  @ConditionalOnProperty()
  public void createAssets() {
    if (storageType.equals("memory")) {
      Mono.fromCallable(() -> Files.createDirectories(Paths.get(assetsPath)))
          .subscribeOn(Schedulers.boundedElastic()).block();
    }
  }

  @Bean
  @ConditionalOnProperty(value = "storage",prefix = "application", havingValue = "memory")
  public RouterFunction<?> serveAssets() {
    System.out.println(assetsPath);
    String path = Paths.get(assetsPath).toAbsolutePath().toString();
    System.out.println(path);
    return RouterFunctions.resources("/assets/**", new FileSystemResource(path));
  }
}
