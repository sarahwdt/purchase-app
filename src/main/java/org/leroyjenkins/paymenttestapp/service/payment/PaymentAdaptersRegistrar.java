package org.leroyjenkins.paymenttestapp.service.payment;

import jakarta.annotation.Nonnull;

public interface PaymentAdaptersRegistrar {
    /**
     * Check is adapter for payment processor registered
     *
     * @param paymentProcessorName payment processor name to check
     * @return is payment processor adapter registered
     */
    boolean isAdapterRegistered(@Nonnull String paymentProcessorName);

    /**
     * Get adapter for payment processor
     *
     * @param paymentProcessorName payment processor name to find corresponding adapter
     * @return payment processor adapter
     */
    PaymentProcessorAdapter getAdapter(@Nonnull String paymentProcessorName);
}
