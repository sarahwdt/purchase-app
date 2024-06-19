package org.leroyjenkins.paymenttestapp.service.payment.processor;

import jakarta.annotation.Nonnull;
import org.leroyjenkins.paymenttestapp.exception.PayPalPaymentException;
import org.springframework.stereotype.Service;

@Service
public class PayPalPaymentProcessor {
    private static final int MAX_PAYMENT_AMOUNT = 100_000;

    public void makePayment(@Nonnull Integer paymentAmount) throws PayPalPaymentException {
        if (paymentAmount > MAX_PAYMENT_AMOUNT) {
            throw new PayPalPaymentException(paymentAmount);
        }
    }
}
