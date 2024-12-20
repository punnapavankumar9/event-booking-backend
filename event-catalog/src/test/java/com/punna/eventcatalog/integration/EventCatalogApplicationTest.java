package com.punna.eventcatalog.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.punna.eventcatalog.dto.EventRequestDto;
import com.punna.eventcatalog.dto.EventResponseDto;
import com.punna.eventcatalog.repository.EventRepository;
import lombok.SneakyThrows;
import org.junit.jupiter.api.*;
import org.punna.commons.exception.ProblemDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSource;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.Locale;

import static com.punna.eventcatalog.fixtures.EventFixtures.SAMPLE_EVENT_REQ_DTO;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(PER_CLASS)
public class EventCatalogApplicationTest extends ContainerBase {

    private final String eventsV1Url = "/api/v1/events";

    @Autowired
    ApplicationContext applicationContext;


    @Autowired
    WebTestClient webTestClient;

    @Autowired
    MessageSource messageSource;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private ObjectMapper objectMapper;


    @SneakyThrows
    public <T> T clone(T obj, Class<T> clazz) {
        String s = objectMapper.writeValueAsString(obj);
        return objectMapper.readValue(s, clazz);
    }

    @Test
    @Order(1)
    void contextLoads() {
        assertThat(applicationContext).isNotNull();
        assertThat(mongoContainer.isRunning()).isTrue();
        assertThat(applicationContext.getBean(ReactiveMongoTemplate.class)).isNotNull();
    }

    @BeforeAll
    void setUp() {
        eventRepository
                .deleteAll()
                .block();
    }

    @Test
    @Order(2)
    void givenEventWithId_whenCreateEvent_thenReturnBadRequest() {
        EventRequestDto eventReqDto = clone(SAMPLE_EVENT_REQ_DTO, EventRequestDto.class);
        eventReqDto.setId("dummyId");
        ProblemDetail responseBody = webTestClient
                .post()
                .uri(eventsV1Url)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(eventReqDto)
                .exchange()
                .expectStatus()
                .isBadRequest()
                .expectBody(ProblemDetail.class)
                .returnResult()
                .getResponseBody();
        assert responseBody != null;
        assertThat(responseBody.getMessage()).isEqualTo(messageSource.getMessage("validation.invalid-body",
                null,
                Locale.getDefault()));
        assertThat(responseBody.getStatus()).isEqualTo(400);
        assertThat(responseBody
                .getErrors()
                .size()).isEqualTo(1);
    }

    @Test
    @Order(2)
    void givenEventWithoutId_whenCreateEvent_thenCreateEventAndReturn() {
        EventRequestDto eventReqDto = clone(SAMPLE_EVENT_REQ_DTO, EventRequestDto.class);
        EventResponseDto responseBody = webTestClient
                .post()
                .uri(eventsV1Url)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(eventReqDto)
                .exchange()
                .expectStatus()
                .isCreated()
                .expectBody(EventResponseDto.class)
                .returnResult()
                .getResponseBody();
        assert responseBody != null;
        assertThat(responseBody.getCreatedAt()).isNotNull();
        assertThat(responseBody.getLastModifiedAt()).isNotNull();
        assertThat(responseBody.getId()).isNotNull();
        assertThat(responseBody.getId()).hasSize(24);
    }
}