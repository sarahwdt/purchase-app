package org.leroyjenkins.paymenttestapp.service.payment;

import jakarta.annotation.Nonnull;
import org.leroyjenkins.paymenttestapp.dto.PaymentProcessingResult;

import java.math.BigDecimal;

public interface PaymentProcessorAdapter {
    /**
     * Get processor identity
     *
     * @return processor name
     */
    @Nonnull
    String getProcessorName();

    /**
     * Wrapper for payment processing for different processors
     *
     * @param paymentAmount amount to pay
     * @return result of processing
     */
    @Nonnull
    PaymentProcessingResult makePayment(@Nonnull BigDecimal paymentAmount);
}
