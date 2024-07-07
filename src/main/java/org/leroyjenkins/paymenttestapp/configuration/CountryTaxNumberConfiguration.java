package org.leroyjenkins.paymenttestapp.configuration;

import lombok.Getter;
import org.leroyjenkins.paymenttestapp.pojo.TaxNumberPredicateTaxPercentage;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Set;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Configuration
public class CountryTaxNumberConfiguration {

    @Bean
    public Set<TaxNumberPredicateTaxPercentage> hardCodedCountryTaxes() {
        return Arrays.stream(COUNTRY_TAX.values())
                .map(countryTax -> new TaxNumberPredicateTaxPercentage(countryTax::test, countryTax.getTaxPercentage()))
                .collect(Collectors.toSet());
    }

    public enum COUNTRY_TAX {
        GERMAN("^DE\\d{9}$", BigDecimal.valueOf(19)),
        ITALY("^IT\\d{11}$", BigDecimal.valueOf(22)),
        GREECE("^GR\\d{9}$", BigDecimal.valueOf(24)),
        FRANCE("^FR[\\w&&[^_]]{2}\\d{9}$", BigDecimal.valueOf(20));

        private final Predicate<String> taxNumberPatternPredicate;

        @Getter
        private final BigDecimal taxPercentage;

        COUNTRY_TAX(String taxNumberPatternString, BigDecimal taxPercentage) {
            this.taxNumberPatternPredicate = Pattern.compile(taxNumberPatternString).asPredicate();
            this.taxPercentage = taxPercentage;
        }

        public boolean test(String taxNumber) {
            return taxNumberPatternPredicate.test(taxNumber);
        }
    }
}
