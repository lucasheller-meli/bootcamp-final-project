package com.mercadolibre.bootcamp_g1_final_project.controller.response;
import com.mercadolibre.bootcamp_g1_final_project.entities.Product;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class BatchResponse {

    private Integer id;
    private Product product;
    private Float currentTemperature;
    private Float minimumTemperature;
    private Integer initialQuantity;
    private Integer currentQuantity;
    private LocalDateTime dueDate;
    private LocalDateTime manufacturingDate;
}
