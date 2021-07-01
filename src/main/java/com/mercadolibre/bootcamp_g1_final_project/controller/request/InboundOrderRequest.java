package com.mercadolibre.bootcamp_g1_final_project.controller.request;

import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class InboundOrderRequest {

    @NotNull(message = "Warehouse id cant be null")
    private Integer warehouseId;

    @NotNull(message = "Section id cant be null")
    private Integer sectionId;

    @Valid
    private List<BatchRequest> batches;
}
