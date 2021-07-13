package com.mercadolibre.bootcamp_g1_final_project.util.strategy;

import com.mercadolibre.bootcamp_g1_final_project.controller.response.BuyerResponse;
import com.mercadolibre.bootcamp_g1_final_project.controller.response.PurchasedProductResponse;

import java.util.Comparator;
import java.util.List;

public class TotalPriceAscStrategy implements BuyerStrategy {

    @Override
    public BuyerResponse order(List<PurchasedProductResponse> purchased) {
        purchased.sort(Comparator.comparingDouble(PurchasedProductResponse::getTotalPrice));
        return BuyerResponse.builder().products(purchased).build();
    }
}
