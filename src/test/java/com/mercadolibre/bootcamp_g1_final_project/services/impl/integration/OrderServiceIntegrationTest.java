package com.mercadolibre.bootcamp_g1_final_project.services.impl.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mercadolibre.bootcamp_g1_final_project.controller.request.BatchRequest;
import com.mercadolibre.bootcamp_g1_final_project.controller.request.InboundOrderRequest;
import com.mercadolibre.bootcamp_g1_final_project.controller.request.LoginRequest;
import com.mercadolibre.bootcamp_g1_final_project.controller.response.LoginResponse;
import com.mercadolibre.bootcamp_g1_final_project.integration.ControllerTest;
import com.mercadolibre.bootcamp_g1_final_project.repositories.ProductRepository;
import com.mercadolibre.bootcamp_g1_final_project.repositories.SectionRepository;
import com.mercadolibre.bootcamp_g1_final_project.repositories.UserRepository;
import com.mercadolibre.bootcamp_g1_final_project.repositories.WarehouseRepository;
import com.mercadolibre.bootcamp_g1_final_project.security.TokenService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
public class OrderServiceIntegrationTest extends ControllerTest {

    private final MockMvc mockMvc;

    private final ObjectMapper mapper;

    @Autowired
    public OrderServiceIntegrationTest(ObjectMapper mapper, WebApplicationContext webApplicationContext) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).apply(springSecurity()).build();;
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



    @Test
    public void createASucessfulInboundOrder() throws Exception {
        LoginRequest login = LoginRequest.builder().email("representative@email.com").password("1234").build();
        LoginResponse loginResponse = this.testRestTemplate.postForObject("/api/v1/auth/login",login,LoginResponse.class);

        this.mockMvc.perform(post("/api/v1/fresh-products/inboundorder")
                .contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(orderRequest))
                .header("Authorization", String.format("Bearer %s", loginResponse.getToken())))
                .andExpect(status().isCreated());
    }

    @Test
    public void unauthorizedUserCreatingInboundOrder() throws Exception {
        LoginRequest login = LoginRequest.builder().email("seller@email.com").password("1234").build();
        LoginResponse loginResponse = this.testRestTemplate.postForObject("/api/v1/auth/login",login,LoginResponse.class);

        this.mockMvc.perform(post("/api/v1/fresh-products/inboundorder")
                .contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(orderRequest))
                .header("Authorization", String.format("Bearer %s", loginResponse.getToken())))
                .andExpect(status().isForbidden());
    }

    @Test
    public void creatingInboundOrderInexistentWarehouse() throws Exception {
        LoginRequest login = LoginRequest.builder().email("representative@email.com").password("1234").build();

        LoginResponse loginResponse = this.testRestTemplate.postForObject("/api/v1/auth/login",login,LoginResponse.class);
        orderRequest.setWarehouseId(200);
        this.mockMvc.perform(post("/api/v1/fresh-products/inboundorder")
                .contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(orderRequest))
                .header("Authorization", String.format("Bearer %s", loginResponse.getToken())))
                .andExpect(status().isNotFound());
    }

}
