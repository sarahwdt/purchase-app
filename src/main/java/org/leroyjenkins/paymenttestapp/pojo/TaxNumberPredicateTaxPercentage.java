package org.leroyjenkins.paymenttestapp.pojo;

import java.math.BigDecimal;
import java.util.function.Predicate;

public record TaxNumberPredicateTaxPercentage(
        Predicate<String> taxNumberPredicate,
        BigDecimal taxPercentage) {
}
