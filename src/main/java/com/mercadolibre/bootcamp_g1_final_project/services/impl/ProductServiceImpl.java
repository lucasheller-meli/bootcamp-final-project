package com.mercadolibre.bootcamp_g1_final_project.services.impl;

import com.mercadolibre.bootcamp_g1_final_project.controller.response.BatchListResponse;
import com.mercadolibre.bootcamp_g1_final_project.controller.response.ProductListResponse;
import com.mercadolibre.bootcamp_g1_final_project.entities.*;
import com.mercadolibre.bootcamp_g1_final_project.entities.users.Representative;
import com.mercadolibre.bootcamp_g1_final_project.exceptions.NotFoundProductInBatch;
import com.mercadolibre.bootcamp_g1_final_project.exceptions.ProductNotExistException;
import com.mercadolibre.bootcamp_g1_final_project.repositories.ProductRepository;
import com.mercadolibre.bootcamp_g1_final_project.services.BatchService;
import com.mercadolibre.bootcamp_g1_final_project.services.ProductService;
import com.mercadolibre.bootcamp_g1_final_project.services.WarehouseService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final BatchService batchService;
    private final WarehouseService warehouseService;

    public ProductServiceImpl(ProductRepository productRepository, BatchService batchService, WarehouseService warehouseService) {
        this.productRepository = productRepository;
        this.batchService = batchService;
        this.warehouseService = warehouseService;
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

    public List<BatchListResponse> listProductsInBatch(Integer productId, String order) throws NotFoundProductInBatch {
        List<Batch> batchList = batchService.findBatchesByProductId(productId);

        if(batchList.isEmpty()) throw new NotFoundProductInBatch();

        List<BatchListResponse> batchResponseList = convertBatchToBatchResponse(batchList);

        if(order!= null) {
            if(order.equals("C")) {
                batchResponseList.sort(BatchListResponse.quantityCompare);
            } else if(order.equals("F")) {
                batchResponseList.sort(BatchListResponse.duedateCompare);
            }
        }
        return batchResponseList;
    }

    private List<BatchListResponse> convertBatchToBatchResponse(List<Batch> batchList){
        List<BatchListResponse> batchResponseList = new ArrayList<>();

        Representative representative = (Representative) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Warehouse warehouse = warehouseService.findByRepresentative(representative);

        for (Batch b: batchList) {
            Product product = b.getProduct();
            Section section = b.getSector();
            BatchListResponse batchListResponse = BatchListResponse.builder()
                    .sectionId(section.getId())
                    .warehouseId(warehouse.getId())
                    .batchNumber(b.getId())
                    .product(new ProductListResponse(product.getId(), product.getName()))
                    .currentQuantity(b.getCurrentQuantity())
                    .dueDate(b.getDueDate())
                    .build();
            batchResponseList.add(batchListResponse);
        }
        return batchResponseList;
    }

}
