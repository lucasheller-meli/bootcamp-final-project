package com.mercadolibre.bootcamp_g1_final_project.services;


import com.mercadolibre.bootcamp_g1_final_project.entities.Batch;

import java.util.List;

public interface BatchService {
    List<Batch> findBatchesByProductId(Integer productId);

    Batch save(Batch batch);
}
