package com.mercadolibre.bootcamp_g1_final_project.services.impl;

import com.mercadolibre.bootcamp_g1_final_project.entities.Warehouse;
import com.mercadolibre.bootcamp_g1_final_project.exceptions.WarehouseNotExistException;
import com.mercadolibre.bootcamp_g1_final_project.repositories.WarehouseRepository;
import com.mercadolibre.bootcamp_g1_final_project.services.WarehouseService;
import org.springframework.stereotype.Service;

@Service
public class WarehouseServiceImpl implements WarehouseService {
    private final WarehouseRepository warehouseRepository;

    public WarehouseServiceImpl(WarehouseRepository warehouseRepository) throws WarehouseNotExistException {
        this.warehouseRepository = warehouseRepository;
    }

    public Warehouse findById(Integer id) {
        return warehouseRepository.findById(id).orElseThrow(WarehouseNotExistException::new);
    }
}
