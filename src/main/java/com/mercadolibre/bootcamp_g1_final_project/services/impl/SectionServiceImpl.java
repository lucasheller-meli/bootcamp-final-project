package com.mercadolibre.bootcamp_g1_final_project.services.impl;

import com.mercadolibre.bootcamp_g1_final_project.entities.Section;
import com.mercadolibre.bootcamp_g1_final_project.exceptions.NotFoundSectionException;
import com.mercadolibre.bootcamp_g1_final_project.repositories.SectionRepository;
import com.mercadolibre.bootcamp_g1_final_project.services.SectionService;
import org.springframework.stereotype.Service;

@Service
public class SectionServiceImpl implements SectionService {

    private final SectionRepository sectionRepository;

    public SectionServiceImpl(SectionRepository sectionRepository) {
        this.sectionRepository = sectionRepository;
    }

    @Override
    public Section findById(Integer id) throws NotFoundSectionException {
        return sectionRepository.findById(id).orElseThrow(NotFoundSectionException::new);
    }
}
