package com.mercadolibre.bootcamp_g1_final_project.controller;

import com.mercadolibre.bootcamp_g1_final_project.controller.response.ProductsResponse;
import com.mercadolibre.bootcamp_g1_final_project.services.ProductService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/fresh-products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public List<ProductsResponse> findAll(){
        return productService.listProducts();
    }
}
