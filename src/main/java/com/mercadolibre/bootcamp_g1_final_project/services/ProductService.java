package com.mercadolibre.bootcamp_g1_final_project.services;

import com.mercadolibre.bootcamp_g1_final_project.controller.response.BatchListResponse;
import com.mercadolibre.bootcamp_g1_final_project.controller.response.ProductListResponse;
import com.mercadolibre.bootcamp_g1_final_project.entities.Product;

import java.util.List;

public interface ProductService {
    Product findById(Integer id);

    List<ProductListResponse> listProducts(String category);

    public List<BatchListResponse> listProductsInBatch(Integer productId);

}
