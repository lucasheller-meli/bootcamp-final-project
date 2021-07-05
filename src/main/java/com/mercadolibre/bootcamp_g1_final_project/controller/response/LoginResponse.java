package com.mercadolibre.bootcamp_g1_final_project.controller.response;

public class LoginResponse {
    private final String email;
    private final String token;

    public LoginResponse(String email, String token) {
        this.email = email;
        this.token = token;
    }

    public String getEmail() {
        return email;
    }

    public String getToken() {
        return token;
    }
}
