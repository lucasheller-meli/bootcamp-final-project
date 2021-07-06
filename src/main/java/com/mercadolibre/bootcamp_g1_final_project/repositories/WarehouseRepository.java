package com.mercadolibre.bootcamp_g1_final_project.repositories;

import com.mercadolibre.bootcamp_g1_final_project.entities.Section;
import com.mercadolibre.bootcamp_g1_final_project.entities.Warehouse;
import com.mercadolibre.bootcamp_g1_final_project.entities.users.Representative;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface WarehouseRepository extends CrudRepository<Warehouse, Integer> {

    Warehouse findWarehousesByRepresentatives(Representative representative);

}
