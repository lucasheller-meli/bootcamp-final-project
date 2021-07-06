package com.mercadolibre.bootcamp_g1_final_project.controller.request;

import javax.validation.constraints.Positive;

public class PurchaseOrderItemRequest {
  @Positive
  private Integer productId;
  @Positive
  private Integer quantity;

  public PurchaseOrderItemRequest(Integer productId, Integer quantity) {
    this.productId = productId;
    this.quantity = quantity;
  }

  public PurchaseOrderItemRequest() {
  }

  public Integer getProductId() {
    return productId;
  }

  public Integer getQuantity() {
    return quantity;
  }
}
