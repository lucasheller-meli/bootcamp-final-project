package com.mercadolibre.bootcamp_g1_final_project.services;

import com.mercadolibre.bootcamp_g1_final_project.entities.User;

import java.util.Optional;

public interface UserService {
    User findById(Integer id);

    Optional<User> findByEmail(String email);
}
