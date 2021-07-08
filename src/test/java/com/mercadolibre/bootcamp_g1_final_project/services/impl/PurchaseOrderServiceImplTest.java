package com.mercadolibre.bootcamp_g1_final_project.services.impl;

import com.mercadolibre.bootcamp_g1_final_project.controller.request.PurchaseOrderItemRequest;
import com.mercadolibre.bootcamp_g1_final_project.controller.request.PurchaseOrderRequest;
import com.mercadolibre.bootcamp_g1_final_project.controller.response.PurchaseOrderItemsResponse;
import com.mercadolibre.bootcamp_g1_final_project.controller.response.PurchaseOrderResponse;
import com.mercadolibre.bootcamp_g1_final_project.entities.*;
import com.mercadolibre.bootcamp_g1_final_project.entities.users.Buyer;
import com.mercadolibre.bootcamp_g1_final_project.entities.users.Seller;
import com.mercadolibre.bootcamp_g1_final_project.exceptions.ApiException;
import com.mercadolibre.bootcamp_g1_final_project.repositories.PurchaseOrderRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PurchaseOrderServiceImplTest {
  @InjectMocks
  private PurchaseOrderServiceImpl purchaseOrderService;

  @Mock
  private PurchaseOrderRepository repository;

  @Mock
  private BatchServiceImpl batchService;

  @Mock
  private ProductServiceImpl productService;

  @Mock
  private PurchaseOrderItemServiceImpl purchaseOrderItemService;

  @Mock
  private SecurityContext securityContext;

  private static MockedStatic<SecurityContextHolder> securityContextHolder;

  @BeforeAll
  static void setup() {
    securityContextHolder = mockStatic(SecurityContextHolder.class);
  }

  @Test
  void whenOrderItemsIsCalledWithValidIdItReturnsTheOrderItems() {
    when(repository.findById(defaultId)).thenReturn(Optional.of(purchaseOrder()));

    PurchaseOrderItemsResponse order = purchaseOrderService.orderItems(defaultId);

    assertEquals(defaultId, order.getProducts().get(0).getProductId());
    assertEquals(purchaseQuantity, order.getProducts().get(0).getQuantity());
  }

  @Test
  void whenOrderItemsIsCalledWithNonExistingIdItThrowsException() {
    when(repository.findById(defaultId)).thenReturn(Optional.empty());

    assertThrows(ApiException.class, () -> purchaseOrderService.orderItems(defaultId));
  }

  @Test
  void whenUpdatePurchaseOrderIsCalledWithValidRequestItReturnsTheResponse() {
    when(repository.findById(defaultId)).thenReturn(Optional.of(purchaseOrder()));
    securityContextHolder.when(SecurityContextHolder::getContext).thenReturn(securityContext);
    when(securityContext.getAuthentication()).thenReturn(authentication());
    when(batchService.findBatchesByProductId(defaultId)).thenReturn(batches());
    when(productService.findById(defaultId)).thenReturn(product());
    when(repository.save(any())).thenReturn(savedPurchaseOrder());

    PurchaseOrderResponse response = purchaseOrderService.updatePurchaseOrder(request(), defaultId);

    assertEquals(defaultId, response.getId());
    assertEquals(now, response.getDate());
    assertEquals(defaultId, response.getBuyerId());
    assertEquals(150.0, response.getTotalPrice());

    assertEquals(defaultId, response.getProducts().get(0).getProductId());
    assertEquals(purchaseQuantity, response.getProducts().get(0).getQuantity());
  }

  @Test
  void whenUpdatePurchaseOrderIsCalledWithNonExistingIdItThrowsException() {
    when(repository.findById(defaultId)).thenReturn(Optional.empty());

    assertThrows(ApiException.class, () -> purchaseOrderService.updatePurchaseOrder(request(), defaultId));
  }

  @Test
  void whenUpdatePurchaseOrderIsCalledWithDifferentBuyerItThrowsException() {
    when(repository.findById(defaultId)).thenReturn(Optional.of(purchaseOrder()));
    securityContextHolder.when(SecurityContextHolder::getContext).thenReturn(securityContext);
    when(securityContext.getAuthentication()).thenReturn(differentAuthentication());

    assertThrows(ApiException.class, () ->purchaseOrderService.updatePurchaseOrder(request(), defaultId));
  }

  @Test
  void whenPurchaseOrderIsCalledWithValidRequestItReturnsTheResponse() {
    when(batchService.findBatchesByProductId(defaultId)).thenReturn(batches());
    securityContextHolder.when(SecurityContextHolder::getContext).thenReturn(securityContext);
    when(securityContext.getAuthentication()).thenReturn(authentication());
    when(productService.findById(defaultId)).thenReturn(product());
    when(repository.save(any())).thenReturn(savedPurchaseOrder());

    PurchaseOrderResponse response = purchaseOrderService.purchaseOrder(request());

    assertEquals(defaultId, response.getId());
    assertEquals(now, response.getDate());
    assertEquals(defaultId, response.getBuyerId());
    assertEquals(150.0, response.getTotalPrice());

    assertEquals(defaultId, response.getProducts().get(0).getProductId());
    assertEquals(purchaseQuantity, response.getProducts().get(0).getQuantity());
  }

  @Test
  void
  whenPurchaseOrderIsCalledWithProductWithoutEnoughStockItThrowsException() {
    when(batchService.findBatchesByProductId(defaultId)).thenReturn(List.of());

    ApiException exception = assertThrows(ApiException.class, () -> purchaseOrderService.purchaseOrder(request()));

    assertEquals("Product with id " + defaultId + " does not have " + 15 + " units available", exception.getMessage());
    assertEquals(HttpStatus.BAD_REQUEST.name(), exception.getCode());
    assertEquals(HttpStatus.BAD_REQUEST.value(), exception.getStatusCode());
  }

  private PurchaseOrder purchaseOrder() {
    return new PurchaseOrder(
        defaultId,
        buyer(),
        List.of(purchaseOrderItem()),
        now
    );
  }

  private List<Batch> batches() {
    return List.of(lowerQuantityDefaultBatch(), defaultBatch());
  }

  private Batch defaultBatch() {
    return new Batch(
        defaultId,
        product(),
        sector(),
        defaultMinimumTemperature,
        defaultCurrentTemperature,
        defaultQuantity,
        defaultQuantity,
        defaultDueDate,
        defaultManufacturingTime
    );
  }

  private Batch lowerQuantityDefaultBatch() {
    return new Batch(
        defaultId,
        product(),
        sector(),
        defaultMinimumTemperature,
        defaultCurrentTemperature,
        lowerDefaultQuantity,
        lowerDefaultQuantity,
        defaultDueDate,
        defaultManufacturingTime
    );
  }

  private PurchaseOrder savedPurchaseOrder() {
    return new PurchaseOrder(
        defaultId,
        buyer(),
        List.of(purchaseOrderItem()),
        now
    );
  }

  private PurchaseOrderRequest request() {
    return new PurchaseOrderRequest(
        List.of(new PurchaseOrderItemRequest(product().getId(), purchaseQuantity))
    );
  }

  private PurchaseOrderItem purchaseOrderItem() {
    return new PurchaseOrderItem(
        defaultId,
        product(),
        purchaseQuantity,
        List.of(
            new BatchQuantity(
                defaultId,
                lowerQuantityDefaultBatch(),
                lowerDefaultQuantity
            ),
            new BatchQuantity(
                defaultId,
                defaultBatch(),
                purchaseQuantity - lowerDefaultQuantity
            )
        )
    );
  }

  private Product product() {
    return new Product(defaultId, "product", new Seller(), ProductType.FF, 10.0);
  }

  private Section sector() {
    return new Section(defaultId, "section", ProductType.FF);
  }

  private Authentication authentication() {
    return new UsernamePasswordAuthenticationToken(buyer(), defaultPassword);
  }

  private Buyer buyer() {
    return new Buyer(defaultId, defaultEmail, defaultPassword);
  }

  private Authentication differentAuthentication() {
    return new UsernamePasswordAuthenticationToken(differentBuyer(), defaultPassword);
  }

  private Buyer differentBuyer() {
    return new Buyer(defaultId + 1, defaultEmail, defaultPassword);
  }

  private final String defaultEmail = "email@email.com";
  private final String defaultPassword = "1234";

  private final Integer defaultId = 1;
  private final Integer defaultQuantity = 10;
  private final Integer lowerDefaultQuantity = 9;
  private final Float defaultMinimumTemperature = 15.5F;
  private final Float defaultCurrentTemperature = 20.4F;
  private final LocalDateTime defaultManufacturingTime = LocalDateTime.now().minusWeeks(4);
  private final LocalDateTime defaultDueDate = LocalDateTime.now().plusWeeks(4);

  private final LocalDate now = LocalDate.now();

  private final Integer purchaseQuantity = 15;
}