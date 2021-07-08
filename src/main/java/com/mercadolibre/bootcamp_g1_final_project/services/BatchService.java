package com.mercadolibre.bootcamp_g1_final_project.services;


import com.mercadolibre.bootcamp_g1_final_project.entities.Batch;
import com.mercadolibre.bootcamp_g1_final_project.entities.Section;

import java.util.List;

public interface BatchService {
    List<Batch> findBatchesByProductId(Integer productId);


    public List<Batch> findBatchesBySectorIn(List<Section> section);

    Batch save(Batch batch);
}
