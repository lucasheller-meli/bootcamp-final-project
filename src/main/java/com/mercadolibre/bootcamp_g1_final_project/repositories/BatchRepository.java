package com.mercadolibre.bootcamp_g1_final_project.repositories;

import com.mercadolibre.bootcamp_g1_final_project.entities.Batch;
import org.springframework.data.repository.CrudRepository;

public interface BatchRepository extends CrudRepository<Batch, Integer> {
}

