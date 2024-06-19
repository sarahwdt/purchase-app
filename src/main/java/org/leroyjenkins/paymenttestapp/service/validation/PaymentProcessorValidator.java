package org.leroyjenkins.paymenttestapp.service.validation;

import jakarta.annotation.Nonnull;
import org.leroyjenkins.paymenttestapp.exception.PaymentProcessorNotRegisteredException;

public interface PaymentProcessorValidator {
    /**
     * Check is payment processor adapter for payment processor registered
     *
     * @param paymentProcessorName payment processor name to check
     * @throws PaymentProcessorNotRegisteredException if payment processor adapter isn't registered
     */
    void validatePaymentProcessor(@Nonnull String paymentProcessorName);
}
