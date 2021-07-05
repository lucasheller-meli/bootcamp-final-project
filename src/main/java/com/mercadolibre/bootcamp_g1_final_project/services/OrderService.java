package com.mercadolibre.bootcamp_g1_final_project.services;

import com.mercadolibre.bootcamp_g1_final_project.controller.request.InboundOrderRequest;
import com.mercadolibre.bootcamp_g1_final_project.controller.response.BatchResponse;

import java.util.List;

public interface OrderService {
    List<BatchResponse> inboundOrder(InboundOrderRequest inboundOrderRequest);
}
