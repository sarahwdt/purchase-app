package org.leroyjenkins.paymenttestapp.service;

import jakarta.annotation.Nonnull;
import org.leroyjenkins.paymenttestapp.exception.EntityNotFoundException;

import java.math.BigDecimal;

public interface PriceCalculationService {
    /**
     * Calculate price with all taxes and bonuses
     *
     * @param productId  product for which the price will be calculated
     * @param taxNumber  tax number to calculate tax
     * @param couponCode coupon code to calculate bonus
     * @return price with all taxes and bonuses were applied
     * @throws EntityNotFoundException when product isn't found
     */
    @Nonnull
    BigDecimal calculatePrice(int productId, @Nonnull String taxNumber, String couponCode);
}
