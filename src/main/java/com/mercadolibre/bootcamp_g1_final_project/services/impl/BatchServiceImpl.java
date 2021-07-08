package com.mercadolibre.bootcamp_g1_final_project.services.impl;

import com.mercadolibre.bootcamp_g1_final_project.entities.Batch;
import com.mercadolibre.bootcamp_g1_final_project.entities.Section;
import com.mercadolibre.bootcamp_g1_final_project.exceptions.NotFoundProductInBatch;
import com.mercadolibre.bootcamp_g1_final_project.exceptions.ProductNotExistException;
import com.mercadolibre.bootcamp_g1_final_project.repositories.BatchRepository;
import com.mercadolibre.bootcamp_g1_final_project.services.BatchService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BatchServiceImpl implements BatchService {
    private final BatchRepository batchRepository;

    public BatchServiceImpl(BatchRepository batchRepository) {
        this.batchRepository = batchRepository;
    }

    public List<Batch> findBatchesByProductId(Integer productId) throws NotFoundProductInBatch {
        return batchRepository.findBatchesByProductId(productId);
    }

    public List<Batch> findBatchesBySectorIn(List<Section> section){
        return batchRepository.findBatchesBySectorIn(section);
    }

    public Batch save(Batch batch) {
        return batchRepository.save(batch);
    }
}
