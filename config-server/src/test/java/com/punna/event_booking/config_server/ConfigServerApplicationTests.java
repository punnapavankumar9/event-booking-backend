package com.punna.event_booking.config_server;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ConfigServerApplicationTests {

    @LocalServerPort
    private int port;

    private TestRestTemplate testRestTemplate;

    private String baseUrl;

    @Test
    void contextLoads() {
    }

    @BeforeAll
    void setUp() {
        testRestTemplate = new TestRestTemplate();
        baseUrl = "http://localhost:" + port;
    }

    @Test
    void includeRootApplicationYml() {

        ResponseEntity<DefaultConfig> exchange = testRestTemplate.exchange(baseUrl + "/default/default",
                HttpMethod.GET,
                null,
                DefaultConfig.class);

        DefaultConfig responseBody = exchange.getBody();
        assert responseBody != null;
        assertThat(responseBody.getName()).isEqualTo("default");
        assertThat(responseBody
                .getProfiles()
                .size()).isEqualTo(1);
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    private static class DefaultConfig {
        private String name;
        private List<String> profiles;
    }


}
