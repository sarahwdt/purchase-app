package org.leroyjenkins.paymenttestapp.exception;

public class ApplicationBootstrapError extends Error {
    public ApplicationBootstrapError(String message) {
        super(message);
    }
}
