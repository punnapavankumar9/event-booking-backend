package com.punna.identity.utils;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;

public class TestUtils {

    public static HttpHeaders authHeader(String token) {
        return new HttpHeaders() {{
            add("Authorization", "Bearer " + token);
        }};
    }

    public static <T> HttpEntity<T> createHttpEntity(T entity, String token) {
        return new HttpEntity<>(entity, authHeader(token));
    }
}
