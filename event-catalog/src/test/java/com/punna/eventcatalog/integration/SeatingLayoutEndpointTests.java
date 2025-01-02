package com.punna.eventcatalog.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.punna.eventcatalog.dto.SeatingLayoutDto;
import com.punna.eventcatalog.fixtures.TestFixtures;
import com.punna.eventcatalog.model.Seat;
import com.punna.eventcatalog.model.SeatTier;
import com.punna.eventcatalog.repository.SeatingLayoutRepository;
import lombok.SneakyThrows;
import org.junit.jupiter.api.*;
import org.punna.commons.exception.ProblemDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.List;

import static com.punna.eventcatalog.TestUtils.setAuthHeader;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(PER_CLASS)
public class SeatingLayoutEndpointTests extends ContainerBase {

    private final String arrangementsV1Url = "/api/v1/seating-layout";
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private SeatingLayoutRepository seatingLayoutRepository;
    @Autowired
    private WebTestClient webTestClient;

    @SneakyThrows
    public <T> T clone(T obj, Class<T> clazz) {
        String s = objectMapper.writeValueAsString(obj);
        return objectMapper.readValue(s, clazz);
    }

    @BeforeAll
    void setUp() {
        seatingLayoutRepository
                .deleteAll()
                .block();
    }

    @Test
    @Order(1)
    void givenInvalidSeatingLayout_whenCreate_thenReturnBadRequest() {
        // total 9 errors should come.
        SeatingLayoutDto arrangementDto = SeatingLayoutDto
                // 1 error
                .builder()
                .id("DummyId") // 1 error
                // 4 errors
                .seatTiers(List.of(SeatTier
                        .builder()
                        .seats(List.of(Seat // 3 errors
                                .builder()
                                .build()))
                        .build()))
                .build();
        ProblemDetail responseBody = webTestClient
                .post()
                .uri(arrangementsV1Url)
                .bodyValue(arrangementDto)
                .headers(headers -> setAuthHeader(headers, "punna"))
                .exchange()
                .expectStatus()
                .isBadRequest()
                .expectBody(ProblemDetail.class)
                .returnResult()
                .getResponseBody();
        assertThat(responseBody).isNotNull();
        assertThat(responseBody
                .getErrors()
                .size()).isEqualTo(9);
    }

    @Test
    @Order(2)
    void givenValidSeatingLayout_whenCreate_thenReturnCreated() {
        SeatingLayoutDto arrangementDto = clone(TestFixtures.SAMPLE_SEATING_ARRANGEMENT_DTO,
                SeatingLayoutDto.class);
        SeatingLayoutDto responseBody = webTestClient
                .post()
                .uri(arrangementsV1Url)
                .bodyValue(arrangementDto)
                .headers(headers -> setAuthHeader(headers, "punna"))
                .exchange()
                .expectStatus()
                .isCreated()
                .expectBody(SeatingLayoutDto.class)
                .returnResult()
                .getResponseBody();
        assertThat(responseBody).isNotNull();
    }

}