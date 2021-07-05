package com.mercadolibre.bootcamp_g1_final_project.services.impl;

import com.mercadolibre.bootcamp_g1_final_project.entities.Product;
import com.mercadolibre.bootcamp_g1_final_project.exceptions.ProductNotExistException;
import com.mercadolibre.bootcamp_g1_final_project.repositories.ProductRepository;
import com.mercadolibre.bootcamp_g1_final_project.services.ProductService;
import org.springframework.stereotype.Service;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public Product findById(Integer id) throws ProductNotExistException {
        return productRepository.findById(id).orElseThrow(ProductNotExistException::new);
    }
}
