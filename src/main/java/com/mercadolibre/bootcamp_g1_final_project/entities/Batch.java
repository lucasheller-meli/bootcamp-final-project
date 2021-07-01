package com.mercadolibre.bootcamp_g1_final_project.entities;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.time.LocalDateTime;

@Data
@Entity
public class Batch {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer id;

    @ManyToOne
    public Product product;

    public Float currentTemperature;

    public Float minimumTemperature;

    public Integer initialQuantity;

    public Integer currentQuantity;

    @CreationTimestamp
    public LocalDateTime manufacturingDate;

    public LocalDateTime dueDate;

}
