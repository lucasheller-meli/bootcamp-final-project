package com.mercadolibre.bootcamp_g1_final_project.exceptions;

public class WarehouseNotExistException extends RuntimeException{
    public WarehouseNotExistException(){
        super("Warehouse does not exist.");
    }
}
