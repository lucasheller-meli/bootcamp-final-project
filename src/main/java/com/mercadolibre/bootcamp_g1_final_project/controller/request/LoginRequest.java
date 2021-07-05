package com.mercadolibre.bootcamp_g1_final_project.controller.request;

import javax.validation.constraints.NotNull;

public class LoginRequest {
    @NotNull(message = "Email cant be null")
    private String email;
    @NotNull(message = "Password cant be null")
    private String password;

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}
