package org.leroyjenkins.paymenttestapp.service.impl;

import jakarta.annotation.Nonnull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.leroyjenkins.paymenttestapp.service.PaymentProcessingService;
import org.leroyjenkins.paymenttestapp.service.PriceCalculationService;
import org.leroyjenkins.paymenttestapp.service.PurchaseService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@Slf4j
@RequiredArgsConstructor
public class PurchaseServiceImpl implements PurchaseService {
    private final PaymentProcessingService paymentProcessingService;
    private final PriceCalculationService priceCalculationService;

    @Override
    @Nonnull
    public BigDecimal calculatePrice(int productId, @Nonnull String taxNumber, String couponCode) {
        return priceCalculationService.calculatePrice(productId, taxNumber, couponCode);
    }

    @Override
    public void makePayment(int productId, @Nonnull String taxNumber, String couponCode,
                            @Nonnull String paymentProcessor) {
        BigDecimal price = priceCalculationService.calculatePrice(productId, taxNumber, couponCode);
        paymentProcessingService.processPayment(price, taxNumber, paymentProcessor);
    }
}
