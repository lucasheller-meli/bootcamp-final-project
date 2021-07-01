package com.mercadolibre.bootcamp_g1_final_project.entities;

import java.time.LocalDateTime;

public class Batch {

    public Integer id;
    public Product product;
    public Float currentTemperature;
    public Float minimumTemperature;
    public Integer initialQuantity;
    public Integer currentQuantity;
    public LocalDateTime manufacturingDate;
    public LocalDateTime manufacturingTime;
    public LocalDateTime dueDate;

}
