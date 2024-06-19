package org.leroyjenkins.paymenttestapp.service.payment.processor;

import org.springframework.stereotype.Service;

@Service
public class StripePaymentProcessor {
    private static final float EPSILON = 0.01f;
    private static final float MAX_PAYMENT_AMOUNT = 100.0f;

    public boolean pay(float amount) {
        return isEqualsToMaxPaymentAmount(amount)
                || Float.compare(amount, MAX_PAYMENT_AMOUNT) >= 0;
    }

    private boolean isEqualsToMaxPaymentAmount(float a) {
        return Math.abs(a - MAX_PAYMENT_AMOUNT) <= EPSILON;
    }
}
