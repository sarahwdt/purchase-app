package org.leroyjenkins.paymenttestapp.unit.service.payment.processor;

import org.junit.jupiter.api.Test;
import org.leroyjenkins.paymenttestapp.exception.PayPalPaymentException;
import org.leroyjenkins.paymenttestapp.service.payment.processor.PayPalPaymentProcessor;
import org.leroyjenkins.paymenttestapp.unit.AbstractUnitTest;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class PayPalPaymentProcessorTest extends AbstractUnitTest {
    private final PayPalPaymentProcessor payPalPaymentProcessor = new PayPalPaymentProcessor();

    @Test
    void Should_ThrowException_When_makePaymentIsCalledAndPaymentAmountMoreThan100_000() {
        int paymentAmount = 100_001;

        assertThatThrownBy(() -> payPalPaymentProcessor.makePayment(paymentAmount))
                .isInstanceOf(PayPalPaymentException.class);
    }

    @Test
    void Should_DoNothing_When_makePaymentIsCalledAndPaymentAmountLessThanOrEqual100_000() {
        int paymentAmount = 100_000;

        assertThatCode(() -> payPalPaymentProcessor.makePayment(paymentAmount))
                .doesNotThrowAnyException();
    }
}