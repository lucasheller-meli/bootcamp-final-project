package com.mercadolibre.bootcamp_g1_final_project.controller.request;

import com.mercadolibre.bootcamp_g1_final_project.util.strategy.BuyerParameter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BuyerRequest {

    private BuyerParameter parameter;

}
