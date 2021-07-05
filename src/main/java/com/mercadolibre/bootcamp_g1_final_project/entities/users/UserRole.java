package com.mercadolibre.bootcamp_g1_final_project.entities.users;

import org.springframework.security.core.GrantedAuthority;

public enum UserRole implements GrantedAuthority {
    BUYER(Name.BUYER), SELLER(Name.SELLER), REPRESENTATIVE(Name.REPRESENTATIVE);

    public static class Name {
        public static final String BUYER = "BUYER";
        public static final String SELLER = "SELLER";
        public static final String REPRESENTATIVE = "REPRESENTATIVE";
    }

    private final String authority;

    UserRole(String authority) {
        this.authority = authority;
    }

    @Override
    public String getAuthority() {
        return authority;
    }
}
