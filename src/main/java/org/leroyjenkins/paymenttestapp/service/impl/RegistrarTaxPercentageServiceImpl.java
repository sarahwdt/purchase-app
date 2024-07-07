package org.leroyjenkins.paymenttestapp.service.impl;

import jakarta.annotation.Nonnull;
import lombok.RequiredArgsConstructor;
import org.leroyjenkins.paymenttestapp.exception.TaxCalculationException;
import org.leroyjenkins.paymenttestapp.pojo.TaxNumberPredicateTaxPercentage;
import org.leroyjenkins.paymenttestapp.service.TaxPercentageService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class RegistrarTaxPercentageServiceImpl implements TaxPercentageService {
    private final Set<TaxNumberPredicateTaxPercentage> taxByPredicateSet;

    @Override
    public boolean isTaxNumberPatternRegistered(@Nonnull String taxNumber) {
        return taxByPredicateSet.stream().map(TaxNumberPredicateTaxPercentage::taxNumberPredicate)
                .anyMatch(predicate -> predicate.test(taxNumber));
    }

    @Override
    @Nonnull
    public BigDecimal getTaxPercent(@Nonnull String taxNumber) {
        return taxByPredicateSet.stream().filter(pattern -> pattern.taxNumberPredicate().test(taxNumber))
                .findFirst()
                .map(TaxNumberPredicateTaxPercentage::taxPercentage)
                .orElseThrow(() -> new TaxCalculationException(taxNumber));
    }
}
