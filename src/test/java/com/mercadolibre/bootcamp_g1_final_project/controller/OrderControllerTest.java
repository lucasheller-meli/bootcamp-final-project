package com.mercadolibre.bootcamp_g1_final_project.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mercadolibre.bootcamp_g1_final_project.controller.request.*;
import com.mercadolibre.bootcamp_g1_final_project.controller.response.LoginResponse;
import com.mercadolibre.bootcamp_g1_final_project.integration.ControllerTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDateTime;
import java.util.List;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
class OrderControllerTest extends ControllerTest {

    private final MockMvc mockMvc;

    private final ObjectMapper mapper;

    @Autowired
    public OrderControllerTest(ObjectMapper mapper, WebApplicationContext webApplicationContext) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).apply(springSecurity()).build();
        this.mapper = mapper;
    }

    private final BatchRequest batchRequest = BatchRequest.builder()
            .productId(1)
            .currentTemperature(3.4F)
            .minimumTemperature(3.4F)
            .quantity(20)
            .dueDate(LocalDateTime.now())
            .build();

    private final InboundOrderRequest orderRequest = InboundOrderRequest
            .builder()
            .warehouseId(1)
            .sectionId(1)
            .batches(List.of(batchRequest))
            .build();

    private final BatchUpdateRequest updateBatchRequest = BatchUpdateRequest.builder()
            .productId(1)
            .currentTemperature(3.4F)
            .minimumTemperature(3.4F)
            .quantity(20)
            .dueDate(LocalDateTime.now())
            .build();

    private final InboundOrderUpdateRequest updateOrderRequest = InboundOrderUpdateRequest
            .builder()
            .batches(List.of(updateBatchRequest))
            .build();


    @Test
    public void createASucessfulInboundOrder() throws Exception {
        LoginRequest login = LoginRequest.builder().email("representative@email.com").password("1234").build();
        LoginResponse loginResponse = this.testRestTemplate.postForObject("/api/v1/auth/login", login, LoginResponse.class);

        this.mockMvc.perform(post("/api/v1/fresh-products/inboundorder")
                .contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(orderRequest))
                .header("Authorization", String.format("Bearer %s", loginResponse.getToken())))
                .andExpect(status().isCreated());
    }

    @Test
    public void unauthorizedUserCreatingInboundOrder() throws Exception {
        LoginRequest login = LoginRequest.builder().email("seller@email.com").password("1234").build();
        LoginResponse loginResponse = this.testRestTemplate.postForObject("/api/v1/auth/login", login, LoginResponse.class);

        this.mockMvc.perform(post("/api/v1/fresh-products/inboundorder")
                .contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(orderRequest))
                .header("Authorization", String.format("Bearer %s", loginResponse.getToken())))
                .andExpect(status().isForbidden());
    }

    @Test
    public void creatingInboundOrderInexistentWarehouse() throws Exception {
        LoginRequest login = LoginRequest.builder().email("representative@email.com").password("1234").build();

        LoginResponse loginResponse = this.testRestTemplate.postForObject("/api/v1/auth/login", login, LoginResponse.class);
        orderRequest.setWarehouseId(200);
        this.mockMvc.perform(post("/api/v1/fresh-products/inboundorder")
                .contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(orderRequest))
                .header("Authorization", String.format("Bearer %s", loginResponse.getToken())))
                .andExpect(status().isNotFound());
    }

    @Test
    public void updateASucessfulInboundOrder() throws Exception {
        LoginRequest login = LoginRequest.builder().email("representative@email.com").password("1234").build();
        LoginResponse loginResponse = this.testRestTemplate.postForObject("/api/v1/auth/login", login, LoginResponse.class);

        this.mockMvc.perform(patch("/api/v1/fresh-products/inboundorder/1")
                .contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(updateOrderRequest))
                .header("Authorization", String.format("Bearer %s", loginResponse.getToken())))
                .andExpect(status().isCreated());
    }

    @Test
    public void unauthorizedUserUpdatingInboundOrder() throws Exception {
        LoginRequest login = LoginRequest.builder().email("seller@email.com").password("1234").build();
        LoginResponse loginResponse = this.testRestTemplate.postForObject("/api/v1/auth/login", login, LoginResponse.class);

        this.mockMvc.perform(patch("/api/v1/fresh-products/inboundorder/1")
                .contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(updateOrderRequest))
                .header("Authorization", String.format("Bearer %s", loginResponse.getToken())))
                .andExpect(status().isForbidden());
    }

    @Test
    public void updatingInboundOrderInexistentProduct() throws Exception {
        LoginRequest login = LoginRequest.builder().email("representative@email.com").password("1234").build();

        LoginResponse loginResponse = this.testRestTemplate.postForObject("/api/v1/auth/login", login, LoginResponse.class);
        updateBatchRequest.setProductId(200);
        this.mockMvc.perform(patch("/api/v1/fresh-products/inboundorder/1")
                .contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(updateOrderRequest))
                .header("Authorization", String.format("Bearer %s", loginResponse.getToken())))
                .andExpect(status().isNotFound());
    }



}