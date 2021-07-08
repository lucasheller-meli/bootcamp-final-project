package com.mercadolibre.bootcamp_g1_final_project.integration;

import com.mercadolibre.bootcamp_g1_final_project.controller.request.LoginRequest;
import com.mercadolibre.bootcamp_g1_final_project.controller.response.LoginResponse;
import com.mercadolibre.bootcamp_g1_final_project.entities.users.Seller;
import com.mercadolibre.bootcamp_g1_final_project.exceptions.ApiError;
import com.mercadolibre.bootcamp_g1_final_project.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.DirtiesContext;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class AuthControllerTest extends ControllerTest {
  private final PasswordEncoder encoder;
  private final UserRepository userRepository;

  private final String loginUrl = "/api/v1/auth/login";

  private final String email = "seller@email.com";
  private final String password = "1234";

  @Autowired
  public AuthControllerTest(PasswordEncoder encoder, UserRepository userRepository) {
    this.encoder = encoder;
    this.userRepository = userRepository;
  }

  @BeforeEach
  void setup() {
    userRepository.save(new Seller(email, encoder.encode(password)));
  }

  @Test
  void whenLoginIsCalledWithValidCredentialsItReturnsTheToken() {
    LoginRequest request = new LoginRequest(email, password);
    ResponseEntity<LoginResponse> response = this.testRestTemplate.postForEntity(loginUrl, request, LoginResponse.class);

    assertEquals(HttpStatus.OK, response.getStatusCode());

    LoginResponse body = Objects.requireNonNull(response.getBody());
    assertEquals(email, body.getEmail());
    assertEquals(149, body.getToken().length());
    assertTrue(body.getToken().startsWith("ey"));
  }

  @Test
  void whenLoginIsCalledWithNonExistingEmailItThrowsBadCredentialsException() {
    LoginRequest request = new LoginRequest("not-saved-email@email.com", password);
    ResponseEntity<ApiError> response = this.testRestTemplate.postForEntity(loginUrl, request, ApiError.class);

    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

    ApiError body = Objects.requireNonNull(response.getBody());
    assertEquals(HttpStatus.BAD_REQUEST.name(), body.getError());
    assertEquals(HttpStatus.BAD_REQUEST.value(), body.getStatus());
    assertEquals("Bad credentials", body.getMessage());
  }

  @Test
  void whenLoginIsCalledWithWrongPasswordItThrowsBadCredentialsException() {
    LoginRequest request = new LoginRequest(email, "wrong password");
    ResponseEntity<ApiError> response = this.testRestTemplate.postForEntity(loginUrl, request, ApiError.class);

    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

    ApiError body = Objects.requireNonNull(response.getBody());
    assertEquals(HttpStatus.BAD_REQUEST.name(), body.getError());
    assertEquals(HttpStatus.BAD_REQUEST.value(), body.getStatus());
    assertEquals("Bad credentials", body.getMessage());
  }
}
