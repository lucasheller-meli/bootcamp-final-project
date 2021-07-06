package com.mercadolibre.bootcamp_g1_final_project.controller.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StockWarehouse {

    private Integer productId;

    private List<WarehouseProductResponse> warehouseProducts;

}
