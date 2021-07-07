package com.mercadolibre.bootcamp_g1_final_project.repositories;

import com.mercadolibre.bootcamp_g1_final_project.entities.Section;
import com.mercadolibre.bootcamp_g1_final_project.entities.Warehouse;
import com.mercadolibre.bootcamp_g1_final_project.entities.users.Representative;
import com.mercadolibre.bootcamp_g1_final_project.controller.response.WarehouseProductResponse;
import com.mercadolibre.bootcamp_g1_final_project.entities.Warehouse;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface WarehouseRepository extends CrudRepository<Warehouse, Integer> {

    Warehouse findWarehousesByRepresentatives(Representative representative);

    @Query(value = "select sum(b.current_quantity) as count, w.warehouse_id as warehouseId from batch as b  INNER JOIN warehouse_section as ws on b.sector_id = ws.section_id INNER JOIN warehouse as w on w.warehouse_id = ws.warehouse_warehouse_id where b.product_id = :productId and b.current_quantity > 0 group by warehouse_id;",nativeQuery = true)
    List<WarehouseProductResponse> findWarehouseByProductId(Integer productId);

}
