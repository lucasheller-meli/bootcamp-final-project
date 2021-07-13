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

//@Profile("test")
@Component
public class PopulateDatabase implements CommandLineRunner {
    private final UserRepository userRepository;
    private final WarehouseRepository warehouseRepository;
    private final ProductRepository productRepository;
    private final SectionRepository sectionRepository;
    private final InboundOrderRepository inboundOrderRepository;
    private final BatchRepository batchRepository;
    private final PasswordEncoder encoder;

    public PopulateDatabase(UserRepository userRepository, WarehouseRepository warehouseRepository, ProductRepository productRepository, SectionRepository sectionRepository, InboundOrderRepository inboundOrderRepository, BatchRepository batchRepository, PasswordEncoder encoder) {
        this.userRepository = userRepository;
        this.warehouseRepository = warehouseRepository;
        this.productRepository = productRepository;
        this.sectionRepository = sectionRepository;
        this.inboundOrderRepository = inboundOrderRepository;
        this.batchRepository = batchRepository;
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
        productRepository.save(new Product(null, "laranja", seller, ProductType.FS, 15.6));
        productRepository.save(new Product(null, "leite", seller, ProductType.RF, 134.2));

        inboundOrderRepository.save(new InboundOrder(987,warehouse, List.of(new Batch(987,lasanha, frozenSection, 10.0F, 5.0F,10,10, LocalDateTime.now(), LocalDateTime.now())), representative, LocalDateTime.now()));

    }
}
