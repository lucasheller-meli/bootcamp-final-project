package com.mercadolibre.bootcamp_g1_final_project.controller;

import com.mercadolibre.bootcamp_g1_final_project.controller.request.PurchaseOrderRequest;
import com.mercadolibre.bootcamp_g1_final_project.controller.response.PurchaseOrderItemsResponse;
import com.mercadolibre.bootcamp_g1_final_project.controller.response.PurchaseOrderResponse;
import com.mercadolibre.bootcamp_g1_final_project.services.PurchaseOrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/fresh-products/orders")
public class PurchaseOrderController {
  private final PurchaseOrderService purchaseOrderService;

  public PurchaseOrderController(PurchaseOrderService purchaseOrderService) {
    this.purchaseOrderService = purchaseOrderService;
  }

  @GetMapping("/{id}")
  public ResponseEntity<PurchaseOrderItemsResponse> orderItems(@PathVariable Integer id) {
    return ResponseEntity.ok(purchaseOrderService.orderItems(id));
  }

  @PutMapping("/{id}")
  public ResponseEntity<PurchaseOrderResponse> updatePurchaseOrder(@Valid @RequestBody PurchaseOrderRequest request, @PathVariable Integer id) {
    return ResponseEntity.ok(purchaseOrderService.updatePurchaseOrder(request, id));
  }

  @PostMapping
  public ResponseEntity<PurchaseOrderResponse> purchaseOrder(@Valid @RequestBody PurchaseOrderRequest request) {
    return ResponseEntity.status(HttpStatus.CREATED).body(purchaseOrderService.purchaseOrder(request));
  }
}
