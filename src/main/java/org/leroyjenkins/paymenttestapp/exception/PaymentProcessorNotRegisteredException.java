package org.leroyjenkins.paymenttestapp.exception;

import lombok.Getter;

import java.util.Map;

@Getter
public class PaymentProcessorNotRegisteredException extends BusinessLogicException {
    private static final String ERROR_MESSAGE = "Payment processor not registered";
    private final String paymentProcessorName;

    public PaymentProcessorNotRegisteredException(String paymentProcessorName) {
        super(ERROR_MESSAGE, String.format("'%s': %s", paymentProcessorName, ERROR_MESSAGE),
                Map.of("paymentProcessor", paymentProcessorName));
        this.paymentProcessorName = paymentProcessorName;
    }
}
