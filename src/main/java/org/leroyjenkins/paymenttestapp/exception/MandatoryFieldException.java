package org.leroyjenkins.paymenttestapp.exception;

public class MandatoryFieldException extends ValidationException {
    private static final String ERROR_MESSAGE = "Field is mandatory";

    public MandatoryFieldException(String field) {
        super(field, ERROR_MESSAGE);
    }
}
