package org.leroyjenkins.paymenttestapp.controller;

import org.leroyjenkins.paymenttestapp.dto.CalculatePriceRequest;
import org.leroyjenkins.paymenttestapp.dto.MakePurchaseRequest;
import org.leroyjenkins.paymenttestapp.service.PaymentWebService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping
public class PaymentController {
    @Autowired
    private PaymentWebService paymentWebService;

    @PostMapping(value = "/calculate-price",
            consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(code = HttpStatus.OK)
    public void calculatePrice(@RequestBody CalculatePriceRequest calculatePriceRequest) {
        paymentWebService.calculatePrice(calculatePriceRequest);
    }

    @PostMapping(value = "/purchase",
            consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(code = HttpStatus.OK)
    public void makePurchase(@RequestBody MakePurchaseRequest makePurchaseRequest) {
        paymentWebService.makePurchase(makePurchaseRequest);
    }
}
