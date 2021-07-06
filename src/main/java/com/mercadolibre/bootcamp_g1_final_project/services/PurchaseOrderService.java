package com.mercadolibre.bootcamp_g1_final_project.services;

import com.mercadolibre.bootcamp_g1_final_project.controller.request.PurchaseOrderRequest;
import com.mercadolibre.bootcamp_g1_final_project.controller.request.PurchaseOrderUpdateRequest;
import com.mercadolibre.bootcamp_g1_final_project.controller.response.PurchaseOrderResponse;

public interface PurchaseOrderService {
  PurchaseOrderResponse purchaseOrder(PurchaseOrderRequest request);

  PurchaseOrderResponse updateOrder(Integer id, PurchaseOrderUpdateRequest request);

  PurchaseOrderResponse findPurchasedOrder(Integer id);

}
