package org.leroyjenkins.paymenttestapp.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.MathContext;
import java.math.RoundingMode;

@Configuration
public class MathContextConfiguration {
    @Value("${application.precision.default}")
    private int defaultPrecision;

    @Value("${application.rounding-mode.default}")
    private RoundingMode defaultRoundingMode;

    @Bean
    public MathContext defaultMathContext() {
        return new MathContext(defaultPrecision, defaultRoundingMode);
    }

    @Bean
    public RoundingMode defaultRoundingMode() {
        return defaultRoundingMode;
    }
}
