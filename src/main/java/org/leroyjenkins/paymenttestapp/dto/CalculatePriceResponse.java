package org.leroyjenkins.paymenttestapp.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

public record CalculatePriceResponse(
        @JsonProperty(value = "calculatedPrice")
        BigDecimal calculatedPrice) {
}
