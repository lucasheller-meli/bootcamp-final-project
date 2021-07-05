package com.mercadolibre.bootcamp_g1_final_project.services.impl;

import com.mercadolibre.bootcamp_g1_final_project.controller.request.BatchRequest;
import com.mercadolibre.bootcamp_g1_final_project.controller.request.InboundOrderRequest;
import com.mercadolibre.bootcamp_g1_final_project.controller.response.BatchResponse;
import com.mercadolibre.bootcamp_g1_final_project.entities.*;
import com.mercadolibre.bootcamp_g1_final_project.exceptions.ProductNotExistException;
import com.mercadolibre.bootcamp_g1_final_project.exceptions.SectionInWarehouseNotFoundException;
import com.mercadolibre.bootcamp_g1_final_project.exceptions.SectionNotExistException;
import com.mercadolibre.bootcamp_g1_final_project.exceptions.WarehouseNotExistException;
import com.mercadolibre.bootcamp_g1_final_project.repositories.OrderRepository;


import com.mercadolibre.bootcamp_g1_final_project.controller.request.InboundOrderUpdateRequest;
import com.mercadolibre.bootcamp_g1_final_project.controller.response.BatchResponse;
import com.mercadolibre.bootcamp_g1_final_project.entities.Batch;
import com.mercadolibre.bootcamp_g1_final_project.entities.InboundOrder;
import com.mercadolibre.bootcamp_g1_final_project.entities.Product;
import com.mercadolibre.bootcamp_g1_final_project.entities.Warehouse;
import com.mercadolibre.bootcamp_g1_final_project.exceptions.InboundOrderNotFound;
import com.mercadolibre.bootcamp_g1_final_project.repositories.InboundOrderRepository;
import com.mercadolibre.bootcamp_g1_final_project.services.ProductService;
import com.mercadolibre.bootcamp_g1_final_project.util.MockitoExtension;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class OrderServiceImplTest {


    private BatchResponse batchResponseTest;
    LocalDateTime orderDateTest = LocalDateTime.now();

    @Mock
    private WarehouseServiceImpl warehouseServiceTest;
    @Mock
    private SectionServiceImpl sectionServiceTest;
    @Mock
    private ProductServiceImpl productServiceTest;
    @Mock
    private OrderRepository orderRepositoryTest;
    @Mock
    private InboundOrderRepository inboundOrderRepository;
    @InjectMocks
    private OrderServiceImpl orderServiceTest;


    private final BatchRequest batchRequestTest = BatchRequest.builder()
            .productId(1)
            .currentTemperature(3.4F)
            .minimumTemperature(3.4F)
            .quantity(20)
            .dueDate(LocalDateTime.now())
            .build();

    private final Section sectionTest = Section.builder().id(1).name("SectionA").build();

    private final Warehouse warehouse = Warehouse.builder()
            .id(1)
            .name("warehouseSP")
            .location("SP")
            .section(List.of(sectionTest))
            .build();

    private final InboundOrderRequest inboundOrderRequestTest = InboundOrderRequest
            .builder()
            .warehouseId(1)
            .sectionId(1)
            .batches(List.of(batchRequestTest))
            .build();

    //SUCCESSFUL TEST
    @Test
    void inboundOrderTest() {
        //arrange
        Product productTest = Product.builder().id(1).name("tomate").build();

        Batch batchTest = Batch.builder()
                .id(1)
                .currentTemperature(3.4F)
                .minimumTemperature(3.4F)
                .initialQuantity(20)
                .currentQuantity(20)
                .build();

        final InboundOrder inboundOrderTest = InboundOrder.builder()
                .id(1)
                .warehouse(warehouse)
                .batch(Collections.singletonList(batchTest))
                .orderDate(orderDateTest)
                .build();

        batchResponseTest = BatchResponse.builder()
                .id(1)
                .currentTemperature(3.4F)
                .minimumTemperature(3.4F)
                .initialQuantity(20)
                .currentQuantity(20)
                .build();

        Mockito.when(warehouseServiceTest.findById(1)).thenReturn(warehouse);
        Mockito.when(productServiceTest.findById(1)).thenReturn(productTest);
        Mockito.when(sectionServiceTest.findById(1)).thenReturn(sectionTest);
        Mockito.when(orderRepositoryTest.save(any())).thenReturn(inboundOrderTest);

        List<BatchResponse> batchResponseList = orderServiceTest.inboundOrder(inboundOrderRequestTest);

        //act
        List<BatchResponse> batchResponseListExpected = new ArrayList<>();
        batchResponseListExpected.add(batchResponseTest);

        //assert
        assertEquals(batchResponseListExpected.size(), batchResponseList.size());


    }

    // FAILED TEST
    @Test
    void shouldNotExistSection(){
        //arrange
        String messageExpected = "Section does not exist.";

        inboundOrderRequestTest.setSectionId(2);

        //act
        Mockito.when(warehouseServiceTest.findById(1)).thenReturn(warehouse);
        Mockito.when(sectionServiceTest.findById(2)).thenThrow(new SectionNotExistException());
        Exception exception = assertThrows(SectionNotExistException.class, () -> {orderServiceTest.inboundOrder(inboundOrderRequestTest);});

        //assert
        assertEquals(messageExpected, exception.getMessage());

    }

    @Test
    void shouldNotExistWarehouse(){
        //arrange
        String messageExpected = "Warehouse does not exist.";

        //act
        Mockito.when(warehouseServiceTest.findById(1)).thenThrow(new WarehouseNotExistException());
        Exception exception = assertThrows(WarehouseNotExistException.class, () -> {orderServiceTest.inboundOrder(inboundOrderRequestTest);});

        //assert
        assertEquals(messageExpected, exception.getMessage());
    }

    @Test
    void shouldNotVerifySectionInWareHouse(){
        //arrange
        String messageExpected = "Section exists but was not found in the warehouse.";

        inboundOrderRequestTest.setSectionId(2);
        Section sectionTest2 = Section.builder().id(2).name("SectionB").build();

        //act
        Mockito.when(warehouseServiceTest.findById(1)).thenReturn(warehouse);
        Mockito.when(sectionServiceTest.findById(2)).thenReturn(sectionTest2);
        Exception exception = assertThrows(SectionInWarehouseNotFoundException.class, () -> {orderServiceTest.inboundOrder(inboundOrderRequestTest);});

        //assert
        assertEquals(messageExpected, exception.getMessage());

    }

    @Test
    void shouldNotFoundProduct(){
        //arrange
        String messageExpected = "Product does not exist.";

        //act
        Mockito.when(warehouseServiceTest.findById(1)).thenReturn(warehouse);
        Mockito.when(sectionServiceTest.findById(1)).thenReturn(sectionTest);
        Mockito.when(productServiceTest.findById(1)).thenThrow(new ProductNotExistException());
        Exception exception = assertThrows(ProductNotExistException.class, () -> {orderServiceTest.inboundOrder(inboundOrderRequestTest);});

        //assert
        assertEquals(messageExpected, exception.getMessage());
    }

    @Test
    public void updateWithSucess() {
        InboundOrderUpdateRequest requestMock = InboundOrderUpdateRequest.builder().batches(List.of(BatchRequest.builder()
                .currentTemperature(12F)
                .minimumTemperature(8F).id(10)
                .productId(2)
                .dueDate(LocalDateTime.now())
                .quantity(5).build())).build();
        InboundOrder orderMock = InboundOrder.builder().id(1)
                .warehouse(Warehouse.builder().id(1).name("ws_sp").location("sp").build())
                .batch(List.of(Batch.builder().id(12)
                        .currentTemperature(12F)
                        .minimumTemperature(8F)
                        .product(Product.builder().name("ameixa").id(1).build())
                        .dueDate(LocalDateTime.now())
                        .currentQuantity(3)
                        .initialQuantity(5).build())).build();
        Product productMock = Product.builder().name("banana").id(2).build();
        Mockito.when(inboundOrderRepository.findById(1)).thenReturn(Optional.of(orderMock));
        Mockito.when(productServiceTest.findById(2)).thenReturn(productMock);

        List<BatchResponse> actualList = orderServiceTest.updateInboundOrder(1, requestMock);

        Assertions.assertEquals(2,actualList.size());
    }

    @Test
    public void inboundOrderNotFound() {
        Mockito.when(inboundOrderRepository.findById(1)).thenReturn(Optional.empty());
        Assertions.assertThrows(InboundOrderNotFound.class, () -> this.orderServiceTest.updateInboundOrder(1, InboundOrderUpdateRequest.builder().build()));
    }

}

