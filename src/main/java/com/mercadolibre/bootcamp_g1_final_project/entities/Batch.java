package com.mercadolibre.bootcamp_g1_final_project.entities;

import com.mercadolibre.bootcamp_g1_final_project.exceptions.ApiException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.http.HttpStatus;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.time.LocalDate;
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

    @ManyToOne
    private Section sector;

    private Float currentTemperature;

    private Float minimumTemperature;

    private Integer initialQuantity;

    private Integer currentQuantity;

    private LocalDate dueDate;

    @CreationTimestamp
    private LocalDateTime manufacturingDate;

    public void reduceCurrentQuantityBy(Integer quantityToReduce) {
        if (currentQuantity < quantityToReduce) throw new ApiException(
            HttpStatus.BAD_REQUEST.name(),
            "Cannot reduce quantity of product with id " + id + " by " + quantityToReduce + " because it is bigger than current quantity",
            HttpStatus.BAD_REQUEST.value());

        this.currentQuantity -= quantityToReduce;
    }

    public void increaseCurrentQuantityBy(Integer quantityToIncrease) {
        this.currentQuantity += quantityToIncrease;
    }
}
