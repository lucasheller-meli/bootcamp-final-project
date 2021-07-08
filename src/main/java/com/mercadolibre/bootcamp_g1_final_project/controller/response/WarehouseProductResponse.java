package com.mercadolibre.bootcamp_g1_final_project.controller.response;

import lombok.Builder;
import lombok.Data;


public interface WarehouseProductResponse {
    String getWarehouseId();
    Integer getCount();
}
