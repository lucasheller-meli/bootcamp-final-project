package com.mercadolibre.bootcamp_g1_final_project.services.impl;

import com.mercadolibre.bootcamp_g1_final_project.controller.response.BuyerResponse;
import com.mercadolibre.bootcamp_g1_final_project.controller.response.PurchasedProductResponse;
import com.mercadolibre.bootcamp_g1_final_project.entities.Product;
import com.mercadolibre.bootcamp_g1_final_project.entities.PurchaseOrder;
import com.mercadolibre.bootcamp_g1_final_project.entities.PurchaseOrderItem;
import com.mercadolibre.bootcamp_g1_final_project.exceptions.PurchasedOrderNotFound;
import com.mercadolibre.bootcamp_g1_final_project.repositories.PurchaseOrderRepository;
import com.mercadolibre.bootcamp_g1_final_project.services.BuyerService;
import com.mercadolibre.bootcamp_g1_final_project.util.strategy.BuyerParameter;
import com.mercadolibre.bootcamp_g1_final_project.util.strategy.BuyerStrategy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class BuyerServiceImpl implements BuyerService {

    private final PurchaseOrderRepository purchaseOrderRepository;

    public BuyerServiceImpl(PurchaseOrderRepository purchaseOrderRepository) {
        this.purchaseOrderRepository = purchaseOrderRepository;
    }

    @Override
    @Transactional
    public BuyerResponse getAllPurchasedProducts(Integer buyerId, BuyerParameter parameter) {
        List<PurchaseOrder> purchasedByClient = purchaseOrderRepository.findAllByBuyer_Id(buyerId).orElseThrow(() -> new PurchasedOrderNotFound(buyerId));
        BuyerStrategy strategy = parameter.getStrategy();
        return strategy.order(prepareResponse(purchasedByClient));
    }

    private List<PurchasedProductResponse> prepareResponse(List<PurchaseOrder> orders) {
        List<PurchasedProductResponse> products = new ArrayList<>();
        for(PurchaseOrder order : orders) {
            for (PurchaseOrderItem cart : order.getProducts()) {
                Product product = cart.getProduct();
                products.add(PurchasedProductResponse.builder().name(product.getName())
                        .price(product.getPrice()).quantity(cart.getQuantity())
                        .totalPrice(cart.getQuantity()*product.getPrice()).build());
            }
        }
        return products;
    }

}
