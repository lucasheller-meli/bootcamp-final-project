package com.mercadolibre.bootcamp_g1_final_project.services.impl;

import com.mercadolibre.bootcamp_g1_final_project.entities.*;
import com.mercadolibre.bootcamp_g1_final_project.entities.users.Seller;
import com.mercadolibre.bootcamp_g1_final_project.repositories.PurchaseOrderItemRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@ExtendWith(MockitoExtension.class)
public class PurchaseOrderItemServiceImplTest {
  @InjectMocks
  private PurchaseOrderItemServiceImpl service;

  @Mock
  private PurchaseOrderItemRepository repository;

  @Test
  void whenDeleteAllIsCalledWithOrderItemsItDeletesTheThem() {
    assertDoesNotThrow(() -> service.deleteAll(orderItems()));
  }

  private List<PurchaseOrderItem> orderItems() {
    return List.of(
        new PurchaseOrderItem(
            defaultId,
            product(),
            defaultQuantity,
            List.of(new BatchQuantity(defaultId, batch(), defaultQuantity))
        )
    );
  }

  private Product product() {
    return new Product(defaultId, "produto", new Seller(), ProductType.FF, 10.0);
  }

  private Batch batch() {
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

  private Section sector() {
    return new Section(defaultId, "section", ProductType.FF);
  }

  private final Integer defaultId = 1;
  private final Integer defaultQuantity = 10;
  private final Float defaultMinimumTemperature = 15.5F;
  private final Float defaultCurrentTemperature = 20.4F;
  private final LocalDateTime defaultManufacturingTime = LocalDateTime.now().minusWeeks(4);
  private final LocalDateTime defaultDueDate = LocalDateTime.now().plusWeeks(4);
}