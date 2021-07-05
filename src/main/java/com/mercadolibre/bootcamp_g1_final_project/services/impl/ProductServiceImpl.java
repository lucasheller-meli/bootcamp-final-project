package com.mercadolibre.bootcamp_g1_final_project.services.impl;

import com.mercadolibre.bootcamp_g1_final_project.controller.response.ProductsResponse;
import com.mercadolibre.bootcamp_g1_final_project.entities.Product;
import com.mercadolibre.bootcamp_g1_final_project.entities.ProductType;
import com.mercadolibre.bootcamp_g1_final_project.exceptions.ProductNotExistException;
import com.mercadolibre.bootcamp_g1_final_project.repositories.ProductRepository;
import com.mercadolibre.bootcamp_g1_final_project.services.ProductService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public Product findById(Integer id) throws ProductNotExistException{
        return productRepository.findById(id).orElseThrow(ProductNotExistException::new);
    }

    public List<ProductsResponse> listProducts(String category) throws ProductNotExistException {
        List<Product> products = (List<Product>) productRepository.findAll();

        if(products.isEmpty()) throw new ProductNotExistException();

        List<ProductsResponse> productsResponse = new ArrayList<>();

        for (Product p : products){
            ProductType type = p.getType();

            if(category != null){
                if (type.toString().compareTo(category) == 0){
                    ProductsResponse pr = new ProductsResponse(p.getId(),p.getName());
                    productsResponse.add(pr);
                }
            } else {
                ProductsResponse pr = new ProductsResponse(p.getId(),p.getName());
                productsResponse.add(pr);
            }
        }

        return productsResponse;

    }

}
