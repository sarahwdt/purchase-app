package org.leroyjenkins.paymenttestapp.configuration;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.leroyjenkins.paymenttestapp.controller.PurchaseController;
import org.leroyjenkins.paymenttestapp.exception.BusinessLogicException;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.net.URI;

@Slf4j
@ControllerAdvice(basePackageClasses = PurchaseController.class)
public class GlobalErrorHandlingConfiguration extends ResponseEntityExceptionHandler {

    @ExceptionHandler(BusinessLogicException.class)
    public ResponseEntity<ProblemDetail> handleCouponCodeNotExistException(BusinessLogicException e, HttpServletRequest request) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatusCode.valueOf(400), e.getDetail());
        if (request != null) {
            URI uri = URI.create(request.getRequestURI());
            problemDetail.setInstance(uri);
        }
        problemDetail.setTitle(e.getTitle());
        problemDetail.setProperties(e.getProperties());
        return ResponseEntity.badRequest()
                .contentType(MediaType.APPLICATION_PROBLEM_JSON)
                .body(problemDetail);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ProblemDetail> handleException(Exception e, HttpServletRequest request) {
        log.error("Internal error was occurred: {}", e.getMessage(), e);
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatusCode.valueOf(500),
                "Internal Server Error");
        problemDetail.setTitle("Internal Server Error");
        if (request != null) {
            URI uri = URI.create(request.getRequestURI());
            problemDetail.setInstance(uri);
        }
        return ResponseEntity.badRequest()
                .contentType(MediaType.APPLICATION_PROBLEM_JSON)
                .body(problemDetail);
    }
}
