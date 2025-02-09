package com.punna.eventcore.integration;

import com.punna.eventcore.dto.EventDurationDetailsDto;
import com.punna.eventcore.dto.EventRequestDto;
import com.punna.eventcore.dto.EventResponseDto;
import com.punna.eventcore.dto.SeatingLayoutDto;
import com.punna.eventcore.model.PricingTierMap;
import com.punna.eventcore.model.SeatLocation;
import com.punna.eventcore.model.SeatState;
import org.junit.jupiter.api.*;
import org.punna.commons.exception.EntityNotFoundException;
import org.punna.commons.exception.ProblemDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.MessageSource;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import static com.punna.eventcore.TestUtils.setAuthHeader;
import static com.punna.eventcore.fixtures.TestFixtures.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(PER_CLASS)
public class EventEndpointTests extends EndPointTests {

    private final String eventsV1Url = "/api/v1/events";
    private final String organizerIdPunna = "punna";
    @Autowired
    MessageSource messageSource;
    private String eventId;
    private String venueId;
    private String invalidBodyErrorMessage;

    @Autowired
    public WebTestClient webTestClient;

    @BeforeAll
    void setUp() {
        eventRepository
                .deleteAll()
                .block();
        venueRepository
                .deleteAll()
                .block();
        seatingLayoutRepository
                .deleteAll()
                .block();

        SeatingLayoutDto seatingLayoutDto = seatingLayoutService
                .createSeatingLayout(SAMPLE_SEATING_LAYOUT_DTO)
                .block();
        assert seatingLayoutDto != null;
        SAMPLE_VENUE_DTO.setSeatingLayoutId(seatingLayoutDto.getId());
        venueId = Objects.requireNonNull(venueService
                        .createVenue(SAMPLE_VENUE_DTO)
                        .block())
                .getId();
        SAMPLE_EVENT_REQ_DTO.setVenueId(venueId);
        invalidBodyErrorMessage = messageSource.getMessage("validation.invalid-body", null, Locale.getDefault());
    }

    @Test
    @Order(1)
    void contextLoads() {
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
                .headers(headers -> setAuthHeader(headers, "punna"))
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
        EventRequestDto eventReqDto = EventRequestDto.builder()
                // this should give two errors from nested Object(PricingTierMap)
                .pricingTierMaps(List.of(new PricingTierMap()))
                .seatState(SeatState
                        .builder()
                        .blockedSeats(List.of(SeatLocation
                                .builder()
                                .build()))
                        .bookedSeats(List.of(SeatLocation
                                .builder()
                                .build()))
                        .build())
                .build();

        ProblemDetail responseBody = webTestClient
                .post()
                .uri(eventsV1Url)
                .contentType(MediaType.APPLICATION_JSON)
                .headers(headers -> setAuthHeader(headers, "punna"))
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
                Locale.getDefault()
        ));
        assertThat(responseBody.getStatus()).isEqualTo(400);
        assertThat(responseBody
                .getErrors()
                .size()).isEqualTo(12);

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
                .headers(headers -> setAuthHeader(headers, "punna"))
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
    @Order(5)
    void givenEventWithoutId_whenCreateEvent_thenCreateEventAndReturn() {
        EventRequestDto eventReqDto = clone(SAMPLE_EVENT_REQ_DTO, EventRequestDto.class);
        EventResponseDto responseBody = webTestClient
                .post()
                .uri(eventsV1Url)
                .contentType(MediaType.APPLICATION_JSON)
                .headers(headers -> setAuthHeader(headers, "punna"))
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
        assertThat(responseBody.getSeatingLayoutId()).isNotNull();
        eventId = responseBody.getId();
    }

    @Test
    @Order(6)
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
                .headers(headers -> setAuthHeader(headers, "punna"))
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
    @Order(7)
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
                .headers(headers -> setAuthHeader(headers, "punna"))
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
    @Order(8)
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
                .headers(headers -> setAuthHeader(headers, "punna"))
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
    @Order(9)
    void givenInvalidEventWithId_whenDeleteEvent_thenReturnNotFound() {
        webTestClient
                .delete()
                .uri(eventsV1Url + "/" + 123213)
                .headers(headers -> setAuthHeader(headers, "punna"))
                .exchange()
                .expectStatus()
                .isNotFound();
    }

    @Test
    @Order(10)
    void givenInvalidId_whenFindById_thenReturnNotFound() {
        webTestClient
                .get()
                .uri(eventsV1Url + "/dummyId")
                .exchange()
                .expectStatus()
                .isNotFound();
    }

    @Test
    @Order(11)
    void givenValidId_whenFindById_thenReturnOk() {
        List<EventResponseDto> responseBody = webTestClient
                .get()
                .uri(eventsV1Url + "/" + eventId)
                .headers(headers -> setAuthHeader(headers, "punna"))
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
    @Order(12)
    void givenNonAdminUser_whenUpdateEvent_thenReturnForbidden() {
        webTestClient
                .patch()
                .uri(eventsV1Url)
                .contentType(MediaType.APPLICATION_JSON)
                .headers(headers -> setAuthHeader(headers, "non-admin"))
                .bodyValue(EventRequestDto
                        .builder()
                        .id(eventId)
                        .price(BigDecimal.valueOf(123))
                        .build())
                .exchange()
                .expectStatus()
                .isForbidden();
    }

    @Test
    @Order(13)
    void giveNonAdminUser_whenDeleteEvent_thenReturnUnAuthorized() {
        webTestClient
                .delete()
                .uri(eventsV1Url + "/" + eventId)
                .accept(MediaType.APPLICATION_JSON)
                .headers(headers -> setAuthHeader(headers, "non-admin"))
                .exchange()
                .expectStatus()
                .isForbidden();
    }

    @Test
    @Order(14)
    void givenEventWithId_whenDeleteEvent_thenDelete() {
        webTestClient
                .delete()
                .uri(eventsV1Url + "/" + eventId)
                .headers(headers -> setAuthHeader(headers, "punna"))
                .exchange()
                .expectStatus()
                .isOk();
        StepVerifier
                .create(eventRepository.count())
                .expectNextMatches(t -> t == 0L)
                .verifyComplete();
    }

    @Test
    @Order(15)
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
                .headers(headers -> setAuthHeader(headers, "punna"))
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