package org.leroyjenkins.paymenttestapp.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record CalculatePriceRequest(
        @JsonProperty(value = "product")
        Integer productId,
        @JsonProperty(value = "taxNumber")
        String taxNumber,
        @JsonProperty(value = "couponCode")
        String couponCode) {
}
