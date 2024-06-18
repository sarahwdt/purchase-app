package org.leroyjenkins.paymenttestapp.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record MakePurchaseRequest(
        @JsonProperty(value = "product")
        Integer productId,
        @JsonProperty(value = "taxNumber")
        String taxNumber,
        @JsonProperty(value = "couponCode")
        String couponCode,
        @JsonProperty(value = "paymentProcessor")
        String paymentProcessor) {
}
