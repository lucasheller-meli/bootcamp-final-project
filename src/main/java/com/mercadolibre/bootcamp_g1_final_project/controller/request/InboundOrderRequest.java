package com.mercadolibre.bootcamp_g1_final_project.controller.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class InboundOrderRequest {

    @NotNull(message = "Warehouse id cant be null")
    private Integer warehouseId;

    @NotNull(message = "Section id cant be null")
    private Integer sectionId;

    @Valid
    private List<BatchRequest> batches;
}
