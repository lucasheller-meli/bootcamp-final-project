package com.mercadolibre.bootcamp_g1_final_project.util.strategy;

public enum BuyerParameter {

    TOTAL_ASC, TOTAL_DESC, PRICE_ASC, PRICE_DESC;

    public BuyerStrategy getStrategy() {
        if (this == TOTAL_ASC) {
            return new TotalPriceAscStrategy();
        } else if (this == TOTAL_DESC) {
            return new TotalPriceDescStrategy();
        } else if (this == PRICE_ASC) {
            return new ExpensiveProductStrategy();
        } else if (this == PRICE_DESC) {
            return new CheapestProductStrategy();
        }
        return null;
    }
}
