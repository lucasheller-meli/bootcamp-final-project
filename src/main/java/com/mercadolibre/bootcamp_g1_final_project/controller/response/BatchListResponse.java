package com.mercadolibre.bootcamp_g1_final_project.controller.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.mercadolibre.bootcamp_g1_final_project.entities.Section;
import com.mercadolibre.bootcamp_g1_final_project.entities.Warehouse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Comparator;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BatchListResponse{

    private Integer warehouseId;
    private Integer sectionId;
    private Integer batchNumber;
    private ProductListResponse product;
    private Integer currentQuantity;
    @JsonFormat(pattern = "YYYY-MM-dd HH:mm")
    private LocalDateTime dueDate;


    public static Comparator<BatchListResponse> quantityCompare = new Comparator<BatchListResponse>() {
        @Override
        public int compare(BatchListResponse b1, BatchListResponse b2) {
            Integer quantityB1 = b1.currentQuantity;
            Integer quantityB2 = b2.currentQuantity;

            return quantityB1.compareTo(quantityB2);
        }
    };

    public static Comparator<BatchListResponse> duedateCompare = new Comparator<BatchListResponse>() {
        @Override
        public int compare(BatchListResponse b1, BatchListResponse b2) {
            LocalDateTime duedateB1 = b1.dueDate;
            LocalDateTime duedateB2 = b2.dueDate;

            return duedateB1.compareTo(duedateB2);
        }
    };

}
