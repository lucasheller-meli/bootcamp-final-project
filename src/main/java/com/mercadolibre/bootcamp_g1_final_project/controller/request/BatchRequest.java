package com.mercadolibre.bootcamp_g1_final_project.controller.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BatchRequest {

    private Integer id;

    @NotNull(message = "Product id cant be null")
    private Integer productId;

    @NotNull(message = "current temperature cant be null")
    private Float currentTemperature;

    @NotNull(message = "minimum temperature cant be null")
    private Float minimumTemperature;

    @NotNull(message = "Quantity cant be null")
    private Integer quantity;

    @NotNull(message = "Due date cant be null")
    private LocalDate dueDate;
}
