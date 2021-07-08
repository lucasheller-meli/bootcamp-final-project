package com.mercadolibre.bootcamp_g1_final_project.controller;

import com.mercadolibre.bootcamp_g1_final_project.controller.response.BatchListResponse;
import com.mercadolibre.bootcamp_g1_final_project.controller.response.ProductListResponse;
import com.mercadolibre.bootcamp_g1_final_project.services.BatchService;
import com.mercadolibre.bootcamp_g1_final_project.services.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/fresh-products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/list")
    public ResponseEntity<ProductListResponse> listProduct(@RequestParam(required = false) String category){
        return ResponseEntity.ok(productService.listProducts(category));
    }

    @GetMapping("/list-batch/{productId}/order")
    public List<BatchListResponse> listBatch(@PathVariable Integer productId, @RequestParam(required = false) String order){
        return productService.listProductsInBatch(productId, order);
    }

    @GetMapping("/duedate/{days}")
    public List<BatchListResponse> checkBatchPerDuedate(@PathVariable Integer days, @RequestParam(required = false) String category){
        return productService.listProductPerDuedata(days, category);
    }

}
