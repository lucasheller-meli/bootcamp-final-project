package com.mercadolibre.bootcamp_g1_final_project.exceptions;

public class SectionNotExistException extends RuntimeException{
    public SectionNotExistException(){
        super("Section does not exist.");
    }
}
