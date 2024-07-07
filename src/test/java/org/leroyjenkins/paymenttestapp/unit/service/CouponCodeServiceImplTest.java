package org.leroyjenkins.paymenttestapp.unit.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.leroyjenkins.paymenttestapp.entity.CouponCode;
import org.leroyjenkins.paymenttestapp.exception.BusinessLogicException;
import org.leroyjenkins.paymenttestapp.exception.CouponCodeNotExistException;
import org.leroyjenkins.paymenttestapp.repository.CouponCodeRepository;
import org.leroyjenkins.paymenttestapp.service.impl.CouponCodeServiceImpl;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.Optional;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class CouponCodeServiceImplTest extends AbstractUnitTest {
    private final static int PRECISION = 18;
    private final static RoundingMode ROUNDING_MODE = RoundingMode.HALF_EVEN;

    @Mock
    private CouponCodeRepository couponCodeRepository;
    @Mock
    private BigDecimal mockedPrice;
    @Mock
    // This is not actually a mock, but we use @Mock annotation for @InjectMocks injection
    private static MathContext mathContext = new MathContext(PRECISION, ROUNDING_MODE);

    @InjectMocks
    private CouponCodeServiceImpl couponCodeService;

    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    void Should_ReturnIsCouponCodeExists_When_isCouponCodeExistsIsCalled(boolean isExist) {
        String couponCode = "coupon-code";
        when(couponCodeRepository.existsById(couponCode)).thenReturn(isExist);

        boolean actualResult = couponCodeService.isCouponCodeExists(couponCode);

        assertThat(actualResult).isEqualTo(isExist);
    }

    @Test
    void Should_ThrowException_When_applyCouponCodeIsCalledAndCouponCodeNotExists() {
        BigDecimal price = BigDecimal.valueOf(100);
        String couponCode = "coupon-code";
        when(couponCodeRepository.findById(couponCode)).thenReturn(Optional.empty());
        BusinessLogicException expectedException = new CouponCodeNotExistException(couponCode);

        assertThatThrownByIsTheSameAs(() -> couponCodeService.applyCouponCode(price, couponCode),
                expectedException);
    }

    private static Stream<Arguments> Should_applyCouponCode_When_applyCouponCodeIsCalledAndCouponCodeExists_Param() {
        BigDecimal price = BigDecimal.valueOf(100);
        CouponCode fixedCouponCode = new CouponCode();
        BigDecimal fixedRate = BigDecimal.valueOf(10);
        fixedCouponCode.setFixed(fixedRate);
        CouponCode percentCouponCode = new CouponCode();
        BigDecimal percentRate = BigDecimal.valueOf(33.333333);
        percentCouponCode.setPercent(percentRate);

        BigDecimal expectedPriceWithFixedRate = price.subtract(fixedRate, mathContext);
        BigDecimal expectedPriceWithPercentRate = price.subtract(
                price.multiply(percentRate.movePointLeft(2), mathContext), mathContext);

        return Stream.of(
                Arguments.of(fixedCouponCode, price, expectedPriceWithFixedRate),
                Arguments.of(fixedCouponCode, BigDecimal.valueOf(9), BigDecimal.ZERO),
                Arguments.of(percentCouponCode, price, expectedPriceWithPercentRate)
        );
    }

    @ParameterizedTest
    @MethodSource("Should_applyCouponCode_When_applyCouponCodeIsCalledAndCouponCodeExists_Param")
    void Should_applyCouponCode_When_applyCouponCodeIsCalledAndCouponCodeExists(CouponCode couponCodeEntity, BigDecimal price,
                                                                                BigDecimal expectedResult) {
        String couponCode = "coupon-code";
        when(couponCodeRepository.findById(couponCode)).thenReturn(Optional.of(couponCodeEntity));

        BigDecimal actualResult = couponCodeService.applyCouponCode(price, couponCode);

        assertThat(actualResult).isEqualByComparingTo(expectedResult);
    }

    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    void Should_UseMathContext_When_applyCouponCodeIsCalledAndCouponCodeExists(boolean isFixed) {
        String couponCode = "coupon-code";
        CouponCode couponCodeEntity = new CouponCode();
        BigDecimal rate = BigDecimal.valueOf(10);
        if (isFixed) {
            couponCodeEntity.setFixed(rate);
        } else {
            couponCodeEntity.setPercent(rate);
        }
        when(couponCodeRepository.findById(couponCode)).thenReturn(Optional.of(couponCodeEntity));
        when(mockedPrice.subtract(any(), any())).thenReturn(mockedPrice);
        if (!isFixed) {
            when(mockedPrice.multiply(any(), any())).thenReturn(mockedPrice);
        }

        couponCodeService.applyCouponCode(mockedPrice, couponCode);

        if (isFixed) {
            verify(mockedPrice, times(1)).subtract(any(), eq(mathContext));
        } else {
            verify(mockedPrice, times(1)).subtract(any(), eq(mathContext));
            verify(mockedPrice, times(1)).multiply(any(), eq(mathContext));
        }
    }
}