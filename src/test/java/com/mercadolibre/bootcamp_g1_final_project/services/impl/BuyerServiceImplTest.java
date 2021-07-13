package com.mercadolibre.bootcamp_g1_final_project.services.impl;

import com.mercadolibre.bootcamp_g1_final_project.controller.response.BuyerResponse;
import com.mercadolibre.bootcamp_g1_final_project.controller.response.PurchasedProductResponse;
import com.mercadolibre.bootcamp_g1_final_project.entities.Product;
import com.mercadolibre.bootcamp_g1_final_project.entities.PurchaseOrder;
import com.mercadolibre.bootcamp_g1_final_project.entities.PurchaseOrderItem;
import com.mercadolibre.bootcamp_g1_final_project.entities.users.Buyer;
import com.mercadolibre.bootcamp_g1_final_project.exceptions.ProductNotExistException;
import com.mercadolibre.bootcamp_g1_final_project.exceptions.PurchasedOrderNotFound;
import com.mercadolibre.bootcamp_g1_final_project.repositories.PurchaseOrderRepository;
import com.mercadolibre.bootcamp_g1_final_project.util.MockitoExtension;
import com.mercadolibre.bootcamp_g1_final_project.util.strategy.BuyerParameter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BuyerServiceImplTest {

    private List<PurchaseOrder> commonPurchaseOrder = new ArrayList<>();

    @InjectMocks
    private BuyerServiceImpl service;

    @Mock
    private PurchaseOrderRepository repository;

    @BeforeEach
    public void setup(){
        Buyer buyer = new Buyer(1,"buyer","123");
        commonPurchaseOrder.add(PurchaseOrder.builder().buyer(buyer).orderDate(LocalDate.now().plusWeeks(5))
                .products(List.of(
                        PurchaseOrderItem.builder().product(Product.builder().name("cogumelo").price(15.0).build()).quantity(10).build(),
                        PurchaseOrderItem.builder().product(Product.builder().name("abacaxi").price(12.0).build()).quantity(10).build(),
                        PurchaseOrderItem.builder().product(Product.builder().name("azeite").price(20.0).build()).quantity(7).build())).build());

    }

    @Test
    public void sucessGetOrderByTotalAsc() {
        Mockito.when(repository.findAllByBuyer_Id(1)).thenReturn(Optional.of(commonPurchaseOrder));
        BuyerResponse response = service.getAllPurchasedProducts(1, BuyerParameter.TOTAL_ASC);
        List<PurchasedProductResponse> products = response.getProducts();
        Assertions.assertTrue(products.get(2).getTotalPrice() > products.get(0).getTotalPrice());
        Assertions.assertTrue(products.get(2).getPrice() < products.get(1).getPrice());

    }

    @Test
    public void sucessGetOrderBTotalDesc() {
        Mockito.when(repository.findAllByBuyer_Id(1)).thenReturn(Optional.of(commonPurchaseOrder));
        BuyerResponse response = service.getAllPurchasedProducts(1, BuyerParameter.TOTAL_DESC);
        List<PurchasedProductResponse> products = response.getProducts();
        Assertions.assertTrue(products.get(0).getTotalPrice() > products.get(2).getTotalPrice());
        Assertions.assertTrue(products.get(0).getPrice() < products.get(1).getPrice());
    }

    @Test
    public void sucessGetOrderByPriceAsc() {
        Mockito.when(repository.findAllByBuyer_Id(1)).thenReturn(Optional.of(commonPurchaseOrder));
        BuyerResponse response = service.getAllPurchasedProducts(1, BuyerParameter.PRICE_ASC);
        List<PurchasedProductResponse> products = response.getProducts();
        Assertions.assertTrue(products.get(2).getPrice() > products.get(0).getPrice());
        Assertions.assertTrue(products.get(2).getTotalPrice() < products.get(1).getTotalPrice());
    }

    @Test
    public void sucessGetOrderByPriceDesc() {
        Mockito.when(repository.findAllByBuyer_Id(1)).thenReturn(Optional.of(commonPurchaseOrder));
        BuyerResponse response = service.getAllPurchasedProducts(1, BuyerParameter.PRICE_DESC);
        List<PurchasedProductResponse> products = response.getProducts();
        Assertions.assertTrue(products.get(0).getPrice() > products.get(2).getPrice());
        Assertions.assertTrue(products.get(0).getTotalPrice() < products.get(1).getTotalPrice());
    }

    @Test
    public void cartNotFoundException() {
        Mockito.when(repository.findAllByBuyer_Id(1)).thenReturn(Optional.empty());
        String messageExpected = "Did not found any order of the buyer of id 1.";

        Exception exception = assertThrows(PurchasedOrderNotFound.class, () -> service.getAllPurchasedProducts(1, BuyerParameter.PRICE_DESC));

        assertEquals(messageExpected, exception.getMessage());
    }

}