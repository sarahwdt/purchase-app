package org.leroyjenkins.paymenttestapp.service.impl;

import jakarta.annotation.Nonnull;
import lombok.RequiredArgsConstructor;
import org.leroyjenkins.paymenttestapp.service.TaxPercentageService;
import org.leroyjenkins.paymenttestapp.service.TaxService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.MathContext;

@Service
@RequiredArgsConstructor
public class TaxServiceImpl implements TaxService {
    private final TaxPercentageService taxPercentageService;
    private final MathContext mathContext;

    @Override
    public boolean isTaxCanBeCalculated(@Nonnull String taxNumber) {
        return taxPercentageService.isTaxNumberPatternRegistered(taxNumber);
    }


    @Override
    @Nonnull
    public BigDecimal applyTax(@Nonnull BigDecimal price, @Nonnull String taxNumber) {
        BigDecimal taxPercent = taxPercentageService.getTaxPercent(taxNumber);
        price = price.add(
                price.multiply(
                        taxPercent.movePointLeft(2),
                        mathContext), mathContext);
        return price;
    }
}
