package com.mercadolibre.bootcamp_g1_final_project.services;

import com.mercadolibre.bootcamp_g1_final_project.controller.response.BuyerResponse;
import com.mercadolibre.bootcamp_g1_final_project.util.strategy.BuyerParameter;

public interface BuyerService {

    BuyerResponse getAllPurchasedProducts(Integer buyerId, BuyerParameter parameter);
}
