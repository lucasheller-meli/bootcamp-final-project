package com.mercadolibre.bootcamp_g1_final_project.controller;


import com.mercadolibre.bootcamp_g1_final_project.controller.request.BuyerRequest;
import com.mercadolibre.bootcamp_g1_final_project.controller.response.BuyerResponse;
import com.mercadolibre.bootcamp_g1_final_project.services.BuyerService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;

@RestController
@RequestMapping("/api/v1/fresh-products")
public class BuyerController {

    private final BuyerService buyerService;

    public BuyerController(BuyerService buyerService) {
        this.buyerService = buyerService;
    }

    @GetMapping("/buyer/{id}")
    @ResponseStatus(HttpStatus.OK)
    public BuyerResponse getPurchasedItens(@PathVariable("id") Integer buyerId, @RequestBody @NotNull BuyerRequest request) {
        return this.buyerService.getAllPurchasedProducts(buyerId,request.getParameter());
    }
}
