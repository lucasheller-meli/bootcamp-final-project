package com.mercadolibre.bootcamp_g1_final_project.exceptions;

public class SectionInWarehouseNotFoundException extends RuntimeException{

    public SectionInWarehouseNotFoundException(){
        super("Section exists but was not found in the warehouse.");
    }
}
