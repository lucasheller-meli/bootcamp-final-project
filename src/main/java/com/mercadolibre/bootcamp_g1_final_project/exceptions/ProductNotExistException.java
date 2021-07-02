package com.mercadolibre.bootcamp_g1_final_project.exceptions;

public class ProductNotExistException extends RuntimeException{
    public ProductNotExistException(){
        super("Product does not exist.");
    }
}
