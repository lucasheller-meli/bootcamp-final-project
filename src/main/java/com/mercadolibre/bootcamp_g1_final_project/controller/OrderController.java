package com.mercadolibre.bootcamp_g1_final_project.controller;

import com.mercadolibre.bootcamp_g1_final_project.controller.request.InboundOrderRequest;
import com.mercadolibre.bootcamp_g1_final_project.controller.response.BatchResponse;
import com.mercadolibre.bootcamp_g1_final_project.exceptions.NotFoundSectionInWarehouseException;
import com.mercadolibre.bootcamp_g1_final_project.services.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/fresh-products")
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/inboundorder")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<List<BatchResponse>> inboundOrder(@RequestBody InboundOrderRequest inboundOrderRequest) throws NotFoundSectionInWarehouseException {
        return ResponseEntity.ok(orderService.inboundOrder(inboundOrderRequest));
    }
}
