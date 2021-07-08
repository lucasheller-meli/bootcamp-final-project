package com.mercadolibre.bootcamp_g1_final_project.services.impl;

import com.mercadolibre.bootcamp_g1_final_project.entities.PurchaseOrderItem;
import com.mercadolibre.bootcamp_g1_final_project.repositories.PurchaseOrderItemRepository;
import com.mercadolibre.bootcamp_g1_final_project.services.PurchaseOrderItemService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PurchaseOrderItemServiceImpl implements PurchaseOrderItemService {
  private final PurchaseOrderItemRepository repository;

  public PurchaseOrderItemServiceImpl(PurchaseOrderItemRepository repository) {
    this.repository = repository;
  }

  public void deleteAll(List<PurchaseOrderItem> orderItems) {
    repository.deleteAll(orderItems);
  }
}
