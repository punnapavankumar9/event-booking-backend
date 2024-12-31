package com.punna.identity.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.punna.identity.dto.UserRequestDto;
import com.punna.identity.dto.UserResponseDto;
import com.punna.identity.dto.UsernamePasswordDto;
import com.punna.identity.model.User;
import com.punna.identity.repository.UserRepository;
import lombok.SneakyThrows;
import org.junit.jupiter.api.*;
import org.punna.commons.exception.EntityNotFoundException;
import org.punna.commons.exception.ProblemDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;

import java.net.URI;

import static com.punna.identity.fixtures.TestFixtures.SAMPLE_USER_DTO;
import static com.punna.identity.utils.TestUtils.authHeader;
import static com.punna.identity.utils.TestUtils.createHttpEntity;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(PER_CLASS)
class IdentityServiceApplicationTests extends ContainerBase {

    private final String usersV1BaseUrl = "/api/v1/users";
    private final String updatedEmail = "newEmail@gmail.com";
    private String jwtToken;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private ObjectMapper objectMapper;

    @SneakyThrows
    public <T> T clone(T object, Class<T> clazz) {
        String s = objectMapper.writeValueAsString(object);
        return objectMapper.readValue(s, clazz);
    }

    @BeforeAll
    void setUp() {
        userRepository.deleteAll();
    }

    @Test
    @Order(1)
    void contextLoads() {
    }

    @Test
    @Order(2)
    void givenInvalidUser_whenCreatingUser_thenReturnBadRequest() {
        ResponseEntity<ProblemDetail> problemDetailResponseEntity = restTemplate.postForEntity(usersV1BaseUrl,
                new UserRequestDto(),
                ProblemDetail.class);
        assertThat(problemDetailResponseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        ProblemDetail body = problemDetailResponseEntity.getBody();
        assertThat(body).isNotNull();
        assertThat(body
                .getErrors()
                .size()).isEqualTo(3);
    }

    @Test
    @Order(3)
    void givenValidUser_whenCreatingUser_thenCreateAndReturnUser() {
        UserRequestDto userRequestDto = clone(SAMPLE_USER_DTO, UserRequestDto.class);
        ResponseEntity<UserResponseDto> response = restTemplate.postForEntity(usersV1BaseUrl,
                userRequestDto,
                UserResponseDto.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        UserResponseDto body = response.getBody();
        assertThat(body).isNotNull();
        assertThat(body.getCreatedAt()).isNotNull();
        assertThat(body.getLastModifiedAt()).isNotNull();
        assertThat(body.getEmail()).isEqualTo(userRequestDto.getEmail());
        assertThat(body.getLastLoginAt()).isNull();
    }

    @Test
    @Order(4)
    void givenInvalidUsername_whenLoggingUser_thenReturnNotFound() {
        UsernamePasswordDto usernamePasswordDto = new UsernamePasswordDto("Dummy", SAMPLE_USER_DTO.getPassword());
        ResponseEntity<String> response = restTemplate.postForEntity(usersV1BaseUrl + "/login",
                usernamePasswordDto,
                String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    @Order(5)
    void givenInvalidPassword_whenLoggingUser_thenReturnNotFound() {
        UsernamePasswordDto usernamePasswordDto = new UsernamePasswordDto("pavan", "Dummy123");
        ResponseEntity<String> response = restTemplate.postForEntity(usersV1BaseUrl + "/login",
                usernamePasswordDto,
                String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    @Order(6)
    void givenValidUser_whenLoggingUser_thenLoginAndReturnToken() {
        UsernamePasswordDto usernamePasswordDto = new UsernamePasswordDto(SAMPLE_USER_DTO.getUsername(),
                SAMPLE_USER_DTO.getPassword());
        ResponseEntity<String> response = restTemplate.postForEntity(usersV1BaseUrl + "/login",
                usernamePasswordDto,
                String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        String body = response.getBody();
        assertThat(body).isNotNull();
        assertThat(StringUtils.countOccurrencesOf(body, ".")).isEqualTo(2);
        jwtToken = body;
    }

    @Test
    @Order(7)
    void givenUsername_whenFindUser_thenPasswordMustBeEncrypted() {
        User user = userRepository
                .findByUsernameOrEmail(SAMPLE_USER_DTO.getUsername(), SAMPLE_USER_DTO.getEmail())
                .orElseThrow(EntityNotFoundException::new);
        assertThat(user.getPassword()).hasSizeGreaterThan(20);
    }

    @Test
    @Order(8)
    void givenNoJwtToken_whenUpdateUser_thenReturnForbidden() {
        UserRequestDto userRequestDto = UserRequestDto
                .builder()
                .email("invalidEmail")
                .password("shorter") // shorter password
                .build();
        ResponseEntity<String> exchange = restTemplate.exchange(URI.create(usersV1BaseUrl),
                HttpMethod.PATCH,
                new HttpEntity<>(userRequestDto),
                String.class);
        assertThat(exchange.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }

    @Test
    @Order(9)
    void givenJwtTokenAndInValidUser_whenUpdateUser_thenReturnBadRequest() {
        UserRequestDto userRequestDto = UserRequestDto
                .builder()
                .email("invalidEmail")
                .password("shorter") // shorter password
                .build();
        ResponseEntity<ProblemDetail> exchange = restTemplate.exchange(URI.create(usersV1BaseUrl),
                HttpMethod.PATCH,
                new HttpEntity<>(userRequestDto, authHeader(jwtToken)),
                ProblemDetail.class);
        assertThat(exchange.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        ProblemDetail body = exchange.getBody();
        assertThat(body).isNotNull();
        assertThat(body
                .getErrors()
                .size()).isEqualTo(3);
    }

    @Test
    @Order(9)
    void givenJwtTokenAndValidUser_whenUpdateUser_thenReturnUpdatedUser() {
        UserRequestDto userRequestDto = UserRequestDto
                .builder()
                .username(SAMPLE_USER_DTO.getUsername())
                .email(updatedEmail)
                .password("longer121") // shorter password
                .build();
        ResponseEntity<UserResponseDto> exchange = restTemplate.exchange(URI.create(usersV1BaseUrl),
                HttpMethod.PATCH,
                new HttpEntity<>(userRequestDto, authHeader(jwtToken)),
                UserResponseDto.class);
        assertThat(exchange.getStatusCode()).isEqualTo(HttpStatus.OK);
        UserResponseDto body = exchange.getBody();
        assertThat(body).isNotNull();
        assertThat(body.getEmail()).isEqualTo(userRequestDto.getEmail());
        assertThat(body.getUsername()).isEqualTo(userRequestDto.getUsername());
    }


    @Test
    @Order(10)
    void givenInvalidUsername_whenFindUser_thenReturnNotFound() {
        ResponseEntity<ProblemDetail> response = restTemplate.exchange(usersV1BaseUrl + "/dummy",
                HttpMethod.GET,
                createHttpEntity(null, jwtToken),
                ProblemDetail.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    @Order(11)
    void givenValidUsername_whenFindUser_thenReturnUser() {
        ResponseEntity<UserResponseDto> response = restTemplate.exchange(usersV1BaseUrl + "/" + SAMPLE_USER_DTO.getUsername(),
                HttpMethod.GET,
                createHttpEntity(null, jwtToken),
                UserResponseDto.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        UserResponseDto body = response.getBody();
        assertThat(body).isNotNull();
        assertThat(body.getUsername()).isEqualTo(SAMPLE_USER_DTO.getUsername());
        assertThat(body.getEmail()).isEqualTo(updatedEmail);
    }
}
