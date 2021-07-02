package com.mercadolibre.bootcamp_g1_final_project.exceptions;

public class NotFoundSectionException extends RuntimeException{
    public NotFoundSectionException(){
        super("Section does not exist");
    }
}
