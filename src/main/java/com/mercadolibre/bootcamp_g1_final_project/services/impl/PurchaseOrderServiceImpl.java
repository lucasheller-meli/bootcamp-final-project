package com.mercadolibre.bootcamp_g1_final_project.services.impl;

import com.mercadolibre.bootcamp_g1_final_project.controller.request.PurchaseOrderRequest;
import com.mercadolibre.bootcamp_g1_final_project.controller.request.PurchaseProductRequest;
import com.mercadolibre.bootcamp_g1_final_project.controller.response.PurchaseOrderResponse;
import com.mercadolibre.bootcamp_g1_final_project.entities.Batch;
import com.mercadolibre.bootcamp_g1_final_project.entities.users.Buyer;
import com.mercadolibre.bootcamp_g1_final_project.repositories.PurchaseOrderRepository;
import com.mercadolibre.bootcamp_g1_final_project.services.PurchaseOrderService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PurchaseOrderServiceImpl implements PurchaseOrderService {
  private final PurchaseOrderRepository purchaseOrderRepository;
  private final BatchServiceImpl batchService;

  public PurchaseOrderServiceImpl(PurchaseOrderRepository purchaseOrderRepository, BatchServiceImpl batchService) {
    this.purchaseOrderRepository = purchaseOrderRepository;
    this.batchService = batchService;
  }

  public PurchaseOrderResponse purchaseOrder(PurchaseOrderRequest request) {
    Buyer buyer = (Buyer) SecurityContextHolder.getContext().getAuthentication().getPrincipal();



    List<Batch> batches = batchService.findBatchesByProductId();



    return null;
  }

  private boolean productHasEnoughStock(Integer productId, Integer quantity) {
    return batchService.findBatchesByProductId(productId).stream().mapToInt(Batch::getCurrentQuantity).sum()
  }


}
