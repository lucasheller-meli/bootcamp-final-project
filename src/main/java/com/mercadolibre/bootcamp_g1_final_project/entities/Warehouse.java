package com.mercadolibre.bootcamp_g1_final_project.entities;

import com.mercadolibre.bootcamp_g1_final_project.entities.users.Representative;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Warehouse {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String location;

    private String name;

    @ManyToMany(fetch = FetchType.EAGER)
    private List<Section> section;

    @OneToMany
    private List<Representative> representatives;
}
