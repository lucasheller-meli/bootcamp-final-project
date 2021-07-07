package com.mercadolibre.bootcamp_g1_final_project.services.impl;

import com.mercadolibre.bootcamp_g1_final_project.controller.response.BatchListResponse;
import com.mercadolibre.bootcamp_g1_final_project.controller.response.ProductListResponse;
import com.mercadolibre.bootcamp_g1_final_project.controller.response.ProductResponse;
import com.mercadolibre.bootcamp_g1_final_project.entities.*;
import com.mercadolibre.bootcamp_g1_final_project.entities.users.Representative;
import com.mercadolibre.bootcamp_g1_final_project.entities.users.Seller;
import com.mercadolibre.bootcamp_g1_final_project.exceptions.NotFoundProductInBatch;
import com.mercadolibre.bootcamp_g1_final_project.exceptions.ProductNotExistException;
import com.mercadolibre.bootcamp_g1_final_project.repositories.ProductRepository;
import com.mercadolibre.bootcamp_g1_final_project.repositories.SectionRepository;
import com.mercadolibre.bootcamp_g1_final_project.repositories.WarehouseRepository;
import com.mercadolibre.bootcamp_g1_final_project.services.WarehouseService;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {

    @Mock
    private SecurityContext securityContext;
    private static MockedStatic<SecurityContextHolder> securityContextHolder;
    private final Integer defaultId = 1;
    private final String defaultEmail = "email@email.com";
    private final String defaultPassword = "1234";

    @BeforeAll
    static void setup() {
        securityContextHolder = mockStatic(SecurityContextHolder.class);
    }

    @AfterAll
    static void close() {
        securityContextHolder.close();
    }

    @Mock
    private ProductRepository productRepositoryTest;
    @Mock
    private WarehouseService warehouseServiceTest;
    @Mock
    private WarehouseRepository warehouseRepositoryTest;
    @Mock
    private SectionRepository sectionRepositoryTest;
    @Mock
    private BatchServiceImpl batchServiceTest;
    @InjectMocks
    private ProductServiceImpl productServiceTest;

    private final Seller seller1 = new Seller("joao@gmail.com", "1234");

    private final Product product1 = new Product(1, "Lasanha congelada", seller1, ProductType.FF,10.0);
    private final Product product2 = new Product(2, "Danone", seller1, ProductType.RF,5.0);
    private final Product product3 = new Product(3, "Tomate", seller1, ProductType.FS,3.0);
    private final List<Product> productslist = List.of(product1, product2, product3);

    private final ProductListResponse pr1 = new ProductListResponse(1, "Lasanha congelada");
    private final ProductListResponse pr2 = new ProductListResponse(2, "Danone");
    private final ProductListResponse pr3 = new ProductListResponse(3, "Tomate");

    Representative representative = new Representative(defaultId, defaultEmail, defaultPassword);

    private Authentication authentication() {
        return new UsernamePasswordAuthenticationToken(representative, defaultPassword);
    }

    private final Section section = Section.builder().id(1).name("Fresh").type(ProductType.FS).build();

    private final Batch batch = Batch.builder()
            .id(1)
            .product(product1)
            .sector(section)
            .currentTemperature(30.4F)
            .minimumTemperature(20.2F)
            .initialQuantity(40)
            .currentQuantity(10)
            .dueDate(LocalDate.now())
            .manufacturingDate(LocalDateTime.now())
            .build();

    private final ProductResponse productListResponseExpected = ProductResponse.builder().productId(1).name("Lasanha congelada").type(ProductType.FF).build();

    private final BatchListResponse batchResponseExpected = BatchListResponse.builder()
            .warehouseId(1)
            .sectionId(1)
            .batchNumber(1)
            .product(productListResponseExpected)
            .currentQuantity(10)
            .dueDate(LocalDate.now())
            .build();

    private final Section frozenSection = new Section(1, "frozen", ProductType.FF);
    private final Section freshSection = new Section(2, "fresh", ProductType.FS);
    private final Section refrigeratedSection = new Section(3, "refrigerated", ProductType.RF);

    Warehouse warehouse1 = new Warehouse(1, "algum lugar", "nome 1", List.of(frozenSection, freshSection, refrigeratedSection), List.of(representative), List.of());

    @Test
    void shouldListProduct(){
        //arrange
        String category = null;

        List<ProductListResponse> productListResponsesExpected = List.of(pr1, pr2, pr3);

        //act
        when(productRepositoryTest.findAll()).thenReturn(productslist);
        List<ProductListResponse> productListResponseList = productServiceTest.listProducts(category);

        //assert
        assertEquals(productListResponsesExpected.size(), productListResponseList.size());
        assertEquals(productListResponsesExpected.get(0), productListResponseList.get(0));
    }

    @Test
    void shouldListProductFresh(){
        //arrange
        String category = "FS";

        List<ProductListResponse> productListResponsesExpected = List.of(pr3);

        //act
        when(productRepositoryTest.findAll()).thenReturn(productslist);
        List<ProductListResponse> productListResponseList = productServiceTest.listProducts(category);

        //assert
        assertEquals(productListResponsesExpected.size(), productListResponseList.size());
        assertEquals(productListResponsesExpected.get(0), productListResponseList.get(0));
    }

    @Test
    void shouldListProductFrozen(){
        //arrange
        String category = "FF";

        List<ProductListResponse> productListResponsesExpected = List.of(pr1);

        //act
        when(productRepositoryTest.findAll()).thenReturn(productslist);
        List<ProductListResponse> productListResponseList = productServiceTest.listProducts(category);

        //assert
        assertEquals(productListResponsesExpected.size(), productListResponseList.size());
        assertEquals(productListResponsesExpected.get(0), productListResponseList.get(0));
    }

    @Test
    void shouldListProductRefrigerated(){
        //arrange
        String category = "RF";

        List<ProductListResponse> productListResponsesExpected = List.of(pr2);

        //act
        when(productRepositoryTest.findAll()).thenReturn(productslist);
        List<ProductListResponse> productListResponseList = productServiceTest.listProducts(category);

        //assert
        assertEquals(productListResponsesExpected.size(), productListResponseList.size());
        assertEquals(productListResponsesExpected.get(0), productListResponseList.get(0));
    }

    @Test
    void shouldDoesNotExistListProduct(){
        //arrange
        String messageExpected = "Product does not exist.";
        String category = null;

        //act
        when(productRepositoryTest.findAll()).thenThrow(new ProductNotExistException());
        Exception exception = assertThrows(ProductNotExistException.class, () -> {productServiceTest.listProducts(category);});

        //assert
        assertEquals(messageExpected, exception.getMessage());
    }

    //Product in batch teste
    @Test
    void shouldListProductsInBatch(){
        //arrange
        String order = null;

        List<Batch> listBatchTest = List.of(batch);
        List<BatchListResponse> batchListResponseExpected = List.of(batchResponseExpected);

        //act
        securityContextHolder.when(SecurityContextHolder::getContext).thenReturn(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication());

        when(warehouseServiceTest.findByRepresentative(representative)).thenReturn(warehouse1);
        when(batchServiceTest.findBatchesByProductId(1)).thenReturn(listBatchTest);
        List<BatchListResponse> batchListResponseTest = productServiceTest.listProductsInBatch(1, order);

        //assert
        assertEquals(batchListResponseExpected.get(0), batchListResponseTest.get(0));
        assertEquals(batchListResponseExpected.size(), batchListResponseTest.size());
    }

    @Test
    void shouldListProductsInBatchPerQuantity(){
        //arrange
        String order = "C";

        Batch batch1 = Batch.builder()
                .id(1)
                .product(product1)
                .sector(section)
                .currentTemperature(30.4F)
                .minimumTemperature(20.2F)
                .initialQuantity(40)
                .currentQuantity(30)
                .dueDate(LocalDate.now())
                .manufacturingDate(LocalDateTime.now())
                .build();

        List<Batch> listBatchTest = List.of(batch, batch1);

        BatchListResponse batchResponseExpected1 = BatchListResponse.builder()
                .warehouseId(1)
                .sectionId(1)
                .batchNumber(1)
                .product(productListResponseExpected)
                .currentQuantity(30)
                .dueDate(LocalDate.now())
                .build();

        List<BatchListResponse> batchListResponseExpected = List.of(batchResponseExpected, batchResponseExpected1);

        //act
        securityContextHolder.when(SecurityContextHolder::getContext).thenReturn(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication());

        when(warehouseServiceTest.findByRepresentative(representative)).thenReturn(warehouse1);
        when(batchServiceTest.findBatchesByProductId(1)).thenReturn(listBatchTest);
        List<BatchListResponse> batchListResponseTest = productServiceTest.listProductsInBatch(1, order);

        //assert
        assertEquals(batchListResponseExpected.get(0), batchListResponseTest.get(0));
        assertEquals(batchListResponseExpected.size(), batchListResponseTest.size());
    }

    @Test
    void shouldListProductsInBatchPerDueDate(){
        //arrange
        String order = "F";
        LocalDate dateTest = LocalDate.of(2021, 7, 7);

        Batch batch1 = Batch.builder()
                .id(1)
                .product(product1)
                .sector(section)
                .currentTemperature(30.4F)
                .minimumTemperature(20.2F)
                .initialQuantity(40)
                .currentQuantity(30)
                .dueDate(dateTest)
                .manufacturingDate(LocalDateTime.now())
                .build();

        List<Batch> listBatchTest = List.of(batch, batch1);

        BatchListResponse batchResponseExpected1 = BatchListResponse.builder()
                .warehouseId(1)
                .sectionId(1)
                .batchNumber(1)
                .product(productListResponseExpected)
                .currentQuantity(30)
                .dueDate(dateTest)
                .build();

        List<BatchListResponse> batchListResponseExpected = List.of(batchResponseExpected, batchResponseExpected1);

        //act
        securityContextHolder.when(SecurityContextHolder::getContext).thenReturn(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication());

        when(warehouseServiceTest.findByRepresentative(representative)).thenReturn(warehouse1);
        when(batchServiceTest.findBatchesByProductId(1)).thenReturn(listBatchTest);
        List<BatchListResponse> batchListResponseTest = productServiceTest.listProductsInBatch(1, order);

        //assert
        assertEquals(batchListResponseExpected.get(0), batchListResponseTest.get(0));
        assertEquals(batchListResponseExpected.size(), batchListResponseTest.size());
    }

    @Test
    void shouldDoesNotProductInBatch(){
        //arrange
        String messageExpected = "There's no linked product in batch";
        String order = null;

        //act
        when(batchServiceTest.findBatchesByProductId(1)).thenThrow(new NotFoundProductInBatch());
        Exception exception = assertThrows(NotFoundProductInBatch.class, () -> {productServiceTest.listProductsInBatch(1, order);});

        //assert
        assertEquals(messageExpected, exception.getMessage());
    }
}
