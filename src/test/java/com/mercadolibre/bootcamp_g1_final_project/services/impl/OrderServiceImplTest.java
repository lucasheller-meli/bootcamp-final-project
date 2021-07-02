package com.mercadolibre.bootcamp_g1_final_project.services.impl;

import com.mercadolibre.bootcamp_g1_final_project.controller.request.BatchRequest;
import com.mercadolibre.bootcamp_g1_final_project.controller.request.InboundOrderRequest;
import com.mercadolibre.bootcamp_g1_final_project.controller.response.BatchResponse;
import com.mercadolibre.bootcamp_g1_final_project.entities.*;
import com.mercadolibre.bootcamp_g1_final_project.exceptions.SectionInWarehouseNotFoundException;
import com.mercadolibre.bootcamp_g1_final_project.exceptions.SectionNotExistException;
import com.mercadolibre.bootcamp_g1_final_project.repositories.OrderRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

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
    @InjectMocks
    private OrderServiceImpl orderServiceTest;


    @Test
    void inboundOrderTest() {
        //arrange
        BatchRequest batchRequestTest = BatchRequest.builder()
                .productId(1)
                .currentTemperature(3.4F)
                .minimumTemperature(3.4F)
                .quantity(20)
                .dueDate(LocalDateTime.now())
                .build();

        final InboundOrderRequest inboundOrderRequestTest = InboundOrderRequest
                .builder()
                .warehouseId(1)
                .sectionId(1)
                .batches(List.of(batchRequestTest))
                .build();

        Section sectionTest = Section.builder().id(1).name("SectionA").build();

        Warehouse warehouse = Warehouse.builder()
                .id(1)
                .name("warehouseSP")
                .location("SP")
                .section(List.of(sectionTest))
                .build();

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
    void shouldNotExistection(){
        //arrange
        String messageExpected = "Section does not exist.";

        BatchRequest batchRequestTest = BatchRequest.builder()
                .productId(1)
                .currentTemperature(3.4F)
                .minimumTemperature(3.4F)
                .quantity(20)
                .dueDate(LocalDateTime.now())
                .build();

        final InboundOrderRequest inboundOrderRequestTest = InboundOrderRequest.builder()
                .warehouseId(1)
                .sectionId(2)
                .batches(List.of(batchRequestTest))
                .build();

        Section sectionTest = Section.builder().id(1).name("SectionA").build();

        Warehouse warehouse = Warehouse.builder()
                .id(1)
                .name("warehouseSP")
                .location("SP")
                .section(List.of(sectionTest))
                .build();

        //act
        Mockito.when(warehouseServiceTest.findById(1)).thenReturn(warehouse);
        Mockito.when(sectionServiceTest.findById(2)).thenThrow(new SectionNotExistException());
        Exception exception = assertThrows(SectionNotExistException.class, () -> {orderServiceTest.inboundOrder(inboundOrderRequestTest);});

        //assert
        assertEquals(messageExpected, exception.getMessage());

    }

    //PROBLEMA
//    @Test
//    void shouldNotVerifySectionInWareHouse(){
//        //arrange
//        String messageExpected = "Section exists but was not found in the warehouse.";
//
//        BatchRequest batchRequestTest = BatchRequest.builder()
//                .productId(1)
//                .currentTemperature(3.4F)
//                .minimumTemperature(3.4F)
//                .quantity(20)
//                .dueDate(LocalDateTime.now())
//                .build();
//
//        final InboundOrderRequest inboundOrderRequestTest = InboundOrderRequest.builder()
//                .warehouseId(1)
//                .sectionId(1)
//                .batches(List.of(batchRequestTest))
//                .build();
//
//        Section sectionTest = Section.builder().id(1).name("SectionA").build();
//        Section sectionTest2 = Section.builder().id(2).name("SectionB").build();
//
//        Warehouse warehouse = Warehouse.builder()
//                .id(1)
//                .name("warehouseSP")
//                .location("SP")
//                .section(List.of(sectionTest))
//                .build();
//
//        //act
//        Mockito.when(warehouseServiceTest.findById(1)).thenReturn(warehouse);
//        Mockito.when(sectionServiceTest.findById(2)).thenReturn(sectionTest2);
//        Exception exception = assertThrows(SectionInWarehouseNotFoundException.class, () -> {orderServiceTest.inboundOrder(inboundOrderRequestTest);});
//
//        //assert
//        assertEquals(messageExpected, exception.getMessage());
//
//    }

    @Test
    void shouldNotFoundProduct(){

    }

    @Test
    void shouldNotConvertBatchRequestToBatch(){

    }
}
