package com.mercadolibre.bootcamp_g1_final_project.controller.response;

import java.util.List;

public class PurchaseOrderItemsResponse {
  private final List<PurchaseOrderItemResponse> products;

  public PurchaseOrderItemsResponse(List<PurchaseOrderItemResponse> products) {
    this.products = products;
  }

  public List<PurchaseOrderItemResponse> getProducts() {
    return products;
  }
}
