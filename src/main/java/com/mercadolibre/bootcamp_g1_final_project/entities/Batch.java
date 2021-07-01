package com.mercadolibre.bootcamp_g1_final_project.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.time.LocalDateTime;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Batch {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    private Product product;

    private Float currentTemperature;

    private Float minimumTemperature;

    private Integer initialQuantity;

    private Integer currentQuantity;

    private LocalDateTime dueDate;

    @CreationTimestamp
    private LocalDateTime manufacturingDate;

}
