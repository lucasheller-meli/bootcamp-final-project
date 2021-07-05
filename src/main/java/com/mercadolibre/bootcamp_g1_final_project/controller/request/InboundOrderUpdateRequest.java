package com.mercadolibre.bootcamp_g1_final_project.controller.request;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class InboundOrderUpdateRequest {

    private List<BatchRequest> batches;
}
