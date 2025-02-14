package com.punna.eventcatalog.service.impl;

import com.punna.eventcatalog.properties.MinioProperties;
import com.punna.eventcatalog.service.StorageService;
import io.minio.BucketExistsArgs;
import io.minio.GetPresignedObjectUrlArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.http.Method;
import jakarta.annotation.PostConstruct;
import java.io.ByteArrayInputStream;
import java.util.Objects;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Service
@ConditionalOnProperty(prefix = "application", value = "storage", havingValue = "minio")
public class MinioStorageServiceImpl implements StorageService {


  private final MinioClient minioClient;
  private final MinioProperties minioProperties;
  @Value("${application.config.assets}")
  private String folderPath;

  @PostConstruct
  public void init() throws Exception {
    if (!minioClient.bucketExists(
        BucketExistsArgs.builder().bucket(minioProperties.getBucket()).build())) {
      minioClient.makeBucket(MakeBucketArgs.builder().bucket(minioProperties.getBucket()).build());
    }
  }

  @Override
  public Mono<String> storeImage(FilePart filePart) {

    return Mono.from(filePart.content()).map(dataBuffer -> {
      byte[] bytes = new byte[dataBuffer.readableByteCount()];
      dataBuffer.read(bytes);
      DataBufferUtils.release(dataBuffer); // Release buffer
      return bytes;
    }).flatMap(bytes -> {
      String fileName = folderPath + UUID.randomUUID() + filePart.filename();
      try (ByteArrayInputStream byteStream = new ByteArrayInputStream(bytes)) {
        minioClient.putObject(
            PutObjectArgs.builder().bucket(minioProperties.getBucket()).object(fileName)
                .contentType(Objects.requireNonNull(filePart.headers().getContentType()).toString())
                .stream(byteStream, bytes.length, -1).build());
        return Mono.just(fileName);
      } catch (Exception e) {
        return Mono.error(e);
      }
    });
  }

  @Override
  public String getImageUrl(String path) {
    try {
      return minioClient.getPresignedObjectUrl(
          GetPresignedObjectUrlArgs.builder().object(path).method(Method.GET)
              .bucket(minioProperties.getBucket()).build());
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
