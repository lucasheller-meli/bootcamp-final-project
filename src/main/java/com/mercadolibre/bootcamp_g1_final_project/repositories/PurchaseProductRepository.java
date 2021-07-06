package com.mercadolibre.bootcamp_g1_final_project.repositories;

import com.mercadolibre.bootcamp_g1_final_project.entities.PurchaseProduct;
import org.springframework.data.repository.CrudRepository;

public interface PurchaseProductRepository extends CrudRepository<PurchaseProduct, Integer> {
}
