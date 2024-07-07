package org.leroyjenkins.paymenttestapp.unit.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.leroyjenkins.paymenttestapp.exception.TaxCalculationException;
import org.leroyjenkins.paymenttestapp.pojo.TaxNumberPredicateTaxPercentage;
import org.leroyjenkins.paymenttestapp.service.TaxPercentageService;
import org.leroyjenkins.paymenttestapp.service.impl.RegistrarTaxPercentageServiceImpl;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class RegistrarTaxPercentageServiceImplTest extends AbstractUnitTest {
    private static final String TAX_NUMBER_1 = "tax-number-1";
    private static final String TAX_NUMBER_2 = "tax-number-2";
    private static final Predicate<String> TAX_NUMBER_PREDICATE_1 = s -> Objects.equals(TAX_NUMBER_1, s);
    private static final Predicate<String> TAX_NUMBER_PREDICATE_2 = s -> Objects.equals(TAX_NUMBER_2, s);
    private static final BigDecimal TAX_NUMBER_1_PERCENTAGE = BigDecimal.valueOf(1);
    private static final BigDecimal TAX_NUMBER_2_PERCENTAGE = BigDecimal.valueOf(2);
    private static final Set<TaxNumberPredicateTaxPercentage> TAX_PERCENTAGES = Set.of(
            new TaxNumberPredicateTaxPercentage(TAX_NUMBER_PREDICATE_1, TAX_NUMBER_1_PERCENTAGE),
            new TaxNumberPredicateTaxPercentage(TAX_NUMBER_PREDICATE_2, TAX_NUMBER_2_PERCENTAGE));

    private final TaxPercentageService taxPercentageService = new RegistrarTaxPercentageServiceImpl(TAX_PERCENTAGES);

    private static Stream<Arguments> Should_ReturnIsAppropriatePredicateFound_When_isPredicateRegisteredCalled_Param() {
        return Stream.of(
                Arguments.of(TAX_NUMBER_1, true),
                Arguments.of(TAX_NUMBER_2, true),
                Arguments.of("unknown-tax-number", false)
        );
    }

    @ParameterizedTest
    @MethodSource("Should_ReturnIsAppropriatePredicateFound_When_isPredicateRegisteredCalled_Param")
    void Should_ReturnIsAppropriatePredicateFound_When_isPredicateRegisteredCalled(String taxNumber,
                                                                                   boolean expectedResult) {
        boolean actualResult = taxPercentageService.isTaxNumberPatternRegistered(taxNumber);

        assertThat(actualResult).isEqualTo(expectedResult);
    }

    private static Stream<Arguments> Should_ReturnRegisteredTaxPercentages_When_getTaxPercentagesCalledWithKnownTaxNumber_Param() {
        return Stream.of(
                Arguments.of(TAX_NUMBER_1, BigDecimal.valueOf(1)),
                Arguments.of(TAX_NUMBER_2, BigDecimal.valueOf(2))
        );
    }

    @ParameterizedTest
    @MethodSource("Should_ReturnRegisteredTaxPercentages_When_getTaxPercentagesCalledWithKnownTaxNumber_Param")
    void Should_ReturnRegisteredTaxPercentages_When_getTaxPercentagesCalledWithKnownTaxNumber(String taxNumber,
                                                                                              BigDecimal taxPercentage) {
        BigDecimal actualResult = taxPercentageService.getTaxPercent(taxNumber);

        assertThat(actualResult).isEqualByComparingTo(taxPercentage);
    }

    @Test
    void Should_ThrowException_When_getTaxPercentagesCalledWithUnknownTaxNumber() {
        String unknownTaxNumber = "unknown-tax-number";

        assertThatThrownByIsTheSameAs(() -> taxPercentageService.getTaxPercent(unknownTaxNumber),
                new TaxCalculationException(unknownTaxNumber));
    }
}