package org.leroyjenkins.paymenttestapp.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Builder;
import org.leroyjenkins.paymenttestapp.configuration.serialization.MoneySerializer;

import java.math.BigDecimal;

@Builder
public record CalculatePriceResponse(
        @JsonProperty(value = "calculatedPrice")
        @JsonSerialize(using = MoneySerializer.class)
        BigDecimal calculatedPrice) {
}
