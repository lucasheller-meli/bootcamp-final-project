package com.mercadolibre.bootcamp_g1_final_project.unit.beans;

import com.mercadolibre.bootcamp_g1_final_project.dtos.SampleDTO;
import com.mercadolibre.bootcamp_g1_final_project.beans.RandomSampleBean;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class RandomSampleBeanTest {

	@Test
	public void randomPositiveTestOK() {
		RandomSampleBean randomSample = new RandomSampleBean();

		SampleDTO sample = randomSample.random();
		
		assertTrue(sample.getRandom() >= 0);
	}
}
