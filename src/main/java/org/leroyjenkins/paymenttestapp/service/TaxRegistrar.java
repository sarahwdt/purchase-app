package org.leroyjenkins.paymenttestapp.service;

import jakarta.annotation.Nonnull;
import org.leroyjenkins.paymenttestapp.exception.TaxCalculationException;

import java.math.BigDecimal;

public interface TaxRegistrar {
    /**
     * Check is tax number format registered in system
     *
     * @param taxNumber tax number to check
     * @return is tax number registered in system
     */
    boolean isTaxPatternRegistered(@Nonnull String taxNumber);

    /**
     * Get tax percent for tax number
     *
     * @param taxNumber tax number to find tax
     * @return tax percent
     * @throws TaxCalculationException when tax number isn't registered in system
     */
    @Nonnull
    BigDecimal getTaxPercent(@Nonnull String taxNumber);
}
