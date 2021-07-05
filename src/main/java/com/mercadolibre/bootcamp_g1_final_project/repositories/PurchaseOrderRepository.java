package com.mercadolibre.bootcamp_g1_final_project.repositories;

import com.mercadolibre.bootcamp_g1_final_project.entities.PurchaseOrder;
import org.springframework.data.repository.CrudRepository;

public interface PurchaseOrderRepository extends CrudRepository<PurchaseOrder, Integer> {
}
