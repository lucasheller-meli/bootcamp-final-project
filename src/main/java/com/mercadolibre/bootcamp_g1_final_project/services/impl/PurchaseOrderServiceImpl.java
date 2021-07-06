package com.mercadolibre.bootcamp_g1_final_project.services.impl;

import com.mercadolibre.bootcamp_g1_final_project.controller.request.PurchaseOrderRequest;
import com.mercadolibre.bootcamp_g1_final_project.controller.request.PurchaseProductRequest;
import com.mercadolibre.bootcamp_g1_final_project.controller.response.PurchaseOrderResponse;
import com.mercadolibre.bootcamp_g1_final_project.controller.response.PurchaseProductResponse;
import com.mercadolibre.bootcamp_g1_final_project.entities.Batch;
import com.mercadolibre.bootcamp_g1_final_project.entities.PurchaseOrder;
import com.mercadolibre.bootcamp_g1_final_project.entities.PurchaseProduct;
import com.mercadolibre.bootcamp_g1_final_project.entities.users.Buyer;
import com.mercadolibre.bootcamp_g1_final_project.exceptions.ApiException;
import com.mercadolibre.bootcamp_g1_final_project.repositories.PurchaseOrderRepository;
import com.mercadolibre.bootcamp_g1_final_project.services.ProductService;
import com.mercadolibre.bootcamp_g1_final_project.services.PurchaseOrderService;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PurchaseOrderServiceImpl implements PurchaseOrderService {
    private final PurchaseOrderRepository purchaseOrderRepository;
    private final BatchServiceImpl batchService;
    private final ProductService productService;

    public PurchaseOrderServiceImpl(PurchaseOrderRepository purchaseOrderRepository, BatchServiceImpl batchService, ProductService productService) {
        this.purchaseOrderRepository = purchaseOrderRepository;
        this.batchService = batchService;
        this.productService = productService;
    }

    public PurchaseOrderResponse purchaseOrder(PurchaseOrderRequest request) {
        List<PurchaseProductRequest> products = request.getProducts();

        checkIfProductsHaveEnoughStock(products);

        updateBatchesProductQuantities(products);

        Buyer buyer = (Buyer) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        PurchaseOrder savedOrder = purchaseOrderRepository.save(PurchaseOrder.builder().buyer(buyer).products(mappedProducts(products)).build());
        List<PurchaseProduct> purchasedProducts = savedOrder.getProducts();

        return PurchaseOrderResponse.builder()
                .id(savedOrder.getId())
                .date(savedOrder.getOrderDate())
                .buyerId(buyer.getId())
                .products(responseProducts(purchasedProducts))
                .totalPrice(totalPrice(purchasedProducts))
                .build();
    }

    private void checkIfProductsHaveEnoughStock(List<PurchaseProductRequest> products) {
        products.forEach(product -> checkIfProductHasEnoughStock(product.getProductId(), product.getQuantity()));
    }

    private void updateBatchesProductQuantities(List<PurchaseProductRequest> products) {
        products.forEach(this::updateBatchesProductQuantity);
    }

    private void checkIfProductHasEnoughStock(Integer productId, Integer quantity) {
        boolean hasStock = batchService.findBatchesByProductId(productId).stream().mapToInt(Batch::getCurrentQuantity).sum() > quantity;

        if (!hasStock) throw new ApiException(
                HttpStatus.BAD_REQUEST.name(),
                "Product with id " + productId + " does not have " + quantity + " units available",
                HttpStatus.BAD_REQUEST.value()
        );
    }

    private void updateBatchesProductQuantity(PurchaseProductRequest product) {
        Integer quantityToRemove = product.getQuantity();

        for (Batch batch : batchService.findBatchesByProductId(product.getProductId())) {
            if (quantityToRemove == 0) return;

            Integer batchProductQuantity = batch.getCurrentQuantity();

            if (batchProductQuantity >= quantityToRemove) {
                batch.setCurrentQuantity(batchProductQuantity - quantityToRemove);
                batchService.save(batch);
                return;
            }

            quantityToRemove -= batchProductQuantity;
            batch.setCurrentQuantity(0);
            batchService.save(batch);
        }
    }

    private List<PurchaseProduct> mappedProducts(List<PurchaseProductRequest> products) {
        return products.stream()
                .map(product -> PurchaseProduct.builder()
                        .product(productService.findById(product.getProductId()))
                        .quantity(product.getQuantity())
                        .build())
                .collect(Collectors.toList());
    }

    private List<PurchaseProductResponse> responseProducts(List<PurchaseProduct> products) {
        return products.stream()
                .map(product -> PurchaseProductResponse.builder()
                        .productId(product.getId())
                        .quantity(product.getQuantity())
                        .build())
                .collect(Collectors.toList());
    }

    private Double totalPrice(List<PurchaseProduct> products) {
        return products.stream().mapToDouble(product -> product.getProduct().getPrice() * product.getQuantity()).sum();
    }
}
