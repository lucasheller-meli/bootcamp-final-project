package com.mercadolibre.bootcamp_g1_final_project.services.impl;

import com.mercadolibre.bootcamp_g1_final_project.controller.response.ProductListResponse;
import com.mercadolibre.bootcamp_g1_final_project.controller.response.ProductsResponse;
import com.mercadolibre.bootcamp_g1_final_project.entities.Product;
import com.mercadolibre.bootcamp_g1_final_project.entities.ProductType;
import com.mercadolibre.bootcamp_g1_final_project.controller.response.BatchListResponse;
import com.mercadolibre.bootcamp_g1_final_project.controller.response.ProductListResponse;
import com.mercadolibre.bootcamp_g1_final_project.controller.response.ProductResponse;
import com.mercadolibre.bootcamp_g1_final_project.entities.*;
import com.mercadolibre.bootcamp_g1_final_project.entities.users.Representative;
import com.mercadolibre.bootcamp_g1_final_project.entities.users.Seller;
import com.mercadolibre.bootcamp_g1_final_project.exceptions.CategoryPerDuedateNotFoundException;
import com.mercadolibre.bootcamp_g1_final_project.exceptions.ListProductPerDuedateNotExistException;
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
import static org.mockito.ArgumentMatchers.any;
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

    private final Product product1 = new Product(1, "Lasanha congelada", seller1, ProductType.FF, 10.0);
    private final Product product2 = new Product(2, "Danone", seller1, ProductType.RF, 5.0);
    private final Product product3 = new Product(3, "Tomate", seller1, ProductType.FS, 3.0);
    private final List<Product> productslist = List.of(product1, product2, product3);

    private final ProductListResponse pr1 = new ProductListResponse(1, "Lasanha congelada");
    private final ProductListResponse pr2 = new ProductListResponse(2, "Danone");
    private final ProductListResponse pr3 = new ProductListResponse(3, "Tomate");

    private final ProductResponse p1 = new ProductResponse(1, "Lasanha congelada", ProductType.FF);
    private final ProductResponse p2 = new ProductResponse(2, "Danone", ProductType.RF);
    private final ProductResponse p3 = new ProductResponse(3, "Tomate", ProductType.RF);

    Representative representative = new Representative(defaultId, defaultEmail, defaultPassword);

    private Authentication authentication() {
        return new UsernamePasswordAuthenticationToken(representative, defaultPassword);
    }

    private final Section section = Section.builder().id(1).name("Fresh").type(ProductType.FS).build();

    LocalDateTime dateTest = LocalDateTime.now();

    private final Batch batch = Batch.builder()
            .id(1)
            .product(product1)
            .sector(section)
            .currentTemperature(30.4F)
            .minimumTemperature(20.2F)
            .initialQuantity(40)
            .currentQuantity(10)
            .dueDate(dateTest)
            .manufacturingDate(LocalDateTime.now())
            .build();

    private final ProductResponse productListResponseExpected = ProductResponse.builder().productId(1).name("Lasanha congelada").type(ProductType.FF).build();

    private final BatchListResponse batchResponseExpected = BatchListResponse.builder()
            .warehouseId(1)
            .sectionId(1)
            .batchNumber(1)
            .product(productListResponseExpected)
            .currentQuantity(10)
            .dueDate(dateTest)
            .build();

    private final Section frozenSection = new Section(1, "frozen", ProductType.FF);
    private final Section freshSection = new Section(2, "fresh", ProductType.FS);
    private final Section refrigeratedSection = new Section(3, "refrigerated", ProductType.RF);

    Warehouse warehouse1 = new Warehouse(1, "algum lugar", "nome 1", List.of(frozenSection, freshSection, refrigeratedSection), List.of(representative), List.of());

    @Test
    void shouldListProduct() {
        //arrange
        String category = null;

        ProductListResponse productsResponsesExpected = new ProductListResponse(List.of(pr1, pr2, pr3));

        //act
        Mockito.when(productRepositoryTest.findAll()).thenReturn(productslist);
        ProductListResponse productsResponseList = productServiceTest.listProducts(category);

        //assert
        assertEquals(productsResponsesExpected.getProducts().size(), productsResponseList.getProducts().size());
        assertEquals(productsResponsesExpected.getProducts().get(0), productsResponseList.getProducts().get(0));
    }

    @Test
    void shouldListProductFresh() {
        //arrange
        String category = "FS";

        ProductListResponse productsResponsesExpected = new ProductListResponse(List.of(pr3));

        //act
        Mockito.when(productRepositoryTest.findAll()).thenReturn(productslist);
        ProductListResponse productsResponseList = productServiceTest.listProducts(category);

        //assert
        assertEquals(productsResponsesExpected.getProducts().size(), productsResponseList.getProducts().size());
        assertEquals(productsResponsesExpected.getProducts().get(0), productsResponseList.getProducts().get(0));
    }

    @Test
    void shouldListProductFrozen() {
        //arrange
        String category = "FF";

        ProductListResponse productsResponsesExpected = new ProductListResponse(List.of(pr1));

        //act
        Mockito.when(productRepositoryTest.findAll()).thenReturn(productslist);
        ProductListResponse productsResponseList = productServiceTest.listProducts(category);

        //assert
        assertEquals(productsResponsesExpected.getProducts().size(), productsResponseList.getProducts().size());
        assertEquals(productsResponsesExpected.getProducts().get(0), productsResponseList.getProducts().get(0));
    }

    @Test
    void shouldListProductRefrigerated() {
        //arrange
        String category = "RF";

        ProductListResponse productsResponsesExpected = new ProductListResponse(List.of(pr2));

        //act
        Mockito.when(productRepositoryTest.findAll()).thenReturn(productslist);
        ProductListResponse productsResponseList = productServiceTest.listProducts(category);
        when(productRepositoryTest.findAll()).thenReturn(productslist);
        List<ProductListResponse> productListResponseList = productServiceTest.listProducts(category);

        //assert
        assertEquals(productsResponsesExpected.getProducts().size(), productsResponseList.getProducts().size());
        assertEquals(productsResponsesExpected.getProducts().get(0), productsResponseList.getProducts().get(0));
        assertEquals(productListResponsesExpected.size(), productListResponseList.size());
        assertEquals(productListResponsesExpected.get(0), productListResponseList.get(0));
    }

    @Test
    void shouldDoesNotExistListProduct() {
        //arrange
        String messageExpected = "Product does not exist.";
        String category = null;

        //act
        when(productRepositoryTest.findAll()).thenThrow(new ProductNotExistException());
        Exception exception = assertThrows(ProductNotExistException.class, () -> {
            productServiceTest.listProducts(category);
        });

        //assert
        assertEquals(messageExpected, exception.getMessage());
    }

    //Product in batch teste
    @Test
    void shouldListProductsInBatch() {
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
    void shouldListProductsInBatchPerQuantity() {
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
    void shouldListProductsInBatchPerDueDate() {
        //arrange
        String order = "F";


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
    void shouldDoesNotProductInBatch() {
        //arrange
        String messageExpected = "There's no linked product in batch";
        String order = null;

        //act
        when(batchServiceTest.findBatchesByProductId(1)).thenThrow(new NotFoundProductInBatch());
        Exception exception = assertThrows(NotFoundProductInBatch.class, () -> {
            productServiceTest.listProductsInBatch(1, order);
        });

        //assert
        assertEquals(messageExpected, exception.getMessage());
    }


    @Test
    void shouldListProductPerDuedate() {
        String category = null;
        //arrange
        LocalDateTime dateTest = LocalDateTime.of(2021, 7, 2, 12, 04, 02);
        List<BatchListResponse> batchListResponseExpected = List.of(batchResponseExpected);
        Batch batch1 = Batch.builder()
                .id(1)
                .product(product1)
                .sector(section)
                .currentTemperature(30.4F)
                .minimumTemperature(20.2F)
                .initialQuantity(40)
                .currentQuantity(10)
                .dueDate(dateTest)
                .manufacturingDate(LocalDateTime.now())
                .build();
        List<Section> sectionList = warehouse1.getSection();

        //act
        securityContextHolder.when(SecurityContextHolder::getContext).thenReturn(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication());

        when(warehouseServiceTest.findByRepresentative(representative)).thenReturn(warehouse1);
        when(batchServiceTest.findBatchesBySectorIn(sectionList)).thenReturn(List.of(batch, batch1));
        List<BatchListResponse> batchListResponseTest = productServiceTest.listProductPerDuedata(3, category);
        //assert

        assertEquals(batchListResponseExpected.size(), batchListResponseTest.size());
    }


    @Test
    void shouldListProductCategoryRF() {
        //arrange
        Integer day = 2;
        String category = "RF";

        Section section2 = Section.builder().id(1).name("Refrigerated").type(ProductType.RF).build();

        List<BatchListResponse> batchListResponseExpected = List.of(batchResponseExpected);

        LocalDateTime dateTest = LocalDateTime.now();

        Batch batch1 = Batch.builder()
                .id(1)
                .product(product2)
                .sector(section2)
                .currentTemperature(30.4F)
                .minimumTemperature(20.2F)
                .initialQuantity(40)
                .currentQuantity(10)
                .dueDate(dateTest)
                .manufacturingDate(LocalDateTime.now())
                .build();
        List<Section> sectionList = warehouse1.getSection();

        //assert
        securityContextHolder.when(SecurityContextHolder::getContext).thenReturn(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication());
        when(warehouseServiceTest.findByRepresentative(representative)).thenReturn(warehouse1);
        when(batchServiceTest.findBatchesBySectorIn(sectionList)).thenReturn(List.of(batch1));
        List<BatchListResponse> batchListResponseTest = productServiceTest.listProductPerDuedata(day, category);

        assertEquals(batchListResponseExpected.size(), batchListResponseTest.size());
    }


    @Test
    void shouldListProductCategoryFS() {
        //arrange
        Integer day = 2;
        String category = "FS";

        Section section1 = Section.builder().id(1).name("Fresh").type(ProductType.FS).build();

        List<BatchListResponse> batchListResponseExpected = List.of(batchResponseExpected);

        LocalDateTime dateTest = LocalDateTime.now();

        Batch batch1 = Batch.builder()
                .id(1)
                .product(product3)
                .sector(section1)
                .currentTemperature(30.4F)
                .minimumTemperature(20.2F)
                .initialQuantity(40)
                .currentQuantity(10)
                .dueDate(dateTest)
                .manufacturingDate(LocalDateTime.now())
                .build();
        List<Section> sectionList = warehouse1.getSection();

        //assert
        securityContextHolder.when(SecurityContextHolder::getContext).thenReturn(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication());
        when(warehouseServiceTest.findByRepresentative(representative)).thenReturn(warehouse1);
        when(batchServiceTest.findBatchesBySectorIn(sectionList)).thenReturn(List.of(batch1));
        List<BatchListResponse> batchListResponseTest = productServiceTest.listProductPerDuedata(day, category);

        assertEquals(batchListResponseExpected.size(), batchListResponseTest.size());
    }


    @Test
    void shouldListProductCategoryFF() {
        //arrange
        Integer day = 2;
        String category = "FF";

        Section section1 = Section.builder().id(1).name("Frozen").type(ProductType.FF).build();

        List<BatchListResponse> batchListResponseExpected = List.of(batchResponseExpected);

        LocalDateTime dateTest = LocalDateTime.now();

        Batch batch1 = Batch.builder()
                .id(1)
                .product(product1)
                .sector(section1)
                .currentTemperature(30.4F)
                .minimumTemperature(20.2F)
                .initialQuantity(40)
                .currentQuantity(10)
                .dueDate(dateTest)
                .manufacturingDate(LocalDateTime.now())
                .build();
        List<Section> sectionList = warehouse1.getSection();

        //assert
        securityContextHolder.when(SecurityContextHolder::getContext).thenReturn(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication());
        when(warehouseServiceTest.findByRepresentative(representative)).thenReturn(warehouse1);
        when(batchServiceTest.findBatchesBySectorIn(sectionList)).thenReturn(List.of(batch1));
        List<BatchListResponse> batchListResponseTest = productServiceTest.listProductPerDuedata(day, category);

        assertEquals(batchListResponseExpected.size(), batchListResponseTest.size());
    }

    @Test
    void shouldDoesNotExistListProductPerDuedate() {
        //arrange
        String messageExpected = "Product not found in this date range.";
        String category = null;
        Integer day = 1;

        LocalDateTime dateTest = LocalDateTime.of(2021, 7, 3, 12, 04, 02);

        Batch batchPerDuedate = Batch.builder()
                .id(1)
                .product(product1)
                .sector(section)
                .currentTemperature(30.4F)
                .minimumTemperature(20.2F)
                .initialQuantity(40)
                .currentQuantity(10)
                .dueDate(dateTest)
                .manufacturingDate(LocalDateTime.now())
                .build();

        //act
        securityContextHolder.when(SecurityContextHolder::getContext).thenReturn(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication());

        when(warehouseServiceTest.findByRepresentative(representative)).thenReturn(warehouse1);
        when(batchServiceTest.findBatchesBySectorIn(any())).thenReturn(List.of(batchPerDuedate));
        Exception exception = assertThrows(ListProductPerDuedateNotExistException.class, () -> {
            productServiceTest.listProductPerDuedata(day, category);
        });

        //assert
        assertEquals(messageExpected, exception.getMessage());
    }

    @Test
    void shouldDoesNotExistListProductCategoryPerDuedate() {
        //arrange
        String messageExpected = "Category not found in this date range.";
        String category = "FF";
        Integer day = 1;


        LocalDateTime dateTest = LocalDateTime.now();

        Batch batchPerDuedateCategory = Batch.builder()
                .id(1)
                .product(product2)
                .sector(section)
                .currentTemperature(30.4F)
                .minimumTemperature(20.2F)
                .initialQuantity(40)
                .currentQuantity(10)
                .dueDate(dateTest)
                .manufacturingDate(LocalDateTime.now())
                .build();

        //act
        securityContextHolder.when(SecurityContextHolder::getContext).thenReturn(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication());

        when(warehouseServiceTest.findByRepresentative(representative)).thenReturn(warehouse1);
        when(batchServiceTest.findBatchesBySectorIn(any())).thenReturn(List.of(batchPerDuedateCategory));
        Exception exception = assertThrows(CategoryPerDuedateNotFoundException.class, () -> {
            productServiceTest.listProductPerDuedata(day, category);
        });

        //assert
        assertEquals(messageExpected, exception.getMessage());

    }
}

