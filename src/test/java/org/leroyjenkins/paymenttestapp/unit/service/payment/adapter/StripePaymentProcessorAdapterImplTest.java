package org.leroyjenkins.paymenttestapp.unit.service.payment.adapter;

import org.junit.jupiter.api.Test;
import org.leroyjenkins.paymenttestapp.dto.PaymentProcessingResult;
import org.leroyjenkins.paymenttestapp.service.payment.adapter.StripePaymentProcessorAdapterImpl;
import org.leroyjenkins.paymenttestapp.service.payment.processor.StripePaymentProcessor;
import org.leroyjenkins.paymenttestapp.unit.service.AbstractUnitTest;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

class StripePaymentProcessorAdapterImplTest extends AbstractUnitTest {
    @Mock
    private RoundingMode ROUNDING_MODE = RoundingMode.HALF_EVEN;
    @Mock
    private StripePaymentProcessor stripePaymentProcessor;

    @InjectMocks
    private StripePaymentProcessorAdapterImpl stripePaymentProcessorAdapter;

    @Test
    void Should_ReturnResultWithIsSuccessfulFalse_When_makePaymentIsCalledAndPaymentProcessorReturnsFalse() {
        BigDecimal paymentAmount = BigDecimal.valueOf(100);
        float expectedFloat = paymentAmount.floatValue();
        when(stripePaymentProcessor.pay(expectedFloat)).thenReturn(false);

        PaymentProcessingResult actualResult = stripePaymentProcessorAdapter.makePayment(paymentAmount);

        assertThat(actualResult.isSuccessful()).isFalse();
    }

    @Test
    void Should_ReturnResultWithIsSuccessfulTrue_When_makePaymentIsCalledAndPaymentProcessorReturnsTrue() {
        BigDecimal paymentAmount = BigDecimal.valueOf(100);
        float expectedFloat = paymentAmount.floatValue();
        when(stripePaymentProcessor.pay(expectedFloat)).thenReturn(true);

        PaymentProcessingResult actualResult = stripePaymentProcessorAdapter.makePayment(paymentAmount);

        assertThat(actualResult.isSuccessful()).isTrue();
    }
}