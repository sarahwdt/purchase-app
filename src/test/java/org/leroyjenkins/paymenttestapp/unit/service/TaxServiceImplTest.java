package org.leroyjenkins.paymenttestapp.unit.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.leroyjenkins.paymenttestapp.exception.TaxCalculationException;
import org.leroyjenkins.paymenttestapp.service.TaxPercentageService;
import org.leroyjenkins.paymenttestapp.service.impl.TaxServiceImpl;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.stream.Stream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith({MockitoExtension.class})
class TaxServiceImplTest {
    private final static int PRECISION = 18;
    private final static RoundingMode ROUNDING_MODE = RoundingMode.HALF_EVEN;

    @Mock
    private TaxPercentageService taxPercentageService;
    // This is not actually a mock, but we use @Mock annotation for @InjectMocks injection
    @Mock
    private static MathContext mathContext = new MathContext(PRECISION, ROUNDING_MODE);

    @InjectMocks
    private TaxServiceImpl taxService;

    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    void Should_DelegateToTaxRegistrar_When_isTaxCanBeCalculatedIsCalled(boolean taxRegistrarResult) {
        String taxNumber = "tax-number";
        when(taxPercentageService.isTaxNumberPatternRegistered(taxNumber)).thenReturn(taxRegistrarResult);

        boolean actualResult = taxService.isTaxCanBeCalculated(taxNumber);

        assertThat(actualResult).isEqualTo(taxRegistrarResult);
    }

    private static Stream<Arguments> Should_ReturnPriceWithTax_When_applyTaxIsCalled_Param() {
        BigDecimal price = new BigDecimal(50);
        BigDecimal periodicalPercentage = BigDecimal.valueOf((1.0 / 3) * 100);
        BigDecimal periodicalTax = price.multiply(periodicalPercentage.movePointLeft(2), mathContext);
        BigDecimal expectedResultWithPeriodicalPercentage = price.add(periodicalTax, mathContext);

        return Stream.of(
                Arguments.of(BigDecimal.valueOf(100), BigDecimal.valueOf(20), BigDecimal.valueOf(120)),
                Arguments.of(BigDecimal.valueOf(1), BigDecimal.valueOf(20), BigDecimal.valueOf(1.2)),
                Arguments.of(BigDecimal.valueOf(100_000), BigDecimal.valueOf(20), BigDecimal.valueOf(120_000)),
                Arguments.of(price, periodicalPercentage, expectedResultWithPeriodicalPercentage)
        );
    }

    @ParameterizedTest
    @MethodSource("Should_ReturnPriceWithTax_When_applyTaxIsCalled_Param")
    void Should_ReturnPriceWithTax_When_applyTaxIsCalled(BigDecimal price, BigDecimal taxPercent,
                                                         BigDecimal expectedResult) {
        String taxNumber = "tax-number";
        when(taxPercentageService.getTaxPercent(taxNumber)).thenReturn(taxPercent);

        BigDecimal actualResult = taxService.applyTax(price, taxNumber);

        assertThat(actualResult).isEqualByComparingTo(expectedResult);
    }

    @Test
    void Should_ThrowTaxCalculationException_When_applyTaxIsCalledAndTaxRegistrarThrowsException() {
        String taxNumber = "tax-number";
        BigDecimal price = new BigDecimal(100);
        Throwable expectedException = new TaxCalculationException(taxNumber);
        when(taxPercentageService.getTaxPercent(taxNumber)).thenThrow(expectedException);

        assertThatThrownBy(() -> taxService.applyTax(price, taxNumber))
                .isEqualTo(expectedException);
    }
}