package org.leroyjenkins.paymenttestapp.unit.service;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.leroyjenkins.paymenttestapp.entity.Product;
import org.leroyjenkins.paymenttestapp.exception.BusinessLogicException;
import org.leroyjenkins.paymenttestapp.exception.CouponCodeNotExistException;
import org.leroyjenkins.paymenttestapp.exception.EntityNotFoundException;
import org.leroyjenkins.paymenttestapp.exception.TaxCalculationException;
import org.leroyjenkins.paymenttestapp.repository.ProductRepository;
import org.leroyjenkins.paymenttestapp.service.CouponCodeService;
import org.leroyjenkins.paymenttestapp.service.TaxService;
import org.leroyjenkins.paymenttestapp.service.impl.PriceCalculationServiceImpl;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

class PriceCalculationServiceImplTest extends AbstractUnitTest {
    @Mock
    private ProductRepository productRepository;
    @Mock
    private CouponCodeService couponCodeService;
    @Mock
    private TaxService taxService;

    @InjectMocks
    private PriceCalculationServiceImpl priceCalculationService;

    private static Stream<Arguments> Should_ThrowException_When_calculatePriceIsCalledAndExceptionIsThrown_Param() {
        int productId = 1;
        return Stream.of(
                Arguments.of(productId, false, null, null,
                        new EntityNotFoundException("Product", productId)),
                Arguments.of(productId, true, new TaxCalculationException("tax-number"), null,
                        new TaxCalculationException("tax-number")),
                Arguments.of(productId, true, null, new CouponCodeNotExistException("coupon-code"),
                        new CouponCodeNotExistException("coupon-code"))
        );
    }

    @ParameterizedTest
    @MethodSource("Should_ThrowException_When_calculatePriceIsCalledAndExceptionIsThrown_Param")
    void Should_ThrowException_When_calculatePriceIsCalledAndExceptionIsThrown(int productId,
                                                                               boolean productFound,
                                                                               BusinessLogicException taxServiceException,
                                                                               BusinessLogicException couponCodeServiceException,
                                                                               BusinessLogicException expectedException) {
        String taxNumber = "tax-number";
        String couponCode = "coupon-code";
        BigDecimal price = BigDecimal.valueOf(100);
        if (productFound) {
            Product product = new Product();
            product.setPrice(price);
            when(productRepository.findById(productId)).thenReturn(Optional.of(product));

            BigDecimal priceWithTax = BigDecimal.valueOf(120);
            if (taxServiceException != null) {
                when(taxService.applyTax(price, taxNumber)).thenThrow(taxServiceException);
            } else {
                when(taxService.applyTax(price, taxNumber)).thenReturn(priceWithTax);
            }
            if (couponCodeServiceException != null) {
                when(couponCodeService.applyCouponCode(priceWithTax, couponCode)).thenThrow(couponCodeServiceException);
            }
        } else {
            when(productRepository.findById(productId)).thenReturn(Optional.empty());
        }

        assertThatThrownByIsTheSameAs(() -> priceCalculationService.calculatePrice(productId, taxNumber, couponCode),
                expectedException);
    }

    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    void Should_calculatePrice_When_calculatePriceIsCalled(boolean isCouponCodeAccepted) {
        int productId = 1;
        String taxNumber = "tax-number";
        String couponCode = "coupon-code";
        BigDecimal price = BigDecimal.valueOf(100);
        BigDecimal priceWithTax = BigDecimal.valueOf(120);
        BigDecimal priceWithCouponCode = BigDecimal.valueOf(150);
        Product product = new Product();
        product.setPrice(price);

        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        when(taxService.applyTax(price, taxNumber)).thenReturn(priceWithTax);
        if (isCouponCodeAccepted) {
            when(couponCodeService.applyCouponCode(priceWithTax, couponCode)).thenReturn(priceWithCouponCode);
        }
        BigDecimal expectedPrice = isCouponCodeAccepted ? priceWithCouponCode : priceWithTax;

        BigDecimal actualPrice;
        if (isCouponCodeAccepted) {
            actualPrice = priceCalculationService.calculatePrice(productId, taxNumber, couponCode);
        } else {
            actualPrice = priceCalculationService.calculatePrice(productId, taxNumber, null);
        }

        assertThat(actualPrice).isEqualByComparingTo(expectedPrice);
    }
}