package com.mercadolibre.bootcamp_g1_final_project.repositories;

import com.mercadolibre.bootcamp_g1_final_project.entities.InboundOrder;
import org.springframework.data.repository.CrudRepository;

public interface InboundOrderRepository extends CrudRepository<InboundOrder, Integer> {
}
