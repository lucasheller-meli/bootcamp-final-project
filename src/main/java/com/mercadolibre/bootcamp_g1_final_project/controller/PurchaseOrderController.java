package com.mercadolibre.bootcamp_g1_final_project.controller;

import com.mercadolibre.bootcamp_g1_final_project.controller.request.PurchaseOrderRequest;
import com.mercadolibre.bootcamp_g1_final_project.controller.request.PurchaseOrderUpdateRequest;
import com.mercadolibre.bootcamp_g1_final_project.controller.response.PurchaseOrderResponse;
import com.mercadolibre.bootcamp_g1_final_project.controller.response.PurchaseProductResponse;
import com.mercadolibre.bootcamp_g1_final_project.services.PurchaseOrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/fresh-products/orders")
public class PurchaseOrderController {
  private final PurchaseOrderService purchaseOrderService;

  public PurchaseOrderController(PurchaseOrderService purchaseOrderService) {
    this.purchaseOrderService = purchaseOrderService;
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public ResponseEntity<PurchaseOrderResponse> purchaseOrder(@Valid @RequestBody PurchaseOrderRequest request) {
    return ResponseEntity.ok(purchaseOrderService.purchaseOrder(request));
  }

  @GetMapping( "/{id}")
  @ResponseStatus(HttpStatus.OK)
  public PurchaseOrderResponse findById(@PathVariable("id") Integer id) {
    return purchaseOrderService.findPurchasedOrder(id);
  }

  @PatchMapping("/{id}")
  @ResponseStatus(HttpStatus.CREATED)
  public PurchaseOrderResponse updatePurchasedOrder(@PathVariable("id") Integer id, @Valid @RequestBody PurchaseOrderUpdateRequest request) {
    return purchaseOrderService.updateOrder(id,request);
  }
 }
