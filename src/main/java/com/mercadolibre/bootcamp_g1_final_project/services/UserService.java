package com.mercadolibre.bootcamp_g1_final_project.services;

import com.mercadolibre.bootcamp_g1_final_project.controller.request.LoginRequest;
import com.mercadolibre.bootcamp_g1_final_project.controller.response.LoginResponse;
import com.mercadolibre.bootcamp_g1_final_project.entities.users.User;

import java.util.Optional;

public interface UserService {
    LoginResponse login(LoginRequest request);

    User findById(Integer id);

    Optional<User> findByEmail(String email);
}
