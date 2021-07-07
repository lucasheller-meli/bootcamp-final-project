package com.mercadolibre.bootcamp_g1_final_project.entities.users;

import javax.persistence.Entity;
import java.util.Collection;
import java.util.List;

@Entity
public class Representative extends User {
    public Representative(String email, String password) {
        super(email, password);
    }

    public Representative(Integer id, String email, String password) {
        super(id, email, password);
    }

    public Representative() {

    }

    @Override
    public Collection<UserRole> getAuthorities() {
        return List.of(UserRole.REPRESENTATIVE);
    }
}
