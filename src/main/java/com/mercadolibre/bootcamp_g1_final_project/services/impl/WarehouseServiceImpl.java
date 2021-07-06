package com.mercadolibre.bootcamp_g1_final_project.services.impl;

import com.mercadolibre.bootcamp_g1_final_project.entities.InboundOrder;
import com.mercadolibre.bootcamp_g1_final_project.entities.Section;
import com.mercadolibre.bootcamp_g1_final_project.entities.Warehouse;
import com.mercadolibre.bootcamp_g1_final_project.entities.users.Representative;
import com.mercadolibre.bootcamp_g1_final_project.exceptions.NotFoundProductInBatch;
import com.mercadolibre.bootcamp_g1_final_project.exceptions.WarehouseNotExistException;
import com.mercadolibre.bootcamp_g1_final_project.repositories.WarehouseRepository;
import com.mercadolibre.bootcamp_g1_final_project.services.WarehouseService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WarehouseServiceImpl implements WarehouseService {
    private final WarehouseRepository warehouseRepository;

    public WarehouseServiceImpl(WarehouseRepository warehouseRepository) throws WarehouseNotExistException {
        this.warehouseRepository = warehouseRepository;
    }

    public Warehouse findById(Integer id) {
        return warehouseRepository.findById(id).orElseThrow(WarehouseNotExistException::new);
    }

    @Override
    public Warehouse findByRepresentative(Representative representative){
        return warehouseRepository.findWarehousesByRepresentatives(representative);
    }


    @Override
    public void updateOrders(Warehouse warehouse, InboundOrder inboundOrder) {
        final List<InboundOrder> orders = warehouse.getOrders();
        orders.add(inboundOrder);
        warehouse.setOrders(orders);
        warehouseRepository.save(warehouse);
    }


}
