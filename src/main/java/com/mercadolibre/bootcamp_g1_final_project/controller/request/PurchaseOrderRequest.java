package com.mercadolibre.bootcamp_g1_final_project.controller.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PurchaseOrderRequest {
  @NotEmpty
  @Valid
  private List<PurchaseProductRequest> products;
}
