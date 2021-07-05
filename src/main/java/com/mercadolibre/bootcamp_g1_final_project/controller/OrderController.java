package com.mercadolibre.bootcamp_g1_final_project.controller;

import com.mercadolibre.bootcamp_g1_final_project.controller.request.InboundOrderUpdateRequest;
import com.mercadolibre.bootcamp_g1_final_project.controller.request.InboundOrderRequest;
import com.mercadolibre.bootcamp_g1_final_project.controller.response.BatchResponse;
import com.mercadolibre.bootcamp_g1_final_project.exceptions.SectionInWarehouseNotFoundException;
import com.mercadolibre.bootcamp_g1_final_project.entities.Warehouse;
import com.mercadolibre.bootcamp_g1_final_project.entities.users.UserRole;
import com.mercadolibre.bootcamp_g1_final_project.services.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
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

    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<List<BatchResponse>> inboundOrder(@Valid @RequestBody InboundOrderRequest inboundOrderRequest) throws SectionInWarehouseNotFoundException {
        return ResponseEntity.ok(orderService.inboundOrder(inboundOrderRequest));
    }

    @PatchMapping("/inboundorder/{order-id}")
    @ResponseStatus(HttpStatus.CREATED)
    public List<BatchResponse> refreshInboundOrder(@PathVariable("order-id") Integer id, @RequestBody InboundOrderUpdateRequest request) {
        return orderService.updateInboundOrder(id,request);
    }
}
