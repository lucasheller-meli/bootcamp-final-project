package com.mercadolibre.bootcamp_g1_final_project.exceptions;

public class InboundOrderNotFound extends RuntimeException {
    public InboundOrderNotFound(Integer id) {
        super(String.format("Inbound Order of id %s not found.",id));
    }
}
