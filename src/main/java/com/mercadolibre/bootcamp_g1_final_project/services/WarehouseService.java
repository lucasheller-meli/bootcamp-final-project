package com.mercadolibre.bootcamp_g1_final_project.services;

import com.mercadolibre.bootcamp_g1_final_project.controller.response.StockWarehouse;
import com.mercadolibre.bootcamp_g1_final_project.entities.InboundOrder;
import com.mercadolibre.bootcamp_g1_final_project.entities.Section;
import com.mercadolibre.bootcamp_g1_final_project.entities.Warehouse;
import com.mercadolibre.bootcamp_g1_final_project.entities.users.Representative;

import java.util.List;

public interface WarehouseService {
   Warehouse findById(Integer id);
   Warehouse findByRepresentative(Representative representative);

   void updateOrders(Warehouse warehouse, InboundOrder inboundOrder);

   StockWarehouse findWarehouseWithProduct(Integer productId);
}
