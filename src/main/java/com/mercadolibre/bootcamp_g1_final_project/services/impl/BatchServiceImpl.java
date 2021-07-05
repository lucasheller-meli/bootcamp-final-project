package com.mercadolibre.bootcamp_g1_final_project.services.impl;

import com.mercadolibre.bootcamp_g1_final_project.entities.Batch;
import com.mercadolibre.bootcamp_g1_final_project.repositories.BatchRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BatchServiceImpl {
    private final BatchRepository batchRepository;

    public BatchServiceImpl(BatchRepository batchRepository) {
        this.batchRepository = batchRepository;
    }

    public List<Batch> findBatchesByProductId(Integer productId) {
        return batchRepository.findBatchesByProductId(productId);
    }
}
