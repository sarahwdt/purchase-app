package org.leroyjenkins.paymenttestapp.unit.service.payment.adapter;

import org.junit.jupiter.api.Test;
import org.leroyjenkins.paymenttestapp.dto.PaymentProcessingResult;
import org.leroyjenkins.paymenttestapp.exception.PayPalPaymentException;
import org.leroyjenkins.paymenttestapp.service.payment.adapter.PayPalPaymentProcessorAdapterImpl;
import org.leroyjenkins.paymenttestapp.service.payment.processor.PayPalPaymentProcessor;
import org.leroyjenkins.paymenttestapp.unit.AbstractUnitTest;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class PayPalPaymentProcessorAdapterImplTest extends AbstractUnitTest {
    @Mock
    private RoundingMode ROUNDING_MODE = RoundingMode.HALF_EVEN;
    @Mock
    private PayPalPaymentProcessor payPalPaymentProcessor;

    @InjectMocks
    private PayPalPaymentProcessorAdapterImpl payPalPaymentProcessorAdapter;

    @Test
    void Should_ReturnResultWithIsSuccessfulFalse_When_makePaymentIsCalledAndPaymentProcessorThrowsException()
            throws PayPalPaymentException {
        BigDecimal paymentAmount = BigDecimal.valueOf(100);
        int expectedInt = paymentAmount.movePointRight(2).intValue();
        doThrow(new PayPalPaymentException(expectedInt)).when(payPalPaymentProcessor).makePayment(expectedInt);

        PaymentProcessingResult actualResult = payPalPaymentProcessorAdapter.makePayment(paymentAmount);

        assertThat(actualResult.isSuccessful()).isFalse();
    }

    @Test
    void Should_ReturnResultWithIsSuccessfulTrue_When_makePaymentIsCalledAndPaymentProcessorDoesntThrowException()
            throws PayPalPaymentException {
        BigDecimal paymentAmount = BigDecimal.valueOf(100);
        int expectedInt = paymentAmount.movePointRight(2).intValue();
        doNothing().when(payPalPaymentProcessor).makePayment(expectedInt);

        PaymentProcessingResult actualResult = payPalPaymentProcessorAdapter.makePayment(paymentAmount);

        assertThat(actualResult.isSuccessful()).isTrue();
    }
}