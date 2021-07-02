package com.mercadolibre.bootcamp_g1_final_project.controller.request;

import lombok.Data;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

@Data
public class LoginRequest {

    private String email;
    private String password;

    public UsernamePasswordAuthenticationToken of(LoginRequest loginRequest){
        return new UsernamePasswordAuthenticationToken(email,password);
    }
}
