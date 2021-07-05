package com.mercadolibre.bootcamp_g1_final_project.services.impl;

import com.mercadolibre.bootcamp_g1_final_project.controller.request.BatchRequest;
import com.mercadolibre.bootcamp_g1_final_project.controller.request.InboundOrderUpdateRequest;
import com.mercadolibre.bootcamp_g1_final_project.controller.request.InboundOrderRequest;
import com.mercadolibre.bootcamp_g1_final_project.controller.response.BatchResponse;
import com.mercadolibre.bootcamp_g1_final_project.entities.*;
import com.mercadolibre.bootcamp_g1_final_project.exceptions.InboundOrderNotFound;
import com.mercadolibre.bootcamp_g1_final_project.repositories.InboundOrderRepository;
import com.mercadolibre.bootcamp_g1_final_project.exceptions.SectionInWarehouseNotFoundException;
import com.mercadolibre.bootcamp_g1_final_project.exceptions.ProductNotExistException;
import com.mercadolibre.bootcamp_g1_final_project.repositories.OrderRepository;
import com.mercadolibre.bootcamp_g1_final_project.services.OrderService;
import com.mercadolibre.bootcamp_g1_final_project.services.ProductService;
import com.mercadolibre.bootcamp_g1_final_project.services.SectionService;
import com.mercadolibre.bootcamp_g1_final_project.services.WarehouseService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {


    private final OrderRepository orderRepository;
    private final InboundOrderRepository inboundOrderRepository;
    private final WarehouseService warehouseService;
    private final SectionService sectionService;
    private final ProductService productService;



    public OrderServiceImpl(OrderRepository orderRepository, WarehouseService warehouseService, SectionService sectionService, ProductService productService, InboundOrderRepository inboundOrderRepository) {
        this.orderRepository = orderRepository;
        this.inboundOrderRepository = inboundOrderRepository;
        this.warehouseService = warehouseService;
        this.sectionService = sectionService;
        this.productService = productService;
    }


    public List<BatchResponse> inboundOrder(InboundOrderRequest inboundOrderRequest) throws SectionInWarehouseNotFoundException {

        Warehouse warehouse = warehouseService.findById(inboundOrderRequest.getWarehouseId());
        List<Section> sections = warehouse.getSection();

        if(!verifySectionInWarehouse(inboundOrderRequest.getSectionId(), sections)) throw new SectionInWarehouseNotFoundException();

        InboundOrder inboundOrder = InboundOrder.builder()
                .warehouse(warehouse)
                .batch(convertBatchRequestToBatch(inboundOrderRequest.getBatches())).build();

        InboundOrder inboundOrderSave = orderRepository.save(inboundOrder);

        return convertBatchToBatchResponse(inboundOrderSave.getBatch());
    }


    public List<BatchResponse> updateInboundOrder(Integer id, InboundOrderUpdateRequest inboundOrderUpdateRequest) {
        InboundOrder order = inboundOrderRepository.findById(id).orElseThrow(() -> new InboundOrderNotFound(id));
        refreshOrAddBatches(order,convertBatchRequestToBatch(inboundOrderUpdateRequest.getBatches()));
        inboundOrderRepository.save(order);
        return convertBatchToBatchResponse(order.getBatch());
    }

    private void refreshOrAddBatches(InboundOrder order, List<Batch> updatedBatches) {
        HashMap<Integer, Batch> batches = new HashMap<>();
        order.getBatch().forEach(b -> batches.put(b.getId(),b));
        updatedBatches.forEach(b-> batches.put(b.getId(),b));
        order.setBatch(new ArrayList<>(batches.values()));
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
                    .id(br.getId())
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
