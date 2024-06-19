package org.leroyjenkins.paymenttestapp.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.annotation.Nullable;

public record CalculatePriceRequest(
        @JsonProperty(value = "product")
        Integer productId,
        @JsonProperty(value = "taxNumber")
        String taxNumber,
        @Nullable
        @JsonProperty(value = "couponCode")
        String couponCode) {
}
