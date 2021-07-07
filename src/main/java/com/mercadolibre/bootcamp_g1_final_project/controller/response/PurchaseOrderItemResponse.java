package com.mercadolibre.bootcamp_g1_final_project.controller.response;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PurchaseOrderItemResponse {
  private Integer productId;
  private Integer quantity;


  public Integer getProductId() {
    return productId;
  }

  public Integer getQuantity() {
    return quantity;
  }
}
