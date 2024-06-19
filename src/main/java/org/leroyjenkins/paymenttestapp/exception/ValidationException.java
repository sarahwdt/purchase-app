package org.leroyjenkins.paymenttestapp.exception;

import lombok.Getter;

import java.util.Map;

@Getter
public class ValidationException extends BusinessLogicException {
    private final String field;
    private final String errorMessage;

    public ValidationException(String field, String errorMessage) {
        super(errorMessage, String.format("'%s': %s", field, errorMessage),
                Map.of("field", field));
        this.field = field;
        this.errorMessage = errorMessage;
    }
}
