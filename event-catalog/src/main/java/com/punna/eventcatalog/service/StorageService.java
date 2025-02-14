package com.punna.eventcatalog.service;


import org.springframework.http.codec.multipart.FilePart;
import reactor.core.publisher.Mono;

public interface StorageService {

  Mono<String> storeImage(FilePart filePart);

  String getImageUrl(String path);
}
