package com.mercadolibre.bootcamp_g1_final_project.services;

import com.mercadolibre.bootcamp_g1_final_project.controller.request.PurchaseOrderRequest;
import com.mercadolibre.bootcamp_g1_final_project.controller.response.PurchaseOrderItemsResponse;
import com.mercadolibre.bootcamp_g1_final_project.controller.response.PurchaseOrderResponse;

public interface PurchaseOrderService {
  PurchaseOrderResponse purchaseOrder(PurchaseOrderRequest request);

  PurchaseOrderResponse updatePurchaseOrder(PurchaseOrderRequest request, Integer id);

  PurchaseOrderItemsResponse orderItems(Integer id);
}
