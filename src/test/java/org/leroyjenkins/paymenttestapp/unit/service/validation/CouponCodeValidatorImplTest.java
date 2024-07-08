package org.leroyjenkins.paymenttestapp.unit.service.validation;

import org.junit.jupiter.api.Test;
import org.leroyjenkins.paymenttestapp.exception.CouponCodeNotExistException;
import org.leroyjenkins.paymenttestapp.service.CouponCodeService;
import org.leroyjenkins.paymenttestapp.service.validation.impl.CouponCodeValidatorImpl;
import org.leroyjenkins.paymenttestapp.unit.service.AbstractUnitTest;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.Mockito.when;

class CouponCodeValidatorImplTest extends AbstractUnitTest {
    @Mock
    private CouponCodeService couponCodeService;

    @InjectMocks
    private CouponCodeValidatorImpl validator;

    @Test
    void Should_ThrowException_When_validateCouponCodeIsCalledAndCouponCodeDoesntExist() {
        String couponCode = "coupon-code";
        when(couponCodeService.isCouponCodeExists(couponCode)).thenReturn(false);

        assertThatThrownByIsTheSameAs(() -> validator.validateCouponCode(couponCode),
                new CouponCodeNotExistException(couponCode));
    }

    @Test
    void Should_DoNothing_When_validateCouponCodeIsCalledAndCouponCodeExists() {
        String couponCode = "coupon-code";
        when(couponCodeService.isCouponCodeExists(couponCode)).thenReturn(true);

        assertThatCode(() -> validator.validateCouponCode(couponCode))
                .doesNotThrowAnyException();
    }
}