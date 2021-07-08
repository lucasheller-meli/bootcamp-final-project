package com.mercadolibre.bootcamp_g1_final_project.controller.response;

import com.mercadolibre.bootcamp_g1_final_project.entities.ProductType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductResponse {

    private Integer productId;

    private String name;

    private ProductType type;

}
