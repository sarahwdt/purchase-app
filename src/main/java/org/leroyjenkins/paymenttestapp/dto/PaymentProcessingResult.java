package org.leroyjenkins.paymenttestapp.dto;

import jakarta.annotation.Nullable;
import lombok.Builder;

@Builder
public record PaymentProcessingResult(
        boolean isSuccessful,
        @Nullable
        Exception catchedException) {
}
