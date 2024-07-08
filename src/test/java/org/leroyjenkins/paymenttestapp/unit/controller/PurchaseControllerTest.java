package org.leroyjenkins.paymenttestapp.unit.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.leroyjenkins.paymenttestapp.dto.CalculatePriceRequest;
import org.leroyjenkins.paymenttestapp.dto.MakePurchaseRequest;
import org.leroyjenkins.paymenttestapp.exception.*;
import org.leroyjenkins.paymenttestapp.service.PurchaseWebService;
import org.leroyjenkins.paymenttestapp.unit.AbstractMvcTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.util.stream.Stream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

class PurchaseControllerTest extends AbstractMvcTest {
    @MockBean
    private PurchaseWebService purchaseWebService;

    @Autowired
    private ObjectMapper objectMapper;

    private static Stream<Arguments>
    Should_ReturnErrorMessage_When_calculatePriceIsRequestedAndPurchaseWebServiceReturnError_Param() {
        return Stream.of(
                Arguments.of(new MandatoryFieldException("product")),
                Arguments.of(new MandatoryFieldException("taxNumber")),
                Arguments.of(new EntityNotFoundException("Product", 1)),
                Arguments.of(new CouponCodeNotExistException("coupon-code")),
                Arguments.of(new TaxCalculationException("tax-number"))
        );
    }

    @ParameterizedTest
    @MethodSource("Should_ReturnErrorMessage_When_calculatePriceIsRequestedAndPurchaseWebServiceReturnError_Param")
    void Should_ReturnErrorMessage_When_calculatePriceIsRequestedAndPurchaseWebServiceThrowsBusinessLogicException(
            BusinessLogicException exception) throws Exception {
        when(purchaseWebService.calculatePrice(any(CalculatePriceRequest.class))).thenThrow(exception);

        MockHttpServletRequestBuilder request = post("/calculate-price")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(CalculatePriceRequest.builder().build()));

        assertErrorResponse(request, exception);
    }

    private static Stream<Arguments>
    Should_ReturnErrorMessage_When_purchaseIsRequestedAndPurchaseWebServiceThrowsBusinessLogicException_Param() {
        return Stream.of(
                Arguments.of(new MandatoryFieldException("product")),
                Arguments.of(new MandatoryFieldException("taxNumber")),
                Arguments.of(new MandatoryFieldException("paymentProcessor")),
                Arguments.of(new EntityNotFoundException("Product", 1)),
                Arguments.of(new CouponCodeNotExistException("coupon-code")),
                Arguments.of(new TaxCalculationException("tax-number")),
                Arguments.of(new PaymentProcessingException("Reason")),
                Arguments.of(new PaymentProcessorNotRegisteredException("payment-processor"))
        );
    }

    @ParameterizedTest
    @MethodSource("Should_ReturnErrorMessage_When_purchaseIsRequestedAndPurchaseWebServiceThrowsBusinessLogicException_Param")
    void Should_ReturnErrorMessage_When_purchaseIsRequestedAndPurchaseWebServiceThrowsBusinessLogicException(
            BusinessLogicException exception) throws Exception {
        doThrow(exception).when(purchaseWebService).makePurchase(any(MakePurchaseRequest.class));

        MockHttpServletRequestBuilder request = post("/purchase")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(MakePurchaseRequest.builder().build()));

        assertErrorResponse(request, exception);
    }
}