package com.mercadolibre.bootcamp_g1_final_project.services;

import com.mercadolibre.bootcamp_g1_final_project.entities.Product;

public interface ProductService {
    Product findById(Integer id);
}
