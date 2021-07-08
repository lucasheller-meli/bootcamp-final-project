package com.mercadolibre.bootcamp_g1_final_project.exceptions;

public class CategoryPerDuedateNotFoundException extends RuntimeException{
    public CategoryPerDuedateNotFoundException(){
        super("Category not found in this date range.");
    }
}
