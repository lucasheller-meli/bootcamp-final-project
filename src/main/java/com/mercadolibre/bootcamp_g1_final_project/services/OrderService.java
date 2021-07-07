package com.mercadolibre.bootcamp_g1_final_project.services;

import com.mercadolibre.bootcamp_g1_final_project.controller.request.InboundOrderUpdateRequest;
import com.mercadolibre.bootcamp_g1_final_project.controller.request.InboundOrderRequest;
import com.mercadolibre.bootcamp_g1_final_project.controller.response.BatchResponse;
import com.mercadolibre.bootcamp_g1_final_project.controller.response.InboundOrderResponse;

import java.util.List;

public interface OrderService {
    InboundOrderResponse inboundOrder(InboundOrderRequest inboundOrderRequest);

    List<BatchResponse> updateInboundOrder(Integer id, InboundOrderUpdateRequest inboundOrderUpdateRequest);
}
