package com.mercadolibre.bootcamp_g1_final_project.entities;

public enum ProductType {
    FS("Fresh"),
    FF("Frozen"),
    RF("Refrigerated");

    private final String name;

    ProductType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public int compareTo(String category) { return name.compareTo(category); }
}
