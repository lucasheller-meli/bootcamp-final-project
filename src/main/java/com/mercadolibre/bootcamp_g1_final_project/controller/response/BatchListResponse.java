package com.mercadolibre.bootcamp_g1_final_project.controller.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BatchListResponse {

    private Integer id;
    private ProductListResponse product;
    private Integer currentQuantity;
    @JsonFormat(pattern = "YYYY-MM-dd HH:mm")
    private LocalDateTime dueDate;

}
