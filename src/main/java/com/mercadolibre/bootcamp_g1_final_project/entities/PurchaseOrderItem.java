package com.mercadolibre.bootcamp_g1_final_project.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PurchaseOrderItem {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @ManyToOne
  private Product product;

  private Integer quantity;

  @OneToMany(cascade = CascadeType.ALL)
  private List<BatchQuantity> batchQuantities;

  public PurchaseOrderItem(Product product, Integer quantity, List<BatchQuantity> batchQuantities) {
    this.product = product;
    this.quantity = quantity;
    this.batchQuantities = batchQuantities;
  }
}
