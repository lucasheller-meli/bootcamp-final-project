package com.mercadolibre.bootcamp_g1_final_project.controller.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PurchaseOrderResponse {
  private Integer id;
  private LocalDate date;
  private Integer buyerId;
  private List<PurchaseProductResponse> products;
  private Double totalPrice;
}
