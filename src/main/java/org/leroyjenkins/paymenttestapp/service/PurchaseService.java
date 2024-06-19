package org.leroyjenkins.paymenttestapp.service;

import jakarta.annotation.Nonnull;

import java.math.BigDecimal;

public interface PurchaseService {
    /**
     * Calculate price with all taxes and bonuses applied
     *
     * @param productId product for which the price will be calculated
     * @param taxNumber tax number to calculate tax
     * @param couponCode coupon code to calculate bonus
     * @return price with all taxes and bonuses were applied
     */
    @Nonnull
    BigDecimal calculatePrice(int productId, @Nonnull String taxNumber, String couponCode);

    /**
     * Execute payment processing with applying taxes and bonuses
     *
     * @param productId product for which the price will be calculated for payment
     * @param taxNumber tax number to calculate tax
     * @param couponCode coupon code to calculate bonus
     * @param paymentProcessor payment processor name to process payment
     */
    void makePayment(int productId, @Nonnull String taxNumber, String couponCode, @Nonnull String paymentProcessor);
}
