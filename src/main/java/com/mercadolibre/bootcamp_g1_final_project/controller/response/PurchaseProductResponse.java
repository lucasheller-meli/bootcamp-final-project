package com.mercadolibre.bootcamp_g1_final_project.controller.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PurchaseProductResponse {
  private Integer productId;
  private Integer quantity;
}
