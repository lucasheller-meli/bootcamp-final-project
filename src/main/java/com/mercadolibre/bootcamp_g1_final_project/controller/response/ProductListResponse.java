package com.mercadolibre.bootcamp_g1_final_project.controller.response;


import java.util.List;

public class ProductListResponse {
  private List<ProductResponse> products;

  public ProductListResponse(List<ProductResponse> products) {
    this.products = products;
  }

  public ProductListResponse() {
  }

  public List<ProductResponse> getProducts() {
    return products;
  }
}
