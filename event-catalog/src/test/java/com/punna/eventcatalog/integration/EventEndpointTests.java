package com.punna.eventcatalog.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.punna.eventcatalog.dto.EventDurationDetailsDto;
import com.punna.eventcatalog.dto.EventRequestDto;
import com.punna.eventcatalog.dto.EventResponseDto;
import com.punna.eventcatalog.repository.EventRepository;
import com.punna.eventcatalog.service.VenueService;
import lombok.SneakyThrows;
import org.junit.jupiter.api.*;
import org.punna.commons.exception.EntityNotFoundException;
import org.punna.commons.exception.ProblemDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSource;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;

import static com.punna.eventcatalog.fixtures.TestFixtures.SAMPLE_EVENT_REQ_DTO;
import static com.punna.eventcatalog.fixtures.TestFixtures.SAMPLE_VENUE_DTO;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(PER_CLASS)
public class EventEndpointTests extends ContainerBase {

    private final String eventsV1Url = "/api/v1/events";
    private final String organizerIdPunna = "punna";
    @Autowired
    ApplicationContext applicationContext;
    @Autowired
    WebTestClient webTestClient;
    @Autowired
    MessageSource messageSource;
    private String eventId;
    private String venueId;
    private String invalidBodyErrorMessage;
    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private VenueService venueService;


    @SneakyThrows
    public <T> T clone(T obj, Class<T> clazz) {
        String s = objectMapper.writeValueAsString(obj);
        return objectMapper.readValue(s, clazz);
    }

    public void setAuthHeader(HttpHeaders httpHeaders) {
        httpHeaders.set("X-USER-DETAILS",
                "{\"username\":\"punna\",\"email\":\"punna@gmail.com\",\"enabled\":true,\"authorities\":[\"ADMIN\",\"OWNER\"],\"createdAt\":\"2024-12-31T18:17:57.290179\",\"lastModifiedAt\":\"2024-12-31T18:17:57.341823\",\"lastLoginAt\":\"2025-01-01T03:37:20.413084\"}");
    }

    @BeforeAll
    void setUp() {
        eventRepository
                .deleteAll()
                .block();
        venueId = venueService
                .createVenue(SAMPLE_VENUE_DTO)
                .block()
                .getId();
        SAMPLE_EVENT_REQ_DTO.setVenueId(venueId);
        invalidBodyErrorMessage = messageSource.getMessage("validation.invalid-body", null, Locale.getDefault());
    }

    @Test
    @Order(1)
    void contextLoads() {
        assertThat(applicationContext).isNotNull();
        assertThat(mongoContainer.isRunning()).isTrue();
        assertThat(applicationContext.getBean(ReactiveMongoTemplate.class)).isNotNull();
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
                .headers(this::setAuthHeader)
                .bodyValue(eventReqDto)
                .exchange()
                .expectStatus()
                .isBadRequest()
                .expectBody(ProblemDetail.class)
                .returnResult()
                .getResponseBody();
        assertThat(responseBody).isNotNull();
        assertThat(responseBody.getMessage()).isEqualTo(invalidBodyErrorMessage);
        assertThat(responseBody.getStatus()).isEqualTo(400);
        assertThat(responseBody
                .getErrors()
                .size()).isEqualTo(1);
    }

    @Test
    @Order(3)
    void givenInvalidEvent_whenCreateEvent_thenReturnBadResultWithErrors() {
        EventRequestDto eventReqDto = EventRequestDto
                .builder()
                .build();
        ProblemDetail responseBody = webTestClient
                .post()
                .uri(eventsV1Url)
                .contentType(MediaType.APPLICATION_JSON)
                .headers(this::setAuthHeader)
                .bodyValue(eventReqDto)
                .exchange()
                .expectStatus()
                .isBadRequest()
                .expectBody(ProblemDetail.class)
                .returnResult()
                .getResponseBody();
        assertThat(responseBody).isNotNull();
        assertThat(responseBody.getMessage()).isEqualTo(messageSource.getMessage("validation.invalid-body",
                null,
                Locale.getDefault()));
        assertThat(responseBody.getStatus()).isEqualTo(400);
        assertThat(responseBody
                .getErrors()
                .size()).isEqualTo(5);

    }


    @Test
    @Order(4)
    void givenEventWithInvalidVenueId_whenCreateEvent_thenReturnNotFound() {
        EventRequestDto eventReqDto = clone(SAMPLE_EVENT_REQ_DTO, EventRequestDto.class);
        eventReqDto.setVenueId("Dummy");
        ProblemDetail responseBody = webTestClient
                .post()
                .uri(eventsV1Url)
                .contentType(MediaType.APPLICATION_JSON)
                .headers(this::setAuthHeader)
                .bodyValue(eventReqDto)
                .exchange()
                .expectStatus()
                .isNotFound()
                .expectBody(ProblemDetail.class)
                .returnResult()
                .getResponseBody();
        assertThat(responseBody).isNotNull();
        assertThat(responseBody.getMessage()).contains("Venue with id::Dummy not found");
        assertThat(responseBody.getStatus()).isEqualTo(404);
    }


    @Test
    @Order(3)
    void givenEventWithoutId_whenCreateEvent_thenCreateEventAndReturn() {
        EventRequestDto eventReqDto = clone(SAMPLE_EVENT_REQ_DTO, EventRequestDto.class);
        EventResponseDto responseBody = webTestClient
                .post()
                .uri(eventsV1Url)
                .contentType(MediaType.APPLICATION_JSON)
                .headers(this::setAuthHeader)
                .bodyValue(eventReqDto)
                .exchange()
                .expectStatus()
                .isCreated()
                .expectBody(EventResponseDto.class)
                .returnResult()
                .getResponseBody();
        assertThat(responseBody).isNotNull();
        assertThat(responseBody.getCreatedAt()).isNotNull();
        assertThat(responseBody.getLastModifiedAt()).isNotNull();
        assertThat(responseBody.getId()).isNotNull();
        assertThat(responseBody.getVenueId()).isEqualTo(venueId);
        assertThat(responseBody.getOrganizerId()).isEqualTo(organizerIdPunna);
        assertThat(responseBody.getId()).hasSize(24);
        eventId = responseBody.getId();
    }

    @Test
    @Order(4)
    void givenInvalidEventWithId_whenUpdateEvent_thenReturnBadRequest() {
        EventRequestDto eventRequestDto = EventRequestDto
                .builder()
                .id(eventId)
                .name("min")
                .price(BigDecimal.valueOf(-1))
                .eventDurationDetails(EventDurationDetailsDto
                        .builder()
                        .startTime(LocalDateTime
                                .now()
                                .minusDays(10))
                        .endTime(LocalDateTime
                                .now()
                                .minusDays(10))
                        .build())
                .build();
        ProblemDetail responseBody = webTestClient
                .patch()
                .uri(eventsV1Url)
                .contentType(MediaType.APPLICATION_JSON)
                .headers(this::setAuthHeader)
                .bodyValue(eventRequestDto)
                .exchange()
                .expectStatus()
                .isBadRequest()
                .expectBody(ProblemDetail.class)
                .returnResult()
                .getResponseBody();
        assertThat(responseBody).isNotNull();
        assertThat(responseBody.getMessage()).isEqualTo(invalidBodyErrorMessage);
        assertThat(responseBody
                .getErrors()
                .size()).isEqualTo(4);
        assertThat(responseBody.getStatus()).isEqualTo(400);
    }

    @Test
    @Order(5)
    void givenEventWithInvalidVenueId_whenUpdateEvent_thenReturnVenueNotFound() {
        EventRequestDto eventRequestDto = clone(SAMPLE_EVENT_REQ_DTO, EventRequestDto.class);
        BigDecimal newPrice = BigDecimal.valueOf(9999999999.9999999989);
        LocalDateTime newEndDate = LocalDateTime
                .now()
                .plusDays(50);
        String newEventCause = "new chase is charity";
        eventRequestDto.setId(eventId);
        eventRequestDto.setOpenForBooking(true);
        eventRequestDto.setPrice(newPrice);
        eventRequestDto.setVenueId("Dummy");
        eventRequestDto
                .getEventDurationDetails()
                .setEndTime(newEndDate);
        eventRequestDto
                .getAdditionalDetails()
                .put("event cause", newEventCause);
        ProblemDetail responseBody = webTestClient
                .patch()
                .uri(eventsV1Url)
                .contentType(MediaType.APPLICATION_JSON)
                .headers(this::setAuthHeader)
                .bodyValue(eventRequestDto)
                .exchange()
                .expectStatus()
                .isNotFound()
                .expectBody(ProblemDetail.class)
                .returnResult()
                .getResponseBody();

        assertThat(responseBody).isNotNull();
        assertThat(responseBody.getMessage()).contains("Venue with id::Dummy not found");
    }


    @Test
    @Order(5)
    void givenEventWithId_whenUpdateEvent_thenUpdateEventAndReturn() {
        EventRequestDto eventRequestDto = clone(SAMPLE_EVENT_REQ_DTO, EventRequestDto.class);
        BigDecimal newPrice = BigDecimal.valueOf(9999999999.9999999989);
        LocalDateTime newEndDate = LocalDateTime
                .now()
                .plusDays(50);
        String newEventCause = "new chase is charity";
        eventRequestDto.setId(eventId);
        eventRequestDto.setOpenForBooking(true);
        eventRequestDto.setPrice(newPrice);
        eventRequestDto
                .getEventDurationDetails()
                .setEndTime(newEndDate);
        eventRequestDto
                .getAdditionalDetails()
                .put("event cause", newEventCause);
        EventResponseDto responseBody = webTestClient
                .patch()
                .uri(eventsV1Url)
                .contentType(MediaType.APPLICATION_JSON)
                .headers(this::setAuthHeader)
                .bodyValue(eventRequestDto)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(EventResponseDto.class)
                .returnResult()
                .getResponseBody();

        assertThat(responseBody).isNotNull();
        assertThat(responseBody.getCreatedAt()).isNotNull();
        assertThat(responseBody
                .getEventDurationDetails()
                .getEndTime()).isEqualTo(newEndDate);
        assertThat(responseBody
                .getEventDurationDetails()
                .getStartTime()).isNotNull();
        assertThat(responseBody.getOrganizerId()).isEqualTo(organizerIdPunna);
        assertThat(responseBody.getPrice()).isEqualTo(newPrice);
        assertThat(responseBody
                .getAdditionalDetails()
                .get("event cause")).isEqualTo(newEventCause);

    }

    @Test
    @Order(6)
    void givenInvalidEventWithId_whenDeleteEvent_thenReturnNotFound() {
        webTestClient
                .delete()
                .uri(eventsV1Url + "/" + 123213)
                .headers(this::setAuthHeader)
                .exchange()
                .expectStatus()
                .isNotFound();
    }

    @Test
    @Order(7)
    void givenInvalidId_whenFindById_thenReturnNotFound() {
        webTestClient
                .get()
                .uri(eventsV1Url + "/dummyId")
                .exchange()
                .expectStatus()
                .isNotFound();
    }

    @Test
    @Order(7)
    void givenValidId_whenFindById_thenReturnOk() {
        List<EventResponseDto> responseBody = webTestClient
                .get()
                .uri(eventsV1Url + "/" + eventId)
                .headers(this::setAuthHeader)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(EventResponseDto.class)
                .returnResult()
                .getResponseBody();
        assertThat(responseBody).isNotNull();
        assertThat(responseBody).hasSize(1);
    }

    @Test
    @Order(9)
    void givenEventWithId_whenDeleteEvent_thenDelete() {
        webTestClient
                .delete()
                .uri(eventsV1Url + "/" + eventId)
                .headers(this::setAuthHeader)
                .exchange()
                .expectStatus()
                .isOk();
        StepVerifier
                .create(eventRepository.count())
                .expectNextMatches(t -> t == 0L)
                .verifyComplete();
    }

    @Test
    @Order(10)
    void givenNonExistingEvent_whenUpdatingEvent_thenReturnBadRequestWithErrors() {
        EventRequestDto eventRequestDto = EventRequestDto
                .builder()
                .build();
        final String invalidId = "InvalidId";
        eventRequestDto.setId(invalidId);

        ProblemDetail responseBody = webTestClient
                .patch()
                .uri(eventsV1Url)
                .contentType(MediaType.APPLICATION_JSON)
                .headers(this::setAuthHeader)
                .bodyValue(eventRequestDto)
                .exchange()
                .expectStatus()
                .isNotFound()
                .expectBody(ProblemDetail.class)
                .returnResult()
                .getResponseBody();
        assertThat(responseBody).isNotNull();
        assertThat(responseBody.getMessage()).isEqualTo(new EntityNotFoundException("Event", invalidId).getMessage());
        assertThat(responseBody.getStatus()).isEqualTo(404);
        assertThat(responseBody
                .getErrors()
                .size()).isEqualTo(0);
    }

}