package org.leroyjenkins.paymenttestapp.unit.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.leroyjenkins.paymenttestapp.exception.BusinessLogicException;
import org.leroyjenkins.paymenttestapp.exception.EntityNotFoundException;
import org.leroyjenkins.paymenttestapp.exception.PaymentProcessingException;
import org.leroyjenkins.paymenttestapp.service.PaymentProcessingService;
import org.leroyjenkins.paymenttestapp.service.PriceCalculationService;
import org.leroyjenkins.paymenttestapp.service.impl.PurchaseServiceImpl;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.math.BigDecimal;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class PurchaseServiceImplTest extends AbstractUnitTest {
    @Mock
    private PaymentProcessingService paymentProcessingService;
    @Mock
    private PriceCalculationService priceCalculationService;

    @InjectMocks
    private PurchaseServiceImpl purchaseService;

    @Test
    void Should_calculatePrice_When_calculatePriceIsCalled() {
        int productId = 1;
        String taxNumber = "tax-number";
        String couponCode = "coupon-code";
        BigDecimal expectedPrice = BigDecimal.valueOf(100);
        when(priceCalculationService.calculatePrice(productId, taxNumber, couponCode)).thenReturn(expectedPrice);

        BigDecimal actualPrice = purchaseService.calculatePrice(productId, taxNumber, couponCode);

        assertThat(actualPrice).isEqualByComparingTo(expectedPrice);
    }

    @Test
    void Should_ThrowException_When_calculatePriceIsCalledAndExceptionIsThrown() {
        int productId = 1;
        String taxNumber = "tax-number";
        String couponCode = "coupon-code";
        EntityNotFoundException expectedException = new EntityNotFoundException("Product", productId);
        when(priceCalculationService.calculatePrice(productId, taxNumber, couponCode)).thenThrow(expectedException);

        assertThatThrownByIsTheSameAs(() -> purchaseService.calculatePrice(productId, taxNumber, couponCode),
                expectedException);
    }

    @Test
    void Should_MakePayment_When_makePaymentIsCalled() {
        int productId = 1;
        String taxNumber = "tax-number";
        String couponCode = "coupon-code";
        String paymentProcessor = "payment-processor";
        BigDecimal calculatedPrice = BigDecimal.valueOf(100);
        when(priceCalculationService.calculatePrice(productId, taxNumber, couponCode)).thenReturn(calculatedPrice);

        purchaseService.makePayment(productId, taxNumber, couponCode, paymentProcessor);

        verify(paymentProcessingService, times(1))
                .processPayment(calculatedPrice, taxNumber, paymentProcessor);
    }

    private static Stream<Arguments> Should_ThrowException_When_makePaymentIsCalledAndExceptionIsThrown_Param() {
        return Stream.of(
                Arguments.of(new EntityNotFoundException("Product", "productId"), null,
                        new EntityNotFoundException("Product", "productId")),
                Arguments.of(null, new PaymentProcessingException("message"),
                        new PaymentProcessingException("message"))
        );
    }

    @ParameterizedTest
    @MethodSource("Should_ThrowException_When_makePaymentIsCalledAndExceptionIsThrown_Param")
    void Should_ThrowException_When_makePaymentIsCalledAndExceptionIsThrown(BusinessLogicException calculatePriceException,
                                                                            BusinessLogicException processPaymentException,
                                                                            BusinessLogicException expectedException) {
        int productId = 1;
        String taxNumber = "tax-number";
        String couponCode = "coupon-code";
        String paymentProcessor = "payment-processor";
        BigDecimal calculatedPrice = BigDecimal.valueOf(100);
        if (calculatePriceException != null) {
            when(priceCalculationService.calculatePrice(productId, taxNumber, couponCode))
                    .thenThrow(calculatePriceException);
        } else {
            when(priceCalculationService.calculatePrice(productId, taxNumber, couponCode))
                    .thenReturn(calculatedPrice);
        }

        if (processPaymentException != null) {
            doThrow(processPaymentException).when(paymentProcessingService)
                    .processPayment(calculatedPrice, taxNumber, paymentProcessor);
        }

        assertThatThrownByIsTheSameAs(() -> purchaseService.makePayment(productId, taxNumber, couponCode, paymentProcessor),
                expectedException);
    }
}