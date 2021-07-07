package com.mercadolibre.bootcamp_g1_final_project.exceptions;

public class ListProductPerDuedateNotExistException extends RuntimeException{
    public ListProductPerDuedateNotExistException(){
        super("Product not found in this date range.");
    }
}
