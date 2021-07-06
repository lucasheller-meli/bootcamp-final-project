package com.mercadolibre.bootcamp_g1_final_project.controller.response;

public class PurchaseOrderItemResponse {
  private final Integer productId;
  private final Integer quantity;

  public PurchaseOrderItemResponse(Integer productId, Integer quantity) {
    this.productId = productId;
    this.quantity = quantity;
  }

  public Integer getProductId() {
    return productId;
  }

  public Integer getQuantity() {
    return quantity;
  }
}
