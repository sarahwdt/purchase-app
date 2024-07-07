package org.leroyjenkins.paymenttestapp.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.annotation.Nullable;
import lombok.Builder;

@Builder(toBuilder = true)
public record MakePurchaseRequest(
        @JsonProperty(value = "product")
        Integer productId,
        @JsonProperty(value = "taxNumber")
        String taxNumber,
        @Nullable
        @JsonProperty(value = "couponCode")
        String couponCode,
        @JsonProperty(value = "paymentProcessor")
        String paymentProcessor) {
}
