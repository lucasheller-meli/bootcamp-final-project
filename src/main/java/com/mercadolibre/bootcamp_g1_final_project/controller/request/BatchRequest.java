package com.mercadolibre.bootcamp_g1_final_project.controller.request;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
public class BatchRequest {

    @NotNull(message = "Product id cant be null")
    private Integer productId;

    @NotNull(message = "current temperature cant be null")
    private Float currentTemperature;

    @NotNull(message = "minimum temperature cant be null")
    private Float minimumTemperature;

    @NotNull(message = "Quantity cant be null")
    private Integer quantity;

    @NotNull(message = "Due date cant be null")
    private LocalDateTime dueDate;

}
