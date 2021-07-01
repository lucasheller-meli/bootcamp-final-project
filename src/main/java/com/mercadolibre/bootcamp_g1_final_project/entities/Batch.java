package com.mercadolibre.bootcamp_g1_final_project.entities;

import java.time.LocalDateTime;

public class Batch {

    public String batchNumber;
    public Product product;
    public String currentTemperature;
    public String minimumTemperature;
    public String initialQuantity;
    public String currentQuantity;
    public LocalDateTime manufacturingDate;
    public LocalDateTime manufacturingTime;
    public LocalDateTime dueDate;

}
