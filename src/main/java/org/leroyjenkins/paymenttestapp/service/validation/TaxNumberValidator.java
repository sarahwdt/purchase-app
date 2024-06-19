package org.leroyjenkins.paymenttestapp.service.validation;

import jakarta.annotation.Nonnull;
import org.leroyjenkins.paymenttestapp.exception.TaxCalculationException;

public interface TaxNumberValidator {
    /**
     * Check is tax for tax number can be calculated
     *
     * @param taxNumber tax number to check
     * @throws TaxCalculationException when tax cannot be calculated
     */
    void validateTaxNumber(@Nonnull String taxNumber);
}
