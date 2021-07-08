package com.mercadolibre.bootcamp_g1_final_project.services.impl;
import com.mercadolibre.bootcamp_g1_final_project.controller.request.BatchRequest;
import com.mercadolibre.bootcamp_g1_final_project.controller.request.InboundOrderRequest;
import com.mercadolibre.bootcamp_g1_final_project.controller.response.StockWarehouse;
import com.mercadolibre.bootcamp_g1_final_project.controller.response.WarehouseProductResponse;
import com.mercadolibre.bootcamp_g1_final_project.entities.Batch;
import com.mercadolibre.bootcamp_g1_final_project.entities.InboundOrder;
import com.mercadolibre.bootcamp_g1_final_project.entities.Product;
import com.mercadolibre.bootcamp_g1_final_project.entities.Section;
import com.mercadolibre.bootcamp_g1_final_project.entities.Warehouse;
import com.mercadolibre.bootcamp_g1_final_project.exceptions.WarehouseNotExistException;
import com.mercadolibre.bootcamp_g1_final_project.repositories.WarehouseRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
@ExtendWith(MockitoExtension.class)
class WarehouseServiceImplTest {
    @InjectMocks
    WarehouseServiceImpl warehouseService;
    @Mock
    WarehouseRepository warehouseRepository;
    @Test
    void findEmptyListWarehouseByProductId() {
        List<WarehouseProductResponse> warehouseProductResponses = new ArrayList<>();
        when(warehouseRepository.findWarehouseByProductId(1)).thenReturn(warehouseProductResponses);
        final StockWarehouse stockWarehouse = warehouseService.findWarehouseWithProduct(1);
        Assertions.assertEquals(stockWarehouse.getProductId(), 1);
        Assertions.assertEquals(stockWarehouse.getWarehouseProducts().size(), 0);
    }
    @Test
    void findByIdSuccessFully() {
        final Warehouse expectedWarehouse = generateWarehouse();
        when(warehouseRepository.findById(1)).thenReturn(java.util.Optional.ofNullable(expectedWarehouse));
        final Warehouse warehouse = warehouseService.findById(1);
        Assertions.assertEquals(expectedWarehouse, warehouse);
    }
    @Test
    void findByIdThrowingException() {
        Assertions.assertThrows(WarehouseNotExistException.class, () -> warehouseService.findById(2));
    }
    private Warehouse generateWarehouse() {
        Section sectionTest = Section.builder().id(1).name("SectionA").build();
        return Warehouse.builder()
                .id(1)
                .orders(Collections.emptyList())
                .name("warehouseSP")
                .location("SP")
                .section(List.of(sectionTest))
                .build();
    }
}