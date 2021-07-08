package com.mercadolibre.bootcamp_g1_final_project.controller.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequest {
    @NotNull(message = "Email cant be null")
    private String email;
    @NotNull(message = "Password cant be null")
    private String password;

}
