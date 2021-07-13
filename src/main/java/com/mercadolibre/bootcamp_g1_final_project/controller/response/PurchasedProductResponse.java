package com.mercadolibre.bootcamp_g1_final_project.controller.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PurchasedProductResponse {

    private String name;

    private Double price;

    private Integer quantity;

    private Double totalPrice;
}
