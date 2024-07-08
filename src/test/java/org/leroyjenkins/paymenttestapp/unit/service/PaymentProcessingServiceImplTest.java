package org.leroyjenkins.paymenttestapp.unit.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.leroyjenkins.paymenttestapp.dto.PaymentProcessingResult;
import org.leroyjenkins.paymenttestapp.exception.BusinessLogicException;
import org.leroyjenkins.paymenttestapp.exception.PayPalPaymentException;
import org.leroyjenkins.paymenttestapp.exception.PaymentProcessingException;
import org.leroyjenkins.paymenttestapp.exception.PaymentProcessorNotRegisteredException;
import org.leroyjenkins.paymenttestapp.service.impl.PaymentProcessingServiceImpl;
import org.leroyjenkins.paymenttestapp.service.payment.PaymentAdaptersRegistrar;
import org.leroyjenkins.paymenttestapp.service.payment.PaymentProcessorAdapter;
import org.leroyjenkins.paymenttestapp.unit.AbstractUnitTest;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.math.BigDecimal;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class PaymentProcessingServiceImplTest extends AbstractUnitTest {
    @Mock
    private PaymentAdaptersRegistrar adaptersRegistrar;
    @Mock
    private PaymentProcessorAdapter paymentProcessorAdapter;

    @InjectMocks
    private PaymentProcessingServiceImpl paymentProcessingService;

    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    void Should_ReturnIsPaymentProcessorRegistered_When_isPaymentProcessorRegisteredIsCalled(boolean registrarResult) {
        String paymentProcessor = "payment-processor";
        when(adaptersRegistrar.isAdapterRegistered(paymentProcessor)).thenReturn(registrarResult);

        boolean actualResult = paymentProcessingService.isPaymentProcessorRegistered(paymentProcessor);

        assertThat(actualResult).isEqualTo(registrarResult);
    }

    private static Stream<Arguments> Should_ThrowException_When_processPaymentIsCalledAndExceptionIsThrown_Param() {
        String paymentProcessor = "payment-processor";
        PayPalPaymentException payPalPaymentException = new PayPalPaymentException(10_000);
        PaymentProcessingResult unsuccessfulResultWithoutException = PaymentProcessingResult.builder()
                .isSuccessful(false).build();

        PaymentProcessingResult unsuccessfulResultWithException = PaymentProcessingResult.builder()
                .isSuccessful(false).catchedException(payPalPaymentException).build();
        return Stream.of(
                Arguments.of(paymentProcessor, false, null,
                        new PaymentProcessorNotRegisteredException(paymentProcessor)),
                Arguments.of(paymentProcessor, true, unsuccessfulResultWithoutException,
                        new PaymentProcessingException()),
                Arguments.of(paymentProcessor, true, unsuccessfulResultWithException,
                        new PaymentProcessingException(payPalPaymentException.getMessage()))
        );
    }

    @ParameterizedTest
    @MethodSource("Should_ThrowException_When_processPaymentIsCalledAndExceptionIsThrown_Param")
    void Should_ThrowException_When_processPaymentIsCalledAndExceptionIsThrown(String paymentProcessor,
                                                                               boolean isPaymentProcessorRegistered,
                                                                               PaymentProcessingResult paymentProcessingResult,
                                                                               BusinessLogicException expectedException) {
        BigDecimal paymentAmount = BigDecimal.valueOf(100);
        String taxNumber = "tax-number";

        if (isPaymentProcessorRegistered) {
            when(adaptersRegistrar.getAdapter(paymentProcessor)).thenReturn(paymentProcessorAdapter);
        }
        if (paymentProcessingResult != null) {
            when(paymentProcessorAdapter.makePayment(paymentAmount)).thenReturn(paymentProcessingResult);
        }

        assertThatThrownByIsTheSameAs(() -> paymentProcessingService.processPayment(paymentAmount, taxNumber, paymentProcessor),
                expectedException);
    }

    @Test
    void Should_ProcessPayment_When_processPaymentIsCalled() {
        BigDecimal paymentAmount = BigDecimal.valueOf(100);
        String taxNumber = "tax-number";
        String paymentProcessor = "payment-processor";
        when(adaptersRegistrar.getAdapter(paymentProcessor)).thenReturn(paymentProcessorAdapter);
        when(paymentProcessorAdapter.makePayment(paymentAmount))
                .thenReturn(PaymentProcessingResult.builder().isSuccessful(true).build());

        paymentProcessingService.processPayment(paymentAmount, taxNumber, paymentProcessor);

        verify(paymentProcessorAdapter, times(1)).makePayment(paymentAmount);
    }
}