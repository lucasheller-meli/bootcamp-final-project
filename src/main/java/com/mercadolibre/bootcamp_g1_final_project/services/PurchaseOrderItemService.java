package com.mercadolibre.bootcamp_g1_final_project.services;

import com.mercadolibre.bootcamp_g1_final_project.entities.PurchaseOrderItem;

import java.util.List;

public interface PurchaseOrderItemService {
  void deleteAll(List<PurchaseOrderItem> orderItems);
}
