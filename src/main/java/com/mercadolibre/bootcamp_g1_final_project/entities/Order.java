package com.mercadolibre.bootcamp_g1_final_project.entities;

import java.util.List;

public class Order {

    public String id;
    public String orderDate;
    public Warehouse warehouse;
    public List<Batch> batch;

}
