package com.mercadolibre.bootcamp_g1_final_project.controller;

import com.mercadolibre.bootcamp_g1_final_project.controller.request.InboundOrderUpdateRequest;
import com.mercadolibre.bootcamp_g1_final_project.controller.request.InboundOrderRequest;
import com.mercadolibre.bootcamp_g1_final_project.controller.response.BatchResponse;
import com.mercadolibre.bootcamp_g1_final_project.controller.response.InboundOrderResponse;
import com.mercadolibre.bootcamp_g1_final_project.exceptions.SectionInWarehouseNotFoundException;
import com.mercadolibre.bootcamp_g1_final_project.services.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/fresh-products")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/inboundorder")
    @ResponseStatus(HttpStatus.CREATED)
    public InboundOrderResponse inboundOrder(@Valid @RequestBody InboundOrderRequest inboundOrderRequest) throws SectionInWarehouseNotFoundException {
        return orderService.inboundOrder(inboundOrderRequest);
    }

    @PatchMapping("/inboundorder/{order-id}")
    @ResponseStatus(HttpStatus.CREATED)
    public List<BatchResponse> refreshInboundOrder(@PathVariable("order-id") Integer id, @RequestBody InboundOrderUpdateRequest request) {
        return orderService.updateInboundOrder(id,request);
    }
}
