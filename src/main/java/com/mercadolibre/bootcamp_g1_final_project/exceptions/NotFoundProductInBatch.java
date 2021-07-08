package com.mercadolibre.bootcamp_g1_final_project.exceptions;

public class NotFoundProductInBatch extends RuntimeException{
    public NotFoundProductInBatch(){
        super("There's no linked product in batch");
    }
}
