package com.mercadolibre.bootcamp_g1_final_project.entities;

import java.time.LocalDateTime;
import java.util.List;

public class Order {

    public Integer id;
    public LocalDateTime orderDate;
    public Warehouse warehouse;
    public List<Batch> batch;

}
