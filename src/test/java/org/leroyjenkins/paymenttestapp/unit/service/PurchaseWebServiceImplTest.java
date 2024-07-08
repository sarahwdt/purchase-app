package org.leroyjenkins.paymenttestapp.unit.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.leroyjenkins.paymenttestapp.dto.CalculatePriceRequest;
import org.leroyjenkins.paymenttestapp.dto.CalculatePriceResponse;
import org.leroyjenkins.paymenttestapp.dto.MakePurchaseRequest;
import org.leroyjenkins.paymenttestapp.exception.*;
import org.leroyjenkins.paymenttestapp.service.PurchaseService;
import org.leroyjenkins.paymenttestapp.service.impl.PurchaseWebServiceImpl;
import org.leroyjenkins.paymenttestapp.service.validation.CouponCodeValidator;
import org.leroyjenkins.paymenttestapp.service.validation.PaymentProcessorValidator;
import org.leroyjenkins.paymenttestapp.service.validation.TaxNumberValidator;
import org.leroyjenkins.paymenttestapp.unit.AbstractUnitTest;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class PurchaseWebServiceImplTest extends AbstractUnitTest {
    @Mock
    private CouponCodeValidator couponCodeValidator;
    @Mock
    private TaxNumberValidator taxNumberValidator;
    @Mock
    private PaymentProcessorValidator paymentProcessorValidator;
    @Mock
    private PurchaseService purchaseService;
    @Mock
    private RoundingMode roundingMode = RoundingMode.HALF_UP;

    @InjectMocks
    private PurchaseWebServiceImpl purchaseWebService;

    private static Stream<Arguments> Should_ThrowException_When_calculatePriceIsCalledAndValidationFailedOrExceptionThrown_Param() {
        int productId = 1;
        String taxNumber = "tax-number";
        String couponCode = "coupon-code";
        CalculatePriceRequest fullCalculatePriceRequest = CalculatePriceRequest.builder()
                .productId(productId).taxNumber(taxNumber).couponCode(couponCode).build();
        CalculatePriceRequest calculatePriceRequestWithoutProductId = fullCalculatePriceRequest.toBuilder()
                .productId(null).build();
        CalculatePriceRequest calculatePriceRequestWithoutTaxNumber = fullCalculatePriceRequest.toBuilder()
                .taxNumber(null).build();
        return Stream.of(
                Arguments.of(calculatePriceRequestWithoutProductId, null, null, null,
                        new MandatoryFieldException("product")),
                Arguments.of(calculatePriceRequestWithoutTaxNumber, null, null, null,
                        new MandatoryFieldException("taxNumber")),
                Arguments.of(fullCalculatePriceRequest, new TaxCalculationException(taxNumber), null, null,
                        new TaxCalculationException(taxNumber)),
                Arguments.of(fullCalculatePriceRequest, null, new CouponCodeNotExistException(couponCode), null,
                        new CouponCodeNotExistException(couponCode)),
                Arguments.of(fullCalculatePriceRequest, null, null, new EntityNotFoundException("Product", productId),
                        new EntityNotFoundException("Product", productId))
        );
    }

    @ParameterizedTest
    @MethodSource("Should_ThrowException_When_calculatePriceIsCalledAndValidationFailedOrExceptionThrown_Param")
    void Should_ThrowException_When_calculatePriceIsCalledAndValidationFailedOrExceptionThrown(
            CalculatePriceRequest request,
            BusinessLogicException taxNumberValidationException,
            BusinessLogicException couponCodeValidationException,
            BusinessLogicException calculatePriceException,
            BusinessLogicException expectedException) {
        if (taxNumberValidationException != null) {
            doThrow(taxNumberValidationException).when(taxNumberValidator).validateTaxNumber(request.taxNumber());
        }
        if (couponCodeValidationException != null && request.couponCode() != null) {
            doThrow(couponCodeValidationException).when(couponCodeValidator).validateCouponCode(request.couponCode());
        }
        if (calculatePriceException != null) {
            doThrow(calculatePriceException)
                    .when(purchaseService).calculatePrice(request.productId(), request.taxNumber(), request.couponCode());
        }

        assertThatThrownByIsTheSameAs(() -> purchaseWebService.calculatePrice(request),
                expectedException);
    }

    private static Stream<Arguments> Should_ReturnCalculatedPrice_When_calculatePriceIsCalledWithValidRequest_Param() {
        int productId = 1;
        String taxNumber = "tax-number";
        String couponCode = "coupon-code";
        CalculatePriceRequest fullCalculatePriceRequest = CalculatePriceRequest.builder()
                .productId(productId).taxNumber(taxNumber).couponCode(couponCode).build();
        CalculatePriceRequest calculatePriceRequestWithoutCouponCode = fullCalculatePriceRequest.toBuilder()
                .couponCode(null).build();
        return Stream.of(
                Arguments.of(fullCalculatePriceRequest, BigDecimal.valueOf(2), BigDecimal.valueOf(2)),
                Arguments.of(calculatePriceRequestWithoutCouponCode, BigDecimal.valueOf(2), BigDecimal.valueOf(2))
        );
    }

    @ParameterizedTest
    @MethodSource("Should_ReturnCalculatedPrice_When_calculatePriceIsCalledWithValidRequest_Param")
    void Should_ReturnCalculatedPrice_When_calculatePriceIsCalledWithValidRequest(CalculatePriceRequest request,
                                                                                  BigDecimal purchaseServiceResponse,
                                                                                  BigDecimal expectedResult) {
        when(purchaseService.calculatePrice(request.productId(), request.taxNumber(), request.couponCode()))
                .thenReturn(purchaseServiceResponse);

        CalculatePriceResponse actualResult = purchaseWebService.calculatePrice(request);

        assertThat(actualResult).isNotNull();
        assertThat(actualResult.calculatedPrice()).isEqualByComparingTo(expectedResult);
    }

    private static Stream<Arguments> Should_ThrowException_When_makePurchaseIsCalledAndValidationFailedOrExceptionThrown_Param() {
        int productId = 1;
        String taxNumber = "tax-number";
        String couponCode = "coupon-code";
        String paymentProcessor = "payment-processor";
        MakePurchaseRequest fullMakePurchaseRequest = MakePurchaseRequest.builder()
                .productId(productId).taxNumber(taxNumber).couponCode(couponCode).paymentProcessor(paymentProcessor).build();
        MakePurchaseRequest makePurchaseRequestWithoutProductId = fullMakePurchaseRequest.toBuilder()
                .productId(null).build();
        MakePurchaseRequest makePurchaseRequestWithoutTaxNumber = fullMakePurchaseRequest.toBuilder()
                .taxNumber(null).build();
        MakePurchaseRequest makePurchaseRequestWithoutPaymentProcessor = fullMakePurchaseRequest.toBuilder()
                .paymentProcessor(null).build();
        return Stream.of(
                Arguments.of(
                        makePurchaseRequestWithoutProductId, null, null, null, null,
                        new MandatoryFieldException("product")),
                Arguments.of(
                        makePurchaseRequestWithoutTaxNumber, null, null, null, null,
                        new MandatoryFieldException("taxNumber")),
                Arguments.of(
                        makePurchaseRequestWithoutPaymentProcessor, null, null, null, null,
                        new MandatoryFieldException("paymentProcessor")),
                Arguments.of(
                        fullMakePurchaseRequest, new TaxCalculationException(taxNumber), null, null, null,
                        new TaxCalculationException(taxNumber)),
                Arguments.of(
                        fullMakePurchaseRequest, null, new CouponCodeNotExistException(couponCode), null, null,
                        new CouponCodeNotExistException(couponCode)),
                Arguments.of(
                        fullMakePurchaseRequest, null, null, new PaymentProcessorNotRegisteredException(paymentProcessor),
                        null, new PaymentProcessorNotRegisteredException(paymentProcessor)),
                Arguments.of(
                        fullMakePurchaseRequest, null, null, null, new PaymentProcessingException("payment failed"),
                        new PaymentProcessingException("payment failed"))
        );
    }

    @ParameterizedTest
    @MethodSource("Should_ThrowException_When_makePurchaseIsCalledAndValidationFailedOrExceptionThrown_Param")
    void Should_ThrowException_When_makePurchaseIsCalledAndValidationFailedOrExceptionThrown(
            MakePurchaseRequest request,
            BusinessLogicException taxNumberValidationException,
            BusinessLogicException couponCodeValidationException,
            BusinessLogicException paymentProcessorValidationException,
            BusinessLogicException makePaymentException,
            BusinessLogicException expectedException) {
        if (taxNumberValidationException != null) {
            doThrow(taxNumberValidationException).when(taxNumberValidator).validateTaxNumber(request.taxNumber());
        }
        if (paymentProcessorValidationException != null) {
            doThrow(paymentProcessorValidationException).when(paymentProcessorValidator)
                    .validatePaymentProcessor(request.paymentProcessor());
        }
        if (couponCodeValidationException != null && request.couponCode() != null) {
            doThrow(couponCodeValidationException).when(couponCodeValidator).validateCouponCode(request.couponCode());
        }
        if (makePaymentException != null) {
            doThrow(makePaymentException).when(purchaseService)
                    .makePayment(request.productId(), request.taxNumber(), request.couponCode(), request.paymentProcessor());
        }

        assertThatThrownByIsTheSameAs(() -> purchaseWebService.makePurchase(request),
                expectedException);
    }

    @Test
    void Should_CallPurchaseService_When_makePurchaseIsCalled() {
        int productId = 1;
        String taxNumber = "tax-number";
        String couponCode = "coupon-code";
        String paymentProcessor = "payment-processor";
        MakePurchaseRequest makePurchaseRequest = MakePurchaseRequest.builder()
                .productId(productId).taxNumber(taxNumber).couponCode(couponCode).paymentProcessor(paymentProcessor)
                .build();

        purchaseWebService.makePurchase(makePurchaseRequest);

        verify(purchaseService, times(1))
                .makePayment(productId, taxNumber, couponCode, paymentProcessor);
    }
}