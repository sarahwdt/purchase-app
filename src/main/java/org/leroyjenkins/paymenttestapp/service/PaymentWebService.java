package org.leroyjenkins.paymenttestapp.service;

import org.leroyjenkins.paymenttestapp.dto.CalculatePriceRequest;
import org.leroyjenkins.paymenttestapp.dto.MakePurchaseRequest;

public interface PaymentWebService {
    void calculatePrice(CalculatePriceRequest calculatePriceRequest);

    void makePurchase(MakePurchaseRequest makePurchaseRequest);
}
