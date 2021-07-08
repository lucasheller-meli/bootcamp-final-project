package com.mercadolibre.bootcamp_g1_final_project.services.impl;

import com.mercadolibre.bootcamp_g1_final_project.controller.request.PurchaseOrderItemRequest;
import com.mercadolibre.bootcamp_g1_final_project.controller.request.PurchaseOrderRequest;
import com.mercadolibre.bootcamp_g1_final_project.controller.response.PurchaseOrderItemResponse;
import com.mercadolibre.bootcamp_g1_final_project.controller.response.PurchaseOrderItemsResponse;
import com.mercadolibre.bootcamp_g1_final_project.controller.response.PurchaseOrderResponse;
import com.mercadolibre.bootcamp_g1_final_project.entities.Batch;
import com.mercadolibre.bootcamp_g1_final_project.entities.BatchQuantity;
import com.mercadolibre.bootcamp_g1_final_project.entities.PurchaseOrder;
import com.mercadolibre.bootcamp_g1_final_project.entities.PurchaseOrderItem;
import com.mercadolibre.bootcamp_g1_final_project.entities.users.Buyer;
import com.mercadolibre.bootcamp_g1_final_project.exceptions.ApiException;
import com.mercadolibre.bootcamp_g1_final_project.repositories.PurchaseOrderRepository;
import com.mercadolibre.bootcamp_g1_final_project.services.BatchService;
import com.mercadolibre.bootcamp_g1_final_project.services.ProductService;
import com.mercadolibre.bootcamp_g1_final_project.services.PurchaseOrderItemService;
import com.mercadolibre.bootcamp_g1_final_project.services.PurchaseOrderService;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class PurchaseOrderServiceImpl implements PurchaseOrderService {
    private final PurchaseOrderRepository repository;
    private final BatchService batchService;
    private final ProductService productService;
    private final PurchaseOrderItemService purchaseOrderItemService;

    private static final Integer MINIMUM_WEEKS_TO_EXPIRE = 3;

    public PurchaseOrderServiceImpl(PurchaseOrderRepository repository, BatchService batchService, ProductService productService, PurchaseOrderItemService purchaseOrderItemService) {
        this.repository = repository;
        this.batchService = batchService;
        this.productService = productService;
        this.purchaseOrderItemService = purchaseOrderItemService;
    }

    public PurchaseOrderItemsResponse orderItems(Integer id) {
        return new PurchaseOrderItemsResponse(responseProducts(findById(id).getProducts()));
    }

    @Transactional
    public PurchaseOrderResponse updatePurchaseOrder(PurchaseOrderRequest request, Integer id) {
        PurchaseOrder order = findById(id);
        Buyer buyer = (Buyer) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        checkIfBuyersAreEqual(order.getBuyer(), buyer);

        returnOldProducts(order.getProducts());

        List<PurchaseOrderItemRequest> products = request.getProducts();
        checkIfProductsHaveEnoughStock(products);

        Map<Integer, List<BatchQuantity>> productBatchQuantities = updateBatchesProductQuantities(products);
        PurchaseOrder savedOrder = repository.save(new PurchaseOrder(order.getId(), buyer, mappedProducts(products, productBatchQuantities)));
        List<PurchaseOrderItem> purchasedProducts = savedOrder.getProducts();

        return new PurchaseOrderResponse(
            savedOrder.getId(),
            savedOrder.getOrderDate(),
            buyer.getId(),
            responseProducts(purchasedProducts),
            totalPrice(purchasedProducts)
        );
    }

    @Transactional
    public PurchaseOrderResponse purchaseOrder(PurchaseOrderRequest request) {
        List<PurchaseOrderItemRequest> products = request.getProducts();

        checkIfProductsHaveEnoughStock(products);
        Map<Integer, List<BatchQuantity>> productBatchQuantities = updateBatchesProductQuantities(products);

        Buyer buyer = (Buyer) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        PurchaseOrder savedOrder = repository.save(new PurchaseOrder(buyer, mappedProducts(products, productBatchQuantities)));
        List<PurchaseOrderItem> purchasedProducts = savedOrder.getProducts();

        return new PurchaseOrderResponse(
            savedOrder.getId(),
            savedOrder.getOrderDate(),
            buyer.getId(),
            responseProducts(purchasedProducts),
            totalPrice(purchasedProducts)
        );
    }

    private PurchaseOrder findById(Integer id) {
        return repository.findById(id).orElseThrow(() -> new ApiException(
            HttpStatus.NOT_FOUND.name(), "Purchase order with id " + id + " not found", HttpStatus.NOT_FOUND.value()
        ));
    }

    private void checkIfProductsHaveEnoughStock(List<PurchaseOrderItemRequest> products) {
        products.forEach(product -> checkIfProductHasEnoughStock(product.getProductId(), product.getQuantity()));
    }

    private Map<Integer, List<BatchQuantity>> updateBatchesProductQuantities(List<PurchaseOrderItemRequest> products) {
        return products.stream()
            .collect(Collectors.toMap(
                PurchaseOrderItemRequest::getProductId,
                this::updateBatchesProductQuantity
            ));
    }

    private void checkIfProductHasEnoughStock(Integer productId, Integer quantity) {
        boolean hasStock = batchService.findBatchesByProductId(productId)
            .stream()
            .mapToInt(batch -> expiresSoon(batch) ? 0 : batch.getCurrentQuantity())
            .sum() >= quantity;

        if (!hasStock) throw new ApiException(
            HttpStatus.BAD_REQUEST.name(),
            "Product with id " + productId + " does not have " + quantity + " units available",
            HttpStatus.BAD_REQUEST.value()
        );
    }

    private List<BatchQuantity> updateBatchesProductQuantity(PurchaseOrderItemRequest product) {
        Integer quantityToRemove = product.getQuantity();
        List<BatchQuantity> batchQuantities = new ArrayList<>();

        for (Batch batch : batchService.findBatchesByProductId(product.getProductId())) {
            if (quantityToRemove == 0) break;

            if (expiresSoon(batch)) continue;

            Integer batchProductQuantity = batch.getCurrentQuantity();

            if (batchProductQuantity >= quantityToRemove) {
                updateBatchProductQuantity(batch, quantityToRemove, batchQuantities);
                break;
            }

            updateBatchProductQuantity(batch, batchProductQuantity, batchQuantities);
            quantityToRemove -= batchProductQuantity;
        }

        return batchQuantities;
    }

    private void updateBatchProductQuantity(Batch batch, Integer quantityToRemove, List<BatchQuantity> batchQuantities) {
        batch.reduceCurrentQuantityBy(quantityToRemove);
        batchService.save(batch);
        batchQuantities.add(new BatchQuantity(batch, quantityToRemove));
    }

    private List<PurchaseOrderItem> mappedProducts(List<PurchaseOrderItemRequest> products, Map<Integer, List<BatchQuantity>> productBatchQuantities) {
        return products.stream()
            .map(product -> new PurchaseOrderItem(
                productService.findById(product.getProductId()),
                product.getQuantity(),
                productBatchQuantities.get(product.getProductId())
            ))
            .collect(Collectors.toList());
    }

    private List<PurchaseOrderItemResponse> responseProducts(List<PurchaseOrderItem> products) {
        return products.stream()
            .map(product -> new PurchaseOrderItemResponse(product.getProduct().getId(), product.getQuantity()))
            .collect(Collectors.toList());
    }

    private Double totalPrice(List<PurchaseOrderItem> products) {
        return products.stream()
            .mapToDouble(product -> product.getProduct().getPrice() * product.getQuantity())
            .sum();
    }

    private boolean expiresSoon(Batch batch) {
        return batch.getDueDate().isBefore(LocalDateTime.now().plusWeeks(MINIMUM_WEEKS_TO_EXPIRE));
    }

    private void checkIfBuyersAreEqual(Buyer orderBuyer, Buyer requesterBuyer) {
        Integer orderBuyerId = orderBuyer.getId();
        Integer requesterBuyerId = requesterBuyer.getId();

        if (!orderBuyerId.equals(requesterBuyerId)) throw new ApiException(
            HttpStatus.FORBIDDEN.name(),
            "Order buyer with id " + orderBuyerId + " is different from requester buyer with id " + requesterBuyerId,
            HttpStatus.FORBIDDEN.value()
        );
    }

    private void returnOldProducts(List<PurchaseOrderItem> products) {
        products.forEach(product -> product.getBatchQuantities()
            .forEach(batchQuantity -> {
                Batch batch = batchQuantity.getBatch();
                batch.increaseCurrentQuantityBy(batchQuantity.getQuantity());
                batchService.save(batch);
            })
        );

        purchaseOrderItemService.deleteAll(products);
    }
}
