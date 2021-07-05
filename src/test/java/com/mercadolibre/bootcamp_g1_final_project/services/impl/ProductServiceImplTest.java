package com.mercadolibre.bootcamp_g1_final_project.services.impl;

import com.mercadolibre.bootcamp_g1_final_project.controller.response.ProductsResponse;
import com.mercadolibre.bootcamp_g1_final_project.entities.Product;
import com.mercadolibre.bootcamp_g1_final_project.entities.ProductType;
import com.mercadolibre.bootcamp_g1_final_project.entities.users.Seller;
import com.mercadolibre.bootcamp_g1_final_project.exceptions.ProductNotExistException;
import com.mercadolibre.bootcamp_g1_final_project.repositories.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {

    @Mock
    private ProductRepository productRepositoryTest;
    @InjectMocks
    private ProductServiceImpl productServiceTest;

    private final Seller seller1 = new Seller("joao@gmail.com", "1234");

    private final Product product1 = new Product(1, "Lasanha congelada", seller1, ProductType.FF);
    private final Product product2 = new Product(2, "Danone", seller1, ProductType.RF);
    private final Product product3 = new Product(3, "Tomate", seller1, ProductType.FS);
    private final List<Product> productslist = List.of(product1, product2, product3);

    private final ProductsResponse pr1 = new ProductsResponse(1, "Lasanha congelada");
    private final ProductsResponse pr2 = new ProductsResponse(2, "Danone");
    private final ProductsResponse pr3 = new ProductsResponse(3, "Tomate");


    @Test
    void shouldListProduct(){
        //arrange
        String category = null;

        List<ProductsResponse> productsResponsesExpected = List.of(pr1, pr2, pr3);

        //act
        Mockito.when(productRepositoryTest.findAll()).thenReturn(productslist);
        List<ProductsResponse> productsResponseList = productServiceTest.listProducts(category);

        //assert
        assertEquals(productsResponsesExpected.size(), productsResponseList.size());
        assertEquals(productsResponsesExpected.get(0), productsResponseList.get(0));
    }

    @Test
    void shouldListProductFresh(){
        //arrange
        String category = "FS";

        List<ProductsResponse> productsResponsesExpected = List.of(pr3);

        //act
        Mockito.when(productRepositoryTest.findAll()).thenReturn(productslist);
        List<ProductsResponse> productsResponseList = productServiceTest.listProducts(category);

        //assert
        assertEquals(productsResponsesExpected.size(), productsResponseList.size());
        assertEquals(productsResponsesExpected.get(0), productsResponseList.get(0));
    }

    @Test
    void shouldListProductFrozen(){
        //arrange
        String category = "FF";

        List<ProductsResponse> productsResponsesExpected = List.of(pr1);

        //act
        Mockito.when(productRepositoryTest.findAll()).thenReturn(productslist);
        List<ProductsResponse> productsResponseList = productServiceTest.listProducts(category);

        //assert
        assertEquals(productsResponsesExpected.size(), productsResponseList.size());
        assertEquals(productsResponsesExpected.get(0), productsResponseList.get(0));
    }

    @Test
    void shouldListProductRefrigerated(){
        //arrange
        String category = "RF";

        List<ProductsResponse> productsResponsesExpected = List.of(pr2);

        //act
        Mockito.when(productRepositoryTest.findAll()).thenReturn(productslist);
        List<ProductsResponse> productsResponseList = productServiceTest.listProducts(category);

        //assert
        assertEquals(productsResponsesExpected.size(), productsResponseList.size());
        assertEquals(productsResponsesExpected.get(0), productsResponseList.get(0));
    }

    @Test
    void shouldDoesNotExistListProduct(){
        //arrange
        String messageExpected = "Product does not exist.";
        String category = null;

        //act
        Mockito.when(productRepositoryTest.findAll()).thenThrow(new ProductNotExistException());
        Exception exception = assertThrows(ProductNotExistException.class, () -> {productServiceTest.listProducts(category);});

        //assert
        assertEquals(messageExpected, exception.getMessage());
    }

}