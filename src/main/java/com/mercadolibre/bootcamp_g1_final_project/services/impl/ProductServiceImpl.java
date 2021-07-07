package com.mercadolibre.bootcamp_g1_final_project.services.impl;

import com.mercadolibre.bootcamp_g1_final_project.controller.response.BatchListResponse;
import com.mercadolibre.bootcamp_g1_final_project.controller.response.ProductListResponse;
import com.mercadolibre.bootcamp_g1_final_project.controller.response.ProductResponse;
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

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

        Representative representative = (Representative) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Warehouse warehouse = warehouseService.findByRepresentative(representative);
        List<BatchListResponse> batchResponseList = convertBatchToBatchResponse(batchList, warehouse);

        if(order!= null) {
            if(order.equals("C")) {
                batchResponseList.sort(BatchListResponse.quantityCompare);
            } else if(order.equals("F")) {
                batchResponseList.sort(BatchListResponse.duedateCompare);
            }
        }
        return batchResponseList;
    }

    private List<BatchListResponse> convertBatchToBatchResponse(List<Batch> batchList, Warehouse warehouse){
        List<BatchListResponse> batchResponseList = new ArrayList<>();

        for (Batch b: batchList) {
            Product product = b.getProduct();
            Section section = b.getSector();
            BatchListResponse batchListResponse = BatchListResponse.builder()
                    .sectionId(section.getId())
                    .warehouseId(warehouse.getId())
                    .batchNumber(b.getId())
                    .product(new ProductResponse(product.getId(), product.getName() , product.getType()))
                    .currentQuantity(b.getCurrentQuantity())
                    .dueDate(b.getDueDate())
                    .build();
            batchResponseList.add(batchListResponse);
        }
        return batchResponseList;
    }

    public List<BatchListResponse> listProductPerDuedata(Integer days, String category){

        Representative representative = (Representative) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Warehouse warehouse = warehouseService.findByRepresentative(representative);
        List<Section> sectionList = warehouse.getSection();

        List<Batch> batchList = batchService.findBatchesBySectorIn(sectionList);

        List<BatchListResponse> batchListResponseList = convertBatchToBatchResponse(batchList, warehouse).stream().filter(b -> b.getDueDate().isAfter(LocalDate.now().minusWeeks(days))).collect(Collectors.toList());

        //excecao de lista vazia

        if(category!=null){
            List<BatchListResponse> batchListResponseListFilter = new ArrayList<>();

            for (BatchListResponse b: batchListResponseList) {
                ProductResponse p = b.getProduct();
                ProductType type = p.getType();

                if (type.toString().compareTo(category) == 0) {
                   batchListResponseListFilter.add(b);
                }
            }
            return batchListResponseListFilter;
        }

        return batchListResponseList;
    }

}
