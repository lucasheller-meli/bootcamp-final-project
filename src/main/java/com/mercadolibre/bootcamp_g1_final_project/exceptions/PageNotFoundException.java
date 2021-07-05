package com.mercadolibre.bootcamp_g1_final_project.exceptions;

public class PageNotFoundException extends RuntimeException {
    public PageNotFoundException(){

        super("404 Page not found");
    }
}
