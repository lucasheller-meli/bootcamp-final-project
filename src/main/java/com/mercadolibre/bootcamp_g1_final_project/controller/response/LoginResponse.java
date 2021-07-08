package com.mercadolibre.bootcamp_g1_final_project.controller.response;

public class LoginResponse {
    private String email;
    private String token;

    public LoginResponse(String email, String token) {
        this.email = email;
        this.token = token;
    }

    public LoginResponse() {
    }

    public String getEmail() {
        return email;
    }

    public String getToken() {
        return token;
    }
}
