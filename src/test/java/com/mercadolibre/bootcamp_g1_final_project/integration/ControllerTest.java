package com.mercadolibre.bootcamp_g1_final_project.integration;

import com.mercadolibre.bootcamp_g1_final_project.entities.users.User;
import com.mercadolibre.bootcamp_g1_final_project.security.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;

public abstract class ControllerTest extends IntegrationTest {
	@Autowired
	protected TestRestTemplate testRestTemplate;

	@Autowired
	private TokenService tokenService;

	protected <T> RequestEntity<T> getDefaultRequestEntity() {
		HttpHeaders headers = new HttpHeaders();
		return new RequestEntity<>(headers, HttpMethod.GET, null);
	}

	protected  <T> RequestEntity<T> authorizedRequest(User user, T body) {
		HttpHeaders headers = new HttpHeaders();
		headers.add(HttpHeaders.AUTHORIZATION, tokenService.generateToken(user));
		return new RequestEntity<>(body, headers, null, null);
	}
}
