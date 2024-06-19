package org.leroyjenkins.paymenttestapp.service;

import jakarta.annotation.Nonnull;
import org.leroyjenkins.paymenttestapp.dto.CalculatePriceRequest;
import org.leroyjenkins.paymenttestapp.dto.CalculatePriceResponse;
import org.leroyjenkins.paymenttestapp.dto.MakePurchaseRequest;
import org.leroyjenkins.paymenttestapp.exception.CouponCodeNotExistException;
import org.leroyjenkins.paymenttestapp.exception.MandatoryFieldException;
import org.leroyjenkins.paymenttestapp.exception.TaxCalculationException;

public interface PurchaseWebService {
    /**
     * Calculate price
     *
     * @param calculatePriceRequest data for price calculation
     * @return calculated price
     * @throws MandatoryFieldException when any of mandatory fields are absent
     * @throws CouponCodeNotExistException when coupon code isn't found
     * @throws TaxCalculationException when tax number isn't registered in system
     */
    @Nonnull
    CalculatePriceResponse calculatePrice(@Nonnull CalculatePriceRequest calculatePriceRequest);

    /**
     * Execute payment processing
     *
     * @param makePurchaseRequest data for payment processing
     * @throws MandatoryFieldException when any of mandatory fields are absent
     * @throws CouponCodeNotExistException when coupon code isn't found
     * @throws TaxCalculationException when tax number isn't registered in system
     */
    void makePurchase(@Nonnull MakePurchaseRequest makePurchaseRequest);
}
