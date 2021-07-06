package com.mercadolibre.bootcamp_g1_final_project.services.impl;

import com.mercadolibre.bootcamp_g1_final_project.controller.request.PurchaseOrderRequest;
import com.mercadolibre.bootcamp_g1_final_project.controller.request.PurchaseOrderUpdateRequest;
import com.mercadolibre.bootcamp_g1_final_project.controller.request.PurchaseProductRequest;
import com.mercadolibre.bootcamp_g1_final_project.controller.request.PurchaseProductUpdateRequest;
import com.mercadolibre.bootcamp_g1_final_project.controller.response.PurchaseOrderResponse;
import com.mercadolibre.bootcamp_g1_final_project.controller.response.PurchaseProductResponse;
import com.mercadolibre.bootcamp_g1_final_project.entities.Batch;
import com.mercadolibre.bootcamp_g1_final_project.entities.PurchaseOrder;
import com.mercadolibre.bootcamp_g1_final_project.entities.PurchaseProduct;
import com.mercadolibre.bootcamp_g1_final_project.entities.users.Buyer;
import com.mercadolibre.bootcamp_g1_final_project.exceptions.ApiException;
import com.mercadolibre.bootcamp_g1_final_project.exceptions.CartNotFoundException;
import com.mercadolibre.bootcamp_g1_final_project.repositories.PurchaseOrderRepository;
import com.mercadolibre.bootcamp_g1_final_project.repositories.PurchaseProductRepository;
import com.mercadolibre.bootcamp_g1_final_project.services.ProductService;
import com.mercadolibre.bootcamp_g1_final_project.services.PurchaseOrderService;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PurchaseOrderServiceImpl implements PurchaseOrderService {
  private final PurchaseOrderRepository purchaseOrderRepository;
  private final BatchServiceImpl batchService;
  private final ProductService productService;
  private final PurchaseProductRepository purchaseProductRepository;

  public PurchaseOrderServiceImpl(PurchaseOrderRepository purchaseOrderRepository, BatchServiceImpl batchService, ProductService productService, PurchaseProductRepository purchaseProductRepository) {
    this.purchaseOrderRepository = purchaseOrderRepository;
    this.batchService = batchService;
    this.productService = productService;
    this.purchaseProductRepository = purchaseProductRepository;
  }

  public PurchaseOrderResponse purchaseOrder(PurchaseOrderRequest request) {
    List<PurchaseProductRequest> products = request.getProducts();

    checkIfProductsHaveEnoughStock(products);

    updateBatchesProductQuantities(products);

    Buyer buyer = (Buyer) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    List<PurchaseProduct> purchaseProducts = mappedProducts(products);
    PurchaseOrder savedOrder = purchaseOrderRepository.save(PurchaseOrder.builder().buyer(buyer).products(purchaseProducts).build());

    return PurchaseOrderResponse.builder()
        .id(savedOrder.getId())
        .date(savedOrder.getOrderDate())
        .buyerId(buyer.getId())
        .products(responseProducts(purchaseProducts))
        .build();
  }

  @Override
  @Transactional(propagation= Propagation.REQUIRED)
  public PurchaseOrderResponse updateOrder(Integer id, PurchaseOrderUpdateRequest request) {
    PurchaseOrder purchasedOrder = purchaseOrderRepository.findById(id).orElseThrow(() -> new CartNotFoundException(id));
    refreshOrAddProducts(purchasedOrder,mappedUpdatedProducts(request.getProducts()));
    purchaseOrderRepository.save(purchasedOrder);
    return PurchaseOrderResponse.builder().id(purchasedOrder.getId()).products(responseProducts(purchasedOrder.getProducts())).buyerId(purchasedOrder.getId()).date(purchasedOrder.getOrderDate()).build();
  }

  @Override
  public PurchaseOrderResponse findPurchasedOrder(Integer id) {
    PurchaseOrder purchasedOrder = purchaseOrderRepository.findById(id).orElseThrow(() -> new CartNotFoundException(id));
    return PurchaseOrderResponse.builder()
            .id(purchasedOrder.getId())
            .date(purchasedOrder.getOrderDate())
            .buyerId(purchasedOrder.getBuyer().getId())
            .products(responseProducts(purchasedOrder.getProducts()))
            .build();
  }


  private void refreshOrAddProducts(PurchaseOrder purchasedOrder, List<PurchaseProduct> updatedProducts) {
    HashMap<Integer, PurchaseProduct> products = new HashMap<>();
    purchasedOrder.getProducts().forEach(p -> products.put(p.getId(), p));
    updatedProducts.forEach(p-> products.put(p.getId(), p));
    purchasedOrder.setProducts(new ArrayList<>(products.values()));
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

    for (Batch batch: batchService.findBatchesByProductId(product.getProductId())) {
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

  private List<PurchaseProduct> mappedUpdatedProducts(List<PurchaseProductUpdateRequest> products) {
    return products.stream()
            .map(product -> {
              PurchaseProduct purchaseProduct = purchaseProductRepository.findById(product.getProductId()).orElse(PurchaseProduct.builder().product(productService.findById(product.getProductId())).build());
              purchaseProduct.setQuantity(product.getQuantity());
              return purchaseProduct;
            }).collect(Collectors.toList());
  }

  private List<PurchaseProductResponse> responseProducts(List<PurchaseProduct> products) {
    return products.stream()
        .map(product -> PurchaseProductResponse.builder()
            .productId(product.getId())
            .quantity(product.getQuantity())
            .build())
        .collect(Collectors.toList());
  }
}
