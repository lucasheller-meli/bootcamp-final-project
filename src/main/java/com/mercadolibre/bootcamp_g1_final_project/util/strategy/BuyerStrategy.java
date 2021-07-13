package com.mercadolibre.bootcamp_g1_final_project.util.strategy;

import com.mercadolibre.bootcamp_g1_final_project.controller.response.BuyerResponse;
import com.mercadolibre.bootcamp_g1_final_project.controller.response.PurchasedProductResponse;

import java.util.List;

public interface BuyerStrategy {
    BuyerResponse order(List<PurchasedProductResponse> purchased);
}
