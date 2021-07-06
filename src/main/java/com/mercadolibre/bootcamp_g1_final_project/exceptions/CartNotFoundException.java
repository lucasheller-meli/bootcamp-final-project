package com.mercadolibre.bootcamp_g1_final_project.exceptions;

public class CartNotFoundException extends RuntimeException {
    public CartNotFoundException(Integer id) {
        super(String.format("Cart of id: %s doesn't exists.",id));
    }
}
