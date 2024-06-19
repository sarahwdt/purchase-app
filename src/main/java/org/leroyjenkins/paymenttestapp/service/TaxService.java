package org.leroyjenkins.paymenttestapp.service;

import jakarta.annotation.Nonnull;

import java.math.BigDecimal;

public interface TaxService {
    /**
     * Is tax can be calculated
     *
     * @param taxNumber tax number to find tax
     * @return is tax number can be calculated
     */
    boolean isTaxCanBeCalculated(@Nonnull String taxNumber);

    /**
     * Apply tax to price
     *
     * @param price price to apply tax
     * @param taxNumber tax number to find tax percent
     * @return price with tax
     */
    @Nonnull
    BigDecimal applyTax(@Nonnull BigDecimal price, @Nonnull String taxNumber);
}
