package com.mercadolibre.bootcamp_g1_final_project.controller.response;

import com.mercadolibre.bootcamp_g1_final_project.entities.ProductType;

import java.util.List;

public class ProductListResponse {
  private List<ProductsResponse> products;

  public ProductListResponse(List<ProductsResponse> products) {
    this.products = products;
  }

  public ProductListResponse() {
  }

  public List<ProductsResponse> getProducts() {
    return products;
  }
}
