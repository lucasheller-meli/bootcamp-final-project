package com.mercadolibre.bootcamp_g1_final_project.exceptions;

public class NotFoundSectionInWarehouseException extends RuntimeException{

    public NotFoundSectionInWarehouseException(){
        super("Section not found in warehouse");
    }
}
