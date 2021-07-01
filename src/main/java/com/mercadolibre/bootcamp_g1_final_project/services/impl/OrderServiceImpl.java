package com.mercadolibre.bootcamp_g1_final_project.services.impl;

import com.mercadolibre.bootcamp_g1_final_project.controller.request.BatchRequest;
import com.mercadolibre.bootcamp_g1_final_project.controller.request.InboundOrderRequest;
import com.mercadolibre.bootcamp_g1_final_project.controller.response.BatchResponse;
import com.mercadolibre.bootcamp_g1_final_project.entities.*;
import com.mercadolibre.bootcamp_g1_final_project.repositories.OrderRepository;
import com.mercadolibre.bootcamp_g1_final_project.services.OrderService;
import com.mercadolibre.bootcamp_g1_final_project.services.WarehouseService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final WarehouseService warehouseService;
    private final SectionService sectionService;
    private final ProductService productService;

    public OrderServiceImpl(OrderRepository orderRepository, WarehouseService warehouseService, SectionService sectionService, ProductService productService) {
        this.orderRepository = orderRepository;
        this.warehouseService = warehouseService;
        this.sectionService = sectionService;
        this.productService = productService;
    }

    public List<BatchResponse> inboundOrder(InboundOrderRequest inboundOrderRequest){

        Warehouse warehouse = warehouseService.findById(inboundOrderRequest.getWarehouseId());
        List<Section> sections = warehouse.getSection();

        if(!verifySectionInWarehouse(inboundOrderRequest.getSectionId(), sections));

        Order order = Order.builder()
                .warehouse(warehouse)
                .batch(convertBatchRequestToBatch(inboundOrderRequest.getBatches())).build();

        Order orderSave = orderRepository.save(order);

        return convertBatchToBatchResponse(orderSave.getBatch());
    }

    private Boolean verifySectionInWarehouse(Integer sectionId, List<Section> sections){
        Section section = sectionService.findById(sectionId);

        for (Section s : sections) {
            if(s.getId().equals(section.getId())){
                return true;
            }
        }

        return false;
    }

    private List<Batch> convertBatchRequestToBatch(List<BatchRequest> batchRequests){

        List<Batch> batchList = new ArrayList<>();

        for (BatchRequest br: batchRequests) {
            Product product = productService.findById(br.getProductId());
            Batch batch = Batch.builder()
                    .product(product)
                    .currentTemperature(br.getCurrentTemperature())
                    .minimumTemperature(br.getMinimumTemperature())
                    .initialQuantity(br.getQuantity())
                    .currentQuantity(br.getQuantity())
                    .dueDate(br.getDueDate())
                    .build();

            batchList.add(batch);
        }

        return batchList;
    }

    private List<BatchResponse> convertBatchToBatchResponse(List<Batch> batchListSave){

        List<BatchResponse> batchResponsesList = new ArrayList<>();

        for (Batch b: batchListSave) {
            BatchResponse br = BatchResponse.builder()
                    .id(b.getId())
                    .product(b.getProduct())
                    .currentTemperature(b.getCurrentTemperature())
                    .minimumTemperature(b.getMinimumTemperature())
                    .initialQuantity(b.getInitialQuantity())
                    .currentQuantity(b.getCurrentQuantity())
                    .dueDate(b.getDueDate())
                    .manufacturingDate(b.getManufacturingDate())
                    .build();

            batchResponsesList.add(br);
        }

        return  batchResponsesList;
    }

}
