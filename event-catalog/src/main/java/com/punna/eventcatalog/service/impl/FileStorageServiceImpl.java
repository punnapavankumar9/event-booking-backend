package com.punna.eventcatalog.service.impl;

import com.punna.eventcatalog.service.StorageService;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "application", value = "storage", havingValue = "memory")
public class FileStorageServiceImpl implements StorageService {

  @Value("${application.config.assets}")
  private String assetsPath;

  @Override
  public Mono<String> storeImage(FilePart filePart) {
    Path filePath = Paths.get(assetsPath);
    filePath = filePath.resolve(UUID.randomUUID() + "-" + filePart.filename());
    return filePart.transferTo(filePath).then(Mono.just(filePath.toString()));
  }

  @Override
  public String getImageUrl(String path) {
    return path;
  }
}
