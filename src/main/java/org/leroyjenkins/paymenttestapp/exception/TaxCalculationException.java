package org.leroyjenkins.paymenttestapp.exception;

import lombok.Getter;

import java.util.Map;

@Getter
public class TaxCalculationException extends BusinessLogicException {
    private static final String ERROR_MESSAGE = "Tax cannot be calculated";
    private final String taxNumber;

    public TaxCalculationException(String taxNumber) {
        super(ERROR_MESSAGE, String.format("'%s': %s", taxNumber, ERROR_MESSAGE),
                Map.of("taxNumber", taxNumber));
        this.taxNumber = taxNumber;
    }
}
