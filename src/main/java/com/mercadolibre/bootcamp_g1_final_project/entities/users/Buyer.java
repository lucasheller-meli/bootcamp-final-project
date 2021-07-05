package com.mercadolibre.bootcamp_g1_final_project.entities.users;

import javax.persistence.Entity;
import java.util.Collection;
import java.util.List;

@Entity
public class Buyer extends User {
    public Buyer(String email, String password) {
        super(email, password);
    }

    public Buyer() {
    }

    @Override
    public Collection<UserRole> getAuthorities() {
        return List.of(UserRole.BUYER);
    }
}
