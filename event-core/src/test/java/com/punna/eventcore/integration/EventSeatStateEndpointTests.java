package com.punna.eventcore.integration;

import static com.punna.eventcore.fixtures.TestFixtures.SAMPLE_EVENT_REQ_DTO;
import static com.punna.eventcore.fixtures.TestFixtures.SAMPLE_SEATING_LAYOUT_DTO;
import static com.punna.eventcore.fixtures.TestFixtures.SAMPLE_VENUE_DTO;
import static org.assertj.core.api.Assertions.assertThat;

import com.punna.eventcore.TestUtils;
import com.punna.eventcore.dto.BookSeatRequestDto;
import com.punna.eventcore.dto.EventRequestDto;
import com.punna.eventcore.dto.EventResponseDto;
import com.punna.eventcore.dto.SeatingLayoutDto;
import com.punna.eventcore.dto.VenueDto;
import com.punna.eventcore.model.SeatLocation;
import java.math.BigDecimal;
import java.time.Duration;
import java.util.List;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import com.punna.commons.exception.ProblemDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class EventSeatStateEndpointTests extends EndPointTests {

  private String seatStateV1Url = "/api/v1/events/%s/seats";

  @Autowired
  private WebTestClient webClient;


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
    mockCatalogServiceWebClient(true);
    mockAuthServiceUsername();
    // creation order: layout -> venue -> event
    EventResponseDto eventResponseDto = seatingLayoutService
        .createSeatingLayout(seatingLayoutDto)
        .flatMap(layout -> {
          VenueDto venueDto = clone(SAMPLE_VENUE_DTO, VenueDto.class);
          venueDto.setSeatingLayoutId(layout.getId());
          return venueService
              .createVenue(venueDto)
              .flatMap(venue -> {
                EventRequestDto eventRequestDto = clone(SAMPLE_EVENT_REQ_DTO,
                    EventRequestDto.class);
                eventRequestDto.setVenueId(venue.getId());
                return eventService.createEvent(eventRequestDto);
              });
        })
        .block();
    assertThat(eventResponseDto).isNotNull();
    String eventId = eventResponseDto.getId();
    seatStateV1Url = String.format(seatStateV1Url, eventId);
  }

  @Test
  @Order(1)
  void givenInvalidSeatLocation_whenBook_thenReturnBadRequest() {
    BookSeatRequestDto bookSeatRequestDto = BookSeatRequestDto.builder()
        .amount(BigDecimal.valueOf(498))
        .seats(List.of(SeatLocation
                .builder()
                .row(0)
                .column(1)
                .build(), // valid
            SeatLocation
                .builder()
                .row(0)
                .column(4)
                .build()// invalid
        ))
        .build();
    ProblemDetail responseBody = webClient
        .post()
        .uri(seatStateV1Url + "/book")
        .bodyValue(bookSeatRequestDto)
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
                .row(0)
                .column(1)
                .build(), // valid
            SeatLocation
                .builder()
                .row(0)
                .column(4)
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
                .row(0)
                .column(1)
                .build(), // valid
            SeatLocation
                .builder()
                .row(0)
                .column(4)
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
                .row(0)
                .column(1)
                .build(), // valid
            SeatLocation
                .builder()
                .row(1)
                .column(2)
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
    BookSeatRequestDto bookSeatRequestDto = BookSeatRequestDto.builder()
        .amount(BigDecimal.valueOf(498))
        .seats(List.of(SeatLocation
                .builder()
                .row(0)
                .column(0)
                .build(), // valid
            SeatLocation
                .builder()
                .row(0)
                .column(1)
                .build()// invalid
        )).build();
    webClient
        .post()
        .uri(seatStateV1Url + "/book")
        .bodyValue(bookSeatRequestDto)
        .headers(headers -> TestUtils.setAuthHeader(headers, "punna"))
        .exchange()
        .expectStatus()
        .isEqualTo(HttpStatus.CONFLICT);
  }

  // blocking already blocked tickets will ignore and add other than those to blocked set
  @Test
  @Order(6)
  void givenBlockedSeatLocation_whenBlock_thenReturnOk() {
    webClient
        .post()
        .uri(seatStateV1Url + "/block")
        .bodyValue(List.of(SeatLocation
                .builder()
                .row(0)
                .column(0)
                .build(), // valid
            SeatLocation
                .builder()
                .row(0)
                .column(1)
                .build()// invalid
        ))
        .headers(headers -> TestUtils.setAuthHeader(headers, "punna"))
        .exchange()
        .expectStatus()
        .isOk();
  }

  // seat layout 1-open, 0-blocked
  // 0 0 1
  // 1 0 1
  // unblocking the unblocked tickets will ignore the unblocked and unblock the blocked. 😂
  @Test
  @Order(7)
  void givenUnblockedSeatLocation_whenUnblock_thenReturnOk() {
    webClient
        .post()
        .uri(seatStateV1Url + "/unblock")
        .bodyValue(List.of(SeatLocation
                .builder()
                .row(0)
                .column(0)
                .build(), // valid
            SeatLocation
                .builder()
                .row(0)
                .column(2)
                .build()// open ticket will result in error
        ))
        .headers(headers -> TestUtils.setAuthHeader(headers, "punna"))
        .exchange()
        .expectStatus()
        .isOk();
  }

  // seat layout, 1-open, 0-blocked
  // 1 0 1
  // 1 0 1

  @Test
  @Order(8)
  void givenUnblockedSeatLocation_whenUnbook_thenReturnOk() {
    webClient
        .post()
        .uri(seatStateV1Url + "/book")
        .bodyValue(new BookSeatRequestDto(BigDecimal.valueOf(498), List.of(SeatLocation
                .builder()
                .row(0)
                .column(0)
                .build(), // valid
            SeatLocation
                .builder()
                .row(0)
                .column(2)
                .build() // valid
        )))
        .headers(headers -> TestUtils.setAuthHeader(headers, "punna"))
        .exchange()
        .expectStatus()
        .isOk();
  }

  @Test
  @Order(9)
  void givenBookedSeatLocation_whenBook_thenReturnConflict() {
    BookSeatRequestDto bookSeatRequestDto = BookSeatRequestDto.builder()
        .seats(List.of(SeatLocation
                .builder()
                .row(0)
                .column(0)
                .build(), // already booked
            SeatLocation
                .builder()
                .row(1)
                .column(2)
                .build()// valid
        ))
        .amount(BigDecimal.valueOf(498))
        .build();
    webClient
        .post()
        .uri(seatStateV1Url + "/book")
        .bodyValue(bookSeatRequestDto)
        .headers(headers -> TestUtils.setAuthHeader(headers, "punna"))
        .exchange()
        .expectStatus()
        .isEqualTo(HttpStatus.CONFLICT);

  }

  @Test
  @Order(10)
  void givenBookedSeatLocation_whenBlock_thenReturnConflict() {
    webClient
        .post()
        .uri(seatStateV1Url + "/block")
        .bodyValue(List.of(SeatLocation
                .builder()
                .row(0)
                .column(0)
                .build(), // already booked
            SeatLocation
                .builder()
                .row(1)
                .column(2)
                .build()// valid
        ))
        .headers(headers -> TestUtils.setAuthHeader(headers, "punna"))
        .exchange()
        .expectStatus()
        .isEqualTo(HttpStatus.CONFLICT);
  }

  @Test
  @Order(11)
  void givenBookedSeatLocation_whenUnblock_thenReturnConflict() {
    webClient
        .post()
        .uri(seatStateV1Url + "/block")
        .bodyValue(List.of(SeatLocation
                .builder()
                .row(0)
                .column(0)
                .build(), // already booked
            SeatLocation
                .builder()
                .row(1)
                .column(2)
                .build()// valid
        ))
        .headers(headers -> TestUtils.setAuthHeader(headers, "punna"))
        .exchange()
        .expectStatus()
        .isEqualTo(HttpStatus.CONFLICT);
  }
}
