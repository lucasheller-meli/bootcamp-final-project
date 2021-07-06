package com.mercadolibre.bootcamp_g1_final_project.controller;

import com.mercadolibre.bootcamp_g1_final_project.controller.response.BatchListResponse;
import com.mercadolibre.bootcamp_g1_final_project.controller.response.ProductListResponse;
import com.mercadolibre.bootcamp_g1_final_project.services.BatchService;
import com.mercadolibre.bootcamp_g1_final_project.services.ProductService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/fresh-products")
public class ProductController {

    private final ProductService productService;

    private final BatchService batchService;

    public ProductController(ProductService productService, BatchService batchService) {
        this.productService = productService;
        this.batchService = batchService;
    }

    @GetMapping("/list")
    public List<ProductListResponse> listProduct(@RequestParam(required = false) String category){
        return productService.listProducts(category);
    }

    @GetMapping("/list-batch/{productId}")
    public List<BatchListResponse> listBatch(@PathVariable Integer productId){
        return productService.listProductsInBatch(productId);
    }

}
