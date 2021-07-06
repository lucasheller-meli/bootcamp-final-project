package com.mercadolibre.bootcamp_g1_final_project.services.impl;

import com.mercadolibre.bootcamp_g1_final_project.controller.response.BatchListResponse;
import com.mercadolibre.bootcamp_g1_final_project.controller.response.ProductListResponse;
import com.mercadolibre.bootcamp_g1_final_project.entities.Batch;
import com.mercadolibre.bootcamp_g1_final_project.entities.Product;
import com.mercadolibre.bootcamp_g1_final_project.entities.ProductType;
import com.mercadolibre.bootcamp_g1_final_project.exceptions.ProductNotExistException;
import com.mercadolibre.bootcamp_g1_final_project.repositories.ProductRepository;
import com.mercadolibre.bootcamp_g1_final_project.services.BatchService;
import com.mercadolibre.bootcamp_g1_final_project.services.ProductService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final BatchService batchService;

    public ProductServiceImpl(ProductRepository productRepository, BatchService batchService) {
        this.productRepository = productRepository;
        this.batchService = batchService;
    }

    @Override
    public Product findById(Integer id) throws ProductNotExistException {
        return productRepository.findById(id).orElseThrow(ProductNotExistException::new);
    }

    public List<ProductListResponse> listProducts(String category) throws ProductNotExistException {
        final List<Product> products = (List<Product>) productRepository.findAll();

        if (products.isEmpty()) throw new ProductNotExistException();

        return getProductsResponses(category, products);

    }

    private List<ProductListResponse> getProductsResponses(String category, List<Product> products) {
        final List<ProductListResponse> productListResponse = new ArrayList<>();

        for (Product p : products) {
            ProductType type = p.getType();

            if (category != null) {
                if (type.toString().compareTo(category) == 0) {
                    ProductListResponse pr = new ProductListResponse(p.getId(), p.getName());
                    productListResponse.add(pr);
                }
            } else {
                ProductListResponse pr = new ProductListResponse(p.getId(), p.getName());
                productListResponse.add(pr);
            }
        }
        return productListResponse;
    }

    public List<BatchListResponse> listProductsInBatch(Integer productId){
        List<Batch> batchList = batchService.findBatchesByProductId(productId);

        List<BatchListResponse> batchResponseList = convertBatchToBatchResponse(batchList);
        //ordenar lista

        return batchResponseList;
    }

    private List<BatchListResponse> convertBatchToBatchResponse(List<Batch> batchList){
        List<BatchListResponse> batchResponseList = new ArrayList<>();

        for (Batch b: batchList) {
            Product product = b.getProduct();
            BatchListResponse batchListResponse = BatchListResponse.builder()
                    .id(b.getId())
                    .product(new ProductListResponse(product.getId(), product.getName()))
                    .currentQuantity(b.getCurrentQuantity())
                    .dueDate(b.getDueDate())
                    .build();
            batchResponseList.add(batchListResponse);
        }
        return batchResponseList;
    }



}
