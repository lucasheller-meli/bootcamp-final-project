package com.mercadolibre.bootcamp_g1_final_project.entities;

import com.mercadolibre.bootcamp_g1_final_project.entities.users.Representative;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;

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

    //tirei o cascade
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "warehouse_id")
    private List<Section> section;

    @OneToMany
    private List<Representative> representatives;

    @OneToMany(cascade = CascadeType.ALL)
    private List<InboundOrder> orders;
}
