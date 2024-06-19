package org.leroyjenkins.paymenttestapp.service.impl;

import jakarta.annotation.Nonnull;
import lombok.Getter;
import org.leroyjenkins.paymenttestapp.exception.TaxCalculationException;
import org.leroyjenkins.paymenttestapp.service.TaxRegistrar;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.function.Predicate;
import java.util.regex.Pattern;

@Service
public class TaxRegistrarImpl implements TaxRegistrar {
    @Override
    public boolean isTaxPatternRegistered(@Nonnull String taxNumber) {
        return Arrays.stream(COUNTRIES.values()).anyMatch(pattern -> pattern.test(taxNumber));
    }

    @Override
    @Nonnull
    public BigDecimal getTaxPercent(@Nonnull String taxNumber) {
        return Arrays.stream(COUNTRIES.values()).filter(pattern -> pattern.test(taxNumber))
                .findFirst()
                .map(COUNTRIES::getTax)
                .orElseThrow(() -> new TaxCalculationException(taxNumber));
    }

    private enum COUNTRIES {
        GERMAN("^DE\\d{9}$", new BigDecimal(19)),
        ITALY("^IT\\d{11}$", new BigDecimal(22)),
        GREECE("^GR\\d{9}$", new BigDecimal(24)),
        FRANCE("^FR[\\w&&[^_]]{2}\\d{9}$", new BigDecimal(20));

        private final Predicate<String> taxNumberPatternPredicate;

        @Getter
        private final BigDecimal tax;

        COUNTRIES(String taxNumberPatternString, BigDecimal tax) {
            this.taxNumberPatternPredicate = Pattern.compile(taxNumberPatternString).asPredicate();
            this.tax = tax;
        }

        public boolean test(String taxNumber) {
            return taxNumberPatternPredicate.test(taxNumber);
        }
    }
}
