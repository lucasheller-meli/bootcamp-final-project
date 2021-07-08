package com.mercadolibre.bootcamp_g1_final_project.integration;

import com.mercadolibre.bootcamp_g1_final_project.controller.response.ProductListResponse;
import com.mercadolibre.bootcamp_g1_final_project.entities.Product;
import com.mercadolibre.bootcamp_g1_final_project.entities.ProductType;
import com.mercadolibre.bootcamp_g1_final_project.entities.users.Seller;
import com.mercadolibre.bootcamp_g1_final_project.repositories.ProductRepository;
import com.mercadolibre.bootcamp_g1_final_project.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.DirtiesContext;

import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class ProductControllerTest extends ControllerTest {
  private final ProductRepository productRepository;
  private final UserRepository userRepository;
  private final PasswordEncoder encoder;

  private final String productsUrl = "/api/v1/fresh-products/list";

  private Product cheapFrozen;
  private Product fresh;
  private Product refrigerated;
  private Product expensiveFrozen;

  @Autowired
  public ProductControllerTest(ProductRepository productRepository, UserRepository userRepository, PasswordEncoder encoder) {
    this.productRepository = productRepository;
    this.userRepository = userRepository;
    this.encoder = encoder;
  }

  @BeforeEach
  void setup() {
    Seller seller = userRepository.save(new Seller("seller@email.com", encoder.encode("1234")));
    cheapFrozen = new Product(null, "cheap frozen", seller, ProductType.FF, 2.4);
    fresh = new Product(null, "fresh", seller, ProductType.FS, 9.6);
    refrigerated = new Product(null, "refrigerated", seller, ProductType.RF, 12.8);
    expensiveFrozen = new Product(null, "expensive frozen", seller, ProductType.FF, 22.8);

    productRepository.saveAll(List.of(fresh, cheapFrozen, refrigerated, expensiveFrozen));
  }

  @Test
  void whenProductsIsCalledItReturnsAListOfAllProducts() {
    ResponseEntity<ProductListResponse> response = this.testRestTemplate.getForEntity(productsUrl, ProductListResponse.class);

    assertEquals(HttpStatus.OK, response.getStatusCode());

    ProductListResponse body = Objects.requireNonNull(response.getBody());
    assertEquals(4, body.getProducts().size());

    assertEquals(fresh.getId(), body.getProducts().get(0).getId());
    assertEquals(fresh.getName(), body.getProducts().get(0).getName());

    assertEquals(cheapFrozen.getId(), body.getProducts().get(1).getId());
    assertEquals(cheapFrozen.getName(), body.getProducts().get(1).getName());

    assertEquals(refrigerated.getId(), body.getProducts().get(2).getId());
    assertEquals(refrigerated.getName(), body.getProducts().get(2).getName());

    assertEquals(expensiveFrozen.getId(), body.getProducts().get(3).getId());
    assertEquals(expensiveFrozen.getName(), body.getProducts().get(3).getName());
  }

  @Test
  void whenProductsIsCalledWithProductTypeItReturnsAListOfSelectedTypeProducts() {
    ResponseEntity<ProductListResponse> response = this.testRestTemplate.getForEntity(productsUrl + "?category=" + ProductType.FF, ProductListResponse.class);

    assertEquals(HttpStatus.OK, response.getStatusCode());

    ProductListResponse body = Objects.requireNonNull(response.getBody());
    assertEquals(2, body.getProducts().size());

    assertEquals(cheapFrozen.getId(), body.getProducts().get(0).getId());
    assertEquals(cheapFrozen.getName(), body.getProducts().get(0).getName());

    assertEquals(expensiveFrozen.getId(), body.getProducts().get(1).getId());
    assertEquals(expensiveFrozen.getName(), body.getProducts().get(1).getName());
  }
}
