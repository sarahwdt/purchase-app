package org.leroyjenkins.paymenttestapp.exception;

import lombok.Getter;

import java.util.Map;

@Getter
public class BusinessLogicException extends RuntimeException {
    private final String title;
    private final String detail;
    private final Map<String, Object> properties;

    protected BusinessLogicException(String title, String detail, Map<String, Object> properties) {
        super(detail);
        this.title = title;
        this.detail = detail;
        this.properties = properties;
    }
}
