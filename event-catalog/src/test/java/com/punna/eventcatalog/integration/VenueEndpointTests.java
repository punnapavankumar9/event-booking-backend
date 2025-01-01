package com.punna.eventcatalog.integration;

import com.punna.eventcatalog.dto.VenueDto;
import com.punna.eventcatalog.fixtures.TestFixtures;
import com.punna.eventcatalog.repository.VenueRepository;
import org.junit.jupiter.api.*;
import org.punna.commons.exception.ProblemDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.List;

import static com.punna.eventcatalog.TestUtils.setAuthHeader;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class VenueEndpointTests extends ContainerBase {

    private final String venuesV1Url = "/api/v1/venues";
    public String venueId = null;
    @Autowired
    private WebTestClient webTestClient;
    @Autowired
    private VenueRepository venueRepository;

    @BeforeAll
    void setUp() {
        venueRepository
                .deleteAll()
                .block();
    }


    @Test
    @Order(1)
    void givenInvalidVenue_whenCreatingVenue_thenShouldReturnBadRequest() {
        VenueDto venueDto = new VenueDto();
        ProblemDetail responseBody = webTestClient
                .post()
                .uri(venuesV1Url)
                .contentType(MediaType.APPLICATION_JSON)
                .headers(headers -> setAuthHeader(headers, "punna"))
                .bodyValue(venueDto)
                .exchange()
                .expectStatus()
                .isBadRequest()
                .expectBody(ProblemDetail.class)
                .returnResult()
                .getResponseBody();
        assert responseBody != null;
        assertThat(responseBody.getStatus()).isEqualTo(400);
        assertThat(responseBody
                .getErrors()
                .size()).isEqualTo(8);
    }

    @Test
    @Order(2)
    void givenVenueWithId_whenCreatingVenue_thenReturnBadRequest() {
        VenueDto venueDto = TestFixtures.SAMPLE_VENUE_DTO;
        venueDto.setId("DummyId");
        ProblemDetail responseBody = webTestClient
                .post()
                .uri(venuesV1Url)
                .contentType(MediaType.APPLICATION_JSON)
                .headers(headers -> setAuthHeader(headers, "punna"))
                .bodyValue(venueDto)
                .exchange()
                .expectStatus()
                .isBadRequest()
                .expectBody(ProblemDetail.class)
                .returnResult()
                .getResponseBody();
        assert responseBody != null;
        assertThat(responseBody.getStatus()).isEqualTo(400);
        assertThat(responseBody
                .getErrors()
                .size()).isEqualTo(1);
    }

    @Test
    @Order(3)
    void givenValidVenue_whenCreatingVenue_thenCreateAndReturn() {
        VenueDto venueDto = TestFixtures.SAMPLE_VENUE_DTO;
        venueDto.setId(null);
        VenueDto responseBody = webTestClient
                .post()
                .uri(venuesV1Url)
                .contentType(MediaType.APPLICATION_JSON)
                .headers(headers -> setAuthHeader(headers, "punna"))
                .bodyValue(venueDto)
                .exchange()
                .expectStatus()
                .isCreated()
                .expectBody(VenueDto.class)
                .returnResult()
                .getResponseBody();
        assert responseBody != null;
        assertThat(responseBody.getId()).isNotNull();
        assertThat(responseBody.getOwnerId()).isEqualTo("punna");
        venueId = responseBody.getId();
    }

    @Test
    @Order(4)
    void givenInvalidVenueId_whenUpdating_thenReturnNotFound() {
        VenueDto venueDto = new VenueDto();
        venueDto.setId("DummyId");
        webTestClient
                .patch()
                .uri(venuesV1Url)
                .headers(headers -> setAuthHeader(headers, "punna"))
                .bodyValue(venueDto)
                .exchange()
                .expectStatus()
                .isNotFound();
    }

    @Test
    @Order(5)
    void givenNoVenueId_whenUpdating_thenReturnNotFound() {
        VenueDto venueDto = new VenueDto();
        ProblemDetail responseBody = webTestClient
                .patch()
                .uri(venuesV1Url)
                .bodyValue(venueDto)
                .headers(headers -> setAuthHeader(headers, "punna"))
                .exchange()
                .expectStatus()
                .isBadRequest()
                .expectBody(ProblemDetail.class)
                .returnResult()
                .getResponseBody();
        assert responseBody != null;
        assertThat(responseBody.getStatus()).isEqualTo(400);
        assertThat(responseBody
                .getErrors()
                .size()).isEqualTo(1);
    }

    @Test
    @Order(6)
    void givenNonAdminUser_whenUpdating_thenReturnForbidden() {
        webTestClient
                .patch()
                .uri(venuesV1Url)
                .bodyValue(VenueDto
                        .builder()
                        .id(venueId)
                        .name("Dummy name")
                        .build())
                .headers(headers -> setAuthHeader(headers, "non-admin"))
                .exchange()
                .expectStatus()
                .isForbidden();
    }

    @Test
    @Order(7)
    void givenValidVenue_whenUpdating_thenReturnOk() {
        final String newStateName = "Telangana TS";
        VenueDto venueDto = VenueDto
                .builder()
                .id(venueId)
                .state(newStateName)
                .capacity(1000)
                .build();
        VenueDto responseBody = webTestClient
                .patch()
                .uri(venuesV1Url)
                .contentType(MediaType.APPLICATION_JSON)
                .headers(headers -> setAuthHeader(headers, "punna"))
                .bodyValue(venueDto)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(VenueDto.class)
                .returnResult()
                .getResponseBody();
        assert responseBody != null;
        assertThat(responseBody.getCapacity()).isEqualTo(1000);
        assertThat(responseBody.getId()).isEqualTo(venueId);
        assertThat(responseBody.getState()).isEqualTo(newStateName);
    }

    @Test
    @Order(8)
    void givenInvalidVenueId_whenDeleting_thenReturnNotFound() {
        webTestClient
                .delete()
                .uri(venuesV1Url + "/dummyId")
                .headers(headers -> setAuthHeader(headers, "punna"))
                .exchange()
                .expectStatus()
                .isNotFound();
    }

    @Test
    @Order(9)
    void givenNothing_whenFindAll_thenReturnOk() {
        List<VenueDto> responseBody = webTestClient
                .get()
                .uri(venuesV1Url)
                .headers(headers -> setAuthHeader(headers, "punna"))
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(VenueDto.class)
                .returnResult()
                .getResponseBody();
        assert responseBody != null;
        assertThat(responseBody.size()).isEqualTo(1);
    }

    @Test
    @Order(10)
    void givenInvalidId_whenFindById_thenReturnNotFound() {
        webTestClient
                .get()
                .uri(venuesV1Url + "/DummyId")
                .headers(headers -> setAuthHeader(headers, "punna"))
                .exchange()
                .expectStatus()
                .isNotFound();
    }

    @Test
    @Order(11)
    void givenValidId_whenFindById_thenReturnOk() {
        List<VenueDto> responseBody = webTestClient
                .get()
                .uri(venuesV1Url + "/" + venueId)
                .headers(headers -> setAuthHeader(headers, "punna"))
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(VenueDto.class)
                .returnResult()
                .getResponseBody();
        assertThat(responseBody).isNotNull();
        assertThat(responseBody.size()).isEqualTo(1);
    }

    @Test
    @Order(12)
    void givenNonAdminUser_whenDelete_thenReturnForbidden() {
        webTestClient
                .delete()
                .uri(venuesV1Url + "/" + venueId)
                .headers(headers -> setAuthHeader(headers, "non-admin"))
                .exchange()
                .expectStatus()
                .isForbidden();
    }

    @Test
    @Order(13)
    void givenValidVenue_whenDeleting_thenReturnOk() {
        webTestClient
                .delete()
                .uri(venuesV1Url + "/" + venueId)
                .headers(headers -> setAuthHeader(headers, "punna"))
                .exchange()
                .expectStatus()
                .isOk();
        List<VenueDto> responseBody = webTestClient
                .get()
                .uri(venuesV1Url)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(VenueDto.class)
                .returnResult()
                .getResponseBody();
        assert responseBody != null;
        assertThat(responseBody.size()).isEqualTo(0);
    }

}
