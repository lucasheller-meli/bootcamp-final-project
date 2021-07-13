package com.mercadolibre.bootcamp_g1_final_project.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mercadolibre.bootcamp_g1_final_project.controller.request.BuyerRequest;
import com.mercadolibre.bootcamp_g1_final_project.controller.request.LoginRequest;
import com.mercadolibre.bootcamp_g1_final_project.controller.response.LoginResponse;
import com.mercadolibre.bootcamp_g1_final_project.integration.ControllerTest;
import com.mercadolibre.bootcamp_g1_final_project.util.strategy.BuyerParameter;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;


import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("local")
class BuyerControllerTest extends ControllerTest {

    private final MockMvc mockMvc;

    private final ObjectMapper mapper;

    @Autowired
    public BuyerControllerTest(WebApplicationContext webApplicationContext, ObjectMapper mapper) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).apply(springSecurity()).build();
        this.mapper = mapper;
    }

    @Test
    public void getASucessfulHistoric() throws Exception {
        LoginRequest login = LoginRequest.builder().email("buyer@email.com").password("1234").build();
        LoginResponse loginResponse = this.testRestTemplate.postForObject("/api/v1/auth/login", login, LoginResponse.class);

        this.mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/fresh-products/buyer/1")
                .contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(BuyerRequest.builder().parameter(BuyerParameter.TOTAL_ASC).build()))
                .header("Authorization", String.format("Bearer %s", loginResponse.getToken())))
                .andExpect(status().isOk());
    }

    @Test
    public void unauthorizedUserGettingHistoric() throws Exception {
        LoginRequest login = LoginRequest.builder().email("seller@email.com").password("1234").build();
        LoginResponse loginResponse = this.testRestTemplate.postForObject("/api/v1/auth/login", login, LoginResponse.class);

        this.mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/fresh-products/inboundorder")
                .contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(BuyerRequest.builder().parameter(BuyerParameter.TOTAL_ASC).build()))
                .header("Authorization", String.format("Bearer %s", loginResponse.getToken())))
                .andExpect(status().isForbidden());
    }

}