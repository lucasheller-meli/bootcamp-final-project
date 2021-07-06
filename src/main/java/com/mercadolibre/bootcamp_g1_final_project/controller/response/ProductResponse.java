package com.mercadolibre.bootcamp_g1_final_project.controller.response;

import com.mercadolibre.bootcamp_g1_final_project.entities.ProductType;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProductResponse {

    private Integer productId;

    private String name;

    private ProductType type;

}
