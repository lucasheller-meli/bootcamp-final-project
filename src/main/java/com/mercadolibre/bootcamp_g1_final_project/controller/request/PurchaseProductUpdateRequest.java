package com.mercadolibre.bootcamp_g1_final_project.controller.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Positive;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PurchaseProductUpdateRequest {

    @Positive
    private Integer purchaseProductId;
    @Positive
    private Integer productId;
    @Positive
    private Integer quantity;
}
