package com.mercadolibre.bootcamp_g1_final_project.services;

import com.mercadolibre.bootcamp_g1_final_project.controller.request.InboundOrderRequest;
import com.mercadolibre.bootcamp_g1_final_project.controller.response.BatchResponse;
import com.mercadolibre.bootcamp_g1_final_project.entities.Batch;

import java.util.List;

public interface OrderService {
    List<BatchResponse> inboundOrder(InboundOrderRequest inboundOrderRequest);
}
