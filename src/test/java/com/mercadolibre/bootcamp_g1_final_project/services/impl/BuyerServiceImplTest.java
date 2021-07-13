package com.mercadolibre.bootcamp_g1_final_project.services.impl;

import com.mercadolibre.bootcamp_g1_final_project.controller.response.BuyerResponse;
import com.mercadolibre.bootcamp_g1_final_project.entities.Product;
import com.mercadolibre.bootcamp_g1_final_project.entities.PurchaseOrder;
import com.mercadolibre.bootcamp_g1_final_project.entities.PurchaseOrderItem;
import com.mercadolibre.bootcamp_g1_final_project.entities.users.Buyer;
import com.mercadolibre.bootcamp_g1_final_project.repositories.PurchaseOrderRepository;
import com.mercadolibre.bootcamp_g1_final_project.util.MockitoExtension;
import com.mercadolibre.bootcamp_g1_final_project.util.strategy.BuyerParameter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class BuyerServiceImplTest {

    private List<PurchaseOrder> commonPurchaseOrder = new ArrayList<>();

    @InjectMocks
    private BuyerServiceImpl service;

    @Mock
    private PurchaseOrderRepository repository;

    @BeforeAll
    public void setup(){
        Buyer buyer = new Buyer(1,"buyer","123");
        commonPurchaseOrder.add(PurchaseOrder.builder().buyer(buyer).orderDate(LocalDate.now().plusWeeks(5))
                .products(List.of(
                        PurchaseOrderItem.builder().product(Product.builder().name("cogumelo").price(15.0).build()).quantity(10).build(),
                        PurchaseOrderItem.builder().product(Product.builder().name("abacaxi").price(12.0).build()).quantity(10).build(),
                        PurchaseOrderItem.builder().product(Product.builder().name("azeite").price(20.0).build()).quantity(7).build())).build());
        Mockito.when(repository.findAllByBuyer_Id(1)).thenReturn(Optional.of(commonPurchaseOrder));
    }

    @Test
    public void sucessGetOrderByTotalAsc() {
        BuyerResponse response = service.getAllPurchasedProducts(1, BuyerParameter.TOTAL_ASC);
        Assertions.assertEquals(response.getProducts().get(0).getPrice(),12.0);

    }

    @Test
    public void sucessGetOrderByPriceAsc() {
        BuyerResponse response = service.getAllPurchasedProducts(1, BuyerParameter.TOTAL_DESC);
        Assertions.assertEquals(response.getProducts().get(0).getPrice(),12.0);
    }

    @Test
    public void sucessGetOrderByTotalDesc() {
        BuyerResponse response = service.getAllPurchasedProducts(1, BuyerParameter.PRICE_ASC);
        Assertions.assertEquals(response.getProducts().get(0).getPrice(),12.0);
    }

    @Test
    public void sucessGetOrderByPriceDesc() {
        BuyerResponse response = service.getAllPurchasedProducts(1, BuyerParameter.PRICE_DESC);
        Assertions.assertEquals(response.getProducts().get(0).getPrice(),12.0);
    }

    @Test
    public void cartNotFoundException() {

    }

}