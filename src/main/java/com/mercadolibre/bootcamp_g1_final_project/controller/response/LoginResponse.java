package com.mercadolibre.bootcamp_g1_final_project.controller.response;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponse {
    private String email;
    private String token;

    public String getEmail() {
        return email;
    }

    public String getToken() {
        return token;
    }
}
