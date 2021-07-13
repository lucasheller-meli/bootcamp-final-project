package com.mercadolibre.bootcamp_g1_final_project.exceptions;

public class PurchasedOrderNotFound extends RuntimeException {
    public PurchasedOrderNotFound(Integer id) {
        super(String.format("Did not found any order of the buyer of id %s.", id));
    }
}
