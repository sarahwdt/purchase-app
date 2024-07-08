package org.leroyjenkins.paymenttestapp.unit.service.validation;

import org.junit.jupiter.api.Test;
import org.leroyjenkins.paymenttestapp.exception.PaymentProcessorNotRegisteredException;
import org.leroyjenkins.paymenttestapp.service.PaymentProcessingService;
import org.leroyjenkins.paymenttestapp.service.validation.impl.PaymentProcessorValidatorImpl;
import org.leroyjenkins.paymenttestapp.unit.AbstractUnitTest;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.Mockito.when;

class PaymentProcessorValidatorImplTest extends AbstractUnitTest {
    @Mock
    private PaymentProcessingService paymentProcessingService;

    @InjectMocks
    private PaymentProcessorValidatorImpl paymentProcessorValidator;

    @Test
    void Should_ThrowException_When_validatePaymentProcessorIsCalledAndPaymentProcessorDoesntRegistered() {
        String paymentProcessor = "payment-processor";
        when(paymentProcessingService.isPaymentProcessorRegistered(paymentProcessor)).thenReturn(false);

        assertThatThrownByIsTheSameAs(() -> paymentProcessorValidator.validatePaymentProcessor(paymentProcessor),
                new PaymentProcessorNotRegisteredException(paymentProcessor));
    }

    @Test
    void Should_DoNothing_When_validatePaymentProcessorIsCalledAndPaymentProcessorRegistered() {
        String paymentProcessor = "payment-processor";
        when(paymentProcessingService.isPaymentProcessorRegistered(paymentProcessor)).thenReturn(true);

        assertThatCode(() -> paymentProcessorValidator.validatePaymentProcessor(paymentProcessor))
                .doesNotThrowAnyException();
    }
}