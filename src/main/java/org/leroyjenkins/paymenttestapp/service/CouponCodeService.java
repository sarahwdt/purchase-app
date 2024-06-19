package org.leroyjenkins.paymenttestapp.service;

import jakarta.annotation.Nonnull;
import org.leroyjenkins.paymenttestapp.exception.CouponCodeNotExistException;

import java.math.BigDecimal;

public interface CouponCodeService {
    /**
     * Check is coupon code exists in system
     *
     * @param couponCode coupon code
     * @return is coupon code exists in system
     */
    boolean isCouponCodeExists(@Nonnull String couponCode);

    /**
     * Apply coupon code to price
     *
     * @param price price to apply coupon code
     * @param couponCode coupon code to apply
     * @return price after coupon code was applied
     * @throws CouponCodeNotExistException - when coupon code isn't exist in system
     */
    @Nonnull
    BigDecimal applyCouponCode(@Nonnull BigDecimal price, @Nonnull String couponCode);
}
