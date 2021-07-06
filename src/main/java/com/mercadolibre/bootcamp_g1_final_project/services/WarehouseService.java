package com.mercadolibre.bootcamp_g1_final_project.services;

import com.mercadolibre.bootcamp_g1_final_project.entities.InboundOrder;
import com.mercadolibre.bootcamp_g1_final_project.entities.Warehouse;

public interface WarehouseService {
   Warehouse findById(Integer id);

   void updateOrders(Warehouse warehouse, InboundOrder inboundOrder);
}
