package com.mercadolibre.bootcamp_g1_final_project.entities;

import javax.persistence.*;

@Entity
public class BatchQuantity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @ManyToOne
  private Batch batch;

  private Integer quantity;

  public BatchQuantity(Integer id, Batch batch, Integer quantity) {
    this.id = id;
    this.batch = batch;
    this.quantity = quantity;
  }

  public BatchQuantity(Batch batch, Integer quantity) {
    this.batch = batch;
    this.quantity = quantity;
  }

  public BatchQuantity() {
  }

  public Integer getId() {
    return id;
  }

  public Batch getBatch() {
    return batch;
  }

  public Integer getQuantity() {
    return quantity;
  }
}
