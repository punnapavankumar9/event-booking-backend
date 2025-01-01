package com.punna.eventcatalog;

import org.springframework.http.HttpHeaders;

public class TestUtils {
    public static void setAuthHeader(HttpHeaders httpHeaders, String user) {
        if (user.equals("punna")) {
            httpHeaders.set("X-USER-DETAILS",
                     "{\"username\":\"punna\",\"email\":\"punna@gmail.com\",\"enabled\":true,\"authorities\":[\"ROLE_ADMIN\",\"ROLE_OWNER\"],\"createdAt\":\"2024-12-31T18:17:57.290179\",\"lastModifiedAt\":\"2024-12-31T18:17:57.341823\",\"lastLoginAt\":\"2025-01-01T03:37:20.413084\"}");
        } else if (user.equals("non-admin")) {
            httpHeaders.set("X-USER-DETAILS",
                    "{\"username\":\"nonadmin\",\"email\":\"nonadmin@gmail.com\",\"enabled\":true,\"authorities\":[],\"createdAt\":\"2024-12-31T18:17:57.290179\",\"lastModifiedAt\":\"2024-12-31T18:17:57.341823\",\"lastLoginAt\":\"2025-01-01T03:37:20.413084\"}");
        }
    }
}
