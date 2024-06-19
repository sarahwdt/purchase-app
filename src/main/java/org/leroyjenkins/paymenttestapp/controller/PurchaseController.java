package org.leroyjenkins.paymenttestapp.controller;

import lombok.RequiredArgsConstructor;
import org.leroyjenkins.paymenttestapp.dto.CalculatePriceRequest;
import org.leroyjenkins.paymenttestapp.dto.CalculatePriceResponse;
import org.leroyjenkins.paymenttestapp.dto.MakePurchaseRequest;
import org.leroyjenkins.paymenttestapp.service.PurchaseWebService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class PurchaseController {
    private final PurchaseWebService purchaseWebService;

    @PostMapping(value = "/calculate-price",
            consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(code = HttpStatus.OK)
    public CalculatePriceResponse calculatePrice(@RequestBody CalculatePriceRequest calculatePriceRequest) {
        return purchaseWebService.calculatePrice(calculatePriceRequest);
    }

    @PostMapping(value = "/purchase",
            consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(code = HttpStatus.OK)
    public void makePurchase(@RequestBody MakePurchaseRequest makePurchaseRequest) {
        purchaseWebService.makePurchase(makePurchaseRequest);
    }
}
