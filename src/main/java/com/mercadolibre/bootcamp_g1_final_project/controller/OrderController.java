package com.mercadolibre.bootcamp_g1_final_project.controller;

import com.mercadolibre.bootcamp_g1_final_project.controller.request.InboundOrderRequest;
import com.mercadolibre.bootcamp_g1_final_project.controller.response.BatchResponse;
import com.mercadolibre.bootcamp_g1_final_project.entities.Warehouse;
import com.mercadolibre.bootcamp_g1_final_project.entities.users.UserRole;
import com.mercadolibre.bootcamp_g1_final_project.services.OrderService;
import com.mercadolibre.bootcamp_g1_final_project.services.WarehouseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/fresh-products")
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @RolesAllowed(UserRole.Name.REPRESENTATIVE)
    @PostMapping("/inboundorder")
    public ResponseEntity<List<BatchResponse>> inboundOrder(@RequestBody InboundOrderRequest inboundOrderRequest){
        return ResponseEntity.ok(orderService.inboundOrder(inboundOrderRequest));
    }
}
