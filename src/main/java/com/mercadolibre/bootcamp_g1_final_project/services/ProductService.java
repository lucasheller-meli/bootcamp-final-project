package com.mercadolibre.bootcamp_g1_final_project.services;

import com.mercadolibre.bootcamp_g1_final_project.controller.response.ProductsResponse;
import com.mercadolibre.bootcamp_g1_final_project.entities.Product;

import java.util.List;

public interface ProductService {
    Product findById(Integer id);

    List<ProductsResponse> listProducts(String category);

}
