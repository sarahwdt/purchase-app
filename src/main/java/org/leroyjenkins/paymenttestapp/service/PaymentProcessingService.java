package org.leroyjenkins.paymenttestapp.service;

import jakarta.annotation.Nonnull;
import org.leroyjenkins.paymenttestapp.exception.PaymentProcessingException;
import org.leroyjenkins.paymenttestapp.exception.PaymentProcessorNotRegisteredException;

import java.math.BigDecimal;

public interface PaymentProcessingService {
    /**
     * Check if payment processor registered in system
     *
     * @param paymentProcessorName payment processor name to check
     * @return is payment processor registered in system
     */
    boolean isPaymentProcessorRegistered(@Nonnull String paymentProcessorName);

    /**
     * Execute payment processing with applying taxes and bonuses
     *
     * @param paymentAmount original amount to be paid
     * @param taxNumber coupon code to calculate bonus
     * @param paymentProcessorName payment processor name to process payment
     * @throws PaymentProcessorNotRegisteredException when payment processor isn't registered
     * @throws PaymentProcessingException when payment processing failed with exception
     */
    void processPayment(@Nonnull BigDecimal paymentAmount, @Nonnull String taxNumber, @Nonnull String paymentProcessorName);
}
