package com.punna.eventcatalog.integration;

import com.punna.eventcatalog.TestUtils;
import com.punna.eventcatalog.dto.EventRequestDto;
import com.punna.eventcatalog.dto.EventResponseDto;
import com.punna.eventcatalog.dto.SeatingLayoutDto;
import com.punna.eventcatalog.dto.VenueDto;
import com.punna.eventcatalog.model.SeatLocation;
import org.junit.jupiter.api.*;
import org.punna.commons.exception.ProblemDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.time.Duration;
import java.util.List;

import static com.punna.eventcatalog.fixtures.TestFixtures.*;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class EventSeatStateEndpointTests extends EndPointTests {

    private String seatStateV1Url = "/api/v1/events/%s/seats";

    @Autowired
    private WebTestClient webClient;

    private String eventId;

    @BeforeAll
    void setUp() {
        webClient = webClient
                .mutate()
                .responseTimeout(Duration.ofMinutes(5))
                .build();
        eventRepository
                .deleteAll()
                .block();
        venueRepository
                .deleteAll()
                .block();
        seatingLayoutRepository
                .deleteAll()
                .block();
        SeatingLayoutDto seatingLayoutDto = clone(SAMPLE_SEATING_LAYOUT_DTO, SeatingLayoutDto.class);

        // creation order: layout -> venue -> event
        EventResponseDto eventResponseDto = seatingLayoutService
                .createSeatingLayout(seatingLayoutDto)
                .flatMap(layout -> {
                    VenueDto venueDto = clone(SAMPLE_VENUE_DTO, VenueDto.class);
                    venueDto.setSeatingLayoutId(layout.getId());
                    return venueService
                            .createVenue(venueDto)
                            .flatMap(venue -> {
                                EventRequestDto eventRequestDto = clone(SAMPLE_EVENT_REQ_DTO, EventRequestDto.class);
                                eventRequestDto.setVenueId(venue.getId());
                                return eventService.createEvent(eventRequestDto);
                            });
                })
                .block();
        assertThat(eventResponseDto).isNotNull();
        eventId = eventResponseDto.getId();
        seatStateV1Url = String.format(seatStateV1Url, eventId);
    }

    @Test
    @Order(1)
    void givenInvalidSeatLocation_whenBook_thenReturnBadRequest() {
        ProblemDetail responseBody = webClient
                .post()
                .uri(seatStateV1Url + "/book")
                .bodyValue(List.of(SeatLocation
                                .builder()
                                .row(1)
                                .column(2)
                                .build(), // valid
                        SeatLocation
                                .builder()
                                .row(1)
                                .column(5)
                                .build()// invalid
                ))
                .headers(headers -> TestUtils.setAuthHeader(headers, "punna"))
                .exchange()
                .expectStatus()
                .isBadRequest()
                .expectBody(ProblemDetail.class)
                .returnResult()
                .getResponseBody();
        assertThat(responseBody).isNotNull();
    }

    @Test
    @Order(2)
    void givenInvalidSeatLocation_whenBlock_thenReturnBadRequest() {
        ProblemDetail responseBody = webClient
                .post()
                .uri(seatStateV1Url + "/block")
                .bodyValue(List.of(SeatLocation
                                .builder()
                                .row(1)
                                .column(2)
                                .build(), // valid
                        SeatLocation
                                .builder()
                                .row(1)
                                .column(5)
                                .build()// invalid
                ))
                .headers(headers -> TestUtils.setAuthHeader(headers, "punna"))
                .exchange()
                .expectStatus()
                .isBadRequest()
                .expectBody(ProblemDetail.class)
                .returnResult()
                .getResponseBody();
        assertThat(responseBody).isNotNull();
    }

    @Test
    @Order(3)
    void givenInvalidSeatLocation_whenUnBlock_thenReturnBadRequest() {
        ProblemDetail responseBody = webClient
                .post()
                .uri(seatStateV1Url + "/unblock")
                .bodyValue(List.of(SeatLocation
                                .builder()
                                .row(1)
                                .column(2)
                                .build(), // valid
                        SeatLocation
                                .builder()
                                .row(1)
                                .column(5)
                                .build()// invalid
                ))
                .headers(headers -> TestUtils.setAuthHeader(headers, "punna"))
                .exchange()
                .expectStatus()
                .isBadRequest()
                .expectBody(ProblemDetail.class)
                .returnResult()
                .getResponseBody();
        assertThat(responseBody).isNotNull();
    }

    // block middle column
    @Test
    @Order(4)
    void givenValidSeatLocation_whenBlock_thenReturnOk() {
        webClient
                .post()
                .uri(seatStateV1Url + "/block")
                .bodyValue(List.of(SeatLocation
                                .builder()
                                .row(1)
                                .column(2)
                                .build(), // valid
                        SeatLocation
                                .builder()
                                .row(2)
                                .column(3)
                                .build()// valid
                ))
                .headers(headers -> TestUtils.setAuthHeader(headers, "punna"))
                .exchange()
                .expectStatus()
                .isOk();

    }

    // booking blocked tickets will give error
    @Test
    @Order(5)
    void givenBlockedSeatLocation_whenBook_thenReturnConflict() {
        webClient
                .post()
                .uri(seatStateV1Url + "/book")
                .bodyValue(List.of(SeatLocation
                                .builder()
                                .row(1)
                                .column(1)
                                .build(), // valid
                        SeatLocation
                                .builder()
                                .row(1)
                                .column(2)
                                .build()// invalid
                ))
                .headers(headers -> TestUtils.setAuthHeader(headers, "punna"))
                .exchange()
                .expectStatus()
                .isEqualTo(HttpStatus.CONFLICT);
    }

    // blocking already blocked tickets will ignore and add other than those to blocked set
    @Test
    @Order(6)
    void givenBlockedSeatLocation_whenBlock_thenReturnConflict() {
        webClient
                .post()
                .uri(seatStateV1Url + "/block")
                .bodyValue(List.of(SeatLocation
                                .builder()
                                .row(1)
                                .column(1)
                                .build(), // valid
                        SeatLocation
                                .builder()
                                .row(1)
                                .column(2)
                                .build()// invalid
                ))
                .headers(headers -> TestUtils.setAuthHeader(headers, "punna"))
                .exchange()
                .expectStatus()
                .isOk();
    }


    // seat layout
    //  1-blocked     2-blocked       3-open
    //  4-open        5-blocked       6-open


}
