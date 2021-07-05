package com.mercadolibre.bootcamp_g1_final_project;


import com.mercadolibre.bootcamp_g1_final_project.entities.Product;
import com.mercadolibre.bootcamp_g1_final_project.entities.ProductType;
import com.mercadolibre.bootcamp_g1_final_project.entities.Section;
import com.mercadolibre.bootcamp_g1_final_project.entities.Warehouse;
import com.mercadolibre.bootcamp_g1_final_project.entities.users.Buyer;
import com.mercadolibre.bootcamp_g1_final_project.entities.users.Representative;
import com.mercadolibre.bootcamp_g1_final_project.entities.users.Seller;
import com.mercadolibre.bootcamp_g1_final_project.repositories.ProductRepository;
import com.mercadolibre.bootcamp_g1_final_project.repositories.SectionRepository;
import com.mercadolibre.bootcamp_g1_final_project.repositories.UserRepository;
import com.mercadolibre.bootcamp_g1_final_project.repositories.WarehouseRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PopulateDatabase implements CommandLineRunner {
    private final UserRepository userRepository;
    private final WarehouseRepository warehouseRepository;
    private final ProductRepository productRepository;
    private final SectionRepository sectionRepository;
    private final PasswordEncoder encoder;

    public PopulateDatabase(UserRepository userRepository, WarehouseRepository warehouseRepository, ProductRepository productRepository, SectionRepository sectionRepository, PasswordEncoder encoder) {
        this.userRepository = userRepository;
        this.warehouseRepository = warehouseRepository;
        this.productRepository = productRepository;
        this.sectionRepository = sectionRepository;
        this.encoder = encoder;
    }

    public void run(String... args) {
        System.out.println("Inserting data...");
//        Seller seller = userRepository.save(new Seller("seller@email.com", encoder.encode("1234")));
//        Buyer buyer = userRepository.save(new Buyer("buyer@email.com", encoder.encode("1234")));
//        Representative representative = userRepository.save(new Representative("representative@email.com", encoder.encode("1234")));
//
//        Section frozenSection = sectionRepository.save(new Section(null, "frozen", ProductType.FF));
//        Section freshSection = sectionRepository.save(new Section(null, "fresh", ProductType.FS));
//        Section refrigeratedSection = sectionRepository.save(new Section(null, "refrigerated", ProductType.RF));
//
//        warehouseRepository.save(new Warehouse(null, "algum lugar", "nome 1", List.of(frozenSection, freshSection, refrigeratedSection), List.of(representative)));
//        warehouseRepository.save(new Warehouse(null, "outro lugar", "nome 2", List.of(frozenSection, freshSection, refrigeratedSection), List.of()));
//
//        productRepository.save(new Product(null, "lasanha congelada", seller, ProductType.FF, 22.22));
//        productRepository.save(new Product(null, "laranja", seller, ProductType.FS, 15.6));
//        productRepository.save(new Product(null, "leite", seller, ProductType.RF, 134.2));
    }
}
