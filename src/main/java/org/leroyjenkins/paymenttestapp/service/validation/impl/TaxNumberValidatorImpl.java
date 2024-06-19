package org.leroyjenkins.paymenttestapp.service.validation.impl;

import jakarta.annotation.Nonnull;
import lombok.RequiredArgsConstructor;
import org.leroyjenkins.paymenttestapp.exception.TaxCalculationException;
import org.leroyjenkins.paymenttestapp.service.TaxService;
import org.leroyjenkins.paymenttestapp.service.validation.TaxNumberValidator;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TaxNumberValidatorImpl implements TaxNumberValidator {
    private final TaxService taxService;

    @Override
    public void validateTaxNumber(@Nonnull String taxNumber) {
        if (!taxService.isTaxCanBeCalculated(taxNumber)) {
            throw new TaxCalculationException(taxNumber);
        }
    }
}
