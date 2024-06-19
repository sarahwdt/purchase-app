package org.leroyjenkins.paymenttestapp.service.impl;

import jakarta.annotation.Nonnull;
import lombok.RequiredArgsConstructor;
import org.leroyjenkins.paymenttestapp.entity.CouponCode;
import org.leroyjenkins.paymenttestapp.exception.CouponCodeNotExistException;
import org.leroyjenkins.paymenttestapp.repository.CouponCodeRepository;
import org.leroyjenkins.paymenttestapp.service.CouponCodeService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.MathContext;

@Service
@RequiredArgsConstructor
public class CouponCodeServiceImpl implements CouponCodeService {
    private final CouponCodeRepository couponCodeRepository;
    private final MathContext mathContext;

    @Override
    public boolean isCouponCodeExists(@Nonnull String couponCode) {
        return couponCodeRepository.existsById(couponCode);
    }

    @Override
    @Nonnull
    public BigDecimal applyCouponCode(@Nonnull BigDecimal price, @Nonnull String couponCode) {
        CouponCode couponCodeEntity = couponCodeRepository.findById(couponCode)
                .orElseThrow(() -> new CouponCodeNotExistException(couponCode));
        if (couponCodeEntity.isFixed()) {
            price = price.subtract(couponCodeEntity.getFixed(), mathContext);
            if (price.signum() < 0) {
                price = BigDecimal.ZERO;
            }
        } else {
            price = price.subtract(
                    price.multiply(
                            couponCodeEntity.getPercent().movePointLeft(2),
                            mathContext), mathContext);
        }
        return price;
    }
}
