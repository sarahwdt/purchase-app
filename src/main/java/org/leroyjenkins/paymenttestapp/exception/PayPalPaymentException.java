package org.leroyjenkins.paymenttestapp.exception;

public class PayPalPaymentException extends Exception {
    private static final String ERROR_MESSAGE_TEMPLATE = "Payment amount greater than limit: '%.2f'";

    public PayPalPaymentException(int amount) {
        super(String.format(ERROR_MESSAGE_TEMPLATE, amount * 0.01f));
    }
}
