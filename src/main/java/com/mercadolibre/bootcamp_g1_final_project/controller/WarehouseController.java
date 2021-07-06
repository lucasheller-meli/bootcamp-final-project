package com.mercadolibre.bootcamp_g1_final_project.controller;


import com.mercadolibre.bootcamp_g1_final_project.controller.response.StockWarehouse;
import com.mercadolibre.bootcamp_g1_final_project.services.WarehouseService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WarehouseController {

    private final WarehouseService warehouseService;


    public WarehouseController(WarehouseService warehouseService) {
        this.warehouseService = warehouseService;
    }


    @GetMapping("/warehouse/{productId}")
    public ResponseEntity<StockWarehouse> listWarehouseByProductId(@PathVariable Integer productId) {
        return ResponseEntity.ok(warehouseService.findWarehouseWithProduct(productId));
    }

}
