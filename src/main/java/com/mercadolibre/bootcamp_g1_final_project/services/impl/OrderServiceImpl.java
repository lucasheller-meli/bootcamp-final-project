package com.mercadolibre.bootcamp_g1_final_project.services.impl;

import com.mercadolibre.bootcamp_g1_final_project.controller.request.BatchRequest;
import com.mercadolibre.bootcamp_g1_final_project.controller.request.BatchUpdateRequest;
import com.mercadolibre.bootcamp_g1_final_project.controller.request.InboundOrderRequest;
import com.mercadolibre.bootcamp_g1_final_project.controller.request.InboundOrderUpdateRequest;
import com.mercadolibre.bootcamp_g1_final_project.controller.response.BatchResponse;

import com.mercadolibre.bootcamp_g1_final_project.exceptions.InboundOrderNotFound;
import com.mercadolibre.bootcamp_g1_final_project.repositories.InboundOrderRepository;

import com.mercadolibre.bootcamp_g1_final_project.controller.response.InboundOrderResponse;
import com.mercadolibre.bootcamp_g1_final_project.controller.response.ProductResponse;
import com.mercadolibre.bootcamp_g1_final_project.entities.Batch;
import com.mercadolibre.bootcamp_g1_final_project.entities.InboundOrder;
import com.mercadolibre.bootcamp_g1_final_project.entities.Product;
import com.mercadolibre.bootcamp_g1_final_project.entities.Section;
import com.mercadolibre.bootcamp_g1_final_project.entities.Warehouse;

import com.mercadolibre.bootcamp_g1_final_project.exceptions.SectionInWarehouseNotFoundException;
import com.mercadolibre.bootcamp_g1_final_project.repositories.OrderRepository;
import com.mercadolibre.bootcamp_g1_final_project.services.OrderService;
import com.mercadolibre.bootcamp_g1_final_project.services.ProductService;
import com.mercadolibre.bootcamp_g1_final_project.services.SectionService;
import com.mercadolibre.bootcamp_g1_final_project.services.WarehouseService;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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

    @Transactional
    public InboundOrderResponse inboundOrder(InboundOrderRequest inboundOrderRequest) throws SectionInWarehouseNotFoundException {
        final Warehouse warehouse = warehouseService.findById(inboundOrderRequest.getWarehouseId());
        final List<Section> sections = warehouse.getSection();

        if (!verifySectionInWarehouse(inboundOrderRequest.getSectionId(), sections))
            throw new SectionInWarehouseNotFoundException();

        final InboundOrder inboundOrder = InboundOrder.builder()
                .warehouse(warehouse)
                .batch(convertBatchRequestToBatch(inboundOrderRequest.getBatches(), inboundOrderRequest.getSectionId()))
                .build();

        final InboundOrder inboundOrderSave = orderRepository.save(inboundOrder);

        warehouseService.updateOrders(warehouse,inboundOrderSave);

        return new InboundOrderResponse(convertBatchToBatchResponse(inboundOrderSave.getBatch()));
    }

    @Transactional
    public List<BatchResponse> updateInboundOrder(Integer id, InboundOrderUpdateRequest inboundOrderUpdateRequest) {
        InboundOrder order = inboundOrderRepository.findById(id).orElseThrow(() -> new InboundOrderNotFound(id));
        Section section = order.getBatch().get(0).getSector();
        refreshOrAddBatches(order, convertBatchUpdateRequestToBatch(inboundOrderUpdateRequest.getBatches(),section));
        inboundOrderRepository.save(order);
        return convertBatchToBatchResponse(order.getBatch());
    }

    private List<Batch> convertBatchUpdateRequestToBatch(List<BatchUpdateRequest> batches, Section commonSection) {
        final List<Batch> batchList = new ArrayList<>();

        for (BatchUpdateRequest br : batches) {
            Product product = productService.findById(br.getProductId());
            Batch batch = Batch.builder()
                    .product(product)
                    .id(br.getId())
                    .sector(commonSection)
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

    private void refreshOrAddBatches(InboundOrder order, List<Batch> updatedBatches) {
        final HashMap<Integer, Batch> batches = new HashMap<>();
        order.getBatch().forEach(b -> batches.put(b.getId(), b));
        updatedBatches.forEach(b -> batches.put(b.getId(), b));
        order.setBatch(new ArrayList<>(batches.values()));
    }

    private Boolean verifySectionInWarehouse(Integer sectionId, List<Section> sections) {
        final Section section = sectionService.findById(sectionId);

        for (Section s : sections) {
            if (s.getId().equals(section.getId())) {
                return true;
            }
        }

        return false;
    }

    private List<Batch> convertBatchRequestToBatch(List<BatchRequest> batchRequests, Integer sectionId) {

        final List<Batch> batchList = new ArrayList<>();
        Section section = sectionService.findById(sectionId);

        for (BatchRequest br : batchRequests) {
            Product product = productService.findById(br.getProductId());
            Batch batch = Batch.builder()
                    .sector(section)
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

    private List<BatchResponse> convertBatchToBatchResponse(List<Batch> batchListSave) {

        final List<BatchResponse> batchResponsesList = new ArrayList<>();

        for (Batch b : batchListSave) {
            BatchResponse br = BatchResponse.builder()
                    .id(b.getId())
                    .product(ProductResponse
                            .builder()
                            .productId(b.getProduct().getId())
                            .name(b.getProduct().getName())
                            .type(b.getProduct().getType())
                            .build())
                    .currentTemperature(b.getCurrentTemperature())
                    .minimumTemperature(b.getMinimumTemperature())
                    .initialQuantity(b.getInitialQuantity())
                    .currentQuantity(b.getCurrentQuantity())
                    .dueDate(b.getDueDate())
                    .manufacturingDate(b.getManufacturingDate())
                    .build();

            batchResponsesList.add(br);
        }

        return batchResponsesList;
    }

}
