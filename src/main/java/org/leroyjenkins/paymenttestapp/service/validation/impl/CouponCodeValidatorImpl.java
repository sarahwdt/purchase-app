package org.leroyjenkins.paymenttestapp.service.validation.impl;

import jakarta.annotation.Nonnull;
import lombok.RequiredArgsConstructor;
import org.leroyjenkins.paymenttestapp.exception.CouponCodeNotExistException;
import org.leroyjenkins.paymenttestapp.service.CouponCodeService;
import org.leroyjenkins.paymenttestapp.service.validation.CouponCodeValidator;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CouponCodeValidatorImpl implements CouponCodeValidator {
    private final CouponCodeService couponCodeService;

    @Override
    public void validateCouponCode(@Nonnull String couponCode) {
        if (!couponCodeService.isCouponCodeExists(couponCode)) {
            throw new CouponCodeNotExistException(couponCode);
        }
    }
}
