package org.leroyjenkins.paymenttestapp.service.impl;

import jakarta.annotation.Nonnull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.leroyjenkins.paymenttestapp.dto.CalculatePriceRequest;
import org.leroyjenkins.paymenttestapp.dto.CalculatePriceResponse;
import org.leroyjenkins.paymenttestapp.dto.MakePurchaseRequest;
import org.leroyjenkins.paymenttestapp.exception.MandatoryFieldException;
import org.leroyjenkins.paymenttestapp.service.PurchaseService;
import org.leroyjenkins.paymenttestapp.service.PurchaseWebService;
import org.leroyjenkins.paymenttestapp.service.validation.CouponCodeValidator;
import org.leroyjenkins.paymenttestapp.service.validation.PaymentProcessorValidator;
import org.leroyjenkins.paymenttestapp.service.validation.TaxNumberValidator;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
@Slf4j
@RequiredArgsConstructor
public class PurchaseWebServiceImpl implements PurchaseWebService {
    private final CouponCodeValidator couponCodeValidator;
    private final TaxNumberValidator taxNumberValidator;
    private final PaymentProcessorValidator paymentProcessorValidator;
    private final PurchaseService purchaseService;
    private final RoundingMode roundingMode;

    @Override
    @Nonnull
    public CalculatePriceResponse calculatePrice(@Nonnull CalculatePriceRequest calculatePriceRequest) {
        if (calculatePriceRequest.productId() == null) {
            throw new MandatoryFieldException("product");
        }
        if (calculatePriceRequest.taxNumber() == null) {
            throw new MandatoryFieldException("taxNumber");
        }
        taxNumberValidator.validateTaxNumber(calculatePriceRequest.taxNumber());
        if (calculatePriceRequest.couponCode() != null) {
            couponCodeValidator.validateCouponCode(calculatePriceRequest.couponCode());
        }

        BigDecimal calculatedPrice = purchaseService.calculatePrice(
                calculatePriceRequest.productId(),
                calculatePriceRequest.taxNumber(),
                calculatePriceRequest.couponCode());
        return new CalculatePriceResponse(calculatedPrice.setScale(2, roundingMode));
    }

    @Override
    public void makePurchase(@Nonnull MakePurchaseRequest makePurchaseRequest) {
        if (makePurchaseRequest.productId() == null) {
            throw new MandatoryFieldException("product");
        }
        if (makePurchaseRequest.taxNumber() == null) {
            throw new MandatoryFieldException("taxNumber");
        }
        taxNumberValidator.validateTaxNumber(makePurchaseRequest.taxNumber());
        if (makePurchaseRequest.paymentProcessor() == null) {
            throw new MandatoryFieldException("paymentProcessor");
        }
        paymentProcessorValidator.validatePaymentProcessor(makePurchaseRequest.paymentProcessor());
        if (makePurchaseRequest.couponCode() != null) {
            couponCodeValidator.validateCouponCode(makePurchaseRequest.couponCode());
        }

        purchaseService.makePayment(
                makePurchaseRequest.productId(),
                makePurchaseRequest.taxNumber(),
                makePurchaseRequest.couponCode(),
                makePurchaseRequest.paymentProcessor()
        );
    }
}
