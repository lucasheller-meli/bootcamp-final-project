package com.mercadolibre.bootcamp_g1_final_project;

import com.mercadolibre.bootcamp_g1_final_project.entities.*;
import com.mercadolibre.bootcamp_g1_final_project.entities.users.Buyer;
import com.mercadolibre.bootcamp_g1_final_project.entities.users.Representative;
import com.mercadolibre.bootcamp_g1_final_project.entities.users.Seller;
import com.mercadolibre.bootcamp_g1_final_project.repositories.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Profile("local")
@Component
public class PopulateDatabase implements CommandLineRunner {
    private final UserRepository userRepository;
    private final WarehouseRepository warehouseRepository;
    private final ProductRepository productRepository;
    private final SectionRepository sectionRepository;
    private final InboundOrderRepository inboundOrderRepository;
    private final PurchaseOrderRepository purchaseOrderRepository;
    private final PasswordEncoder encoder;

    public PopulateDatabase(UserRepository userRepository, WarehouseRepository warehouseRepository, ProductRepository productRepository, SectionRepository sectionRepository, InboundOrderRepository inboundOrderRepository, PurchaseOrderRepository purchaseOrderRepository, PasswordEncoder encoder) {
        this.userRepository = userRepository;
        this.warehouseRepository = warehouseRepository;
        this.productRepository = productRepository;
        this.sectionRepository = sectionRepository;
        this.inboundOrderRepository = inboundOrderRepository;
        this.purchaseOrderRepository = purchaseOrderRepository;
        this.encoder = encoder;
    }

    public void run(String... args) {
        System.out.println("Inserting data...");
        Seller seller = userRepository.save(new Seller("seller@email.com", encoder.encode("1234")));
        Buyer buyer = userRepository.save(new Buyer("buyer@email.com", encoder.encode("1234")));
        Representative representative = userRepository.save(new Representative("representative@email.com", encoder.encode("1234")));

        Section frozenSection = sectionRepository.save(new Section(null, "frozen", ProductType.FF));
        Section freshSection = sectionRepository.save(new Section(null, "fresh", ProductType.FS));
        Section refrigeratedSection = sectionRepository.save(new Section(null, "refrigerated", ProductType.RF));

        Warehouse warehouse = warehouseRepository.save(new Warehouse(null, "algum lugar", "nome 1", List.of(frozenSection, freshSection, refrigeratedSection), List.of(representative), List.of()));
        warehouseRepository.save(new Warehouse(null, "outro lugar", "nome 2", List.of(frozenSection, freshSection, refrigeratedSection), List.of(), List.of()));

        Product lasanha = productRepository.save(new Product(null, "lasanha congelada", seller, ProductType.FF, 22.22));
        Product laranja = productRepository.save(new Product(null, "laranja", seller, ProductType.FS, 15.6));
        Product leite = productRepository.save(new Product(null, "leite", seller, ProductType.RF, 134.2));
        Batch batchLasanha = new Batch(null, lasanha, frozenSection, 10.0F, 5.0F, 20, 20, LocalDateTime.now().plusWeeks(5), LocalDateTime.now());
        Batch batchLaranja = new Batch(null, laranja, freshSection, 20.0F, 15.0F, 20, 20, LocalDateTime.now().plusWeeks(5), LocalDateTime.now());
        Batch batchLeite = new Batch(null, leite, freshSection, 18.0F, 10.0F, 20, 20, LocalDateTime.now().plusWeeks(5), LocalDateTime.now());
        inboundOrderRepository.saveAndFlush(new InboundOrder(null,warehouse, List.of(batchLasanha, batchLaranja, batchLeite), representative, LocalDateTime.now()));
        purchaseOrderRepository.saveAndFlush(new PurchaseOrder(null,buyer,List.of(new PurchaseOrderItem(lasanha,18, List.of(new BatchQuantity(batchLasanha,18))), new PurchaseOrderItem(laranja,4, List.of(new BatchQuantity(batchLaranja, 4))), new PurchaseOrderItem(leite,1, List.of(new BatchQuantity(batchLeite,1))))));
        purchaseOrderRepository.saveAndFlush(new PurchaseOrder(null,buyer,List.of(new PurchaseOrderItem(leite,2, List.of(new BatchQuantity(batchLeite,2))), new PurchaseOrderItem(laranja,5, List.of(new BatchQuantity(batchLaranja,5))))));

    }
}
