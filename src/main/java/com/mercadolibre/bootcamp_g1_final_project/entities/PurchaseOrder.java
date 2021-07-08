package com.mercadolibre.bootcamp_g1_final_project.entities;

import com.mercadolibre.bootcamp_g1_final_project.entities.users.Buyer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PurchaseOrder {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @ManyToOne
  private Buyer buyer;

  @OneToMany(cascade = CascadeType.ALL)
  private List<PurchaseOrderItem> products;

  @CreationTimestamp
  private LocalDate orderDate;

  public PurchaseOrder(Integer id, Buyer buyer, List<PurchaseOrderItem> products) {
    this.id = id;
    this.buyer = buyer;
    this.products = products;
  }

  public PurchaseOrder(Buyer buyer, List<PurchaseOrderItem> products) {
    this.buyer = buyer;
    this.products = products;
  }
}
