package org.leroyjenkins.paymenttestapp.service.validation;

import jakarta.annotation.Nonnull;
import org.leroyjenkins.paymenttestapp.exception.CouponCodeNotExistException;

public interface CouponCodeValidator {
    /**
     * Check is coupon code exists in system
     *
     * @param couponCode coupon code
     * @throws CouponCodeNotExistException if coupon code isn't found
     */
    void validateCouponCode(@Nonnull String couponCode);
}
