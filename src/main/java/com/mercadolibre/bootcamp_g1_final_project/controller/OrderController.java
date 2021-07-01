package com.mercadolibre.bootcamp_g1_final_project.controller;

import com.mercadolibre.bootcamp_g1_final_project.controller.request.InboundOrderRequest;
import com.mercadolibre.bootcamp_g1_final_project.entities.Warehouse;
import com.mercadolibre.bootcamp_g1_final_project.services.OrderService;
import com.mercadolibre.bootcamp_g1_final_project.services.WarehouseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/fresh-products")
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/inboundorder")
    public ResponseEntity inboundOrder(@RequestBody InboundOrderRequest inboundOrderRequest){
        orderService.inboundOrder(inboundOrderRequest);
        return ResponseEntity.ok().build();
    }
}
