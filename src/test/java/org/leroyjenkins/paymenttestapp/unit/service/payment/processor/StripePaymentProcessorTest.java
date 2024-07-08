package org.leroyjenkins.paymenttestapp.unit.service.payment.processor;

import org.junit.jupiter.api.Test;
import org.leroyjenkins.paymenttestapp.service.payment.processor.StripePaymentProcessor;
import org.leroyjenkins.paymenttestapp.unit.AbstractUnitTest;

import static org.assertj.core.api.Assertions.assertThat;

class StripePaymentProcessorTest extends AbstractUnitTest {
    private final StripePaymentProcessor processor = new StripePaymentProcessor();

    @Test
    void Should_ReturnFalse_When_payIsCalledAndPaymentAmountLessThan100() {
        float amount = 99.99f;

        boolean actualResult = processor.pay(amount);

        assertThat(actualResult).isFalse();
    }

    @Test
    void Should_ReturnTrue_When_payIsCalledAndPaymentAmountGreaterThanOrEquals100() {
        float amount = 100f;

        boolean actualResult = processor.pay(amount);

        assertThat(actualResult).isTrue();
    }

    @Test
    void Should_ReturnTrue_When_payIsCalledAndPaymentAmountApproximatelyEquals100() {
        float amount = 99.991f;

        boolean actualResult = processor.pay(amount);

        assertThat(actualResult).isTrue();
    }
}