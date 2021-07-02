package com.mercadolibre.bootcamp_g1_final_project.entities.users;

import javax.persistence.Entity;
import java.util.Collection;
import java.util.List;

@Entity
public class Seller extends User {
    public Seller(String email, String password) {
        super(email, password);
    }

    public Seller() {
    }

    @Override
    public Collection<UserRole> getAuthorities() {
        return List.of(UserRole.SELLER);
    }
}
