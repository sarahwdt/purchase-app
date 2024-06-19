package org.leroyjenkins.paymenttestapp.exception;

import java.util.Map;

public class PaymentProcessingException extends BusinessLogicException {
    private static final String ERROR_MESSAGE = "Payment processing failed";

    public PaymentProcessingException() {
        super(ERROR_MESSAGE, null, null);
    }

    public PaymentProcessingException(String message) {
        super(ERROR_MESSAGE, String.format("%s: %s", ERROR_MESSAGE, message),
                Map.of("innerMessage", message));
    }
}
