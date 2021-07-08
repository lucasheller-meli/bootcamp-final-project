package com.mercadolibre.bootcamp_g1_final_project.controller.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BatchResponse {

    private Integer id;
    private ProductResponse product;
    private Float currentTemperature;
    private Float minimumTemperature;
    private Integer initialQuantity;
    private Integer currentQuantity;
    @JsonFormat(pattern = "YYYY-MM-dd HH:mm")
    private LocalDateTime dueDate;
    @JsonFormat(pattern = "YYYY-MM-dd HH:mm")
    private LocalDateTime manufacturingDate;
}
