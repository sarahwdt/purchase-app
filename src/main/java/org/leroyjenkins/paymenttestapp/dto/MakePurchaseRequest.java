package org.leroyjenkins.paymenttestapp.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.annotation.Nullable;

public record MakePurchaseRequest(
        @JsonProperty(value = "product")
        Integer productId,
        @JsonProperty(value = "taxNumber")
        String taxNumber,
        @JsonProperty(value = "couponCode")
        String couponCode,
        @Nullable
        @JsonProperty(value = "paymentProcessor")
        String paymentProcessor) {
}
