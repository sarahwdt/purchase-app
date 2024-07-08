package org.leroyjenkins.paymenttestapp.unit.service.validation;

import org.junit.jupiter.api.Test;
import org.leroyjenkins.paymenttestapp.exception.TaxCalculationException;
import org.leroyjenkins.paymenttestapp.service.TaxService;
import org.leroyjenkins.paymenttestapp.service.validation.impl.TaxNumberValidatorImpl;
import org.leroyjenkins.paymenttestapp.unit.AbstractUnitTest;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.Mockito.when;

class TaxNumberValidatorImplTest extends AbstractUnitTest {
    @Mock
    private TaxService taxService;

    @InjectMocks
    private TaxNumberValidatorImpl validator;

    @Test
    void Should_ThrowException_When_validateTaxNumberIsCalledAndTaxCannotBeCalculated() {
        String taxNumber = "tax-number";
        TaxCalculationException expectedException = new TaxCalculationException(taxNumber);
        when(taxService.isTaxCanBeCalculated(taxNumber)).thenReturn(false);

        assertThatThrownByIsTheSameAs(() -> validator.validateTaxNumber(taxNumber),
                expectedException);
    }

    @Test
    void Should_DoNothing_When_validateTaxNumberIsNotCalledAndTaxCanBeCalculated() {
        String taxNumber = "tax-number";
        when(taxService.isTaxCanBeCalculated(taxNumber)).thenReturn(true);

        assertThatCode(() -> validator.validateTaxNumber(taxNumber))
                .doesNotThrowAnyException();
    }
}