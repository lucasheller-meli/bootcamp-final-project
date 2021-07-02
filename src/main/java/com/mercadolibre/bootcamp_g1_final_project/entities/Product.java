package com.mercadolibre.bootcamp_g1_final_project.entities;

import com.mercadolibre.bootcamp_g1_final_project.entities.users.Seller;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    @ManyToOne
    private Seller seller;

    private ProductType type;
}
