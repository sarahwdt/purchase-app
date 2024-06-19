package org.leroyjenkins.paymenttestapp.service.validation.impl;

import jakarta.annotation.Nonnull;
import lombok.RequiredArgsConstructor;
import org.leroyjenkins.paymenttestapp.exception.PaymentProcessorNotRegisteredException;
import org.leroyjenkins.paymenttestapp.service.PaymentProcessingService;
import org.leroyjenkins.paymenttestapp.service.validation.PaymentProcessorValidator;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentProcessorValidatorImpl implements PaymentProcessorValidator {
    private final PaymentProcessingService paymentProcessingService;

    @Override
    public void validatePaymentProcessor(@Nonnull String paymentProcessorName) {
        if (!paymentProcessingService.isPaymentProcessorRegistered(paymentProcessorName)) {
            throw new PaymentProcessorNotRegisteredException(paymentProcessorName);
        }
    }
}
